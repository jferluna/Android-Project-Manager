package com.vaquerosisd.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.object.Project;
import com.vaquerosisd.projectmanager.R;
import com.vaquerosisd.utils.FileOperations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class EditProject extends DialogFragment {
	
	static String STARTDATE_DIALOG_TAG = "startDate Dialog";	//Tag used to identify start date picker dialog
	static String DUEDATE_DIALOG_TAG = "dueDate Dialog";		//Tag used to identify due date picker dialog
	
	public static EditProject newInstance(int id) {
        EditProject f = new EditProject();

        Bundle args = new Bundle();
        args.putInt("id", id);
        f.setArguments(args);
        return f;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    //Don't permit click outside of dialog
	    AlertDialog alert=builder.create();
	    setCancelable(false);
	    alert.setCanceledOnTouchOutside(false);
	    
	    //Get project ID from Bundle
	    final int projectId =  getArguments().getInt("id");
	    
	    //Get project from ID
	    final ProjectOperations db = new ProjectOperations(getActivity());
	    db.open();
	    Project project = db.searchProjectById(projectId);
	    
	    //Set dialog tile and view
	    builder.setTitle("Edit project");
	    View dialogView = inflater.inflate(R.layout.dialog_edit_project, null); 
	    builder.setView(dialogView);
	    
	    //Get UI references
	    final EditText projectNameEditText = (EditText) dialogView.findViewById(R.id.dialog_EditProject_Project);
	    final Spinner statusSpinner = (Spinner) dialogView.findViewById(R.id.dialog_EditProject_Status);
	    final EditText startDateEditText = (EditText) dialogView.findViewById(R.id.dialog_EditProject_StartDate);
	    final EditText dueDateEditText = (EditText) dialogView.findViewById(R.id.dialog_EditProject_DueDate);
	    
	    //Set Spinner
	    ArrayAdapter<CharSequence> statusSpinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(), android.R.layout.simple_spinner_item);;
	    String statusFileName = "StatusTags";
	    setStatusSpinner(statusFileName, statusSpinnerAdapter, statusSpinner);
	    
	    //Initialize data field of the project to edit
	    projectNameEditText.setText(project.getName());
	    int statusPosition = statusSpinnerAdapter.getPosition(project.getStatus());
		statusSpinner.setSelection(statusPosition, true);
		final int[] startDate = {project.getDayStartDate(), project.getMonthStartDate(), project.getYearStartDate()};
		final int[] dueDate = {project.getDayDueDate(), project.getMonthDueDate(), project.getYearDueDate()};
	    startDateEditText.setText(startDate[0] + "/" + startDate[1] + "/" + startDate[2]);
	    dueDateEditText.setText(dueDate[0] + "/" + dueDate[1] + "/" + dueDate[2]);
	    
	    startDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog(v, STARTDATE_DIALOG_TAG, startDateEditText, startDate);
			}
		});
	    
	    dueDateEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog(v, DUEDATE_DIALOG_TAG, dueDateEditText, dueDate);	
			}
		});
	    
	    builder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
        	   String projectNameString = projectNameEditText.getText().toString();
        	   String statusString = statusSpinner.getSelectedItem().toString();;
        	   String startDateString = startDateEditText.getText().toString();
        	   String dueDateString = dueDateEditText.getText().toString();
        	   
        	   if(startDateString.compareTo(dueDateString) > 0) {
        		   Toast.makeText(getActivity(), R.string.dueDateError, Toast.LENGTH_SHORT).show();
        		   return;
        	   } else if(!projectNameString.equals("") && !startDateString.equals("") && !dueDateString.equals("")) {
        		   db.updateProject(projectId, projectNameString, statusString, startDate, dueDate);
        		   Toast.makeText(getActivity(), R.string.projectUpdated, Toast.LENGTH_SHORT).show();
        	   } else {
        		   Toast.makeText(getActivity(), R.string.incomplete, Toast.LENGTH_LONG).show();
        		   return;
        	   }
           }
       });
	    
	    builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
    	   @Override
           public void onClick(DialogInterface dialog, int id) {
    		   
           }
       }); 
	    return builder.create();
	}
	
	private void setStatusSpinner(String statusFileName, ArrayAdapter<CharSequence> statusSpinnerAdapter, Spinner statusSpinner) {
		FileOperations fO = new FileOperations(getActivity());
		String statusOptions = fO.readFile(statusFileName);
		ArrayList<String> statusOptionsArray = fO.convertToStringList(statusOptions);
		Collections.sort(statusOptionsArray.subList(0, statusOptionsArray.size()));
		statusOptionsArray.add("Custom...");
		
		//Set the adapter for the status spinner 
		statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinnerAdapter.addAll(statusOptionsArray);
		statusSpinner.setAdapter(statusSpinnerAdapter);
	}
	
	public void showDatePickerDialog(View v, String tag, final EditText dateEditText, final int[] dateStrings) {
		DatePickerFragment datePicker = new DatePickerFragment();
		
		Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calender.get(Calendar.YEAR));
		args.putInt("month", calender.get(Calendar.MONTH));
		args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		datePicker.setArguments(args);
		
		OnDateSetListener ondate = new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				dateEditText.setText(day + "/" + (month + 1) + "/" + year);
				dateStrings[0] = year;
				dateStrings[1] = month + 1;
				dateStrings[2] = day;
			}
		};
		datePicker.setCallBack(ondate);
	    datePicker.show(getFragmentManager(), tag);
	}
}
