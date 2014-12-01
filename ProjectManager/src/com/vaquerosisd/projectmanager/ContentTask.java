package com.vaquerosisd.projectmanager;

import com.vaquerosisd.utils.TabListener;

import Fragments.TaskDescriptionFragment;
import Fragments.VoiceNotesFragment;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;

public class ContentTask extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get projectId and projectName from ProjectList
		Bundle bundle = getIntent().getExtras();

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
}
