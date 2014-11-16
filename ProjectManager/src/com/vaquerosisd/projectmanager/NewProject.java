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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.CustomStatus;
import com.vaquerosisd.dialog.DatePickerFragment;
import com.vaquerosisd.utils.FileOperations;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewProject extends Activity implements CustomStatus.CustomStatusInterface, OnDateSetListener {
	
	static String STARTDATE_DIALOG_TAG = "startDate Dialog";	//Tag used to identify start date picker dialog
	static String DUEDATE_DIALOG_TAG = "dueDate Dialog";		//Tag used to identify due date picker dialog
	String[] startDateStrings = new String[3];					//String array to store year, month and day of date picker
	String[] dueDateStrings = new String[3];					//String array to store year, month and day of date pickeri
	ProjectOperations db;										//Database operations class
	FileOperations fileOperations;								//
	String statusFileName = "StatusTags";						
	Spinner statusSpinner;
	ArrayAdapter<CharSequence> statusSpinnerAdapter;
	EditText startDateEditText;
	EditText dueDateEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		
		//Set navigation up
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize database operation object
		db = new ProjectOperations(this);
		db.open();
		
		//UI handlers initialization
		final EditText projectNameEditText = (EditText) findViewById(R.id.newProject_projectName);
		startDateEditText = (EditText) findViewById(R.id.newProject_startDate);
		dueDateEditText = (EditText) findViewById(R.id.newProject_dueDate);
		statusSpinner = (Spinner) findViewById(R.id.newProject_statusSpinner);
		final Button addProjectButton = (Button) findViewById(R.id.addProject);
		
		//Create status file options if there is no one present
		if(!isFile(statusFileName)) {
			String initialStatus = "New\nIn progress\nPrinting\nDone\n";
			createFile(statusFileName, initialStatus);
		}
		
		//Obtain status options from file and add custom option to the last place
		String statusOptions = readFile(statusFileName);
		ArrayList<String> statusOptionsArray = convertToStringList(statusOptions);
		Collections.sort(statusOptionsArray.subList(0, statusOptionsArray.size()));
		statusOptionsArray.add("Custom...");
		
		//Set the adapter for the status spinner 
		statusSpinnerAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		statusSpinnerAdapter.addAll(statusOptionsArray);
		statusSpinner.setAdapter(statusSpinnerAdapter);
		
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
				showDatePickerDialog(arg0, STARTDATE_DIALOG_TAG);				
			}
		});
		
		//DueDate click listener
		dueDateEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(arg0, DUEDATE_DIALOG_TAG);				
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
				if(!projectNameString.equals("") && !projectStartDateString.equals("") && !projectDueDateString.equals("")) {
					db.addProject(projectNameString, projectStatusString, startDateStrings, dueDateStrings);
					Toast.makeText(getApplicationContext(), "Project added", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "There is missing information!", Toast.LENGTH_LONG).show();
					return;
				}				
				
				//Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				projectNameEditText.clearFocus();
				statusSpinner.clearFocus();
				imm.hideSoftInputFromWindow(projectNameEditText.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(statusSpinner.getWindowToken(), 0);
				
				//Inform of successful adding
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
	
	//Return to ProjecList 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	//***********************************************************************************************
	//File Methods
	//***********************************************************************************************
	
	//Create file
	public void createFile(String fileName, String fileText) {
		new File(this.getFilesDir(), fileName);
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(fileName, Context.MODE_APPEND);
			outputStream.write(fileText.getBytes());
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	//Append text
	public void appendText(String fileName, String text) {
		FileOutputStream outputStream;
		try {
			outputStream = openFileOutput(fileName, Context.MODE_APPEND);
			outputStream.write(text.getBytes());
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	//Reads internal file
	public String readFile(String filename) {
		try {
			FileInputStream inputStream = openFileInput(filename);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			String fileText = stringBuilder.toString();
			return fileText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//Converts String to ArrayList
	public ArrayList<String> convertToStringList(String text) {
		ArrayList<String> statusList = new ArrayList<String>();
		String[] stringArray = text.split("\n");
		for(int i =0; i < stringArray.length; i++){
			statusList.add(stringArray[i]);
		}
		return statusList;
		
	}
	
	//Checks it there is an existing file in internal storage
	public boolean isFile(String fileName) {
		File directory = getFilesDir();
		for(String file : directory.list()){
			if(file.equals(fileName)){
				return true;
			}
				
		}
		return false;
	}
	
	//***********************************************************************************************
	//Dialog Methods
	//***********************************************************************************************
	
	//Return method of custom status dialog
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String statusOption) {
		//Add new item
		appendText(statusFileName, statusOption + "\n");
		String statusOptions = readFile(statusFileName);
		ArrayList<String> statusOptionsArray = convertToStringList(statusOptions);
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
	
	//Start DatePicker dialog
	public void showDatePickerDialog(View v, String tag) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), tag);
	}
	
	//Return method of DatePicker dialog
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		FragmentManager fragmanager = getFragmentManager();
		
		if(fragmanager.findFragmentByTag(STARTDATE_DIALOG_TAG) != null){
			startDateEditText.setText(day + "/" + (month + 1) + "/" + year);
			startDateStrings[0] = String.valueOf(year);
			startDateStrings[1] = String.valueOf(month + 1);
			startDateStrings[2] = String.valueOf(day);
		}
		
		if(fragmanager.findFragmentByTag(DUEDATE_DIALOG_TAG) != null){
			dueDateEditText.setText(day + "/" + (month + 1) + "/" + year);
			dueDateStrings[0] = String.valueOf(year);
			dueDateStrings[1] = String.valueOf(month + 1);
			dueDateStrings[2] = String.valueOf(day);
		}
		
		
	}

	
}
