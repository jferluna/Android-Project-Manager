package com.vaquerosisd.projectmanager;

import java.util.Timer;
import java.util.TimerTask;

import com.vaquerosisd.object.JsonWrapper;
import com.vaquerosisd.object.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class MainActivity extends Activity implements WebserviceCallback {

	private long splashDelayOnline = 600;
	private long splashDelayOffline = 600;
	
	static private final int LOGIN = 0;
	static private final int CREATE = 1;
	
	private User currentUser;
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    //Set font to the project manager title
    TextView title = (TextView) findViewById(R.id.title);
    Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "fonts/RODUScut100.otf");
    title.setTypeface(typefaceTitle);
    
    currentUser = User.getUser(MainActivity.this);
    
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
        	if(currentUser != null)
        	{
        		Intent intent = new Intent(MainActivity.this, ProjectList.class);
                startActivity(intent);
                finish();
        	}
        	else
        	{
        		Intent intent = new Intent(MainActivity.this, Login.class);
                startActivityForResult(intent, LOGIN);
        	}
        }
      };
    
    //Check is Internet connection is available in order to pull sync data
    if(isNetworkAvailable() == true) {
    	
    	Timer timer = new Timer();
        timer.schedule(task, splashDelayOnline); //Dispatch task at 6 seconds
    } else {
    	Timer timer = new Timer();
        timer.schedule(task, splashDelayOffline); //Dispatch task at 6 seconds
    }
  }
  
  @Override
  public void callback(JsonWrapper jw) {
	  currentUser = User.getUser(MainActivity.this);
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  if(requestCode == LOGIN || requestCode == CREATE)
	  {
		  if(resultCode == RESULT_OK)
		  {
			  currentUser = User.getUser(MainActivity.this);
			  
			  if(currentUser != null)
			  {
				  Intent intent = new Intent(MainActivity.this, ProjectList.class);
	              startActivity(intent);
	              finish();
			  }
		  }
		  else if(resultCode == RESULT_CANCELED)
		  {
			  if(data.getBooleanExtra("LogIn", false))
			  {
				  Intent intent = new Intent(MainActivity.this, Login.class);
	              startActivityForResult(intent, LOGIN);
			  }
			  else if(data.getBooleanExtra("Create", false))
			  {
				  Intent intent = new Intent(MainActivity.this, NewAccount.class);
	              startActivityForResult(intent, CREATE);
			  }
		  }
	  }
  }
  
  //Check if device is connected to Internet
  private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
