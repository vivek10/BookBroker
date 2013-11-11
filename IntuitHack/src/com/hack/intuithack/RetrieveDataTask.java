package com.hack.intuithack;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class RetrieveDataTask extends AsyncTask<String, Void, ArrayList<String>>{
	Intent intent;
	Activity activity;
	Context context;
	public RetrieveDataTask(Activity activity, Intent intent, Context context)
	{
		this.intent = intent;
		this.activity = activity;
		this.context = context;
	}
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(params[0]);

        ArrayList<String> aList = new ArrayList<String>();
        InputStream inputStream = null;
        String result = null;
        try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

		    inputStream = entity.getContent();
		    // json is UTF-8 by default
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    result = sb.toString();
		} catch (Exception e) { 
			    Log.d("ERROR", e.toString());
		}
		finally {
			 Log.d("ERROR", "FINAL");
			    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
		}
        JSONObject jObject;
        try{
        	jObject = new JSONObject(result);
        	aList.add(jObject.getString("cnum"));
        	aList.add(jObject.getString("isbn"));
        	aList.add(jObject.getString("profln"));
        	aList.add(jObject.getString("csubj"));
        	aList.add(jObject.getString("name"));
        	aList.add(jObject.getString("authln"));
        } catch(JSONException e)
        {
        	 Log.d("INACC", params[0]);
        }
        return aList;
	}
	
	@Override
	  protected void onPostExecute(ArrayList<String> result) {
//		  Log.d("ERROR","YOLO WE HAVE PROBS");
		if(result.size() == 0)
		{
			intent.putExtra("BAD", "1");
			Toast.makeText(context, "Please Re-Scan, Book Not Found", Toast.LENGTH_LONG).show();
		}
		else
		{
//			try{
		  intent.putExtra("BAD","0");
	      intent.putExtra("cnum", result.get(0));
	      intent.putExtra("isbn", result.get(1));
	      intent.putExtra("profln", result.get(2));
	      intent.putExtra("csubj", result.get(3));
	      intent.putExtra("name", result.get(4));
	      intent.putExtra("authln", result.get(5));
	      activity.startActivity(intent);
//			}
//	      catch(Exception e)
//	      {
//	    	  Log.d("ERROR", e.toString());
//	      }
		}
	  }
	
}
