package com.vaquerosisd.fragments;

import java.io.File;
import java.util.ArrayList;

import com.vaquerosisd.adapters.VoiceNoteGridViewAdapter;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class VoiceNotesFragment extends Fragment {
	
	Bundle bundle;
	FileOperations fO;
	ProjectOperations db;
	Task task;
	VoiceNoteGridViewAdapter voiceNoteAdapter;
	GridView voiceNotesGridView;
	
	int RECORD_SOUND_REQUEST = 55;
	int PLAY_VOICE_NOTE = 95;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		bundle = getArguments();
        return inflater.inflate(R.layout.fragment_task_voice_notes, container, false);
    }
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		int taskId = bundle.getInt("taskId");
		db = new ProjectOperations(getActivity());
		fO = new FileOperations(getActivity());
		db.open();
		task = db.getTaskById(taskId);
		
		voiceNotesGridView = (GridView) view.findViewById(R.id.fragmentVoiceNote_GridView);
		ArrayList<String> voiceFiles = FileOperations.getFilesByExtension(db.getTaskContentPath(task.getTaskId()), ".3gpp");
		voiceNoteAdapter = new VoiceNoteGridViewAdapter(this.getActivity(), R.layout.gridview_object, voiceFiles);
		voiceNotesGridView.setAdapter(voiceNoteAdapter);
		
		voiceNotesGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String fileDir = db.getTaskContentPath(task.getTaskId());
				String fileName = voiceNotesGridView.getItemAtPosition(position).toString();
				
				Intent playVoiceNoteIntent = new Intent(Intent.ACTION_VIEW);
				File voiceNoteFile = new File(fileDir + "/" + fileName);;
				playVoiceNoteIntent.setDataAndType(Uri.fromFile(voiceNoteFile), "audio/3gpp");
				Intent voiceNoteAppChooserIntent = Intent.createChooser(playVoiceNoteIntent, getResources().getString(R.string.voiceAppChooser));
				startActivityForResult(voiceNoteAppChooserIntent, PLAY_VOICE_NOTE);
			}
		});
		
		setHasOptionsMenu(true);
    }
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.voice_note_menu, menu);
	    super.onCreateOptionsMenu(menu,inflater);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ArrayList<String> voiceFiles = FileOperations.getFilesByExtension(db.getTaskContentPath(task.getTaskId()), ".3gpp");
		voiceNoteAdapter = new VoiceNoteGridViewAdapter(this.getActivity(), R.layout.gridview_object, voiceFiles);
		voiceNotesGridView.setAdapter(voiceNoteAdapter);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.		
		switch (item.getItemId())
		{
		case R.id.actionBar_CaptureVoiceNote:
			Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION); 
			startActivityForResult(intent, RECORD_SOUND_REQUEST);	
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data) {
		if(requestCode == RECORD_SOUND_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				String filePath = fO.getAudioFilePathFromUri(uri);
				String taskDir = db.getTaskContentPath(task.getTaskId()) + "/VoiceNote_" + System.currentTimeMillis() + ".3gpp";
				Log.i("Debug", taskDir);
				FileOperations.moveFile(filePath, taskDir);
			}
      	}
	}
}
