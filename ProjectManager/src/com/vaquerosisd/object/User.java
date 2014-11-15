package com.vaquerosisd.object;

import org.json.JSONException;
import org.json.JSONObject;

import com.vaquerosisd.projectmanager.WebserviceCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class User {
	
	private WebserviceCallback callerActivity;
	private SharedPreferences.Editor editor;
	
	static SharedPreferences prefs;
	
	private String nombre;
	private int id;
	private Context context;
	private static User user = null;
	
	private static final String SESSION_PREFS = "session";
	private JSONObject jsonResult;
	
	private Activity activity;
	private String auth_token;
	
	private static final int NO_USER = -1;
	
	// Constructores

	public User() {
		this.id = -1;
		this.nombre = null;
	}
	
	public User(Activity activity){
		callerActivity = (WebserviceCallback)activity;
		context = activity;
		
		prefs = activity.getSharedPreferences(SESSION_PREFS, activity.MODE_PRIVATE);
	}

	public User(String nombre, Context context) {
		this.nombre = nombre;
		this.context = context;
	}
	
	public User(Activity activity, String nombre, String token){
		this.activity = activity;
		this.auth_token = token;
		this.nombre = nombre;
		callerActivity = (WebserviceCallback)activity;
		context = activity;
		
		prefs = activity.getSharedPreferences(SESSION_PREFS, activity.MODE_PRIVATE);
	}
	
	public User(Activity activity, String nombre){
		this.activity = activity;
		this.nombre = nombre;
		callerActivity = (WebserviceCallback)activity;
		context = activity;
		
		prefs = activity.getSharedPreferences(SESSION_PREFS, activity.MODE_PRIVATE);
	}

	public User(int id, String nombre, Context context) {
		this.nombre = nombre;
		this.id = id;
		this.context = context;
	}

	public static User getInstance() {
		if (user == null) {
			user = new User();
		}
		return user;
	}

	// getters and setters

	
	public String getUsername() {
		return nombre;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getAuthToken() {
		return auth_token;
	}
	

	public void setAuthToken(String token) {
		this.auth_token = token;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public static User getUsr() {
		return user;
	}

	public static void setUsr(User usr) {
		User.user = usr;
	}

	//
	// Metodos de la clase

	// Obtener usuario local (si hay), si no hay regresa null
	public static User getUser(Activity activity) {

		User user = null;
		
		prefs = activity.getSharedPreferences(SESSION_PREFS, activity.MODE_PRIVATE);

		String nom = prefs.getString("nombre", "");
		String token = prefs.getString("auth_token", "");

		if (!nom.isEmpty() && !token.isEmpty()) {

			user = new User(activity, nom, token);
		}

		return user;
	}

	//
	// Metodo para logear en el servicio y obtener el recurso, si existe un usuario
	// previamente registrado con un token en el server, este metodo regresara un codigo
	// de error para controlar si el usuario desea sobreescribir la sesion
	//
	// en ese caso llamar a forceLogIn()
	//
	public void logIn(String password) {


		String urlString = "https://projectmanager-api.herokuapp.com/login/create?nombre="
				+ this.nombre + "&password=" + password;
		new GetData(context).execute(urlString);

		// checa si el usuario no es null (los privates)
		if (this.nombre == null) {
			System.out.println("Intento LogIn con nombre = null");
		}
		// hace peticion al webservice por json


	}
	
	
	//
	// Metodo para forzar la sesion en el webservce
	//
	public void forceLogIn(String password) {


		String urlString = "https://projectmanager-api.herokuapp.com/login/overwrite?nombre="
				+ this.nombre + "&password=" + password;
		new GetData(context).execute(urlString);

		// checa si el usuario no es null (los privates)
		if (this.nombre != null) {
			System.out.println("Intento ForceLogIn con nombre = null");
		}
		else
			System.out.println("Intento ForceLogin de " + this.nombre);
		// hace peticion al webservice por json


	}
	
	
	//
	// Metodo LogOut para destruir el token local y en el servidor
	public  void logOut(){
		
		//final SharedPreferences prefs;
		//prefs = this.context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		
		String urlString = "https://projectmanager-api.herokuapp.com/login/destroy?auth_token="
				+ User.prefs.getString("auth_token", "");
				
				new GetData(context).execute(urlString);	// Peticion al webservice
		
	}
	
	
	//
	// Obtiene arreglo de projects para el user
	public void getProjects(){
		
	}
	
	
	
	
	
	
	//
	// Clase para obtener datos de manera asincrona del WebService en JSON
	//
	
	public class GetData extends AsyncTask<String, Void, JSONObject> {
		private ProgressDialog dialog;
		Context context;

		public GetData(Context context) {
			this.context = context;
		}

		@Override
		protected JSONObject doInBackground(String... urls) {

			WebServiceManager wsm = new WebServiceManager();

			JSONObject json;

			try {
				json = wsm.getJSONFromUrl(urls[0]);
				return json;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		} // end doinBackground

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(this.context);
			dialog.setMessage("Connecting...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected void onPostExecute(JSONObject json) {

			// JSONArray dataJsonArray = null;
			// JSONObject dataJsonObject= null;

			try {
				// dataJsonObject = json.getJSONObject("auth_token");
				if (json == null)
					System.out.println("User: NULL JSON response");

				String status = json.getString("status");
				int code = Integer.parseInt(json.getString("code"));
				String string_code = json.getString("code");

				switch (code) {
				
				case 4:	// sucess login or overwrite
					// Agregar user a shared preferences
					//
					System.out.println("USER: add session to shared prefrences usr:" + nombre + ", token: "+ json.getString("auth_token"));
					editor = User.prefs.edit();
        			editor.putString("nombre", nombre);
        			editor.putString("auth_token", json.getString("auth_token"));
        			editor.commit();
        			
        			callerActivity.callback(new JsonWrapper(json));
        			
        			break;
        			
					
				case 5: // Status OK, session destroyed
					System.out.println("USER: Session destroyed remotely and locally:");
					editor = prefs.edit();
					editor.clear();
					editor.commit();
					callerActivity.callback(new JsonWrapper(json));
					
					default: // Status ERROR
						System.out.println("ERROR IN API CALL: " + string_code);
						callerActivity.callback(new JsonWrapper(json));
						break;
						
				}	// end switch
				

			} catch (JSONException e) {
				System.out.println(e);
			} catch (Exception e){
				System.out.println(e);
			}

			if (dialog.isShowing())
				dialog.dismiss();

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	} // End getData Class

} // end class user