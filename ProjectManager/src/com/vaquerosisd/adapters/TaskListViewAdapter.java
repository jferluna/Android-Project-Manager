package com.vaquerosisd.adapters;

import java.util.Calendar;
import java.util.List;

import com.vaquerosisd.object.Task;
import com.vaquerosisd.projectmanager.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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
		Typeface typefaceTitle = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-LightItalic.ttf");
		
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		
		RelativeLayout layout = (RelativeLayout) row.findViewById(R.id.listTask_row);
		TextView taskName = (TextView) row.findViewById(R.id.listTask_TaskName);
		TextView status = (TextView) row.findViewById(R.id.listTask_Status);
		TextView priority = (TextView) row.findViewById(R.id.listTask_Priority);
		TextView percentajeDone = (TextView) row.findViewById(R.id.listTask_PercentajeDone);
		TextView startDate = (TextView) row.findViewById(R.id.listTask_StartDate);
		TextView dueDate = (TextView) row.findViewById(R.id.listTask_DueDate);
		taskName.setTypeface(typefaceTitle);
		
		if(!taskList.isEmpty()) {
			Task task = taskList.get(position);
			
			taskName.setText(task.getTaskName());
			status.setText(task.getStatus());
			priority.setText(task.getPriority());
			percentajeDone.setText("Done: " + String.valueOf(task.getPercentage()) + "%");
			startDate.setText("Start Date: " + task.getYearStartDate() + "/" + task.getMonthStartDate() + "/" + task.getDayStartDate());
			dueDate.setText("Due Date: " + task.getYearDueDate() + "/" + task.getMonthDueDate() + "/" + task.getDayDueDate());
			
			Calendar today = Calendar.getInstance();
			Calendar taskDueDate = Calendar.getInstance();
			taskDueDate.clear();
			taskDueDate.set(task.getYearDueDate(), task.getMonthDueDate() - 1, task.getDayDueDate());
			
			Calendar taskDueDateWarning = Calendar.getInstance();
			taskDueDateWarning.clear();
			taskDueDateWarning.set(task.getYearDueDate(), task.getMonthDueDate() - 1, task.getDayDueDate());
			taskDueDateWarning.add(Calendar.DAY_OF_MONTH, -3);
			
			if(taskDueDate.before(today)) {
				layout.setBackgroundColor(Color.parseColor("#D32F2F"));
				taskName.setTextColor(Color.parseColor("#FFFFFF"));
				status.setTextColor(Color.parseColor("#FFFFFF"));
				priority.setTextColor(Color.parseColor("#FFFFFF"));
				percentajeDone.setTextColor(Color.parseColor("#FFFFFF"));
				startDate.setTextColor(Color.parseColor("#FFFFFF"));
				dueDate.setTextColor(Color.parseColor("#FFFFFF"));
			} else if(taskDueDateWarning.before(today)) {
				layout.setBackgroundColor(Color.parseColor("#FFF176"));
				taskName.setTextColor(Color.parseColor("#000000"));
				status.setTextColor(Color.parseColor("#000000"));
				priority.setTextColor(Color.parseColor("#000000"));
				percentajeDone.setTextColor(Color.parseColor("#000000"));
				startDate.setTextColor(Color.parseColor("#000000"));
				dueDate.setTextColor(Color.parseColor("#000000"));
			} else {
				taskName.setTextColor(Color.parseColor("#000000"));
				status.setTextColor(Color.parseColor("#000000"));
				priority.setTextColor(Color.parseColor("#000000"));
				percentajeDone.setTextColor(Color.parseColor("#000000"));
				startDate.setTextColor(Color.parseColor("#000000"));
				dueDate.setTextColor(Color.parseColor("#000000"));
			}
		}
		
		return row;
	}
}
