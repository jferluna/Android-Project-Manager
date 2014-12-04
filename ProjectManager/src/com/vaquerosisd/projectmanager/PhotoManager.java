package com.vaquerosisd.projectmanager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.dialog.DeletePhotoDialog;
import com.vaquerosisd.object.PhotoRef;
import com.vaquerosisd.utils.OnSwipeListener;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoManager extends Activity {
	
	ProjectOperations db;		//Database Operations
	int projectId;			//Project ID where the picture belong
	String projectName;			//Project name associated to the Project ID
	boolean photoContent;
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
		projectId = data.getInt("ProjectID");
		projectName = data.getString("ProjectName");
		
		photoDisplay = (ImageView)findViewById(R.id.photoDisplay);
		
		photoDisplay.setOnTouchListener(new OnSwipeListener(this) {
			@Override
		    public void onSwipeRight() {
				moveToPreviousPhoto();
		    }			
			@Override
		    public void onSwipeLeft() {
				moveToNextPhoto();
		    }
		});
		
		getPhotosList();	
		setPhoto();		
	}
	
	private final int REQUEST_IMAGE_CAPTURE = 0;
	
	String currentPhotoPath;
	
	public void takePhoto()
    {
    	Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
	    	
	    	File photoFile = null;
	        try {
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
		String localPath = photosList.get(photoIndex).getPhotoPath();
		File photoFile = new File(storageDir + "/" + localPath);
		
		db.deletePhoto(photosList.get(photoIndex).getPhotoId());
		getPhotosList();
		setPhoto();
		
		if(photoFile.exists())
		{
			photoFile.delete();			
			//Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" +  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)));
			//sendBroadcast(intent);
		}
		
	}
	
	public void setPhoto()
	{
		if(photosList.size() > 0 && photoIndex < photosList.size())
		{
			photoContent = true; 			
			currentPhotoPath = photosList.get(photoIndex).getPhotoPath();
			photoDisplay.setImageURI(Uri.parse("file:" + storageDir + "/" + currentPhotoPath));
			invalidateOptionsMenu();
		}
		else
		{
			photoContent = false;
			photoDisplay.setImageBitmap(null);
			invalidateOptionsMenu();
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
			photosList = db.getAllPhotos(projectId);
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
    		db.addPhoto(currentPhotoPath, projectId);
    		getPhotosList();
    		setPhoto();
            
    		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:" + storageDir + "/" + currentPhotoPath));     
    		sendBroadcast(intent);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)  {
		getMenuInflater().inflate(R.menu.photos_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Intent navigationIntent = NavUtils.getParentActivityIntent(this);
			navigationIntent.putExtra("ProjectID", projectId);
			navigationIntent.putExtra("ProjectName", projectName);
	        NavUtils.navigateUpTo(this, navigationIntent);
	        return true;
	    case R.id.actionBar_AddPhotoIcon:
	    	takePhoto();
	    	return true;
	    case R.id.actionBar_DeletePhotoIcon:
	    	DeletePhotoDialog.newInstance().show(getFragmentManager(), "dialog");
	    	return true;
	    	
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu){
		if(photoContent)
			menu.findItem(R.id.actionBar_DeletePhotoIcon).setVisible(true);
		else
			menu.findItem(R.id.actionBar_DeletePhotoIcon).setVisible(false);
		return true;
	}
}

