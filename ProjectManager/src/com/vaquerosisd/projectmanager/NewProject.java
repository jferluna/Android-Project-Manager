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

import com.vaquerosisd.database.ProjectOperations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProject extends Activity {

	ProjectOperations db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		
		//Initialize database operation object
		db = new ProjectOperations(this);
		db.open();
		
		//UI Handlers
		final EditText projectNameEditText = (EditText) findViewById(R.id.projectName);
		final EditText statusEditText = (EditText) findViewById(R.id.status);
		final Button addProjectButton = (Button) findViewById(R.id.addProject);
		
		//Add project to database
		addProjectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Get EditText info
				String projectName = projectNameEditText.getText().toString();
				String status = statusEditText.getText().toString();
				
				//Add project
				db.addProject(projectName, status);
				Toast.makeText(getApplicationContext(), "Project added", Toast.LENGTH_SHORT).show();
				
				//Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				projectNameEditText.clearFocus();
				statusEditText.clearFocus();
				imm.hideSoftInputFromWindow(projectNameEditText.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(statusEditText.getWindowToken(), 0);
				
				//Inform of successful adding
				Intent intent = new Intent();
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
}
