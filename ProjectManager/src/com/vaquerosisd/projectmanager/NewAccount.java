package com.vaquerosisd.projectmanager;

import com.vaquerosisd.object.JsonWrapper;
import com.vaquerosisd.object.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewAccount extends Activity implements WebserviceCallback {
	
	public void callback(JsonWrapper response){
		System.out.println("Webserive code response: " + response.getInt("code"));
		
		// checar codigo de overtwrite?
		if (response.getCode() == 7)
		{
			setResult(RESULT_OK);
			finish();
			
			Toast.makeText(NewAccount.this, "Cuenta creada", Toast.LENGTH_SHORT).show();
		
		}
		else{
			Toast.makeText(NewAccount.this, "Usuario invalido", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		
		Button registerAccount = (Button)findViewById(R.id.newAccount_RegisterAccount);
		Button login = (Button)findViewById(R.id.newAccount_GoToLogin);
		final EditText accname = (EditText)findViewById(R.id.newAccount_AccountName);
		final EditText accpass = (EditText)findViewById(R.id.newAccount_Password);
		final EditText accmail = (EditText)findViewById(R.id.newAccount_Email);
		
		registerAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//intent
				//if result = invalid
				User user = new User(NewAccount.this, accname.getText().toString());
				
				user.createAccount(accpass.getText().toString(), accmail.getText().toString());
				
				
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
