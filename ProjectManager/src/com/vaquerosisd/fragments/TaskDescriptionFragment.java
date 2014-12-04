package com.vaquerosisd.fragments;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.CustomStatus;
import com.vaquerosisd.object.Task;
import com.vaquerosisd.projectmanager.R;
import com.vaquerosisd.utils.FileOperations;
import com.vaquerosisd.utils.SpinnerUtil;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class TaskDescriptionFragment extends Fragment implements CustomStatus.CustomStatusInterface{
	Bundle bundle;
	FileOperations fO;
	ArrayAdapter<CharSequence> statusSpinnerAdapter;
	ArrayAdapter<CharSequence> prioritySpinnerAdapter;
	String statusFileName = "Tasks_StatusItems";
	String priorityFileName = "Tasks_PriorityItems";
	Spinner statusSpinner;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		bundle = getArguments();
        return inflater.inflate(R.layout.fragment_task_description, container, false);
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		final int taskId = bundle.getInt("TaskId");
		final ProjectOperations db = new ProjectOperations(getActivity());
		fO = new FileOperations(getActivity());
		db.open();
		Task task = db.getTaskById(taskId);
		
		statusSpinner = (Spinner) view.findViewById(R.id.fragmentGeneral_Status);
		final Spinner prioritySpinner  = (Spinner) view.findViewById(R.id.fragmentGeneral_Priority);
		final EditText percentageDoneEditText = (EditText) view.findViewById(R.id.fragmentGeneral_PercentageDone);
		final SeekBar percentageDoneSeekBar = (SeekBar) view.findViewById(R.id.fragmentGeneral_PercentageDoneSeekBar);
		final Button updateTask = (Button) view.findViewById(R.id.fragmentGeneral_Update);
		final EditText taskDescription = (EditText) view.findViewById(R.id.fragmentGeneral_Description);
		
		//Initialize data of UI
		statusSpinnerAdapter = SpinnerUtil.setCustomSpinnerAdapter(getActivity(), this, getFragmentManager(), statusFileName, statusSpinner, true);
		prioritySpinnerAdapter = SpinnerUtil.setSpinnerAdapter(getActivity(),priorityFileName, prioritySpinner, false);
		taskDescription.setText(task.getDescription());
		
		//Put spinners to data
		int position = statusSpinnerAdapter.getPosition(task.getStatus());
		statusSpinner.setSelection(position, true);
		position = prioritySpinnerAdapter.getPosition(task.getPriority());
		prioritySpinner.setSelection(position, true);
		percentageDoneSeekBar.setProgress(task.getPercentage());
		percentageDoneEditText.setText(task.getPercentage() + "%");
		
		percentageDoneSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				percentageDoneEditText.setText(String.valueOf(progress) + "%" );				
			}
		});
		
		updateTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String status = statusSpinner.getSelectedItem().toString();
				String priority = prioritySpinner.getSelectedItem().toString();
				String percentageTemp = percentageDoneEditText.getText().toString();
				int percentage = Integer.parseInt(percentageTemp.substring(0, percentageTemp.length()-1));
				Log.i("Debug", String.valueOf(percentage));
				String description = taskDescription.getText().toString();
				
				db.updateTask(taskId, status, priority, percentage, description);
				Toast.makeText(getActivity(), "Task updated!!", Toast.LENGTH_SHORT).show();
				
			}
		});
		
    }
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String statusOption) {
		SpinnerUtil.addStatusOption(getActivity(), statusFileName, statusOption, statusSpinnerAdapter, statusSpinner);
	}
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) { }
	
}
