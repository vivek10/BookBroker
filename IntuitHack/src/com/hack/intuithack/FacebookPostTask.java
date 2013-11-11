package com.hack.intuithack;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class FacebookPostTask extends AsyncTask<String, Void, Response>{
	String isbn,name,authln,csubj,cnum, profln,price;
	public FacebookPostTask(String isbn, String name, String authln,
			String csubj, String cnum, String profln, String price) {
		this.isbn = isbn;
		this.name = name; 
		this.authln = authln;
		this.csubj = csubj;
		this.cnum = cnum;
		this.profln = profln;
		this.price = price;
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Response doInBackground(String... params) {
		Bundle extras = new Bundle();
		final String GROUP_URI = "/644546108928814/feed";
		if(!price.toLowerCase().equals("price"))
		{
			extras.putString("message", "I am selling "+name+" by "+authln+" for $"+price+ ". The book's isbn is "+
							isbn + ". This book was used in "+csubj + " "+ cnum + " taught by "+ profln +" in FA13. Inbox Me!");
		}
		else
		{
			extras.putString("message", "I am selling "+name+" by "+authln+". The book's isbn is "+
					isbn + ". This book was used in "+csubj + " "+ cnum + " taught by "+ profln +" in FA13. Please message me for price.");	
		}
		extras.putString("access_token", Session.getActiveSession().getAccessToken());
		// Request request = new Request(a,v,c)
		Request request = new Request(Session.getActiveSession(), GROUP_URI,
				extras, HttpMethod.POST);
		Response resp = request.executeAndWait();
		Log.d("QUESTION", resp.toString());
		Log.d("QUESTION","This executed.");
		return resp;
	}
	protected void onPostExecute(Response result)
	{
		
	}
	

}
