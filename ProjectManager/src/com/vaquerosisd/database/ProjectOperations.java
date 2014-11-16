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

package com.vaquerosisd.database;

import java.util.ArrayList;
import java.util.List;

import com.vaquerosisd.object.PhotoRef;
import com.vaquerosisd.object.Project;
import com.vaquerosisd.object.Task;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProjectOperations {
	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	
	//Project Table Constants
	public static final String TABLE_PROJECTS = "projects";
	public static final String PROJECTS_COLUMN_PROJECT_ID = "_projectID";
	public static final String PROJECTS_COLUMN_PROJECT_NAME = "ProjectName";
	public static final String PROJECTS_COLUMN_PROJECT_STARTDATE = "StartDate";
	public static final String PROJECTS_COLUMN_PROJECT_DUEDATE = "DueDate";
	public static final String PROJECTS_COLUMN_PROJECT_STATUS = "Status";
	public static final String PROJECTS_COLUMN_OPEN_TASKS = "OpenTasks";
	public static final String PROJECTS_COLUMN_TOTAL_TASK = "TotalTasks";
	//Task Table Constants
	public static final String TABLE_TASKS = "tasks";
	public static final String TASKS_COLUMN_TASK_ID = "_taskID";
	public static final String TASKS_COLUMN_TASK_NAME = "taskName";
	public static final String TASKS_COLUMN_PROJECT_ID = "projectID";
	//Photos table Constants
	public static final String TABLE_PHOTOS = "photos";
	public static final String PHOTOS_COLUMN_PHOTO_ID = "_photoID";
	public static final String PHOTOS_COLUMN_PHOTO_PATH = "photoPath";
	public static final String PHOTOS_COLUMN_PROJECT_ID = "projectID";
	
		
	public ProjectOperations(Context context) {
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		db = dbHelper.getReadableDatabase();
	}
	
	public void close() throws SQLException {
		dbHelper.close();
		db.close();
	}
	
	//--------------------------------------------------------------------------------
	//Project Table Operations
	//--------------------------------------------------------------------------------
	
	public void addProject(String projectName, String status, String[] startDate, String[] dueDate) {
		ContentValues values = new ContentValues();
		values.put(PROJECTS_COLUMN_PROJECT_NAME, projectName);
		values.put(PROJECTS_COLUMN_PROJECT_STATUS, status);
		values.put(PROJECTS_COLUMN_PROJECT_STARTDATE, startDate[0] + "/" + startDate[1] + "/" + startDate[2]);
		values.put(PROJECTS_COLUMN_PROJECT_DUEDATE, dueDate[0] + "/" + dueDate[1] + "/" + dueDate[2]);
		values.put(PROJECTS_COLUMN_OPEN_TASKS, 0);
		values.put(PROJECTS_COLUMN_TOTAL_TASK, 0);
		db.insert(TABLE_PROJECTS, null, values);
	}
	
	public boolean deleteProject(String projectName, int projectID) {
		boolean result = false;
		db.delete(TABLE_PROJECTS, PROJECTS_COLUMN_PROJECT_NAME + " = ? AND " + PROJECTS_COLUMN_PROJECT_ID + " = ?", new String[] {projectName, String.valueOf(projectID)});
		result = true;
		return result;
	}
	
	public List<Project> searchProjects(String projectName){
		List<Project> projectList = new ArrayList<Project>();
		Project project;
		
		String query;
		
		if(!projectName.equals(""))
			query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE " + PROJECTS_COLUMN_PROJECT_NAME + " LIKE \"" + projectName + "%\";";
		else
			query = "SELECT * FROM " + TABLE_PROJECTS;
		
		Cursor cursor =  db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		int index;
		
		while(cursor.move(1) == true) {
			project = new Project();
			
			//Get all project columns
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_ID);
			project.setId(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_NAME);
			project.setName(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STATUS);
			project.setStatus(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STARTDATE);
			project.setStartDate(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_DUEDATE);
			project.setDueDate(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_OPEN_TASKS);
			project.setOpenTasks(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_TOTAL_TASK);
			project.setTotalTasks(cursor.getInt(index));
			
			//Add project to list
			projectList.add(project);
		}
		cursor.close();
		
		return projectList;
	}
	
	public List<Project> getAllProjects() {
		List<Project> projectList = new ArrayList<Project>();
		Project project;
		int index;
		
		String query = "Select * FROM " + TABLE_PROJECTS + " ORDER BY " + PROJECTS_COLUMN_PROJECT_STARTDATE + ";";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		while(cursor.move(1) == true) {
			project = new Project();
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_ID);
			project.setId(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_NAME);
			project.setName(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STATUS);
			project.setStatus(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STARTDATE);
			project.setStartDate(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_DUEDATE);
			project.setDueDate(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_OPEN_TASKS);
			project.setOpenTasks(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_TOTAL_TASK);
			project.setTotalTasks(cursor.getInt(index));
			
			projectList.add(project);
		}
		cursor.close();
		
		return projectList;
	}
	
	//--------------------------------------------------------------------------------
	//Tasks Table Operations
	//--------------------------------------------------------------------------------
	
	public void addTask(String taskName, int projectId) {
		ContentValues values = new ContentValues();
		values.put(TASKS_COLUMN_TASK_NAME, taskName);
		values.put(TASKS_COLUMN_PROJECT_ID, projectId);
		db.insert(TABLE_TASKS, null, values);
	}
	
	public boolean deleteTask(String taskName, int taskId) {
		boolean result = false;
		db.delete(TABLE_TASKS, TASKS_COLUMN_TASK_NAME + " = ? AND " + TASKS_COLUMN_TASK_ID + " = ?", new String[] {taskName, String.valueOf(taskId)});
		result = true;
		return result;
	}
	
	public List<Task> searchTasks(String taskName, int projectId){
		List<Task> taskList = new ArrayList<Task>();
		Task task;
		
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_TASK_NAME + " LIKE \"" + taskName + "%\" AND " + TASKS_COLUMN_PROJECT_ID + " = \"" + projectId + "\";";
		Cursor cursor =  db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		while(cursor.move(1) == true) {
			task = new Task();
			task.setTaskId(cursor.getInt(0));
			task.setTaskName(cursor.getString(1));
			task.setProjectId(cursor.getInt(2));
			taskList.add(task);
		}
		cursor.close();
		
		return taskList;
	}
	
	public List<Task> getAllTasks(int projectId) {
		List<Task> taskList = new ArrayList<Task>();
		Task task;
		
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_PROJECT_ID + " = \"" + projectId + "\";";
		Log.i("DEBUG", query);
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToPosition(-1);

		while(cursor.move(1) == true) {
			task = new Task();
			task.setTaskId(cursor.getInt(0));
			task.setTaskName(cursor.getString(1));
			task.setProjectId(cursor.getInt(2));
			taskList.add(task);
		}
		cursor.close();
		
		return taskList;
	}
	
	//--------------------------------------------------------------------------------
	//PHOTOs Table Operations
	//--------------------------------------------------------------------------------
	public void addPhoto(String photoPath, int projectId) {
		ContentValues values = new ContentValues();
		values.put(PHOTOS_COLUMN_PHOTO_PATH, photoPath);
		values.put(PHOTOS_COLUMN_PROJECT_ID, projectId);
		db.insert(TABLE_PHOTOS, null, values);
	}
	
	public boolean deletePhoto(String photoPath, int photoId) {
		boolean result = false;
		db.delete(TABLE_PHOTOS, PHOTOS_COLUMN_PHOTO_PATH + " = ? AND " + PHOTOS_COLUMN_PHOTO_ID + " = ?",
				new String[] {photoPath, String.valueOf(photoId)});
		result = true;
		return result;
	}
	
	public boolean deletePhoto(int photoId) {
		boolean result = false;
		db.delete(TABLE_PHOTOS, PHOTOS_COLUMN_PHOTO_ID + " = ?", new String[] {String.valueOf(photoId)});
		result = true;
		return result;
	}
	
	public boolean deleteProjectPhotos(int projectId)
	{
		boolean result = false;
		db.delete(TABLE_PHOTOS, PHOTOS_COLUMN_PROJECT_ID + " = ?", new String[] {String.valueOf(projectId)});
		result = true;
		return result;
	}
	
	public boolean deleteAllPhotos()
	{
		boolean result = false;
		db.delete(TABLE_PHOTOS, null, null);
		result = true;
		return result;
	}
	
	public List<PhotoRef> getAllPhotos(int projectId){
		List<PhotoRef> fileList = new ArrayList<PhotoRef>();
		PhotoRef PhotoFile;
		
		String query = "Select * FROM " + TABLE_PHOTOS + " WHERE " + PHOTOS_COLUMN_PROJECT_ID + " = \"" + projectId + "\";";
		
		Cursor cursor =  db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		while(cursor.move(1) == true) {
			PhotoFile = new PhotoRef();
			PhotoFile.setPhotoId(cursor.getInt(0));
			PhotoFile.setPhotoPath(cursor.getString(1));
			PhotoFile.setProjectId(cursor.getInt(2));
			fileList.add(PhotoFile);
		}
		cursor.close();
		
		return fileList;
	}
}

