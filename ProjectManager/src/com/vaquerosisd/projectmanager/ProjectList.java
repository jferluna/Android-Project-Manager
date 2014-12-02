package com.vaquerosisd.projectmanager;

import java.io.File;
import java.util.List;

import com.vaquerosisd.adapters.ProjectListViewAdapter;
import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.DeleteProjectDialog;
import com.vaquerosisd.dialog.EditProject;
import com.vaquerosisd.object.JsonWrapper;
import com.vaquerosisd.object.PhotoRef;
import com.vaquerosisd.object.Project;
import com.vaquerosisd.object.User;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

public class ProjectList extends Activity implements WebserviceCallback {
	
	ProjectOperations db;					//Database Operations
	ProjectListViewAdapter projectAdapter;	//ListView adapter
	Project selectedProject;				//Selected project object
	View selectedRow;						//View of selected row in ListView
	
	//Actions occurring
	boolean searching = false;
	boolean projectSelected = false;
	
	//Constants
	static private final int LOGIN = 0;
	static private final int ADD_PROJECT_REQUEST = 5;
	
	//User Logging
	private User currentUser;

	//Defined functions	
	public void syncAll(){
		Toast.makeText(ProjectList.this, "Syncing Everything", Toast.LENGTH_SHORT).show();
		
		currentUser.sync();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		//ListView
		final ListView projectListView = (ListView) findViewById(R.id.listProject_ProjectList);
		projectAdapter = new ProjectListViewAdapter(getApplicationContext(), R.layout.listrow_project, getProjectsForListView());
		projectListView.setAdapter(projectAdapter);
		
		projectListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				//Launches TaskList activity of the clicked project.
        		Project project = (Project) projectAdapter.getItem(position);
        		if(selectedRow != null){
        			selectedRow.findViewById(R.id.listProject_information).setBackgroundColor(Color.TRANSPARENT);
            		selectedRow = null;
        		}
        		Intent intent = new Intent(ProjectList.this, TaskList.class);
				intent.putExtra("ProjectID", project.getId());
				intent.putExtra("ProjectName", project.getName());
				startActivity(intent);
			}
			
		});
		
		projectListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				
				//Gets pointer to project object and row view. Launch request to refresh actionBar UI according to selected context.
				selectedProject = (Project) projectAdapter.getItem(position);
				if(selectedRow != null)
					selectedRow.findViewById(R.id.listProject_information).setBackgroundColor(Color.WHITE);
				selectedRow = projectAdapter.getView(position, view, null);
        		selectedRow.findViewById(R.id.listProject_information).setBackgroundColor(Color.parseColor("#42A5F5"));
				projectSelected = true;
        		invalidateOptionsMenu();
				return true;
			}
			
		});
		
	} //End of onCreate
	
	
	//Defined functions	
	@Override
	protected void onResume() {
		super.onResume();
		checkUser();
		projectAdapter.clear();
		projectAdapter.addAll(getProjectsForListView());
		projectAdapter.notifyDataSetChanged();
	}
	
	//Gets all projects in a list object.

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
	
	//Delete the project and its information. Reload the ListView.
	public void deleteProject() {
		
		String projectName = selectedProject.getName();
		int projectID = selectedProject.getId();

		deleteProjectPhotos();		
		db.deleteProjectPhotos(projectID);
		db.deleteProject(projectName, projectID);
		projectAdapter.clear();
		projectAdapter.addAll(getProjectsForListView());
		projectAdapter.notifyDataSetChanged();
		projectSelected = false;
		selectedProject = null;
		selectedRow.findViewById(R.id.listProject_information).setBackgroundColor(Color.TRANSPARENT);
		selectedRow = null;
		invalidateOptionsMenu();
	}
	
	//Deletes all the project related photos
	private void deleteProjectPhotos()
	{
		List<PhotoRef> photosList;
		int projectID = selectedProject.getId();
		
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
	
	
	public void callback(JsonWrapper response){
		System.out.println("Webserive code response: " + response.getCode());
		
		if (response.getCode() == 6){
			Toast.makeText(getApplicationContext(), "Content Synced", Toast.LENGTH_SHORT).show();
		}
		
		checkUser();
	}
	
	public void checkUser() {
		
		currentUser = User.getUser(ProjectList.this);

		if (currentUser == null) // no hay sesion logeada
		{
			//Toast.makeText(getApplicationContext(), "Currently Logged Off", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(ProjectList.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}
	
	//Action Bar Menu
	@Override
	public void onBackPressed(){
		if(searching) {
			//Clears the searched projects
			projectAdapter.clear();
			projectAdapter.addAll(getProjectsForListView());
			projectAdapter.notifyDataSetChanged();
			//Clears up navigation
			getActionBar().setDisplayHomeAsUpEnabled(false);
			searching = false;
			//Request to refresh ActionBar UI
			invalidateOptionsMenu();
			return;
		} else if (projectSelected) {
			//Clears pointer to selected row and removes all enhanced view properties
			selectedRow.findViewById(R.id.listProject_information).setBackgroundColor(Color.WHITE);
			selectedRow = null;
			projectSelected = false;
			//Request to refresh ActionBar UI
			invalidateOptionsMenu();
			return;
		} else {
			//Default onBackPressed action
			super.onBackPressed();
		}
	}
	
	//**************************************************************************************
	//Menu methods
	//**************************************************************************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu
		getMenuInflater().inflate(R.menu.project_menu, menu);
		
		//Shows keyboard input on screen
		if(searching) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
		}
		
		
		MenuItem searchItem = menu.findItem(R.id.actionBar_SearchProjectItem);
		Button clearSearchProject = (Button) searchItem.getActionView().findViewById(R.id.actionBar_ClearSearch);
		final AutoCompleteTextView searchProject = (AutoCompleteTextView) searchItem.getActionView().findViewById(R.id.actionBar_SearchItemEditText);
		
		//AutocompleteTextView behavior
		List<Project> projectTemp = db.getAllProjects();
		String[] projects = new String[projectTemp.size()];
		for(int i = 0; i < projectTemp.size(); i++){
			projects[i] = projectTemp.get(i).getName();
		}
		ArrayAdapter<String> projectsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projects);
		searchProject.setAdapter(projectsAdapter);
		searchProject.setThreshold(1);
		
		
		//Clear text of search AutoCompleteTextView
		clearSearchProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
		case android.R.id.home:
			projectAdapter.clear();
			projectAdapter.addAll(getProjectsForListView());
			projectAdapter.notifyDataSetChanged();
			ActionBar actionBarHome = getActionBar();
		    actionBarHome.setDisplayHomeAsUpEnabled(false);
			searching = false;
			invalidateOptionsMenu();
			return true;
			
		case R.id.actionBar_SearchProjectIcon:
			ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
			searching = true;
			invalidateOptionsMenu();
			return true;

		case R.id.actionBar_DeleteProjectIcon:
			if(selectedProject != null)
				DeleteProjectDialog.newInstance().show(getFragmentManager(), "dialog");
			return true;
			
		case R.id.actionBar_AddProjectIcon:
			Intent addProjectIntent = new Intent(ProjectList.this, NewProject.class);
			startActivityForResult(addProjectIntent, ADD_PROJECT_REQUEST);
			return true;
			
		case R.id.actionBar_EditProjectIcon:
			int id = selectedProject.getId();
			DialogFragment editProjectDialog = EditProject.newInstance(id);
			editProjectDialog.show(getFragmentManager(), "NoticeDialogFragment");

			projectAdapter.clear();
			projectAdapter.addAll(getProjectsForListView());
			projectAdapter.notifyDataSetChanged();
			return true;
			
		case R.id.actionBar_Menu_About:
			//start about activity
			Intent intentAbout = new Intent(ProjectList.this, About.class);
			startActivity(intentAbout);
			return true;
			
		case R.id.actionBar_Menu_Syncall:
			syncAll();
			return true;
			
		case R.id.actionBar_Menu_User:
			if(currentUser != null)
				currentUser.logOut();
			
			//checkUser();
			
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		if(projectSelected) {
			MenuItem deleteProject = menu.findItem(R.id.actionBar_DeleteProjectIcon);
			deleteProject.setVisible(true);
			MenuItem editProjectIcon = menu.findItem(R.id.actionBar_EditProjectIcon);
			editProjectIcon.setVisible(true);
		} else {
			MenuItem deleteProject = menu.findItem(R.id.actionBar_DeleteProjectIcon);
			deleteProject.setVisible(false);
			MenuItem editProjectIcon = menu.findItem(R.id.actionBar_EditProjectIcon);
			editProjectIcon.setVisible(false);
		}
		
		if (searching) {
			getActionBar().setDisplayShowTitleEnabled(false);
			MenuItem searchProjectIcon = menu.findItem(R.id.actionBar_SearchProjectIcon);
			searchProjectIcon.setVisible(false);
			MenuItem searchProjectEditText = menu.findItem(R.id.actionBar_SearchProjectItem);
			searchProjectEditText.setVisible(true);
		} else {
			getActionBar().setDisplayShowTitleEnabled(true);
			MenuItem searchItem = menu.findItem(R.id.actionBar_SearchProjectItem);
			final EditText searchProject = (EditText) searchItem.getActionView().findViewById(R.id.actionBar_SearchItemEditText);
			searchProject.setText("");
			MenuItem searchProjectIcon = menu.findItem(R.id.actionBar_SearchProjectIcon);
			searchProjectIcon.setVisible(true);
			MenuItem searchProjectEditText = menu.findItem(R.id.actionBar_SearchProjectItem);
			searchProjectEditText.setVisible(false);
		}
		
		MenuItem logMenuItem = menu.findItem(R.id.actionBar_Menu_User);
		if (currentUser != null) {
        	logMenuItem.setTitle("Log Out from " + currentUser.getUsername());
        } else {
        	checkUser();
        }
		
		return true;
	}

} //End ProjectList class
