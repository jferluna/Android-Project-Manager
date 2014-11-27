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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	//Database info
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "projectManager.db";
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
	public static final String PROJECTS_COLUMN_COVER_PATH = "CoverPath";
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
	//Photos table Constants
	public static final String TABLE_PHOTOS = "PHOTOs";
	public static final String PHOTOS_COLUMN_PHOTO_ID = "_photoID";
	public static final String PHOTOS_COLUMN_PHOTO_PATH = "photoPath";
	public static final String PHOTOS_COLUMN_PROJECT_ID = "projectID";
	
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_PROJECTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS + " ( " + 
											PROJECTS_COLUMN_PROJECT_ID + " INTEGER PRIMARY KEY, " + 
											PROJECTS_COLUMN_PROJECT_NAME + " TEXT, " + 
											PROJECTS_COLUMN_STARTDATE_YEAR + " INTEGER, " +
											PROJECTS_COLUMN_STARTDATE_MONTH + " INTEGER, " +
											PROJECTS_COLUMN_STARTDATE_DAY + " INTEGER, " +
											PROJECTS_COLUMN_DUEDATE_YEAR + " INTEGER, " + 
											PROJECTS_COLUMN_DUEDATE_MONTH + " INTEGER, " +
											PROJECTS_COLUMN_DUEDATE_DAY + " INTEGER, " +
											PROJECTS_COLUMN_PROJECT_STATUS + " TEXT, " + 
											PROJECTS_COLUMN_OPEN_TASKS + " INTEGER, " + 
											PROJECTS_COLUMN_TOTAL_TASK + " INTEGER, " +
											PROJECTS_COLUMN_COVER_PATH + " TEXT)";
		
		String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + " ( " + 
											TASKS_COLUMN_TASK_ID + " INTEGER PRIMARY KEY, " + 
											TASKS_COLUMN_PROJECT_ID + " INTEGER, " +
											TASKS_COLUMN_TASK_NAME + " TEXT, " +
											TASKS_COLUMN_STATUS + " TEXT, " +
											TASKS_COLUMN_PRIORITY + " TEXT, " +
											TASKS_COLUMN_PERCENTAJE_DONE + " INTEGER, " +
											TASKS_COLUMN_STARTDATE_YEAR + " INTEGER, " + 
											TASKS_COLUMN_STARTDATE_MONTH + " INTEGER, " + 
											TASKS_COLUMN_STARTDATE_DAY + " INTEGER, " + 
											TASKS_COLUMN_DUEDATE_YEAR + " INTEGER, " + 
											TASKS_COLUMN_DUEDATE_MONTH + " INTEGER, " + 
											TASKS_COLUMN_DUEDATE_DAY + " INTEGER, " + 
											TASKS_COLUMN_PHOTO_PATH + " TEXT, " + 
											TASKS_COLUMN_DESCRIPTION + " TEXT);";

		String CREATE_PHOTOS_TABLE = "CREATE TABLE " + TABLE_PHOTOS + " ( " + 
											PHOTOS_COLUMN_PHOTO_ID + " INTEGER PRIMARY KEY, " + 
											PHOTOS_COLUMN_PHOTO_PATH + " TEXT, " + 
											PHOTOS_COLUMN_PROJECT_ID + " INTEGER)";
		
		Log.i("DB projects table onCreate", CREATE_PROJECTS_TABLE);
		Log.i("DB tasks table onCreate", CREATE_TASKS_TABLE);
		Log.i("DB photo table onCreate", CREATE_PHOTOS_TABLE);
		
		db.execSQL(CREATE_PROJECTS_TABLE);
		db.execSQL(CREATE_TASKS_TABLE);
		db.execSQL(CREATE_PHOTOS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		String DELETE_PROJECTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PROJECTS;
		String DELETE_TASKS_TABLE = "DROP TABLE IF EXISTS " + TABLE_TASKS;
		String DELETE_PHOTOS_TABLE = "DROP TABLE IF EXISTS " + TABLE_PHOTOS;
		
		db.execSQL(DELETE_PROJECTS_TABLE);
		db.execSQL(DELETE_TASKS_TABLE);
		db.execSQL(DELETE_PHOTOS_TABLE);
		onCreate(db);
	}

}
