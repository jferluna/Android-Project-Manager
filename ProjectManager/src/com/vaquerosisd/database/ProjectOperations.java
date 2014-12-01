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
	public static final String PROJECTS_COLUMN_PROJECT_ID = "projectID";
	public static final String PROJECTS_COLUMN_PROJECT_NAME = "ProjectName";
	public static final String PROJECTS_COLUMN_STARTDATE_YEAR = "yearStartDate";
	public static final String PROJECTS_COLUMN_STARTDATE_MONTH = "monthStartDate";
	public static final String PROJECTS_COLUMN_STARTDATE_DAY = "dayStartDate";
	public static final String PROJECTS_COLUMN_DUEDATE_YEAR = "yearDueDate";
	public static final String PROJECTS_COLUMN_DUEDATE_MONTH = "monthDueDate";
	public static final String PROJECTS_COLUMN_DUEDATE_DAY = "dayDueDate";
	public static final String PROJECTS_COLUMN_PROJECT_STATUS = "Status";
	public static final String PROJECTS_COLUMN_OPEN_TASKS = "OpenTasks";
	public static final String PROJECTS_COLUMN_TOTAL_TASK = "TotalTasks";
	public static final String PROJECTS_COLUMN_CONTENT_PATH = "ContentPath";
	
	//Task Table Constants
	public static final String TABLE_TASKS = "tasks";
	public static final String TASKS_COLUMN_TASK_ID = "taskID";
	public static final String TASKS_COLUMN_PROJECT_ID = "projectID";
	public static final String TASKS_COLUMN_TASK_NAME = "taskName";
	public static final String TASKS_COLUMN_STATUS = "status";
	public static final String TASKS_COLUMN_PRIORITY = "priority";
	public static final String TASKS_COLUMN_PERCENTAJE_DONE = "percentageDone";
	public static final String TASKS_COLUMN_STARTDATE_YEAR = "yearStartDate";
	public static final String TASKS_COLUMN_STARTDATE_MONTH = "monthStartDate";
	public static final String TASKS_COLUMN_STARTDATE_DAY = "dayStartDate";
	public static final String TASKS_COLUMN_DUEDATE_YEAR = "yearDueDate";
	public static final String TASKS_COLUMN_DUEDATE_MONTH = "monthDueDate";
	public static final String TASKS_COLUMN_DUEDATE_DAY = "dayDueDate";
	public static final String TASKS_COLUMN_PHOTO_PATH = "photoPath";
	public static final String TASKS_COLUMN_DESCRIPTION = "description";
	public static final String TASKS_COLUMN_CONTENT_PATH = "contentPath";
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
	
	public void addProject(String projectName, String status, int[] startDate, int[] dueDate, String contentPath) {
		ContentValues values = new ContentValues();
		values.put(PROJECTS_COLUMN_PROJECT_NAME, projectName);
		values.put(PROJECTS_COLUMN_PROJECT_STATUS, status);
		values.put(PROJECTS_COLUMN_STARTDATE_YEAR, startDate[0]);
		values.put(PROJECTS_COLUMN_STARTDATE_MONTH, startDate[1]);
		values.put(PROJECTS_COLUMN_STARTDATE_DAY, startDate[2]);
		values.put(PROJECTS_COLUMN_DUEDATE_YEAR, dueDate[0]);
		values.put(PROJECTS_COLUMN_DUEDATE_MONTH, dueDate[1]);
		values.put(PROJECTS_COLUMN_DUEDATE_DAY, dueDate[2]);
		values.put(PROJECTS_COLUMN_OPEN_TASKS, 0);
		values.put(PROJECTS_COLUMN_TOTAL_TASK, 0);
		values.put(PROJECTS_COLUMN_CONTENT_PATH, contentPath);
		db.insert(TABLE_PROJECTS, null, values);
	}
	
	public boolean deleteProject(String projectName, int projectID) {
		boolean result = false;
		db.delete(TABLE_PROJECTS, PROJECTS_COLUMN_PROJECT_NAME + " = ? AND " + PROJECTS_COLUMN_PROJECT_ID + " = ?", new String[] {projectName, String.valueOf(projectID)});
		result = true;
		return result;
	}
	
	
	
	public void updateProject(int id, String projectName, String status, int[] startDate, int[] dueDate ) {
		ContentValues arg = new ContentValues();
		String whereClause = PROJECTS_COLUMN_PROJECT_ID + " = " + id;
		arg.put(PROJECTS_COLUMN_PROJECT_NAME, projectName);
		arg.put(PROJECTS_COLUMN_PROJECT_STATUS, status);
		arg.put(PROJECTS_COLUMN_STARTDATE_YEAR, startDate[0]);
		arg.put(PROJECTS_COLUMN_STARTDATE_MONTH, startDate[1]);
		arg.put(PROJECTS_COLUMN_STARTDATE_DAY, startDate[2]);
		arg.put(PROJECTS_COLUMN_DUEDATE_YEAR, dueDate[0]);
		arg.put(PROJECTS_COLUMN_DUEDATE_MONTH, dueDate[1]);
		arg.put(PROJECTS_COLUMN_DUEDATE_DAY, dueDate[2]);
		db.update(TABLE_PROJECTS, arg, whereClause, null);
	}
	
	public List<Project> searchProjects(String projectName){
		List<Project> projectList = new ArrayList<Project>();
		Project project;
		
		String query;
		
		if(!projectName.equals(""))
			query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE " + PROJECTS_COLUMN_PROJECT_NAME + " LIKE \"%" + projectName + "%\";";
		else
			query = "SELECT * FROM " + TABLE_PROJECTS;
		Log.i("DB searchTasks query", query);
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
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_YEAR);
			project.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_MONTH);
			project.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_DAY);
			project.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_YEAR);
			project.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_MONTH);
			project.setMonthDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_DAY);
			project.setDayDueDate(cursor.getInt(index));
			
			project.setOpenTasks(getOpenTasksForProjectId(project.getId()));
			project.setTotalTasks(getTotalTasksForProjectId(project.getId()));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_CONTENT_PATH);
			project.setContentPath(cursor.getString(index));
			
			//Add project to list
			projectList.add(project);
		}
		cursor.close();
		
		return projectList;
	}
	
	public Project searchProjectById(int id){
		
		Project project = new Project();
		
		String query = "SELECT * FROM " + TABLE_PROJECTS + " WHERE " + PROJECTS_COLUMN_PROJECT_ID + " = " + id + ";";
		Log.i("DB searchProjectById query", query);
		Cursor cursor =  db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		int index;
		
		while(cursor.move(1) == true) {
			//Get all project columns
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_ID);
			project.setId(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_NAME);
			project.setName(cursor.getString(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STATUS);
			project.setStatus(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_YEAR);
			project.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_MONTH);
			project.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_DAY);
			project.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_YEAR);
			project.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_MONTH);
			project.setMonthDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_DAY);
			project.setDayDueDate(cursor.getInt(index));
			
			project.setOpenTasks(getOpenTasksForProjectId(project.getId()));
			project.setTotalTasks(getTotalTasksForProjectId(project.getId()));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_CONTENT_PATH);
			project.setContentPath(cursor.getString(index));
		}
		cursor.close();
		
		return project;
	}
	
	public List<Project> getAllProjects() {
		List<Project> projectList = new ArrayList<Project>();
		Project project;
		int index;
		
		String query = "Select * FROM " + TABLE_PROJECTS + " ORDER BY " + PROJECTS_COLUMN_DUEDATE_YEAR + ", " + PROJECTS_COLUMN_DUEDATE_MONTH + ", " + PROJECTS_COLUMN_DUEDATE_DAY + ";";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		
		while(cursor.move(1) == true) {
			project = new Project();
			
			//Get all project columns
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_ID);
			project.setId(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_NAME);
			project.setName(cursor.getString(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_PROJECT_STATUS);
			project.setStatus(cursor.getString(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_YEAR);
			project.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_MONTH);
			project.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_STARTDATE_DAY);
			project.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_YEAR);
			project.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_MONTH);
			project.setMonthDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(PROJECTS_COLUMN_DUEDATE_DAY);
			project.setDayDueDate(cursor.getInt(index));
			
			project.setOpenTasks(getOpenTasksForProjectId(project.getId()));
			project.setTotalTasks(getTotalTasksForProjectId(project.getId()));
			
			index = cursor.getColumnIndex(PROJECTS_COLUMN_CONTENT_PATH);
			project.setContentPath(cursor.getString(index));
			
			projectList.add(project);
		}
		cursor.close();
		
		return projectList;
	}
	
	public int getOpenTasksForProjectId(int id){
		String query = "SELECT " + TASKS_COLUMN_TASK_ID + " FROM " + TABLE_TASKS + " WHERE " + 
				TASKS_COLUMN_PROJECT_ID + " = " + id + " AND " +
				TASKS_COLUMN_STATUS + " <> \"Completed\";";
		Log.i("DB getOpenTasks", query);
		Cursor cursor = db.rawQuery(query, null);
		return cursor.getCount();		
	}
	
	public int getTotalTasksForProjectId(int id) {
		String query = "SELECT " + TASKS_COLUMN_TASK_ID + " FROM " + TABLE_TASKS + " WHERE " + 
				TASKS_COLUMN_PROJECT_ID + " = " + id + ";";
		Log.i("DB getOpenTasks", query);
		Cursor cursor = db.rawQuery(query, null);
		return cursor.getCount();	
	}
	
	public String getProjectContentPath (int id) {
		String query = "SELECT " + PROJECTS_COLUMN_CONTENT_PATH + " FROM " + TABLE_PROJECTS + " WHERE " + PROJECTS_COLUMN_PROJECT_ID + " = " + id + ";";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor.getString(0);
	}
	
	//--------------------------------------------------------------------------------
	//Tasks Table Operations
	//--------------------------------------------------------------------------------
	
	public void addTask(int projectId, String taskName, String status, String priority, int percentageDone, int[] startDate, 
			int[] dueDate, String photoPath, String description, String contentPath) {
		ContentValues values = new ContentValues();
		values.put(TASKS_COLUMN_PROJECT_ID, projectId);
		values.put(TASKS_COLUMN_TASK_NAME, taskName);
		values.put(TASKS_COLUMN_STATUS, status);
		values.put(TASKS_COLUMN_PRIORITY, priority);
		values.put(TASKS_COLUMN_PERCENTAJE_DONE, percentageDone);
		values.put(TASKS_COLUMN_STARTDATE_YEAR, startDate[0]);
		values.put(TASKS_COLUMN_STARTDATE_MONTH, startDate[1]);
		values.put(TASKS_COLUMN_STARTDATE_DAY, startDate[2]);
		values.put(TASKS_COLUMN_DUEDATE_YEAR, startDate[0]);
		values.put(TASKS_COLUMN_DUEDATE_MONTH, startDate[1]);
		values.put(TASKS_COLUMN_DUEDATE_DAY, startDate[2]);
		values.put(TASKS_COLUMN_PHOTO_PATH, photoPath);
		values.put(TASKS_COLUMN_DESCRIPTION, description);
		values.put(TASKS_COLUMN_CONTENT_PATH, contentPath);
		db.insert(TABLE_TASKS, null, values);
	}
	
	public boolean deleteTask(int taskId, String taskName) {
		db.delete(TABLE_TASKS, TASKS_COLUMN_TASK_NAME + " = ? AND " + TASKS_COLUMN_TASK_ID + " = ?", 
				new String[] {taskName, String.valueOf(taskId)});
		return true;
	}
	
	public List<Task> searchTasks(int projectId, String taskName){
		List<Task> taskList = new ArrayList<Task>();
		Task task;
		
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_TASK_NAME + " LIKE \"%" + 
				taskName + "%\" AND " + TASKS_COLUMN_PROJECT_ID + " = \"" + projectId + "\";";
		Log.i("DB searchTasks query", query);
		Cursor cursor =  db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		int index;
		
		while(cursor.move(1) == true) {
			task = new Task();
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_ID);
			task.setTaskId(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PROJECT_ID);
			task.setProjectId(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_NAME);
			task.setTaskName(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STATUS);
			task.setStatus(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PRIORITY);
			task.setPriority(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PERCENTAJE_DONE);
			task.setPercentage(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_YEAR);
			task.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_MONTH);
			task.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_DAY);
			task.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_YEAR);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_MONTH);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_DAY);
			task.setYearDueDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_PHOTO_PATH);
			task.setPhotoPath(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DESCRIPTION);
			task.setDescription(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_CONTENT_PATH);
			task.setContentPath(cursor.getString(index));
			
			taskList.add(task);
		}
		cursor.close();
		
		return taskList;
	}
	
	public List<Task> getAllTasks(int projectId) {
		List<Task> taskList = new ArrayList<Task>();
		Task task;
		
		String query = "Select * FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_PROJECT_ID + " = " + projectId + ";";
		Log.i("DB getAllTasks", query);
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		Log.i("Debug", "Cursor: " + cursor.getCount());
		int index;

		while(cursor.move(1) == true) {
			task = new Task();
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_ID);
			task.setTaskId(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PROJECT_ID);
			task.setProjectId(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_NAME);
			task.setTaskName(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STATUS);
			task.setStatus(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PRIORITY);
			task.setPriority(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PERCENTAJE_DONE);
			task.setPercentage(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_YEAR);
			task.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_MONTH);
			task.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_DAY);
			task.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_YEAR);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_MONTH);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_DAY);
			task.setYearDueDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_PHOTO_PATH);
			task.setPhotoPath(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DESCRIPTION);
			task.setDescription(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_CONTENT_PATH);
			task.setContentPath(cursor.getString(index));
			
			taskList.add(task);
		}
		cursor.close();
		
		return taskList;
	}
	
	public Task getTaskById(int taskId) {
		Task task = new Task();
		String query = "SELECT * FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_TASK_ID + " = " + taskId + ";";
		Log.i("DB getTaskById", query);
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToPosition(-1);
		int index;

		while(cursor.move(1) == true) {
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_ID);
			task.setTaskId(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PROJECT_ID);
			task.setProjectId(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_TASK_NAME);
			task.setTaskName(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STATUS);
			task.setStatus(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PRIORITY);
			task.setPriority(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_PERCENTAJE_DONE);
			task.setPercentage(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_YEAR);
			task.setYearStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_MONTH);
			task.setMonthStartDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_STARTDATE_DAY);
			task.setDayStartDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_YEAR);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_MONTH);
			task.setYearDueDate(cursor.getInt(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DUEDATE_DAY);
			task.setYearDueDate(cursor.getInt(index));
			
			index = cursor.getColumnIndex(TASKS_COLUMN_PHOTO_PATH);
			task.setPhotoPath(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_DESCRIPTION);
			task.setDescription(cursor.getString(index));
			index = cursor.getColumnIndex(TASKS_COLUMN_CONTENT_PATH);
			task.setContentPath(cursor.getString(index));
		}
		cursor.close();
		
		return task;
	}
	
	public String getTaskContentPath (int id) {
		String query = "SELECT " + TASKS_COLUMN_CONTENT_PATH + " FROM " + TABLE_TASKS + " WHERE " + TASKS_COLUMN_PROJECT_ID + " = " + id + ";";
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor.getString(0);
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

