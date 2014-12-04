package com.vaquerosisd.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

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
	
	public static void moveFile(String inputPath, String outputPath) {

	    InputStream in = null;
	    OutputStream out = null;
	    try {

	        in = new FileInputStream(inputPath);        
	        out = new FileOutputStream(outputPath);

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;

	        // write the output file
	        out.flush();
	        out.close();
	        out = null;

	        // delete the original file
	        new File(inputPath).delete();
	        File f = new File(outputPath);
	        f.setReadable(true);

	    } catch (FileNotFoundException fnfe1) {
	        Log.e("tag", fnfe1.getMessage());
	    } catch (Exception e) {
	        Log.e("tag", e.getMessage());
	    }
	}
	
	public String getAudioFilePathFromUri(Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
		return cursor.getString(index);
	}
	
	public static ArrayList<String> getFilesByExtension(String path, String extension) {		
		ArrayList<String> filteredFiles = new ArrayList<String>();
		File directory = new File(path);
		
		String[] files = directory.list();
		
		int extLength = extension.length();
		String tempComp;
		for(int i = 0; i < files.length; i++){
			tempComp = files[i].substring(files[i].length()-extLength, files[i].length());
			if(tempComp.equals(extension)) {
				Log.i("Debug", files[i]);
				filteredFiles.add(files[i]);
			}
		}
		
		return filteredFiles;
	}
	
	public static void deleteFolder(String path) {
		File directory = new File(path);
		if(directory.isDirectory()) {
			String[] children = directory.list();
			for (int i = 0; i < children.length; i++) {
	            new File(path, children[i]).delete();
	        }
		}
		directory.delete();
	}
	
}
