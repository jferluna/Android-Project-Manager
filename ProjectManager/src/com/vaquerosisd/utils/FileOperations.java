package com.vaquerosisd.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;

public class FileOperations {
	private Context context;
	
	public FileOperations(Context context){
		this.context = context;
	}
	
	//Create file
		public void createFile(String fileName, String fileText) {
		new File(context.getFilesDir(), fileName);
		FileOutputStream outputStream;
		try {
			outputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
			outputStream.write(fileText.getBytes());
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
		
	//Create String file if it doesn't exist
	public void createStringFile(String fileName, String textFile) {
		if(!isFile(fileName)) {
			createFile(fileName, textFile);
		}
	}
	
	//Append text
	public void appendText(String fileName, String text) {
		FileOutputStream outputStream;
		try {
			outputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
			outputStream.write(text.getBytes());
			outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
	
	//Reads internal file
	public String readFile(String filename) {
		try {
			FileInputStream inputStream = context.openFileInput(filename);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			String fileText = stringBuilder.toString();
			return fileText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//Converts String to ArrayList
	public ArrayList<String> convertToStringList(String text) {
		ArrayList<String> statusList = new ArrayList<String>();
		String[] stringArray = text.split("\n");
		for(int i =0; i < stringArray.length; i++){
			statusList.add(stringArray[i]);
		}
		return statusList;
		
	}
	
	//Checks it there is an existing file in internal storage
	public boolean isFile(String fileName) {
		File directory = context.getFilesDir();
		for(String file : directory.list()){
			if(file.equals(fileName)){
				return true;
			}
				
		}
		return false;
	}

}
