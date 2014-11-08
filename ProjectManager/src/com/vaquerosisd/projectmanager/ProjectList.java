package com.vaquerosisd.projectmanager;

import java.io.File;
import java.util.List;

import com.vaquerosisd.adapters.ProjectListViewAdapter;
import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.DeleteProjectDialog;
import com.vaquerosisd.object.PhotoRef;
import com.vaquerosisd.object.Project;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class ProjectList extends Activity {
	
	ProjectOperations db;					//Database Operations
	Project selectedProject;				//Last selected Project on ListView
	View selectedRow;						//Last selected row in ListView
	ProjectListViewAdapter projectAdapter;	//ListView adapter
	EditText searchProjectEditText;			//SearchBox of project
	
	boolean searching = false;
	boolean projectSelected = false;
	
	static private final int ADD_PROJECT_REQUEST = 5;

	//Funciones definidas	
	public void syncAll(){
		Toast.makeText(ProjectList.this, "Sincronizar todo", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		//ListView
		final ListView projectListView = (ListView) findViewById(R.id.projectList);
		projectAdapter = new ProjectListViewAdapter(getApplicationContext(), R.layout.listrow_project, getProjectsForListView());
		projectListView.setAdapter(projectAdapter);
		
//		gotoProyectTasks.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(selectedProject != null) {
//					Intent intent = new Intent(ProjectList.this, TaskList.class);
//					intent.putExtra("ProjectID", selectedProject.getProjectId());
//					intent.putExtra("ProjectName", selectedProject.getProjectName());
//					Log.i("DEBUG", String.valueOf(selectedProject.getProjectId()));
//					startActivity(intent);
//				} else {
//					Toast.makeText(getApplicationContext(), "Select a project", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		
		
		projectListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		selectedProject = (Project) projectAdapter.getItem(arg2);
        		if(selectedRow != null)
        			selectedRow.setBackgroundColor(Color.TRANSPARENT);
        		selectedRow = projectAdapter.getView(arg2, arg1, null);
        		selectedRow.setBackgroundColor(Color.LTGRAY);
        		projectSelected = true;
        		invalidateOptionsMenu();
        	}
			
		});
		
	} //End of onCreate
	
	public List<Project> getProjectsForListView(){
		List<Project> list = db.getAllProjects();
		return list;
	}
	
	//Searches the project name in the project 
	public void searchProject(String projectName) {
		List<Project> searchProjectsList = db.searchProjects(projectName);
		projectAdapter.clear();
		projectAdapter.addAll(searchProjectsList);
		projectAdapter.notifyDataSetChanged();
	}
	
	//Delete the project and refresh the ListView
	public void deleteProject() {
		
		String projectName = selectedProject.getProjectName();
		int projectID = selectedProject.getProjectId();
		
		deleteProjectPhotos();
		
		Log.i("DEBUG", projectName);
		db.deleteProjectPhotos(projectID);
		db.deleteProject(projectName, projectID);
		projectAdapter.clear();
		projectAdapter.addAll(getProjectsForListView());
		projectAdapter.notifyDataSetChanged();
		projectSelected = false;
		selectedProject = null;
		invalidateOptionsMenu();
	}
	
	private void deleteProjectPhotos()
	{
		List<PhotoRef> photosList;
		int projectID = selectedProject.getProjectId();
		
		photosList = db.getAllPhotos(projectID);
		
		//Delete references
		db.deleteProjectPhotos(projectID);
		
		//Delete actual files
		for(PhotoRef photo: photosList)
		{
			File photoFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES
								+ "/ProjectManager/" + photo.getPhotoPath());
					
			if(photoFile.exists())
				photoFile.delete();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			//Reloads the ListView when adding a new project on NewProject.class
			if(requestCode == ADD_PROJECT_REQUEST) {
				projectAdapter.clear();
				projectAdapter.addAll(getProjectsForListView());
				projectAdapter.notifyDataSetChanged();
			}
		}    
    }
	
	//**************************************************************************************
	//Menu methods
	//**************************************************************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_menu, menu);
		
		if(searching) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		}
		
		
		MenuItem cancelSearchItem = menu.findItem(R.id.actionBar_searchProjectItem);
		Button cancelSearchProject = (Button) cancelSearchItem.getActionView().findViewById(R.id.actionBar_cancelSearch);
		MenuItem searchItem = menu.findItem(R.id.actionBar_searchProjectItem);
		final EditText searchProject = (EditText) searchItem.getActionView().findViewById(R.id.actionBar_searchProjectEditText);
		
		//Cancel behavior
		cancelSearchProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActionBar().setDisplayShowTitleEnabled(true);
				searchProject.setText("");
			}
		});
		
		//Search project
		searchProject.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_DONE){
					String project = searchProject.getText().toString();
					searchProject(project);
					
					//Hide keyboard
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchProject.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
					
					searching = true;
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
			projectAdapter.clear();
			projectAdapter.addAll(getProjectsForListView());
			projectAdapter.notifyDataSetChanged();
			ActionBar actionBarHome = getActionBar();
		    actionBarHome.setDisplayHomeAsUpEnabled(false);
			searching = false;
			invalidateOptionsMenu();
			return true;
			
		case R.id.actionBar_searchProjectIcon:
			ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
			searching = true;
			invalidateOptionsMenu();
			return true;

		case R.id.actionBar_deleteProjectIcon:
			if(selectedProject != null)
				DeleteProjectDialog.newInstance().show(getFragmentManager(), "dialog");
			return true;
			
		case R.id.actionBar_addProjectIcon:
			Intent addProjectIntent = new Intent(ProjectList.this, NewProject.class);
			startActivityForResult(addProjectIntent, ADD_PROJECT_REQUEST);
			return true;
			
		case R.id.actionBar_menu_about:
			//start about activity
			Intent aboutIntent = new Intent(ProjectList.this, About.class);
			startActivity(aboutIntent);
			return true;
			
		case R.id.actionBar_menu_syncall:
			syncAll();
			return true;
			
		case R.id.actionBar_menu_user:
			//start user activity
			Intent i = new Intent(ProjectList.this, Users.class);
			startActivity(i);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		if(projectSelected) {
			MenuItem deleteProject = menu.findItem(R.id.actionBar_deleteProjectIcon);
			deleteProject.setVisible(projectSelected);
		} else if (searching) {
			getActionBar().setDisplayShowTitleEnabled(false);
			MenuItem searchProjectIcon = menu.findItem(R.id.actionBar_searchProjectIcon);
			searchProjectIcon.setVisible(false);
			MenuItem searchProjectEditText = menu.findItem(R.id.actionBar_searchProjectItem);
			searchProjectEditText.setVisible(true);
		} else if(!searching) {
			getActionBar().setDisplayShowTitleEnabled(true);
			MenuItem searchItem = menu.findItem(R.id.actionBar_searchProjectItem);
			final EditText searchProject = (EditText) searchItem.getActionView().findViewById(R.id.actionBar_searchProjectEditText);
			searchProject.setText("");
			MenuItem searchProjectIcon = menu.findItem(R.id.actionBar_searchProjectIcon);
			searchProjectIcon.setVisible(true);
			MenuItem searchProjectEditText = menu.findItem(R.id.actionBar_searchProjectItem);
			searchProjectEditText.setVisible(false);
		}
		return true;
	}

} //End ProjectList class
