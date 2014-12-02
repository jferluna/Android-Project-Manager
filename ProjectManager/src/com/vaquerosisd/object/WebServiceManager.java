package com.vaquerosisd.object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.json.JSONObject;

public class WebServiceManager{

		
		private static HttpURLConnection urlConnection;
		public WebServiceManager(){
			super();
		}
		
	    //
	    // Metodos JSON
	    
	  //Metodo para obtener una respuesta Json de un webservice
		public JSONObject getJSONFromUrl(String url, String json) throws SocketException{
		
	    JSONObject jsonObject = null;
		//HttpURLConnection urlConnection = null;
		
		try{
			
			URL myUrl = new URL(url);
			
			urlConnection =(HttpURLConnection)myUrl.openConnection();
			
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", "application/json");
			urlConnection.setRequestProperty("Accept", "application/json");
			
			OutputStreamWriter out= new OutputStreamWriter(urlConnection.getOutputStream());
			System.out.println(json);
			out.write(json);
			out.flush();
			out.close();
			
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
