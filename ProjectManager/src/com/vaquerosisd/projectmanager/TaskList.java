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

import java.util.List;

import com.vaquerosisd.adapters.TaskListViewAdapter;
import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.object.Task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class TaskList extends Activity {
	
	ProjectOperations db;				//Database Operations
	Task selectedTask;					//Last selected Task on ListView
	View selectedRow;					//Last selected row in ListView
	TaskListViewAdapter taskAdapter;	//ListView Adapter
	EditText searchTaskEditText;		//SearchBox of task
	String projectId;					//Project ID where the tasks belong
	String projectName;					//Project name associated to the Project ID
	boolean searching = false;
	int tasksNumber = 0;
	
	static private final int ADD_TASK_REQUEST = 6;
	static private final int VIEW_PICTURES = 7;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		//Get intent from Projects.class
		Bundle data = getIntent().getExtras();
		projectId = String.valueOf(data.getInt("ProjectID"));
		projectName = data.getString("ProjectName");
		Log.i("DEBUG", projectId);
		
		//UI Handlers
		final ImageButton cancelSearch = (ImageButton) findViewById(R.id.cancelSearch);
		final ImageButton searchTask = (ImageButton) findViewById(R.id.searchTask);
		final ImageButton deleteTask = (ImageButton) findViewById(R.id.deleteTask);
		final ImageButton addTask = (ImageButton) findViewById(R.id.addTaks);
		final ImageButton gotoTask = (ImageButton) findViewById(R.id.gotoTask);
		searchTaskEditText = (EditText) findViewById(R.id.searchTaskName);
		final ListView taskListView = (ListView) findViewById(R.id.taskList);
		
		final Button goToPictures = (Button)findViewById(R.id.pictures);
		
		//ListView Adapter
		taskAdapter = new TaskListViewAdapter(getApplicationContext(), R.layout.listrow_task, getTasksForListView());
		taskListView.setAdapter(taskAdapter);
		
		//onClickListeners
		cancelSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(searching) {
					taskAdapter.clear();
					taskAdapter.addAll(getTasksForListView());
					taskAdapter.notifyDataSetChanged();
					cancelSearch.setVisibility(View.GONE);
					searching = false;
				}
			}
		});
		
		searchTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String task = searchTaskEditText.getText().toString();
				searchTask(task);
				cancelSearch.setVisibility(View.VISIBLE);
				searching = true;
			}
		});
		
		deleteTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteTask();				
			}
		});
		
		addTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskList.this, NewTask.class);
				intent.putExtra("ProjectID", projectId);
				startActivityForResult(intent, ADD_TASK_REQUEST);			
			}
		});
		
		gotoTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedTask != null) {
					Intent intent = new Intent(TaskList.this, ContentTask.class);
					startActivity(intent);
				}
			}
		});
		
		taskListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedTask = (Task) taskAdapter.getItem(position);
				if(selectedRow != null)
					selectedRow.setBackgroundColor(Color.TRANSPARENT);
				selectedRow = taskAdapter.getView(position, view, null);
				selectedRow.setBackgroundColor(Color.LTGRAY);
			}
		});
		
		goToPictures.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TaskList.this, PhotoManager.class);
				intent.putExtra("ProjectID", projectId);
				intent.putExtra("ProjectName", projectName);
				startActivityForResult(intent, VIEW_PICTURES);
				
			}
		});
		
	} //End of onCreate method
	
	public List<Task> getTasksForListView() {
		List<Task> list = db.getAllTasks(Integer.parseInt(projectId));
		tasksNumber = list.size();
		return list;
	}
	
	//Searches the task name in the task list
	public void searchTask(String taskName) {
		List<Task> searchTaskList = db.searchTasks(searchTaskEditText.getText().toString(), Integer.parseInt(projectId));
		taskAdapter.clear();
		taskAdapter.addAll(searchTaskList);
		taskAdapter.notifyDataSetChanged();
	}
	
	//Delete the task and refresh the listView
	public void deleteTask() {
		String taskName = selectedTask.getTaskName();
		int taskId = selectedTask.getTaskId();
		db.deleteTask(taskName, taskId);
		taskAdapter.clear();
		taskAdapter.addAll(getTasksForListView());
		taskAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			//Reloads the ListView when adding a new task on NewTask.class
			if(requestCode == ADD_TASK_REQUEST) {
				taskAdapter.clear();
				taskAdapter.addAll(getTasksForListView());
				taskAdapter.notifyDataSetChanged();
			}
		}    
    }
}
