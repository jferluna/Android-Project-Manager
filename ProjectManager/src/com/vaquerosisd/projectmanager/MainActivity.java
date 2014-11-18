package com.vaquerosisd.projectmanager;

import java.util.Timer;
import java.util.TimerTask;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class MainActivity extends Activity {

	private long splashDelayOnline = 600;
	private long splashDelayOffline = 600;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    //Set font to the project manager title
    TextView title = (TextView) findViewById(R.id.title);
    Typeface typefaceTitle = Typeface.createFromAsset(getAssets(), "fonts/RODUScut100.otf");
    title.setTypeface(typefaceTitle);
    
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
          Intent intent = new Intent(MainActivity.this, ProjectList.class);
          startActivity(intent);
          finish();
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
  
  //Check if device is connected to Internet
  private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
