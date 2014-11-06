package com.vaquerosisd.dialog;

import com.vaquerosisd.projectmanager.PhotoManager;
import com.vaquerosisd.projectmanager.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DeletePhotoDialog extends DialogFragment {
	
	public static DeletePhotoDialog newInstance() {
        return new DeletePhotoDialog();
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    	
    	View rootView = inflater.inflate(R.layout.dialog_delete_photo, container, false);
    	
        Button positive = (Button)rootView.findViewById(R.id.positive);
        Button negative = (Button)rootView.findViewById(R.id.negative);
        
        positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Activity callerActivity = getActivity();
				
				if(callerActivity instanceof PhotoManager)
				{
					((PhotoManager)callerActivity).deletePhoto();
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

