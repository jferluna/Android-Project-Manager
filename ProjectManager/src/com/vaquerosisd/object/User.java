package com.vaquerosisd.object;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import com.vaquerosisd.database.ProjectOperations;
import com.vaquerosisd.projectmanager.WebserviceCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class User {

	private WebserviceCallback callerActivity;
	private SharedPreferences.Editor editor;

	private final String api_host = "http://projectmanager-api.herokuapp.com/";
	private final String route_login = "login/create";
	private final String route_forcelogin = "login/overwrite";
	private final String route_logout = "login/destroy";
	private final String route_get = "projects/get";
	private final String route_sync = "projects/sync";
	private final String route_create = "login/new";
	private final String route_forgot = "login/forgot";

	private ProjectOperations db;

	static SharedPreferences prefs;

	private String nombre;
	private int id;
	private Context context;
	private static User user = null;

	private static final String SESSION_PREFS = "session";

	private String auth_token;

	// Constructores

	public User() {
		this.id = -1;
		this.nombre = null;
	}

	public User(Activity activity) {
		callerActivity = (WebserviceCallback) activity;
		context = activity;

		prefs = activity.getSharedPreferences(SESSION_PREFS,
				Context.MODE_PRIVATE);
	}

	public User(String nombre, Context context) {
		this.nombre = nombre;
		this.context = context;
	}

	public User(Activity activity, String nombre, String token) {
		this.auth_token = token;
		this.nombre = nombre;
		callerActivity = (WebserviceCallback) activity;
		context = activity;

		prefs = activity.getSharedPreferences(SESSION_PREFS,
				Context.MODE_PRIVATE);
	}

	public User(Activity activity, String nombre) {
		this.nombre = nombre;
		callerActivity = (WebserviceCallback) activity;
		context = activity;

		prefs = activity.getSharedPreferences(SESSION_PREFS,
				Context.MODE_PRIVATE);
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

		prefs = activity.getSharedPreferences(SESSION_PREFS,
				Context.MODE_PRIVATE);

		String nom = prefs.getString("nombre", "");
		String token = prefs.getString("auth_token", "");

		if (!nom.isEmpty() && !token.isEmpty()) {

			user = new User(activity, nom, token);
		}

		return user;
	}

	//
	// Metodo para logear en el servicio y obtener el recurso, si existe un
	// usuario
	// previamente registrado con un token en el server, este metodo regresara
	// un codigo
	// de error para controlar si el usuario desea sobreescribir la sesion
	//
	// en ese caso llamar a forceLogIn()
	//
	public void logIn(String password) {

		String urlString = api_host + route_login;

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("nombre", nombre);
			request.put("password", password);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GetData(context).execute(urlString, json.toString());

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

		String urlString = api_host + route_forcelogin;

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("nombre", nombre);
			request.put("password", password);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GetData(context).execute(urlString, json.toString());

		// checa si el usuario no es null (los privates)
		if (this.nombre != null) {
			System.out.println("Intento ForceLogIn con nombre = null");
		} else
			System.out.println("Intento ForceLogin de " + this.nombre);
		// hace peticion al webservice por json

	}

	//
	// Metodo LogOut para destruir el token local y en el servidor
	public void logOut() {

		// final SharedPreferences prefs;
		// prefs = this.context.getSharedPreferences(PREFS,
		// Context.MODE_PRIVATE);

		String urlString = api_host + route_logout;

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("auth_token", auth_token);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GetData(context).execute(urlString, json.toString()); // Peticion al
																	// webservice

	}

	//
	// Obtiene arreglo de projects para el user
	public void getProjects(String token) {
		


		String urlString = api_host + route_get;

		db = new ProjectOperations(context);
		db.open();

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("auth_token", token);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.close();
		

		new GetData(context).execute(urlString, json.toString());

	}

	//
	// Obtiene arreglo de tasks para el proyecto de usuario definido
	public void sync() {

		String urlString = api_host + route_sync;

		db = new ProjectOperations(context);
		db.open();

		List<Project> list_projects = db.getAllProjects();
		List<Task> list_tasks = db.getAllTasks();

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray projects = new JSONArray();
		JSONArray tasks = new JSONArray();

		// System.out.println(JsonWrapper.project(list_projects.get(0)).toString());
		System.out.println("project array");

		try {

			request.put("auth_token", auth_token);

			Iterator<Project> iterator = list_projects.iterator();

			while (iterator.hasNext()) {
				JSONObject j = JsonWrapper.project(iterator.next());
				projects.put(j);
			}

			Iterator<Task> iteratortask = list_tasks.iterator();

			while (iteratortask.hasNext()) {
				JSONObject t = JsonWrapper.task(iteratortask.next());
				tasks.put(t);
			}

			request.put("projects", projects);
			request.put("tasks", tasks);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		db.close();

		new GetData(context).execute(urlString, json.toString());

	} // end sync

	//
	// Create Account, parecido a login pero siempre es exitoso, intenta crear
	// una nueva cuenta de usuario
	// recibe ademas el email por unica vez
	public void createAccount(String password, String email) {

		String urlString = api_host + route_create;

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("nombre", nombre);
			request.put("password", password);
			request.put("email", email);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GetData(context).execute(urlString, json.toString());

	}

	//
	// Envia request para cambio de password y envio a email registrado
	public void forgotPassword() {

		String urlString = api_host + route_forgot;

		JSONObject request = new JSONObject();
		JSONObject json = new JSONObject();

		try {
			request.put("nombre", nombre);
			json.put("request", request);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new GetData(context).execute(urlString, json.toString());

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

			JSONObject response;

			try {
				response = wsm.getJSONFromUrl(urls[0], urls[1]);
				return response;
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

				if (json == null)
					System.out.println("User: NULL JSON response");

				int code = Integer.parseInt(json.getString("code"));
				String string_code = json.getString("code");

				switch (code) {

				case 7: // succesful account create

				case 4: // sucess login or overwrite
					// Agregar user a shared preferences
					//
					System.out
							.println("USER: add session to shared prefrences usr:"
									+ nombre
									+ ", token: "
									+ json.getString("auth_token"));
					editor = User.prefs.edit();
					editor.putString("nombre", nombre);
					editor.putString("auth_token", json.getString("auth_token"));
					editor.commit();
					
					System.out.println("getprojects en 4 repsonse");

					getProjects(json.getString("auth_token"));

					//callerActivity.callback(new JsonWrapper(json));

					break;

				case 5: // Status OK, session destroyed
					System.out
							.println("USER: Session destroyed remotely and locally:");
					editor = prefs.edit();
					editor.clear();
					editor.commit();
					callerActivity.callback(new JsonWrapper(json, context));

					break;

				case 6: // Status OK, API succeded
					System.out.println("USER: API call succeded:");
					break;
					
				case 8:
					System.out.println("USER: getprojects call succeded:");
					callerActivity.callback(new JsonWrapper(json, context));
					break;

				default: // Status ERROR
					System.out.println("ERROR IN API CALL: " + string_code);
					callerActivity.callback(new JsonWrapper(json, context));
					break;

				} // end switch

			} catch (JSONException e) {
				System.out.println(e);
			} catch (Exception e) {
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