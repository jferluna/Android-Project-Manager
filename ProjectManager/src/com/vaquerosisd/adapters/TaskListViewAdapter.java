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
		
		TextView taskName = (TextView) row.findViewById(R.id.listTask_TaskName);
		TextView status = (TextView) row.findViewById(R.id.listTask_Status);
		TextView priority = (TextView) row.findViewById(R.id.listTask_Priority);
		TextView percentajeDone = (TextView) row.findViewById(R.id.listTask_PercentajeDone);
		TextView startDate = (TextView) row.findViewById(R.id.listTask_StartDate);
		TextView dueDate = (TextView) row.findViewById(R.id.listTask_DueDate); 
		
		if(!taskList.isEmpty()) {
			Task task = taskList.get(position);
			
			taskName.setText(task.getTaskName());
			status.setText(task.getStatus());
			priority.setText(task.getPriority());
			percentajeDone.setText(String.valueOf(task.getPercentage()));
			startDate.setText("Start Date: " + task.getYearStartDate() + "/" + task.getMonthStartDate() + "/" + task.getDayStartDate());
			dueDate.setText("Due Date: " + task.getYearDueDate() + "/" + task.getMonthDueDate() + "/" + task.getDayDueDate());
			
			taskName.setTextColor(Color.parseColor("#000000"));
			status.setTextColor(Color.parseColor("#000000"));
			priority.setTextColor(Color.parseColor("#000000"));
			percentajeDone.setTextColor(Color.parseColor("#000000"));
			startDate.setTextColor(Color.parseColor("#000000"));
			dueDate.setTextColor(Color.parseColor("#000000"));
		}
		
		return row;
	}
}
