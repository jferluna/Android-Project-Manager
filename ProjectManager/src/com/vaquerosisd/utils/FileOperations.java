package com.vaquerosisd.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;

public class FileOperations {
	
	public void createFile(File file, String fileName){
		new File(file, fileName);
	}
	
	public void appendTextFile(Context context, String fileName, String text){
		FileOutputStream outputStream;

		try {
		  outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		  outputStream.write(text.getBytes());
		  outputStream.close();
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

}
