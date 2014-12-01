package com.vaquerosisd.dialog;

import com.vaquerosisd.projectmanager.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

public class CustomStatus extends DialogFragment {
	
	CustomStatusInterface statusDialogListener;
	
	public interface CustomStatusInterface {
		public void onDialogPositiveClick(DialogFragment dialog, String statusOption);
		public void onDialogNegativeClick(DialogFragment dialog);
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
        	if(getTargetFragment() == null)
        		statusDialogListener = (CustomStatusInterface) activity;
        	else
        		statusDialogListener = (CustomStatusInterface) getTargetFragment();
        } catch (ClassCastException e) {
            e.getStackTrace();
        }
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    
	    AlertDialog alert=builder.create();
	    setCancelable(false);
	    alert.setCanceledOnTouchOutside(false);
	    
	    builder.setTitle("Set custom status");
	    builder.setView(inflater.inflate(R.layout.dialog_custom_status, null));
	    builder.setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int id) {
        	   EditText statusText = (EditText) ((AlertDialog) dialog).findViewById(R.id.dialogStatus_customStatusOption);
               statusDialogListener.onDialogPositiveClick(CustomStatus.this, statusText.getText().toString());
           }
       });
	    builder.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
    	   @Override
           public void onClick(DialogInterface dialog, int id) {
    		   statusDialogListener.onDialogNegativeClick(CustomStatus.this);
               CustomStatus.this.getDialog().cancel();
           }
       }); 
	    return builder.create();
	}

}
