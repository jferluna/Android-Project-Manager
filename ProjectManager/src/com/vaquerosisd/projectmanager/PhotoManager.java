package com.vaquerosisd.projectmanager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.DeletePhotoDialog;
import com.vaquerosisd.object.PhotoRef;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoManager extends Activity {
	
	ProjectOperations db;		//Database Operations
	String projectId;			//Project ID where the picture belong
	String projectName;			//Project name associated to the Project ID
	
	List<PhotoRef> photosList;
	
	ImageView photoDisplay;
	int photoIndex;
	
	Button previousPhoto, nextPhoto, deletePhoto;
	
	File storageDir;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos_manager);
		
		//Initialize database
		db = new ProjectOperations(this);
		db.open();
		
		storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/ProjectManager");
		
		//Get intent from Projects.class
		Bundle data = getIntent().getExtras();
		projectId = data.getString("ProjectID");
		projectName = data.getString("ProjectName");
		
		photoDisplay = (ImageView)findViewById(R.id.photoDisplay);
		
		photoDisplay.setOnTouchListener(new OnSwipeListener(this) {
			@Override
		    public void onSwipeRight() {
				moveToPreviousPhoto();
		        Toast.makeText(PhotoManager.this, "right", Toast.LENGTH_SHORT).show();
		    }
			
			@Override
		    public void onSwipeLeft() {
				moveToNextPhoto();
		        Toast.makeText(PhotoManager.this, "left", Toast.LENGTH_SHORT).show();
		    }
		});
		
		Button takePhoto = (Button) findViewById(R.id.takePhoto);
		nextPhoto = (Button) findViewById(R.id.nextPhoto);
		previousPhoto = (Button) findViewById(R.id.previousPhoto);
		deletePhoto = (Button) findViewById(R.id.deletePhoto);
		
		getPhotosList();	
		setPhoto();
		
		takePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePhoto();
			}
		});
		
		nextPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNextPhoto();
			}
		});
		
		previousPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToPreviousPhoto();
			}
		});
		
		deletePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DeletePhotoDialog.newInstance().show(getFragmentManager(), "dialog");
			}
		});
		
	}
	
	private final int REQUEST_IMAGE_CAPTURE = 0;
	
	String currentPhotoPath;
	
	public void takePhoto()
    {
    	Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
	    	
	    	File photoFile = null;
	        try {
	        	//The file name needs to be more than 3 characters long
	            photoFile = createImageFile(projectName + "_Photo");
	        } catch (IOException ex) {}
	        
	        if (photoFile != null) {
	            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
	            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
	        }
	        
	    }
    }
	
	public void deletePhoto()
	{
		File photoFile = new File(storageDir + "/" + photosList.get(photoIndex).getPhotoPath());
		if(photoFile.exists())
			photoFile.delete();
		
		db.deletePhoto(photosList.get(photoIndex).getPhotoId());
		getPhotosList();
		setPhoto();
	}
	
	public void setPhoto()
	{
		if(photosList.size() > 0 && photoIndex < photosList.size())
		{
			previousPhoto.setVisibility(View.VISIBLE);
			nextPhoto.setVisibility(View.VISIBLE);
			photoDisplay.setVisibility(View.VISIBLE);
			deletePhoto.setVisibility(View.VISIBLE);
			
			currentPhotoPath = photosList.get(photoIndex).getPhotoPath();
			photoDisplay.setImageURI(Uri.parse("file:" + storageDir + "/" + currentPhotoPath));
		}
		else
		{
			previousPhoto.setVisibility(View.INVISIBLE);
			nextPhoto.setVisibility(View.INVISIBLE);
			photoDisplay.setVisibility(View.INVISIBLE);
			deletePhoto.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean validatePhoto()
	{
		//If the database has no entries no validations are required
		if(photosList.size() == 0)
			return true;
		
		if (!storageDir.exists()) {
			db.deleteAllPhotos();
			Toast.makeText(getApplicationContext(), "The Project Manager picture folder has been deleted externally.", Toast.LENGTH_LONG).show();
			return false;
        }
		
		File photoFile = new File(storageDir + "/" + photosList.get(photoIndex).getPhotoPath());
		if(!photoFile.exists())
		{
			db.deletePhoto(photosList.get(photoIndex).getPhotoId());
			Toast.makeText(getApplicationContext(), "The target photo has been deleted externally.", Toast.LENGTH_LONG).show();
			return false;
		}
			
		return true;
	}
	
	private void getPhotosList()
	{
		do
		{
			photosList = db.getAllPhotos(Integer.parseInt(projectId));
			photoIndex = photosList.size() - 1;
		} while(!validatePhoto());
	}
	
	public void moveToPreviousPhoto()
	{
		photoIndex = Math.max(photoIndex - 1, 0);
		
		if(!validatePhoto())
			getPhotosList();
		
		setPhoto();
		
	}
	
	public void moveToNextPhoto()
	{
		photoIndex = Math.min(photoIndex + 1, photosList.size() - 1);
		
		if(!validatePhoto())
			getPhotosList();
		
		setPhoto();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        
    	if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
    		db.addPhoto(currentPhotoPath, Integer.parseInt(projectId));
    		getPhotosList();
    		setPhoto();
        }
    }
	
	private File createImageFile(String imageFileName) throws IOException {
		
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        
        
        File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        );
        
        currentPhotoPath = image.getName();      
        
        return image;
    }
}

