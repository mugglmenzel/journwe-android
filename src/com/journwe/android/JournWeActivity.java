package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;

public class JournWeActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private static Session session;
	private static String userid;
	private static String username;
	private static final String URL_LOGIN = "http://www.journwe.com/api/json/mobile/login";
	private static final String URL_STRING = "http://www.journwe.com/api/json/adventures/my.json";
	private static CookieManager cookieManager;
	private ListView lv;
	private static List<Trip> myTrips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journ_we);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		// lv = (ListView) findViewById(R.id.listView1);
		// Button bu = new Button(this);
		// // lv.add(bu);

		myTrips = new ArrayList<Trip>();

		Intent intent = getIntent();
		Bundle b = intent.getExtras();

		if (b != null) {
			session = (Session) b.get(MainActivity.SESSION);
			userid = (String) b.get(MainActivity.USER_ID);
			username = (String) b.get(MainActivity.USER_NAME);
		}

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));

		try {
			String urlstring = URL_LOGIN;

			Log.i("API call", urlstring);

			String cookiestr = "mobileLogin=true&authProvider=facebook&authUserId="
					+ userid
					+ "&expires="
					+ (session.getExpirationDate().getTime() - new Date()
							.getTime());
			cookiestr.replace(" ", "+");

			Log.i("call", cookiestr);

			URL url = new URL(urlstring + "?" + cookiestr);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.connect();

			Log.i("login",
					String.valueOf(connection.getHeaderFields().get(
							"Set-Cookie")));
			cookieManager = (CookieManager) CookieHandler.getDefault();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// catch (URISyntaxException e) {
		// e.printStackTrace();
		// }

		try {
			Log.i("login",
					String.valueOf(cookieManager.getCookieStore().get(
							new URI(URL_LOGIN))));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.journ_we, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
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

	private static String call() {
		String re = "call";
		String urlstring = URL_STRING;

		try {
			URL url = new URL(urlstring);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			re = "";
			InputStream in = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String read = "";
			while ((read = br.readLine()) != null) {
				re += read;
			}

			Log.i("api", re);

			JSONArray jsonArray;
			myTrips = new ArrayList<Trip>();

			try {
				jsonArray = new JSONArray(re);
				Log.i("JSON", "Number of entries " + jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					Status s = null;
					if (jsonObject.getString("status").equals("GOING")) {
						s = Status.GOING;
					}
					
					else if (jsonObject.getString("status").equals("BOOKED")) {
						s = Status.BOOKED;
					}
					
					else if (jsonObject.getString("status").equals("NOTGOING")) {
						s = Status.NOTGOING;
					}
					
					else if (jsonObject.getString("status").equals("UNDECIDED")) {
						s = Status.UNDECIDED;
					}
					
					Trip t = new Trip(jsonObject.getString("id"), jsonObject.getString("name"), jsonObject.getString("link"), Integer.parseInt(jsonObject.getString("peopleCount")), s);
					myTrips.add(t);
					Log.i("Trip", "name: " + t.getName());
					Log.i("Trip", "id: " + t.getId());
					Log.i("Trip", "link: " + t.getLink());
					Log.i("Trip", "people: " + t.getPeople());
					Log.i("Trip", "status: " + t.getStatus());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			re = "MalformedURLException";
			e.printStackTrace();
		} catch (IOException e) {
			re = "IOException";
			e.printStackTrace();
		}

		return re;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_journ_we,
					container, false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_label);

			String text = "";

			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				text = username;
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				text = userid;
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				text = call();
			}

			textView.setText(text); // Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((JournWeActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}
}
