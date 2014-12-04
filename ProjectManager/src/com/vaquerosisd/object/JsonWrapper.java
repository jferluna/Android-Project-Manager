package com.vaquerosisd.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaquerosisd.database.ProjectOperations;

public class JsonWrapper {
	
	private JSONObject jsonObject = null;

	public JsonWrapper(JSONObject jsonObject){
		this.jsonObject = jsonObject;
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
		
		ProjectOperations db = null;					//Database Operations
		JSONArray projects = null;
		JSONArray tasks = null;
		
		db.open();
		db.deleteAllProjects();
		db.deleteAllTasks();
		
		
		
		//db.addProject(projectNameString, projectStatusString, startDateStrings, dueDateStrings, contentDir.getAbsolutePath());
		
		try {
			projects = jsonObject.getJSONArray("projects");
			tasks = jsonObject.getJSONArray("tasks");
			
			
			for (int i = 0; i < projects.length(); i++){
				System.out.println(projects.getJSONObject(i));
				
			}
			
			for (int i = 0; i < tasks.length(); i++){
				System.out.println(tasks.getJSONObject(i));
				
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
