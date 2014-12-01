package com.vaquerosisd.object;

import org.json.JSONException;
import org.json.JSONObject;

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
			json.put("coverpath", p.getCoverPath());
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


}
