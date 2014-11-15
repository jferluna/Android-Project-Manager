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


}
