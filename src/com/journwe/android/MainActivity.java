package com.journwe.android;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class MainActivity extends FragmentActivity {

	public final static String USER = "com.journwe.android.user";
	public final static String PROVIDER = "com.journwe.android.provider";
	private Session fbsession;
	private static ProgressBar progress;
	private static LoginButton authButton;
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		uiHelper = new UiLifecycleHelper(MainActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);
		
		Session session = Session.getActiveSession();

		if (session != null) {
			Log.i("login", session.toString());
			Log.i("login", session.getState().toString());
		}

		else {
			Log.i("login", "null");
		}

		if (session != null && !session.isClosed()) {
			login();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);

		login();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i("TAG", "Logged in...");
		} else if (state.isClosed()) {
			Log.i("TAG", "Logged out...");
			if (progress != null) {
				progress.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			authButton = (LoginButton) rootView.findViewById(R.id.authButton);
			authButton.setReadPermissions("email");

			authButton.setVisibility(1);
			Log.i("!", "!");

			progress = (ProgressBar) rootView.findViewById(R.id.loading_spinner);
			
			return rootView;
		}
	}

	public void login() {
		final Intent intent = new Intent(this, JournWeListActivity.class);

		Log.i("login", "start");
		// start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (authButton != null) {
					authButton.setVisibility(View.GONE);
				}

				fbsession = session;

				if (exception != null) {
					Log.i("login", exception.getMessage());
				}

				if (!state.isOpened()) {
					Log.i("login", "not opened");
					Log.i("login", session.getState().toString());
				}

				if (state.isOpened()) {

					Log.i("login", "session opened");
					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										Log.i("login", "complete");

										intent.putExtra(PROVIDER, "facebook");
										intent.putExtra(USER,
												new JournWeFacebookUser(user,
														fbsession));

									    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

										Log.i("login", "start activity");

										startActivity(intent);
										finish();

										// onDestroy();
									}
								}

							});
				}
			}
		});

	}
}
