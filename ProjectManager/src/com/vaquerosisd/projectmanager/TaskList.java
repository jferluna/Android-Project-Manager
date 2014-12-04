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
import com.vaquerosisd.dialog.DeleteDialog;
import com.vaquerosisd.object.JsonWrapper;
import com.vaquerosisd.object.Task;
import com.vaquerosisd.object.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskList extends Activity implements WebserviceCallback {
	
	ProjectOperations db;				//Database Operations
	Task selectedTask;					//Last selected Task on ListView
	View selectedRow;					//Last selected row in ListView
	TaskListViewAdapter taskAdapter;	//ListView Adapter
	EditText searchTaskEditText;		//SearchBox of task
	int projectId;						//Project ID where the tasks belong
	String projectName;					//Project name associated to the Project ID
	
	boolean taskSelected = false;
	boolean searching = false;
	
	//User Logging
	private User currentUser;
		  
	static private final int ADD_TASK_REQUEST = 6;
	static private final int VIEW_PICTURES = 7;
	
	//**************************************************************************************
	//
	//Activity LifeCycle methods
	//
	//**************************************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		//Get projectId and projectName from ProjectList
		Bundle data = getIntent().getExtras();
		projectId = data.getInt("ProjectID");
		projectName = data.getString("ProjectName");
		
		//ListView adapter
		final ListView taskListView = (ListView) findViewById(R.id.listTask_TaskList);
		taskAdapter = new TaskListViewAdapter(getApplicationContext(), R.layout.listrow_task, getTasksForListView());
		taskListView.setAdapter(taskAdapter);
		
		//Go to content task
		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Remove selected color on selected item in ListView
				if(selectedRow != null) {
					selectedRow.setBackgroundColor(Color.TRANSPARENT);
					selectedRow = null;
				}
				//Get selected task information and send a ContentTask intent
				Task task = (Task) taskAdapter.getItem(position);
				Intent ContentTaskItent = new Intent(TaskList.this, ContentTask.class);
				ContentTaskItent.putExtra("taskId",	task.getTaskId());
				startActivity(ContentTaskItent);
			}
		});
		
		//Select task and obtain reference to selected item on ListView
		taskListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//Change color background on selectedItem
				if(selectedRow != null)
					selectedRow.setBackgroundColor(Color.TRANSPARENT);
				selectedRow = taskAdapter.getView(position, view, null);
				selectedRow.setBackgroundColor(Color.parseColor("#42A5F5"));
				
				//Get information of selected task
				selectedTask = (Task) taskAdapter.getItem(position);
				taskSelected = true;
        		invalidateOptionsMenu();
				return true;
			}
		});		
	} //End of onCreate method
	
	@Override
	public void onResume() {
		super.onResume();
		checkUser();
		taskAdapter.clear();
		taskAdapter.addAll(getTasksForListView());
		taskAdapter.notifyDataSetChanged();
	} // End of onResume method
	
	//*********************************************************************************************************************
	//Activity methods
	//*********************************************************************************************************************
		
	public List<Task> getTasksForListView() {
		List<Task> list = db.getAllTasks(projectId);
		return list;
	}
	
	//Searches the task name in the task list
	public void searchTask(String taskName) {
		Log.i("Debug", taskName);
		List<Task> searchTaskList = db.searchTasks(projectId, taskName);
		taskAdapter.clear();
		taskAdapter.addAll(searchTaskList);
		taskAdapter.notifyDataSetChanged();
	}
	
	//Delete the task and refresh the listView. Called by the DeleteDialog
	public void deleteTask() {
		String taskName = selectedTask.getTaskName();
		int taskId = selectedTask.getTaskId();
		
		db.deleteTask(taskId, taskName);
		taskAdapter.clear();
		taskAdapter.addAll(getTasksForListView());
		taskAdapter.notifyDataSetChanged();
		taskSelected = false;
		selectedTask = null;
		selectedRow.setBackgroundColor(Color.TRANSPARENT);
		selectedRow = null;
		invalidateOptionsMenu();
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
	
	@Override
	public void onBackPressed(){
		if(searching) {
			//Clears the searched projects
			taskAdapter.clear();
			taskAdapter.addAll(getTasksForListView());
			taskAdapter.notifyDataSetChanged();
			getActionBar().setDisplayHomeAsUpEnabled(false);
			searching = false;
			invalidateOptionsMenu();
			return;
		} else if (taskSelected) {
			//Clears pointer to selected row and removes all enhanced view properties
			selectedRow.setBackgroundColor(Color.TRANSPARENT);
			selectedRow = null;
			taskSelected = false;
			invalidateOptionsMenu();
			return;
		} else {
			//Default onBackPressed action
			super.onBackPressed();
		}
	}	
	
	//*********************************************************************************************************************
	//Sync WebService methods
	//*********************************************************************************************************************
	public void checkUser() {
		currentUser = User.getUser(TaskList.this);
		if (currentUser == null) {
			Intent intent = new Intent(TaskList.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
	public void callback(JsonWrapper response){
		System.out.println("Webserive code response: " + response.getCode());
		if (response.getCode() == 6) {
			Toast.makeText(getApplicationContext(), "Content Synced", Toast.LENGTH_SHORT).show();
		}
		checkUser();
	}

	//Defined functions	
	public void syncAll(){
		Toast.makeText(TaskList.this, "Sincronizar todo", Toast.LENGTH_SHORT).show();
	}
	
	//**************************************************************************************
	//ActionBar methods
	//**************************************************************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.task_menu, menu);
		
		MenuItem searchItem = menu.findItem(R.id.actionBar_SearchTaskItem);
		Button clearSearchTask =  (Button) searchItem.getActionView().findViewById(R.id.actionBar_ClearSearch);
		final AutoCompleteTextView searchTask = (AutoCompleteTextView) searchItem.getActionView().findViewById(R.id.actionBar_SearchItemEditText);
		
		//Get all tasks names and add them to an adapter for the AutoCompleteTextView
		List<Task> allTasks = db.getAllTasks(projectId);
		String[] tasksNames = new String[allTasks.size()];
		for(int i = 0; i < allTasks.size(); i++)
			tasksNames[i] = allTasks.get(i).getTaskName();

		ArrayAdapter<String> tasksAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasksNames);
		searchTask.setAdapter(tasksAdapter);
		searchTask.setThreshold(1);
		
		//Clear text of search AutoCompleteTextView
		clearSearchTask.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchTask.setText("");
			}
		});
		
		searchTask.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);
				}
			} 
		}); 

		//Search task
		searchTask.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//When the user press "DONE" key, select the task
				if(actionId == EditorInfo.IME_ACTION_DONE){
					String project = searchTask.getText().toString();
					searchTask(project);
					//Hide keyboard
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchTask.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
					return true;
				}
				return false;
			}
		});	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case android.R.id.home:
			taskAdapter.clear();
			taskAdapter.addAll(getTasksForListView());
			taskAdapter.notifyDataSetChanged();
			getActionBar().setDisplayHomeAsUpEnabled(false);
			searching = false;
			invalidateOptionsMenu();
			return true;
			
		case R.id.actionBar_SearchTaskIcon:
			getActionBar().setDisplayHomeAsUpEnabled(true);
			searching = true;
			invalidateOptionsMenu();
			return true;

		case R.id.actionBar_DeleteTaskIcon:
			if(selectedTask != null)
				DeleteDialog.newInstance().show(getFragmentManager(), "dialog");
			return true;
			
		case R.id.actionBar_AddTaskIcon:
			Intent addTaskIntent = new Intent(TaskList.this, NewTask.class);
			addTaskIntent.putExtra("ProjectID", projectId);
			addTaskIntent.putExtra("ProjectName", projectName);
			startActivityForResult(addTaskIntent, ADD_TASK_REQUEST);
			return true;
			
		case R.id.actionBar_ProjectPhotos:
			Intent intent = new Intent(TaskList.this, PhotoManager.class);
			intent.putExtra("ProjectID", projectId);
			intent.putExtra("ProjectName", projectName);
			startActivityForResult(intent, VIEW_PICTURES);
			return true;
			
		case R.id.actionBar_Menu_about:
			Intent intentAbout = new Intent(TaskList.this, About.class);
			startActivity(intentAbout);
			return true;
			
		case R.id.actionBar_Menu_syncall:
			syncAll();
			return true;
			
		case R.id.actionBar_Menu_user:
			if(currentUser != null)
				currentUser.logOut();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		if(taskSelected)
			menu.findItem(R.id.actionBar_DeleteTaskIcon).setVisible(true);
		else
			menu.findItem(R.id.actionBar_DeleteTaskIcon).setVisible(false);
		
		if (searching) {
			getActionBar().setDisplayShowTitleEnabled(false);
			menu.findItem(R.id.actionBar_SearchTaskIcon).setVisible(false);
			menu.findItem(R.id.actionBar_SearchTaskItem).setVisible(true);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		} else {
			getActionBar().setDisplayShowTitleEnabled(true);
			MenuItem searchItem = menu.findItem(R.id.actionBar_SearchTaskItem);
			final EditText searchProject = (EditText) searchItem.getActionView().findViewById(R.id.actionBar_SearchItemEditText);
			searchProject.setText("");
			menu.findItem(R.id.actionBar_SearchTaskIcon).setVisible(true);
			menu.findItem(R.id.actionBar_SearchTaskItem).setVisible(false);
		}
		
		MenuItem logMenuItem = menu.findItem(R.id.actionBar_Menu_user);
		if (currentUser != null) {
        	logMenuItem.setTitle("Log Out from " + currentUser.getUsername());
        } else {
        	checkUser();
        }
		
		return true;
	}
}
