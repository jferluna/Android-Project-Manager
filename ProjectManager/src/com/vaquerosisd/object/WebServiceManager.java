package com.vaquerosisd.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import android.os.AsyncTask;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class WebServiceManager{

		
		private static HttpURLConnection urlConnection;
		public WebServiceManager(){
			super();
		}
		
	    //
	    // Metodos JSON
	    
	  //Metodo para obtener una respuesta Json de un webservice
		public JSONObject getJSONFromUrl(String url) throws SocketException{
		
	    JSONObject jsonObject = null;
		//HttpURLConnection urlConnection = null;
		
		try{
			
			URL myUrl = new URL(url);
			
			urlConnection =(HttpURLConnection)myUrl.openConnection();
			
			//urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			int responseCode = urlConnection.getResponseCode();
			if(responseCode == HttpStatus.SC_OK){
				String responseString = readStream(urlConnection.getInputStream());
				jsonObject = new JSONObject(responseString);
			}
			
		}
		catch (Exception e){
			System.out.println(e);
		}
		finally{
			if(urlConnection!=null)
				urlConnection.disconnect();
		}
		
		
		return jsonObject;
		
		} // end getJSONFromUrl
		
		
		// Metodo privado para leer un buffered reader del inputstream
		private String readStream(InputStream	in)	{	
			BufferedReader reader	=	null;	
			StringBuffer	response	=	null; 			
			try	{	
				 		reader	=	new	BufferedReader(new	InputStreamReader(in,	"UTF-8"));	
				 		String	line	=	"";	
				 		response	=	new	StringBuffer();	
				 	 while	((line	=	reader.readLine())	!=	null)	{	
				 							response.append(line);	
				 			}	
				 		}	catch	(IOException	e)	{	
				 					e.printStackTrace();	
				 		}	finally	{	
				 					if	(reader	!=	null)	{	
				 							try	{	
				 									reader.close();	
				 							}	catch	(IOException e)	{	
				 									e.printStackTrace();	
				 							}	
				 					}	
				 			}	
				 			return	response.toString();	
				 	}	//end readStream method	

		
		
		
}	 //end class webServiceManager
