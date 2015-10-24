package com.negev.bikeotlv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONObject getJSONFromUrl(String url) throws JSONException {

		// Making HTTP request
		try {


			// defaultHttpClient

			HttpClient client = new DefaultHttpClient(); 
			//HttpPost post = new HttpPost("www.google.com");   

			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			//		    JSONObject obj = new JSONObject();
			//		    obj.put("UserID", "1");
			//		    obj.put("updateDateTime", "1900-01-01T10:00:00.0000000");
			//		    post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);  
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();
			Log.i("response", is.toString());



		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public JSONObject getPostJSONFromUrlWithJSONObj(String url, JSONObject obj) throws JSONException {

		try {

			HttpClient client = new DefaultHttpClient(); 

			HttpPost post = new HttpPost(url);   
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);  
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity == null) {
				return null;
			}
			is = httpEntity.getContent();   

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();


		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}

	public JSONObject getPutJSONFromUrlWithJSONObj(String url, JSONObject obj) throws JSONException {


		try {

			HttpClient client = new DefaultHttpClient(); 

			HttpPut post = new HttpPut(url);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);  
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity == null) {
				return null;
			}
			is = httpEntity.getContent();   


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}


		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
			Log.i("AppFlow", json.toString());
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}


		// return JSON String
		return jObj;
	}

	public JSONObject getGetJSONFromUrl(String url) throws JSONException {


		try {

			HttpClient client = new DefaultHttpClient(); 
			HttpGet post = new HttpGet(url);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			HttpResponse response = client.execute(post);  
			HttpEntity httpEntity = response.getEntity();
			is = httpEntity.getContent();   


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}


		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}


		// return JSON String
		return jObj;
	}
	
	public JSONObject getDeleteJSONFromUrlWithJSONObj(String url, JSONObject obj) throws JSONException {


		try {

			HttpClient client = new DefaultHttpClient(); 
			HttpDeleteWithBody post = new HttpDeleteWithBody(url);
//			HttpDelete post = new HttpDelete(url);
			post.setHeader("Content-type", "application/json");
			post.setHeader("Accept", "application/json");
			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);  
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity == null) {
				return null;
			}
			is = httpEntity.getContent();   


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}


		// try parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
			Log.i("AppFlow", "sdf");
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}


		// return JSON String
		return jObj;
	}

}

