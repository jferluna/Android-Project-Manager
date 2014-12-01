package com.vaquerosisd.adapters;

import java.util.ArrayList;

import com.vaquerosisd.projectmanager.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class VoiceNoteGridViewAdapter extends BaseAdapter {
	private Context context;
	int layoutResourceId;
	private ArrayList<String> files;
	
	public VoiceNoteGridViewAdapter(Context context,int layoutResourceId, ArrayList<String> files){
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(R.drawable.project2);
        return imageView;
	}

}
