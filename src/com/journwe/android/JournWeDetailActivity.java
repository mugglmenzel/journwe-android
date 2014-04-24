package com.journwe.android;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JournWeDetailActivity extends Activity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private static Trip t;
	private static final String URL_BASE = "http://www.journwe.com";
	private static DetailedTrip trip;
	private static DownloadTask dt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journ_we_detail);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		Intent i = getIntent();
		t = (Trip) i.getExtras().get(JournWeListActivity.SEND_TRIP);
		trip = new DetailedTrip(t);
		dt = new DownloadTask(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.journ_we_detail, menu);
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

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private Context context;
		private PowerManager.WakeLock mWakeLock;

		public DownloadTask(Context context) {
			this.context = context;
		}

		@Override
		protected String doInBackground(String... sUrl) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null;
			String in;
			try {
				URL url = new URL(URL_BASE + sUrl[0]);
				
				Log.i("url", url.toString());
				
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				// expect HTTP 200 OK, so we don't mistakenly save error report
				// instead of the file
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP "
							+ connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}

				// download the file
				input = connection.getInputStream();

				BufferedReader br = new BufferedReader(new InputStreamReader(
						input));
				String read = "";
				in = "";

				while ((read = br.readLine()) != null) {
					in += read;
				}

				Log.i("place", in);

				if (isCancelled()) {
					input.close();
					return null;
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (connection != null)
					connection.disconnect();
			}
			return in;
		}
	}

	private static void callPlace() {

		String in = dt.doInBackground("/api/json/adventure/"
				+ trip.getTrip().getId() + "/places.json");

		// try {
		// URL url = new URL(URL_BASE + "/api/json/adventure/"
		// + trip.getTrip().getId() + "/places.json");
		//
		// Log.i("url", url.toString());
		//
		// HttpURLConnection con = (HttpURLConnection) url.openConnection();
		//
		// InputStream in = con.getInputStream();
		//
		// BufferedReader br = new BufferedReader(new InputStreamReader(in));
		// String read = "";
		// String input = "";
		//
		// while ((read = br.readLine()) != null) {
		// input += read;
		// }
		//
		// Log.i("place", input);
		//
		try {
			JSONObject j = new JSONObject(in);

			Log.i("place", j.getString("placeId"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		//
		// } catch (MalformedURLException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	private static void callAdventurers() {
		String in = dt.doInBackground("/api/json/adventure/"
					+ trip.getTrip().getId() + "/adventurers.json");
		
//		try {
//			URL url = new URL(URL_BASE + "/api/json/adventure/"
//					+ trip.getTrip().getId() + "/adventurers.json");
//
//			Log.i("url", url.toString());
//
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//			InputStream in = con.getInputStream();
//
//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
//			String read = "";
//			String input = "";
//
//			while ((read = br.readLine()) != null) {
//				input += read;
//			}
//
//			Log.i("adventurers", input);
//
			JSONArray json = null;

			try {
				Log.i("json", in);
				json = new JSONArray(in);

				for (int i = 0; i < json.length(); i++) {
					JSONObject j = json.getJSONObject(i);

					JournWeAdventurer ja = new JournWeAdventurer(
							j.getString("is"), j.getString("status"),
							j.getString("name"), j.getString("link"));

					Log.i("object", j.toString());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
//
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	/*
	 * [ {"id":"c067681b-054e-45d8-83d1-250c59c512c3",
	 * "link":"http://www.journwe.com/user/c067681b-054e-45d8-83d1-250c59c512c3"
	 * , "name":"Susi",
	 * "image":"https://graph.facebook.com/100008061185656/picture?width=1200",
	 * "status":"GOING" }]
	 */

	private static void callTime() {
		try {
			URL url = new URL(URL_BASE + "/api/json/adventure/"
					+ trip.getTrip().getId() + "/times.json");

			Log.i("url", url.toString());

			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			InputStream in = con.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String read = "";
			String input = "";

			while ((read = br.readLine()) != null) {
				input += read;
			}

			Log.i("time", input);

			try {
				JSONObject j = new JSONObject(input);

				Log.i("adventurer name", j.getString("name"));

			} catch (JSONException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_tab_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_tab_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_tab_section3).toUpperCase(l);
			}
			return null;
		}
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
			View rootView = inflater.inflate(R.layout.fragment_journ_we_detail,
					container, false);
			TextView textView = (TextView) rootView
					.findViewById(R.id.section_tab_label);

			String text = "";
			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				text = trip.getTrip().getFavPlace();
				// callPlace();
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				text = String.valueOf(trip.getTrip().getPeople());
				callAdventurers();
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				text = trip.getTrip().getFavTime();
				// callTime();
			}

			textView.setText(text);
			return rootView;
		}
	}
}
