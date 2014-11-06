package com.vaquerosisd.projectmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Users extends Activity {
	
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {	// PASAR A ON RESUME
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);
		
		final SharedPreferences prefs = getSharedPreferences("session", Activity.MODE_PRIVATE);
		
		String usuario = prefs.getString("user", "");
		
		if (usuario.isEmpty())	// no hay sesion logeada
		{
			System.out.println("AAAA");
			System.out.println(usuario);
			Intent intent = new Intent(Users.this, Login.class);
			startActivity(intent);	// ir a Log In
		}else
		{
			System.out.println("BBBB");
			System.out.println(usuario);
		}
		
	} // end onCreate
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.users, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
