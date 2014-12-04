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
import com.vaquerosisd.dialog.DatePickerFragment;
import com.vaquerosisd.utils.FileOperations;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class NewTask extends Activity {
	
	ProjectOperations db;					//Instance of the class that provides the database operations
	FileOperations fO;						//Instance of the class that provides the file operations
	int projectId;							//Project ID where the tasks belong
	String projectName;						//Project name where the task belong
	
	//File names for the spinners
	String statusFileName = "Tasks_StatusItems";
	String priorityFileName = "Tasks_PriorityItems";
	
	//Tags
	static String STARTDATE_DIALOG_TAG = "startDate Dialog";	//Tag used to identify start date picker dialog
	static String DUEDATE_DIALOG_TAG = "dueDate Dialog";		//Tag used to identify due date picker dialog
	
	//UI handlers
	ArrayAdapter<CharSequence> statusSpinnerAdapter;
	ArrayAdapter<CharSequence> prioritySpinnerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		
		//Set navigation up
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		//Initialize file operations instance
		fO = new FileOperations(this);
		
		//Get intent from Projects.class
		Bundle data = getIntent().getExtras();
		projectId = data.getInt("ProjectID");
		projectName = data.getString("ProjectName");
		
		//Get UI handler references
		final EditText taskNameEditText = (EditText) findViewById(R.id.newTask_TaskName);
		final Spinner statusSpinner = (Spinner) findViewById(R.id.newTask_StatusSpinner);
		final Spinner prioritySpinner = (Spinner) findViewById(R.id.newTask_Priority);
		final EditText percentageDoneEditText = (EditText) findViewById(R.id.newTask_PercentageDone);
		final SeekBar percentageDoneSeekBar = (SeekBar) findViewById(R.id.newTask_PercentageDoneSeekBar);
		final EditText startDateEditText = (EditText) findViewById(R.id.newTask_StartDate);
		final EditText dueDateEditText = (EditText) findViewById(R.id.newTask_DueDate);
		final EditText descriptionEditText = (EditText) findViewById(R.id.newTask_Description);
		final Button addTaskButton = (Button) findViewById(R.id.newTask_AddTask);
		final int[] startDateStrings = {0,0,0};
		final int[] dueDateStrings = {0,0,0};
		
		//Set files they doesn't exist
		String statusItemsString = "New\nIn progress\nPrinting\nOn hold\nCancelled\nReviewing\nDelegated\nCompleted\n";
		fO.createStringFile(statusFileName, statusItemsString);
		String priorityItemsString = "High\nMedium\nLow\n";
		fO.createStringFile(priorityFileName, priorityItemsString);
		
		//Create and set content for the status and priority spinners
		setSpinner(statusFileName, statusSpinnerAdapter, statusSpinner, true);
		setSpinner(priorityFileName, prioritySpinnerAdapter, prioritySpinner, false);
		
		percentageDoneEditText.setText("0%");	
//		int statusPosition = statusSpinnerAdapter.getPosition("New");
//		statusSpinner.setSelection(statusPosition, true);
		
		addTaskButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//Get values for the add task
				String projectDir = db.getProjectContentPath(projectId);
				String taskName = taskNameEditText.getText().toString();
				String status = statusSpinner.getSelectedItem().toString();
				String priority = prioritySpinner.getSelectedItem().toString();
				String percentage = percentageDoneEditText.getText().toString();
				int percentageDone = Integer.parseInt(percentage.substring(0, percentage.length()-1));
				String startDate = startDateEditText.getText().toString();
				String dueDate =dueDateEditText.getText().toString();
				String photoPath = "";
				String description = descriptionEditText.getText().toString();
				String contentPath = projectDir + "/" + taskName;
				Log.i("DEBUG", contentPath);
				File taskContentPath = new File(contentPath);
				taskContentPath.mkdir();
				
				boolean dueDateBeforeStartDate = true;
				if(startDateStrings[0] <= dueDateStrings[0]){
					if(startDateStrings[1] <= dueDateStrings[1]){
						if(startDateStrings[2] <= dueDateStrings[2])
							dueDateBeforeStartDate = false;
					}
				}
				
				if(dueDateBeforeStartDate) {
					Toast.makeText(getApplicationContext(), R.string.dueDateError, Toast.LENGTH_SHORT).show();
					return;
				} else if(!taskName.equals("") && !startDate.equals("") && !dueDate.equals("")){
					db.addTask(projectId, taskName, status, priority, percentageDone, startDateStrings, dueDateStrings, photoPath, description, contentPath);
					Toast.makeText(getApplicationContext(), R.string.taskAdded, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.incomplete, Toast.LENGTH_SHORT).show();
					return;
				}
				
				//Inform of successful adding
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
				finish();
			}
		});	
		
		//SeekBar listener
		percentageDoneSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { }
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				percentageDoneEditText.setText(String.valueOf(progress) + "%");				
			}
		});
		
		//StartDate click listener
		startDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(arg0, STARTDATE_DIALOG_TAG, startDateEditText, startDateStrings);				
			}
		});
		
		//StartDate click listener
		dueDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(arg0, DUEDATE_DIALOG_TAG, dueDateEditText, dueDateStrings);				
			}
		});
	}
	
	//***********************************************************************************************
	// Activity Methods
	//***********************************************************************************************
	
	//Obtain status options from file and add custom option to the last place
	private void setSpinner(String spinnerFileName, ArrayAdapter<CharSequence> spinnerAdapter, Spinner spinnerObject, boolean sort) {
		String spinnerItems = fO.readFile(spinnerFileName);
		ArrayList<String> spinnerItemsArray = fO.convertToStringList(spinnerItems);
		if(sort)
			Collections.sort(spinnerItemsArray.subList(0, spinnerItemsArray.size()));
		
		//Set the adapter for the status spinner 
		spinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAdapter.addAll(spinnerItemsArray);
		spinnerObject.setAdapter(spinnerAdapter);
	}
	
	//Return to ProjecList 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Intent navigationIntent = NavUtils.getParentActivityIntent(this);
			navigationIntent.putExtra("ProjectID", projectId);
			navigationIntent.putExtra("ProjectName", projectName);
	        NavUtils.navigateUpTo(this, navigationIntent);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	//***********************************************************************************************
	// Dialog Methods
	//***********************************************************************************************
	
	//CallDatePicker dialog
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