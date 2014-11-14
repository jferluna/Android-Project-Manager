package com.vaquerosisd.projectmanager;

import java.io.File;
import java.util.List;

import com.vaquerosisd.adapters.ProjectListViewAdapter;
import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.DeleteProjectDialog;
import com.vaquerosisd.object.PhotoRef;
import com.vaquerosisd.object.Project;
import com.vaquerosisd.object.User;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.view.inputmethod.InputMethodManager;

public class ProjectList extends Activity implements WebserviceCallback {
	
	ProjectOperations db;					//Database Operations
	Project selectedProject;				//Last selected Project on ListView
	View selectedRow;						//Last selected row in ListView
	ProjectListViewAdapter projectAdapter;	//ListView adapter
	EditText searchProjectEditText;			//SearchBox of project
	
	boolean searching = false;
	

	static private final int LOGIN = 0;
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
		
		//UI handlers
		final ImageButton cancelSearch = (ImageButton) findViewById(R.id.cancelSearch);
		final ImageButton searchButton = (ImageButton) findViewById(R.id.searchProject);
		final ImageButton deleteButton = (ImageButton) findViewById(R.id.deleteProject);
		final ImageButton addButton = (ImageButton) findViewById(R.id.addProject);
		final ImageButton gotoProyectTasks = (ImageButton) findViewById(R.id.gotoProjectTasks);
		
		searchProjectEditText = (EditText) findViewById(R.id.searchProjectName);
		
		final ListView projectListView = (ListView) findViewById(R.id.projectList);
		
		//ListView Adapter
		projectAdapter = new ProjectListViewAdapter(getApplicationContext(), R.layout.listrow_project, getProjectsForListView());
		projectListView.setAdapter(projectAdapter);
		
		//onClickLsiteners
		cancelSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(searching) {
					projectAdapter.clear();
					projectAdapter.addAll(getProjectsForListView());
					projectAdapter.notifyDataSetChanged();
					cancelSearch.setVisibility(View.GONE);
					searching = false;
				}
			}
		});
		
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String project = searchProjectEditText.getText().toString();
				searchProject(project);
				
				//Hide keyboard
				searchProjectEditText.clearFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchProjectEditText.getWindowToken(), 0);
				
				cancelSearch.setVisibility(View.VISIBLE);
				searching = true;
			}
		});
		
		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectedProject != null)
					DeleteProjectDialog.newInstance().show(getFragmentManager(), "dialog");
			}
		});
		
		addButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ProjectList.this, NewProject.class);
				startActivityForResult(intent, ADD_PROJECT_REQUEST);
			}
		});
		
		gotoProyectTasks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(selectedProject != null) {
					Intent intent = new Intent(ProjectList.this, TaskList.class);
					intent.putExtra("ProjectID", selectedProject.getProjectId());
					intent.putExtra("ProjectName", selectedProject.getProjectName());
					Log.i("DEBUG", String.valueOf(selectedProject.getProjectId()));
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Select a project", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		projectListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        		selectedProject = (Project) projectAdapter.getItem(arg2);
        		if(selectedRow != null)
        			selectedRow.setBackgroundColor(Color.TRANSPARENT);
        		selectedRow = projectAdapter.getView(arg2, arg1, null);
        		selectedRow.setBackgroundColor(Color.LTGRAY);
        	}
		});
		
	} //End of onCreate
	
	@Override
	protected void onResume() {
		checkUser();
		super.onResume();
	}
	
	public List<Project> getProjectsForListView(){
		List<Project> list = db.getAllProjects();
		return list;
	}
	
	//Searches the project name in the project 
	public void searchProject(String projectName) {
		List<Project> searchProjectsList = db.searchProjects(searchProjectEditText.getText().toString());
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
			} else if (requestCode == LOGIN) {
				checkUser();
			}
		}    
    }
	
	//User Logging
	private User currentUser;
	
	public void callback(int code){
		System.out.println("Webserive code response: " + Integer.toString(code));
		checkUser();
	}
	
	public void checkUser() {
		
		currentUser = User.getUser(ProjectList.this);

		if (currentUser == null) // no hay sesion logeada
			Toast.makeText(getApplicationContext(), "Currently Logged Off", Toast.LENGTH_SHORT).show();

	}
	
	//Action Bar Menu
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		switch (item.getItemId())
		{
		case R.id.menu_about:
			//start about activity
			Intent intentAbout = new Intent(ProjectList.this, About.class);
			startActivity(intentAbout);
			return true;
			
		case R.id.menu_syncall:
			syncAll();
			return true;
			
		case R.id.menu_user:
			if(currentUser != null)
				currentUser.logOut();
			else {
				Intent intentLogin = new Intent(ProjectList.this, Login.class);
				startActivityForResult(intentLogin, LOGIN);
			}
			
			return true;
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem logMenuItem = menu.findItem(R.id.menu_user);
        if (currentUser == null) {
        	logMenuItem.setTitle("Log In");
        } else {
        	logMenuItem.setTitle("Log Out from " + currentUser.getUsername());
        }
        
		return super.onPrepareOptionsMenu(menu);
	}

} //end project class
