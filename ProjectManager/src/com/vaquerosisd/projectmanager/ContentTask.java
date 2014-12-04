package com.vaquerosisd.projectmanager;

import com.vaquerosisd.fragments.TaskDescriptionFragment;
import com.vaquerosisd.fragments.VoiceNotesFragment;
import com.vaquerosisd.utils.TabListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class ContentTask extends Activity {
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get projectId and projectName from ProjectList
		bundle = getIntent().getExtras();

	    // setup action bar for tabs
	    ActionBar actionBar = getActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    Tab tab = actionBar.newTab();
	    tab.setText("Description");
	    tab.setTag(bundle);
	    tab.setTabListener(new TabListener<TaskDescriptionFragment>(this, "Description", TaskDescriptionFragment.class));
	    actionBar.addTab(tab);

	    tab = actionBar.newTab();
	    tab.setText("Voice Notes");
	    tab.setTag(bundle);
	    tab.setTabListener(new TabListener<VoiceNotesFragment>(this, "Voice Notes", VoiceNotesFragment.class));
	    actionBar.addTab(tab);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Intent navigationIntent = NavUtils.getParentActivityIntent(this);
			navigationIntent.putExtra("ProjectID", bundle.getInt("ProjectId"));
			navigationIntent.putExtra("ProjectName", bundle.getString("ProjectName"));
	        NavUtils.navigateUpTo(this, navigationIntent);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
