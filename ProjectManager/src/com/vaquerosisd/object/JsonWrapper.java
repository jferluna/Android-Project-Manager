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
			//json.put("status", t.getStatus());
			json.put("id", t.getTaskId());
			json.put("projec_id", t.getProjectId());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}


}
