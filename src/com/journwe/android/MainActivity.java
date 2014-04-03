package com.journwe.android;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public final static String SESSION = "com.journwe.android.session";
	public final static String USER_ID = "com.journwe.android.userid";
	public final static String USER_NAME = "com.journwe.android.username";
	private Session fbsession;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu items for use in the action bar
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.main_activity_actions, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// // Handle presses on the action bar items
	// switch (item.getItemId()) {
	// case R.id.action_search:
	// // openSearch();
	// return true;
	// case R.id.action_settings:
	// // openSettings();
	// return true;
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }

	public void login(View view) {
		final Intent intent = new Intent(this, JournWeActivity.class);
		
		Log.i("login", "start");
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {

				fbsession = session;
				
				if (session.isOpened()) {

					Log.i("login", "session opened");
					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									Log.i("login", "if");
									if (user != null) {
										Log.i("login", "complete");

										intent.putExtra(USER_ID, user.getId());
										intent.putExtra(USER_NAME, user.getName());
										intent.putExtra(SESSION, fbsession);
										
										startActivity(intent);
									}
								}

							});
					HttpClient client = new DefaultHttpClient();
				}
			}
		});

	}
}