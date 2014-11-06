/*
Project Manager - Android application for the administration of projects.
	Copyright (C) 2014 - ITESM

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.


Authors:

   ITESM representatives
	Ing. Martha Sordia Salinas <msordia@itesm.mx>
    Ing. Mario de la Fuente <mario.delafuente@itesm.mx>

   ITESM students
	David Alberto De Leon Villarreal ddeleon93@gmail.com
	Alan Salinas Gonzalez alan.sagz@gmail
	José Fernando Luna Alemán jfernando.luna91@gmail.com
*/

package com.vaquerosisd.dialog;

import com.vaquerosisd.projectmanager.ProjectList;
import com.vaquerosisd.projectmanager.R;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class DeleteProjectDialog extends DialogFragment {
	
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
					((ProjectList)callerActivity).deleteProject();
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
