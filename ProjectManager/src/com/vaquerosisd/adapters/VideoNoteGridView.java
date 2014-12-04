package com.vaquerosisd.adapters;

import java.util.ArrayList;

import com.vaquerosisd.projectmanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoNoteGridView extends BaseAdapter {
	private Context context;
	int layoutResourceId;
	private ArrayList<String> files;
	
	public VideoNoteGridView(Context context,int layoutResourceId, ArrayList<String> files){
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.files = files;
	}
	
	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		return files.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;
		if(row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 	row = inflater.inflate(layoutResourceId, parent, false);
		 	
		 	holder = new RecordHolder();
		 	holder.txtTitle = (TextView) row.findViewById(R.id.gridView_Text);
		   	holder.imageItem = (ImageView) row.findViewById(R.id.gridView_Image);
		   	row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
	  	holder.txtTitle.setText(files.get(position));
	  	return row;
	}
	
	static class RecordHolder {
		TextView txtTitle;
		ImageView imageItem;
	 }
}
