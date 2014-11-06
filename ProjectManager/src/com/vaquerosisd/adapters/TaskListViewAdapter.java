package com.vaquerosisd.adapters;

import java.util.List;

import com.vaquerosisd.object.Task;
import com.vaquerosisd.projectmanager.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TaskListViewAdapter extends ArrayAdapter<Task> {
	private Context context;
	int layoutResourceId;
	List<Task> taskList;
	
	public TaskListViewAdapter(Context context, int resource, List<Task> tasks) {
		super(context, resource, tasks);
		this.context = context;
		this.layoutResourceId = resource;
		this.taskList = tasks;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		
		TextView taskId = (TextView) row.findViewById(R.id.taskId);
		TextView taskName = (TextView) row.findViewById(R.id.taskName);
		TextView projectId = (TextView) row.findViewById(R.id.taskProjectId);
		
		if(!taskList.isEmpty()) {
			Task task = taskList.get(position);
			
			taskId.setText(String.valueOf(task.getTaskId()));
			taskName.setText(task.getTaskName());
			projectId.setText(String.valueOf(task.getProjectId()));
			
			taskId.setTextColor(Color.parseColor("#000000"));
			taskName.setTextColor(Color.parseColor("#000000"));
			projectId.setTextColor(Color.parseColor("#000000"));
		}
		
		return row;
	}
}
