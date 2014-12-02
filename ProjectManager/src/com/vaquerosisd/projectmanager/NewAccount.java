package com.vaquerosisd.projectmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NewAccount extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		
		Button registerAccount = (Button)findViewById(R.id.newAccount_RegisterAccount);
		Button login = (Button)findViewById(R.id.newAccount_GoToLogin);
		
		registerAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//intent
				//if result = invalid
				Toast.makeText(getApplicationContext(), "An account with that name already exists.", Toast.LENGTH_SHORT).show();
			}
		});
		
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("LogIn", true);
				setResult(RESULT_CANCELED, returnIntent);
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
	
}
