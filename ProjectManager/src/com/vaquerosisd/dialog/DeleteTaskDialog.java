package com.vaquerosisd.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.vaquerosisd.projectmanager.ProjectList;
import com.vaquerosisd.projectmanager.R;
import com.vaquerosisd.projectmanager.TaskList;

public class DeleteTaskDialog extends DialogFragment {
	
	public static DeleteProjectDialog newInstance() {
        return new DeleteProjectDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    	
    	View rootView = inflater.inflate(R.layout.dialog_delete_project, container, false);
    	
        Button positive = (Button)rootView.findViewById(R.id.positive);
        Button negative = (Button)rootView.findViewById(R.id.negative);
        
        positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Activity callerActivity = getActivity();
				
				if(callerActivity instanceof ProjectList)
				{
					((TaskList)callerActivity).deleteTask();
				}
				
				dismiss();
			}
		});
        
        negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				
			}
		});
        
        return rootView;
    }

}