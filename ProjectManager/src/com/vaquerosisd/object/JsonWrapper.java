package com.vaquerosisd.object;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.vaquerosisd.database.ProjectOperations;

public class JsonWrapper {
	
	private JSONObject jsonObject = null;
	
	private Context context;

	public JsonWrapper(JSONObject jsonObject, Context context){
		this.jsonObject = jsonObject;
		this.context = context;
	}
	
	
	public String getString(String key){
		String r = "";
		try {
			r =jsonObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}
	
	public int getInt(String key){
		int r = -1;
		try {
			r =Integer.parseInt(jsonObject.getString(key));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}
	
	// get project en formato project, regresa objeto project
	// get task, iterar sobre el arreglo para obtener la lista
	
	// Regresa codigo de respuesta de API
	public int getCode(){
		int r = -1;
		try {
			r =Integer.parseInt(jsonObject.getString("code"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return r;
	}
	
	
	//
	// Regresa projects en lista
	public void getProjects()
	{
		
		ProjectOperations db = new ProjectOperations(context);;					//Database Operations
		JSONArray projects = null;
		JSONArray tasks = null;
		
		db.open();
		db.deleteAllProjects();
		db.deleteAllTasks();
		
		
		
		
		
		try {
			projects = jsonObject.getJSONArray("projects");
			tasks = jsonObject.getJSONArray("tasks");
			
			System.out.println("json projects" + Integer.toString(projects.length()));
			
			for (int i = 0; i < projects.length(); i++){
				System.out.println(projects.getJSONObject(i).getString("name"));
				
				JSONObject p = projects.getJSONObject(i);
				System.out.println(p.toString());
				String name = p.getString("name");
				String status = p.getString("status");
				int id = p.getInt("id");
				int yearstart = p.getInt("yearstart");
				int monthstart = p.getInt("monthstart");
				int daystart = p.getInt("daystart");
				int yeardue = p.getInt("yeardue");
				int monthdue = p.getInt("monthdue");
				int daydue = p.getInt("daydue");
				int opentasks = p.getInt("opentasks");
				int totaltasks = p.getInt("totaltasks");
				String contentpath = p.getString("contentpath");
				
				File contentDir = new File("/storage/sdcard0/ProjectManager/" + name);
				contentDir.mkdirs();
				
				int startdate[] = new int[3];
				int duedate[] = new int[3];
				
				startdate[0] = yearstart;
				startdate[1] = monthstart;
				startdate[2] = daystart;
				

				
				duedate[0] = yeardue;
				duedate[1] = monthdue;
				duedate[2] = daydue;
				
				
				db.addProject(name, status, startdate, duedate, contentDir.getAbsolutePath());
				
			}
			
			for (int i = 0; i < tasks.length(); i++){
				System.out.println(tasks.getJSONObject(i));
				
				JSONObject t = tasks.getJSONObject(i);
				System.out.println(t.toString());
				String name = t.getString("name");
				String status = t.getString("status");
				int id = t.getInt("id");
				int project_id = t.getInt("project_id");
				int yearstart = t.getInt("yearstart");
				int monthstart = t.getInt("monthstart");
				int daystart = t.getInt("daystart");
				int yeardue = t.getInt("yeardue");
				int monthdue = t.getInt("monthdue");
				int daydue = t.getInt("daydue");
				
				String priority = t.getString("priority");
				int percentage = t.getInt("status");
				
				String description = t.getString("description");
				String photopath = t.getString("photopath");
				String contentpath = t.getString("contentpath");
				
				File taskContentPath = new File(contentpath);
				taskContentPath.mkdir();
				
				int startdate[] = new int[3];
				int duedate[] = new int[3];
				
				startdate[0] = yearstart;
				startdate[1] = monthstart;
				startdate[2] = daystart;
				

				
				duedate[0] = yeardue;
				duedate[1] = monthdue;
				duedate[2] = daydue;
				
				db.addTask(project_id, name, status, priority, percentage, startdate, duedate, photopath, description, contentpath);
				
			}
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	db.close();
	
		
		
	}
	
	//
	// Crea respuesta JSON objeto con Project
	public static JSONObject project(Project p){
		JSONObject json = new JSONObject();
		
		try {
			json.put("name", p.getName());
			json.put("status", p.getStatus());
			json.put("id", p.getId());
			json.put("yearstart", p.getYearStartDate());
			json.put("monthstart", p.getMonthStartDate());
			json.put("daystart", p.getDayStartDate());
			json.put("yeardue", p.getYearDueDate());
			json.put("monthdue", p.getMonthDueDate());
			json.put("daydue", p.getDayDueDate());
			json.put("opentasks", p.getOpenTasks());
			json.put("totaltasks", p.getTotalTasks());
			json.put("contentpath", p.getContentPath());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	//
	// Crea respuesta JSON con TASK, objeto
	public static JSONObject task(Task t){
		JSONObject json = new JSONObject();
		
		try {
			json.put("name", t.getTaskName());
			json.put("status", t.getStatus());
			json.put("id", t.getTaskId());
			json.put("project_id", t.getProjectId());
			json.put("priority", t.getPriority());
			json.put("percentage", t.getPercentage());
			json.put("yearstart", t.getYearStartDate());
			json.put("monthstart", t.getMonthStartDate());
			json.put("daystart", t.getDayStartDate());
			json.put("yeardue", t.getYearDueDate());
			json.put("monthdue", t.getMonthDueDate());
			json.put("daydue", t.getDayDueDate());
			json.put("photopath", t.getPhotoPath());
			json.put("description", t.getDescription());
			json.put("contentpath", t.getContentPath());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


}
