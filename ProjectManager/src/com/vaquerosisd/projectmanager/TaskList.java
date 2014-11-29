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
import com.vaquerosisd.dialog.DeleteProjectDialog;
import com.vaquerosisd.object.Task;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskList extends Activity {
	
	ProjectOperations db;				//Database Operations
	Task selectedTask;					//Last selected Task on ListView
	View selectedRow;					//Last selected row in ListView
	TaskListViewAdapter taskAdapter;	//ListView Adapter
	EditText searchTaskEditText;		//SearchBox of task
	int projectId;						//Project ID where the tasks belong
	String projectName;					//Project name associated to the Project ID
	
	boolean taskSelected = false;
	boolean searching = false;
		  
	static private final int ADD_TASK_REQUEST = 6;
	static private final int VIEW_PICTURES = 7;
	
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
		
		taskListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Task task = (Task) taskAdapter.getItem(position);
//				if(selectedRow != null) {
//					selectedRow.findViewById(R.id.list)
//				}
			}
			
		});
		
		//onClickListeners
//		cancelSearch.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(searching) {
//					taskAdapter.clear();
//					taskAdapter.addAll(getTasksForListView());
//					taskAdapter.notifyDataSetChanged();
//					cancelSearch.setVisibility(View.GONE);
//					searching = false;
//				}
//			}
//		});
		
//		searchTask.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				String task = searchTaskEditText.getText().toString();
//				searchTask(task);
//				cancelSearch.setVisibility(View.VISIBLE);
//				searching = true;
//			}
//		});
		
		//SetOnItemListener of task
//		gotoTask.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(selectedTask != null) {
//					Intent intent = new Intent(TaskList.this, ContentTask.class);
//					startActivity(intent);
//				}
//			}
//		});
		
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
		
	} //End of onCreate method
	
	//Defined functions	
	public void syncAll(){
		Toast.makeText(TaskList.this, "Sincronizar todo", Toast.LENGTH_SHORT).show();
	}
		
	public List<Task> getTasksForListView() {
		List<Task> list = db.getAllTasks(projectId);
		if(list.isEmpty())
			Log.i("Debug", "Lista vacia");
		return list;
	}
	
	//Searches the task name in the task list
	public void searchTask(String taskName) {
		List<Task> searchTaskList = db.searchTasks(projectId, searchTaskEditText.getText().toString());
		taskAdapter.clear();
		taskAdapter.addAll(searchTaskList);
		taskAdapter.notifyDataSetChanged();
	}
	
	//Delete the task and refresh the listView
	public void deleteTask() {
		String taskName = selectedTask.getTaskName();
		int taskId = selectedTask.getTaskId();
		
		db.deleteTask(taskId, taskName);
		taskAdapter.clear();
		taskAdapter.addAll(getTasksForListView());
		taskAdapter.notifyDataSetChanged();
		taskSelected = false;
		selectedTask = null;
//		selectedRow.findViewById(R.id.listTask_information).setBackgroundColor(Color.TRANSPARENT);
//		selectedRow = null;
//		invalidateOptionsMenu();
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
	
	//**************************************************************************************
	//Menu methods
	//**************************************************************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		getMenuInflater().inflate(R.menu.task_menu, menu);
		
		//Get view references to the search elements, searchEditText and clearSearch from the menu view
		MenuItem searchItem =
				menu.findItem(R.id.actionBar_searchTaskItem);
		Button clearSearchTask = 
				(Button) searchItem.getActionView().findViewById(R.id.actionBar_ClearSearch);
		final AutoCompleteTextView searchTask = 
				(AutoCompleteTextView) searchItem.getActionView().findViewById(R.id.actionBar_SearchItemEditText);
		
		//Get all tasks names and add them to an adapter for the AutoCompleteTextView
		//in order to show the suggested tasks on a drop-down list
		List<Task> allTasks = db.getAllTasks(projectId);
		String[] tasksNames = new String[allTasks.size()];
		
		for(int i = 0; i < allTasks.size(); i++)
			tasksNames[i] = allTasks.get(i).getTaskName();

		ArrayAdapter<String> tasksAdapter = 
				new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasksNames);
		searchTask.setAdapter(tasksAdapter);
		//Set letter threshold in order to start showing the suggested tasks in the drop-down list
		searchTask.setThreshold(1);
		
		//Shows keyboard input on screen when click on search EditText
		if(searching) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		}
		
		//Clear text of search AutoCompleteTextView
		clearSearchTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchTask.setText("");
				
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		switch (item.getItemId())
		{
//		case android.R.id.home:
//			taskAdapter.clear();
//			taskAdapter.addAll(getProjectsForListView());
//			taskAdapter.notifyDataSetChanged();
//			ActionBar actionBarHome = getActionBar();
//		    actionBarHome.setDisplayHomeAsUpEnabled(false);
//			searching = false;
//			invalidateOptionsMenu();
//			return true;
			
		case R.id.actionBar_searchTaskIcon:
			ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
			searching = true;
			invalidateOptionsMenu();
			return true;

		case R.id.actionBar_deleteTaskIcon:
			if(selectedTask != null)
				DeleteProjectDialog.newInstance().show(getFragmentManager(), "dialog");
			return true;
			
		case R.id.actionBar_addTaskIcon:
			Intent addTaskIntent = new Intent(TaskList.this, NewTask.class);
			addTaskIntent.putExtra("ProjectID", projectId);
			addTaskIntent.putExtra("ProjectName", projectName);
			startActivityForResult(addTaskIntent, ADD_TASK_REQUEST);
			return true;
			
		case R.id.actionBar_projectPhotos:
			Intent intent = new Intent(TaskList.this, PhotoManager.class);
			intent.putExtra("ProjectID", projectId);
			intent.putExtra("ProjectName", projectName);
			startActivityForResult(intent, VIEW_PICTURES);
			return true;
			
		case R.id.actionBar_menu_about:
			//start about activity
			Intent intentAbout = new Intent(TaskList.this, About.class);
			startActivity(intentAbout);
			return true;
			
//		case R.id.actionBar_menu_syncall:
//			syncAll();
//			return true;
			
//		case R.id.actionBar_menu_user:
//			if(currentUser != null)
//				currentUser.logOut();
//			else {
//				Intent intentLogin = new Intent(ProjectList.this, Login.class);
//				startActivityForResult(intentLogin, LOGIN);
//			}
//			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
