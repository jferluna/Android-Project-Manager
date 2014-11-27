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

package com.vaquerosisd.adapters;

import java.util.List;

import com.vaquerosisd.object.Project;
import com.vaquerosisd.projectmanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProjectListViewAdapter extends ArrayAdapter<Project> {
	private Context context;
	int layoutResourceId;
	List<Project> projectList;
	
	public ProjectListViewAdapter(Context context, int resource, List<Project> projects) {
		super(context, resource, projects);
		this.context = context;
		this.layoutResourceId = resource;
		this.projectList = projects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		
//		ImageView coverPhoto = (ImageView) row.findViewById(R.id.listProject_ProjectImage);
		TextView projectName = (TextView) row.findViewById(R.id.listProject_ProjectName);
		TextView startDate = (TextView) row.findViewById(R.id.listProject_StartDate);
		TextView dueDate = (TextView) row.findViewById(R.id.listProject_DueDate);
		TextView projectStatus = (TextView) row.findViewById(R.id.listProject_ProjectStatus);
		TextView openTasks = (TextView) row.findViewById(R.id.listProject_OpenTasks);
		TextView totalTasks = (TextView) row.findViewById(R.id.listProject_TotalTasks);
		
		if(!projectList.isEmpty()) {
			Project project = projectList.get(position);
			projectName.setText(project.getName());
			startDate.setText("Start Date: " + project.getDayStartDate() + "/" + project.getMonthStartDate() + "/" + project.getYearStartDate());
			dueDate.setText("Due date: " + project.getDayDueDate() + "/" + project.getMonthDueDate() + "/" + project.getYearDueDate());
			projectStatus.setText("Status: " + project.getStatus());
			openTasks.setText("Open tasks: " + project.getOpenTasks());
			totalTasks.setText("Total tasks: " + project.getTotalTasks());

		}
		
		return row;
	}
}
