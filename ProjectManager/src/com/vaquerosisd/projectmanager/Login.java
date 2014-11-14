package com.vaquerosisd.projectmanager;

import com.vaquerosisd.object.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements WebserviceCallback{
	
	private boolean force_login = false;
	private EditText editUsername;
	
	// Funcion de retorno de la llamada al webservice
	// descrita en la interface WebserviceCallback
	public void callback(int code){
		System.out.println("Webserive code response: " + Integer.toString(code));
		
		// checar codigo de overtwrite?
		if (code == 2)
		{
			Toast.makeText(Login.this, "Ya hay un usuario logeado, sobreescribir?", Toast.LENGTH_SHORT).show();
			force_login = true;
			editUsername.setBackgroundColor(getResources().getColor(R.color.warning));
		}
		else{
			setResult(RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		final Button btnSignIn = (Button)findViewById(R.id.btnSignIn);
		editUsername = (EditText) findViewById(R.id.editUsername);
		final EditText editPassword = (EditText) findViewById(R.id.editPassword);
		
		force_login=false;
		
		//onClickListeners
		btnSignIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.btnSignIn) {
					User user = new User(Login.this, editUsername.getText().toString());
					//user.setNombre(editUsername.getText().toString());
					
					if (force_login)
						user.forceLogIn(editPassword.getText().toString());
					else
						user.logIn(editPassword.getText().toString());
					
				}
			}
		});
		
	}	// en onCreate
	
}	// end class Login

