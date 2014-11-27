package com.vaquerosisd.dialog;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;


public class DatePickerFragment extends DialogFragment {
	private int year, month, day;
	OnDateSetListener onDateSet;
	
	public DatePickerFragment() {
	}
	
	@Override
	 public void setArguments(Bundle args) {
		super.setArguments(args);
		year = args.getInt("year");
		month = args.getInt("month");
		day = args.getInt("day");
	 }
	
	public void setCallBack(OnDateSetListener ondate) {
	  onDateSet = ondate;
	 }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
	}
}