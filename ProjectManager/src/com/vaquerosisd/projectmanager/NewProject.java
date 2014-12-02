/*
Project Manager - Android application for the administration of projects.
	Copyright (C) 2014 - ITESM

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.


Authors:

   ITESM representatives
	Ing. Martha Sordia Salinas <msordia@itesm.mx>
    Ing. Mario de la Fuente <mario.delafuente@itesm.mx>

   ITESM students
	David Alberto De Leon Villarreal ddeleon93@gmail.com
	Alan Salinas Gonzalez alan.sagz@gmail
	José Fernando Luna Alemán jfernando.luna91@gmail.com
*/

package com.vaquerosisd.projectmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.CustomStatus;
import com.vaquerosisd.dialog.DatePickerFragment;
import com.vaquerosisd.utils.FileOperations;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewProject extends Activity implements CustomStatus.CustomStatusInterface {
	
	ProjectOperations db;										//Instance of the class that provides the database operations
	FileOperations fO;											//Instance of the class that provides the file operations
	String statusFileName = "StatusTags";						//File name for the status options			
	int[] startDateStrings = new int[3];					//String array to store year, month and day of start date picker
	int[] dueDateStrings = new int[3];					//String array to store year, month and day of due date picker
	static String STARTDATE_DIALOG_TAG = "startDate Dialog";	//Tag used to identify start date picker dialog
	static String DUEDATE_DIALOG_TAG = "dueDate Dialog";		//Tag used to identify due date picker dialog
	
	//UI handlers
	Spinner statusSpinner;
	ArrayAdapter<CharSequence> statusSpinnerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize database operation object
		db = new ProjectOperations(this);
		db.open();
		
		//Initialize file operations instance
		fO = new FileOperations(this);
		
		//UI handlers references
		final EditText projectNameEditText = (EditText) findViewById(R.id.newProject_ProjectName);
		final EditText startDateEditText = (EditText) findViewById(R.id.newProject_StartDate);
		final EditText dueDateEditText = (EditText) findViewById(R.id.newProject_DueDate);
		statusSpinner = (Spinner) findViewById(R.id.newProject_StatusSpinner);
		final Button addProjectButton = (Button) findViewById(R.id.addProject);
		
		//Set spinner
		String statusItemsString = "New\nIn progress\nPrinting\nOn hold\nCancelled\nReviewing\nDelegated\nCompleted\n";
		fO.createStringFile(statusFileName, statusItemsString);
		setStatusSpinner();
		int statusPosition = statusSpinnerAdapter.getPosition("New");
		statusSpinner.setSelection(statusPosition, true);
		
		//Check if custom option is selected to open a dialog to provide a custom status		
		statusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if( "Custom...".equals((String) statusSpinnerAdapter.getItem(position))) {
					DialogFragment customStatusDialog = new CustomStatus();
					customStatusDialog.show(getFragmentManager(), "Custom Status Dialog");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
		
		//StartDate click listener
		startDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(arg0, STARTDATE_DIALOG_TAG, startDateEditText, startDateStrings);				
			}
		});
		
		//DueDate click listener
		dueDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(arg0, DUEDATE_DIALOG_TAG, dueDateEditText, dueDateStrings);				
			}
		});
		
		//Add project to database
		addProjectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Get EditText info
				String projectNameString = projectNameEditText.getText().toString();
				String projectStatusString = statusSpinner.getSelectedItem().toString();
				String projectStartDateString = startDateEditText.getText().toString();
				String projectDueDateString = dueDateEditText.getText().toString();
				
				//Add project to database if all input elements are filled
				if(projectStartDateString.compareTo(projectDueDateString) > 0) {
					Toast.makeText(getApplicationContext(), R.string.dueDateError, Toast.LENGTH_LONG).show();
					return;
				}
				else if(!projectNameString.equals("") && !projectStartDateString.equals("") && !projectDueDateString.equals("")) {
					File contentDir = new File("/storage/sdcard0/ProjectManager/" + projectNameString);
					contentDir.mkdirs();
					db.addProject(projectNameString, projectStatusString, startDateStrings, dueDateStrings, contentDir.getAbsolutePath());
					Toast.makeText(getApplicationContext(), R.string.projectAdded, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.incomplete, Toast.LENGTH_LONG).show();
					return;
				}				
				
				//Inform of successful adding
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
	
	//***********************************************************************************************
	// Methods
	//***********************************************************************************************
	
	//Obtain status options from file and add custom option to the last place
	private void setStatusSpinner() {
		String statusOptions = fO.readFile(statusFileName);
		ArrayList<String> statusOptionsArray = fO.convertToStringList(statusOptions);
		Collections.sort(statusOptionsArray.subList(0, statusOptionsArray.size()));
		statusOptionsArray.add("Custom...");
		
		//Set the adapter for the status spinner 
		statusSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinnerAdapter.addAll(statusOptionsArray);
		statusSpinner.setAdapter(statusSpinnerAdapter);
	}
	
	//***********************************************************************************************
	//Dialog Methods
	//***********************************************************************************************
	
	//Return method of custom status dialog
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String statusOption) {
		//Add new item
		fO.appendText(statusFileName, statusOption + "\n");
		String statusOptions = fO.readFile(statusFileName);
		ArrayList<String> statusOptionsArray = fO.convertToStringList(statusOptions);
		Collections.sort(statusOptionsArray.subList(0, statusOptionsArray.size()));
		statusOptionsArray.add("Custom...");
		statusSpinnerAdapter.clear();
		statusSpinnerAdapter.addAll(statusOptionsArray);
		statusSpinnerAdapter.notifyDataSetChanged();
		
		int statusPosition = statusSpinnerAdapter.getPosition(statusOption);
		statusSpinner.setSelection(statusPosition, true);
	}
	
	//On cancel button, set the status spinner to "New" option
	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		int statusPosition = statusSpinnerAdapter.getPosition("New");
		statusSpinner.setSelection(statusPosition, true);
	}
	
	//Call DatePicker dialog
	public void showDatePickerDialog(View v, String tag, final EditText dateEditText, final int[] date) {
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
				date[0] = year;
				date[1] = month + 1;
				date[2] = day;
			}
		};
		datePicker.setCallBack(ondate);
	    datePicker.show(getFragmentManager(), tag);
	}
}
