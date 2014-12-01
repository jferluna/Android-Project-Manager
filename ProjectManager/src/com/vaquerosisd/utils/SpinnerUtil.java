package com.vaquerosisd.utils;

import java.util.ArrayList;
import java.util.Collections;

import com.vaquerosisd.dialog.CustomStatus;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerUtil {
	
	public static ArrayAdapter<CharSequence> setSpinnerAdapter(Context context, String spinnerFileName, Spinner spinnerObject, boolean sort) {
		FileOperations fO = new FileOperations(context);
		String spinnerItems = fO.readFile(spinnerFileName);
		ArrayList<String> spinnerItemsArray = fO.convertToStringList(spinnerItems);
		if(sort)
			Collections.sort(spinnerItemsArray.subList(0, spinnerItemsArray.size()));
		
		//Set the adapter for the status spinner 
		final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAdapter.addAll(spinnerItemsArray);
		spinnerObject.setAdapter(spinnerAdapter);
		
		return spinnerAdapter;
	}
	
	public static ArrayAdapter<CharSequence> setCustomSpinnerAdapter (final Context context, final Fragment fragment, 
			final FragmentManager fragmentManager, String spinnerFileName, Spinner spinner, boolean sort) {
		FileOperations fO = new FileOperations(context);
		
		String spinnerItems = fO.readFile(spinnerFileName);
		ArrayList<String> spinnerItemsArray = fO.convertToStringList(spinnerItems);
		if(sort)
			Collections.sort(spinnerItemsArray.subList(0, spinnerItemsArray.size()));
		spinnerItemsArray.add("Custom...");
		
		//Set the adapter for the status spinner 
		final ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAdapter.addAll(spinnerItemsArray);
		spinner.setAdapter(spinnerAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if( "Custom...".equals((String) spinnerAdapter.getItem(position))) {
					DialogFragment customStatusDialog = new CustomStatus();
					customStatusDialog.setTargetFragment(fragment, 0);
					customStatusDialog.show(fragmentManager, "Custom Status Dialog");
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }
		});
		
		return spinnerAdapter;
	}
	
	public static void addStatusOption(Context context, String spinnerFileName, String statusOption, 
			ArrayAdapter<CharSequence> spinnerAdapter, Spinner spinner){
		FileOperations fO = new FileOperations(context);
		fO.appendText(spinnerFileName, statusOption + "\n");
		String statusOptions = fO.readFile(spinnerFileName);
		ArrayList<String> statusOptionsArray = fO.convertToStringList(statusOptions);
		Collections.sort(statusOptionsArray.subList(0, statusOptionsArray.size()));
		statusOptionsArray.add("Custom...");
		spinnerAdapter.clear();
		spinnerAdapter.addAll(statusOptionsArray);
		spinnerAdapter.notifyDataSetChanged();
		
		int statusPosition = spinnerAdapter.getPosition(statusOption);
		spinner.setSelection(statusPosition, true);
	}

}
