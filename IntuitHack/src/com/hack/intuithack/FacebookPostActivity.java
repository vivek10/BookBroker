package com.hack.intuithack;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class FacebookPostActivity extends Activity {
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_facebook_post);
		setTitle("Book Information");
		TextView bookInfo = (TextView) findViewById(R.id.BookInfo);
		
		bookInfo.setText("ISBN: " + intent.getStringExtra("isbn") +
				"\n\nName: " + intent.getStringExtra("name")+ 
				"\n\nAuthor: " + intent.getStringExtra("authln") + 
				"\n\nSubject: " + intent.getStringExtra("csubj") +
				"\n\nClass Number: " + intent.getStringExtra("cnum") +
				"\n\nProfessor: " + intent.getStringExtra("profln")+"\n");
		final EditText price = (EditText) findViewById(R.id.Price);
		Button postButton = (Button) findViewById(R.id.PostButton);

		postButton.setOnClickListener(new OnClickListener() {
			Intent intent = getIntent();
			String isbn = intent.getStringExtra("isbn");
			String name = intent.getStringExtra("name");
			String authln = intent.getStringExtra("authln");
			String csubj = intent.getStringExtra("csubj");
			String cnum = intent.getStringExtra("cnum");
			String profln = intent.getStringExtra("profln");
			public void onClick(View v) {
				facebookLogin();
				if (Session.getActiveSession().isOpened())
				{
					postToFacebook(isbn, name, authln, csubj, cnum, profln, price.getText().toString());
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	private void facebookLogin() {
//		  Session.openActiveSession(this, true, new Session.StatusCallback() {
//			    // callback when session changes state
	       if (Session.getActiveSession() == null ||
	                !Session.getActiveSession().isOpened()) {
	            Session.openActiveSession(this, true, new Session.StatusCallback() {
			    @SuppressWarnings("deprecation")
				@Override
			    public void call(Session session, SessionState state, Exception exception) {
			    	if (session.isOpened()) {
			    		// make request to the /me API
			    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
			    		  // callback after Graph API response with user object
			    			
			    		  @Override
			    		  public void onCompleted(GraphUser user, Response response) {
			    			  if (user != null) {
			    				  Log.d("FACEBOOK", "got user");
			    				  
			    			  } else {
			    				  Log.d("FACEBOOK", "did not get user");
			    			  }
			    		  }
			    		});
			    	}
			    	else
			    	{
			    		Log.d("FACEBOOK", "couldn't log in");
			    	}
			    }
			  });};
	}

	public void postToFacebook(String isbn, String name, String authln, String csubj, String cnum, String profln, String price) { 
		Session session = Session.getActiveSession();
		 Session.NewPermissionsRequest newPermissionsRequest = new Session
                 .NewPermissionsRequest(this, PERMISSIONS);
		session.requestNewPublishPermissions(newPermissionsRequest);
//		else postToFacebook(isbn, name, authln, csubj, cnum, profln, price);
		new FacebookPostTask(isbn, name, authln, csubj, cnum, profln, price).execute();
		Toast.makeText(this, "Posted Successfully", Toast.LENGTH_LONG).show();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facebook_post, menu);
		return true;
	}

}
