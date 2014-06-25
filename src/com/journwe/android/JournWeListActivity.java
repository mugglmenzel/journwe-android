package com.journwe.android;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class JournWeListActivity extends ActionBarActivity {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */

	private String[] mDrawerLabels;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	public final static String SEND_TRIP = "com.journwe.android.trip";

	private CharSequence mTitle;
	private static final String URL_LOGIN = "http://www.journwe.com/api/json/mobile/authenticate/facebook";
	private static final String URL_CALL = "/api/json/adventures/my.json";
	private static CookieManager cookieManager;
	private static ListView lv;
	private static ArrayList<Trip> myTrips;
	static JournweArrayAdapter adapter;
	private static String provider;
	private static JournWeFacebookUser user;
	private static LinearLayout progress;

	private static Intent intentDetail;
	private static Intent intentAdd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_journ_we);

        mTitle = mDrawerTitle = getTitle();
		mDrawerLabels = new String[] {"My JournWes", "ID", "Name"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
       
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    
        
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerLabels));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
       
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) {
            selectItem(0);
        }
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));

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
			object.put("expires_in", (user.getSession().getExpirationDate()
					.getTime() - new Date().getTime()));
			object.put("", user);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			String urlstring = URL_LOGIN;

			Log.i("API call", urlstring);

			URL url = new URL(urlstring);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");

			connection.connect();

			OutputStreamWriter wr = new OutputStreamWriter(
					connection.getOutputStream());
			wr.write(object.toString());
			wr.flush();

			wr.close();

			Log.i("response", "Response Code : " + connection.getResponseCode());

			cookieManager = (CookieManager) CookieHandler.getDefault();

			Log.i("cookie", cookieManager.getCookieStore().getCookies().size()
					+ "");

			// new TripLoader().execute(this);

			call();

			adapter = new JournweArrayAdapter(this,
					android.R.layout.simple_list_item_1, myTrips);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.i("exception", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i("exception", e.getMessage());
		}

		mTitle = getTitle();

		intentDetail = new Intent(this, JournWeDetail.class);
		intentAdd = new Intent(this, CreateJournWe.class);
	}
	
	/* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_settings:
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//        	
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	public void setTrips(ArrayList<Trip> result) {
		myTrips = result;

		if (lv != null) {
			lv.setAdapter(adapter);
		}
		adapter.clear();
		adapter.addAll(result);
		adapter.notifyDataSetChanged();

		Log.i("data changed", myTrips.size() + "");

		if (progress != null) {
			progress.setVisibility(View.GONE);
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

	public String getUrl() {
		return URL_CALL;
	}

	private void call() {
		new TripLoader().execute(this);
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

//			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				Log.i("start", "call");

				progress = (LinearLayout) rootView
						.findViewById(R.id.linlaHeaderProgress);

				if (progress == null) {
					Log.i("progressbar", "null");
				}

				else {
					Log.i("progressbar", progress.toString());
				}

				if (myTrips == null) {
					// call();
				}

				lv = (ListView) rootView.findViewById(R.id.listview);

				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							final View view, int position, long id) {

						if (position == 0) {
							startActivity(intentAdd);
						}

						else {
							Trip item = (Trip) parent
									.getItemAtPosition(position - 1);
							Log.i("click", item.toString());

							item.setImage(null);

							intentDetail.putExtra(SEND_TRIP, item);
							startActivity(intentDetail);
						}

					}

				});

//			}
//
//			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
//
//			}
//
//			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
//
//			}

			return rootView;
		}
	}
	
	/* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    Fragment fragment = new PlaceholderFragment();

	    // Insert the fragment by replacing any existing fragment
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    fragmentManager.beginTransaction()
	                   .replace(R.id.container, fragment)
	                   .commit();

	    // Highlight the selected item, update the title, and close the drawer
	    mDrawerList.setItemChecked(position, true);
	    setTitle(mDrawerLabels[position]);
	    mDrawerLayout.closeDrawer(mDrawerList);
	    
	    call();
	}

	@Override
	public void setTitle(CharSequence title) {
	    mTitle = title;
	    getSupportActionBar().setTitle(mTitle);
	}

}
