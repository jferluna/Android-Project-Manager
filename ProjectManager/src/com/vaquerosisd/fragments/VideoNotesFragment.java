package com.vaquerosisd.fragments;

import java.io.File;
import java.util.ArrayList;

import com.vaquerosisd.adapters.VideoNoteGridViewAdapter;
import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.object.Task;
import com.vaquerosisd.projectmanager.R;
import com.vaquerosisd.utils.FileOperations;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoNotesFragment extends Fragment {
	Bundle bundle;
	FileOperations fO;
	ProjectOperations db;
	Task task;
	VideoNoteGridViewAdapter videoNoteAdapter;
	GridView videoNotesGridView;
	
	int RECORD_VIDEO_REQUEST = 55;
	int PLAY_VIDEO_NOTE = 95;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		bundle = getArguments();
        return inflater.inflate(R.layout.fragment_task_video_notes, container, false);
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		int taskId = bundle.getInt("TaskId");
		db = new ProjectOperations(getActivity());
		fO = new FileOperations(getActivity());
		db.open();
		task = db.getTaskById(taskId);
		
		videoNotesGridView = (GridView) view.findViewById(R.id.fragmentVideoNote_GridView);
		ArrayList<String> videoFiles = FileOperations.getFilesByExtension(db.getTaskContentPath(task.getTaskId()), ".mp4");
		videoNoteAdapter = new VideoNoteGridViewAdapter(this.getActivity(), R.layout.gridview_video_object, videoFiles);
		videoNotesGridView.setAdapter(videoNoteAdapter);
		
		videoNotesGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String fileDir = db.getTaskContentPath(task.getTaskId());
				String fileName = videoNotesGridView.getItemAtPosition(position).toString();
				
				Intent playVideoNoteIntent = new Intent(Intent.ACTION_VIEW);
				File voiceNoteFile = new File(fileDir + "/" + fileName);;
				playVideoNoteIntent.setDataAndType(Uri.fromFile(voiceNoteFile), "video/mp4");
				Intent videoNoteAppChooserIntent = Intent.createChooser(playVideoNoteIntent, getResources().getString(R.string.videoAppChooser));
				if (videoNoteAppChooserIntent.resolveActivity(getActivity().getPackageManager()) != null)
					startActivityForResult(videoNoteAppChooserIntent, PLAY_VIDEO_NOTE);
			}
		});
		
		setHasOptionsMenu(true);
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.video_note_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ArrayList<String> videoFiles = FileOperations.getFilesByExtension(db.getTaskContentPath(task.getTaskId()), ".mp4");
		videoNoteAdapter = new VideoNoteGridViewAdapter(this.getActivity(), R.layout.gridview_video_object, videoFiles);
		videoNotesGridView.setAdapter(videoNoteAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		switch (item.getItemId())
		{
		case R.id.actionBar_CaptureVideoNote:
			Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); 
			Intent recordVideoIntentChooser = Intent.createChooser(recordVideoIntent, getResources().getString(R.string.videoAppChooser));
			if (recordVideoIntentChooser.resolveActivity(this.getActivity().getPackageManager()) != null)
				startActivityForResult(recordVideoIntentChooser, RECORD_VIDEO_REQUEST);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data) {
		if(requestCode == RECORD_VIDEO_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String filePath = fO.getAudioFilePathFromUri(uri);
				String taskDir = db.getTaskContentPath(task.getTaskId()) + "/Video_" + System.currentTimeMillis() + ".mp4";
				FileOperations.moveFile(filePath, taskDir);
			}
      	}
	}
}
