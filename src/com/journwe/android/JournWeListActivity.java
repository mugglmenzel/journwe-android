package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.facebook.Session;

public class JournWeListActivity extends Activity implements
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

	public final static String SEND_TRIP = "com.journwe.android.trip";

	private CharSequence mTitle;
	private static final String URL_LOGIN = "http://www.journwe.com/api/json/mobile/authenticate/facebook";
	private static final String URL_STRING = "http://www.journwe.com/api/json/adventures/my.json";
	private static CookieManager cookieManager;
	private static ListView lv;
	private static List<Trip> myTrips;
	static JournweArrayAdapter adapter;
	private static String provider;
	private static JournWeFacebookUser user;

	private static Intent intent;

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

		myTrips = new ArrayList<Trip>();

		Log.i("start", "intent");

		Intent i = getIntent();
		Bundle b = i.getExtras();
		
		Log.i("start", "intent complete");
		
		if (b != null) {
			provider = b.getString(MainActivity.PROVIDER);
			Log.i("start", "provider");
			if (provider.equals("facebook")) {
				user = (JournWeFacebookUser) b.get(MainActivity.USER);
			}

			Log.i("start", "user");
			
		}

		intent = new Intent(this, ShowTripActivity.class);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));


		try {
			String urlstring = URL_LOGIN;

			Log.i("API call", urlstring);

			JSONObject object = new JSONObject();
			try {
				object.put("id", user.getId());
				object.put("name", user.getName());
				object.put("first_name", user.getFirst_name());
				object.put("last_name", user.getLast_name());
				object.put("link", user.getLink());
				object.put("username", user.getUsername());
				object.put("gender", user.getGender());
				object.put("email", user.getEmail());
				object.put("timezone", user.getTimezone());
				object.put("locale", user.getLocale());
				object.put("verified", user.getVerified());
				object.put("updeted_time", user.getUpdated_time());
				object.put("birthday", user.getBirthday());
				object.put("access_token", user.getSession().getAccessToken());
				object.put("expires_in",
						(user.getSession().getExpirationDate().getTime() - new Date()
								.getTime()));
				object.put("", user);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Log.i("API call", object.toString());

			URL url = new URL(urlstring);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", 
			           "application/json");
	        
	        connection.connect();
	        
	        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
	        wr.write(object.toString());
	        wr.flush(); 
	       
	        wr.close(); 
	        
			Log.i("response", "Response Code : " + connection.getResponseCode());
	        
			cookieManager = (CookieManager) CookieHandler.getDefault();
	        
			Log.i("cookie", cookieManager.getCookieStore().getCookies().size() + "");
			
			call();

			if (myTrips.size() > 0) {
				while (myTrips.size() < 20) {
					myTrips.add(myTrips.get(0));
				}
			}
			
			Log.i("start", "adapter");
			adapter = new JournweArrayAdapter(this,
					android.R.layout.simple_list_item_1, myTrips);
	//

		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i("exception", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("exception", e.getMessage());
		}

		// try {
		// Log.i("login",
		// String.valueOf(cookieManager.getCookieStore().get(
		// new URI(URL_LOGIN))));
		// } catch (URISyntaxException e) {
		// e.printStackTrace();
		// }

	}

	public void writeJSON() {
		JSONObject object = new JSONObject();
		try {
			object.put("access_token", user.getSession().getAccessToken());
			object.put("expires",
					(user.getSession().getExpirationDate().getTime() - new Date()
							.getTime()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(object);
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
		String re = "";
		String urlstring = URL_STRING;

		try {
			URL url = new URL(urlstring);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

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
					String stat = jsonObject.getString("status");
					
					if (stat.equals("GOING")) {
						s = Status.GOING;
					}

					else if (stat.equals("BOOKED")) {
						s = Status.BOOKED;
					}

					else if (stat.equals("NOTGOING")) {
						s = Status.NOTGOING;
					}

					else {
						s = Status.UNDECIDED;
					}

					// Bitmap b = BitmapFactory.decode;//TODO

					Trip t = new Trip(jsonObject.getString("id"),
							jsonObject.getString("name"),
							jsonObject.getString("link"),
							Integer.parseInt(jsonObject
									.getString("peopleCount")), s, null,
							jsonObject.getString("imageTimestamp"),
							jsonObject.getString("favoritePlace"),
							jsonObject.getString("favoriteTime"));

					myTrips.add(t);
					// Log.i("Trip", "name: " + t.getName());
					// Log.i("Trip", "id: " + t.getId());
					// Log.i("Trip", "link: " + t.getLink());
					// Log.i("Trip", "people: " + t.getPeople());
					// Log.i("Trip", "status: " + t.getStatus());
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

			Log.i("start", "call");
			call();

			lv = (ListView) rootView.findViewById(R.id.listview);

			if (lv != null) {
				Log.i("start", "set adapter");
				lv.setAdapter(adapter);
				Log.i("start", "finish");
			}

			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view,
						int position, long id) {
					Trip item = (Trip) parent.getItemAtPosition(position);
					Log.i("click", item.toString());

					intent.putExtra(SEND_TRIP, item);
					startActivity(intent);
				}

			});

			String text = "";

			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				text = call();
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				text = user.getId();
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				text = user.getName();
			}

			// textView.setText(text); //
			// Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((JournWeListActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}
}
