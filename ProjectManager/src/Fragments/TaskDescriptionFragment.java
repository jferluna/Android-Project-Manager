package Fragments;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.CustomStatus;
import com.vaquerosisd.object.Task;
import com.vaquerosisd.projectmanager.R;
import com.vaquerosisd.utils.FileOperations;
import com.vaquerosisd.utils.SpinnerUtil;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
		
		int taskId = bundle.getInt("taskId");
		ProjectOperations db = new ProjectOperations(getActivity());
		fO = new FileOperations(getActivity());
		db.open();
		Task task = db.getTaskById(taskId);
		
		statusSpinner = (Spinner) view.findViewById(R.id.fragmentGeneral_Status);
		final Spinner prioritySpinner  = (Spinner) view.findViewById(R.id.fragmentGeneral_Priority);		
		final EditText taskDescription = (EditText) view.findViewById(R.id.fragmentGeneral_Description);
       
		//Initialize data of UI
		statusSpinnerAdapter = SpinnerUtil.setCustomSpinnerAdapter(getActivity(), this, getFragmentManager(), statusFileName, statusSpinner, true);
		prioritySpinnerAdapter = SpinnerUtil.setSpinnerAdapter(getActivity(),priorityFileName, prioritySpinner, false);
		taskDescription.setText(task.getDescription());
    }
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String statusOption) {
		SpinnerUtil.addStatusOption(getActivity(), statusFileName, statusOption, statusSpinnerAdapter, statusSpinner);
	}
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) { }
	
}
