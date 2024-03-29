package com.journwe.android;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.maps.MapActivity;

public class CreateJournWe extends MapActivity implements ActionBar.TabListener {

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
	private static GoogleMap map;
	private static Button invite;
	private static Button pick;
	private static Button start;
	private static Button startButton;
	private static Button endButton;
	private Dialog d;
	private static Date startDate;
	private static Date endDate;
	private static SimpleDateFormat format;
	private String dialog;
	private static DateAdapter dateAdapter;
	private static AdventurerAdapter adventurerAdapter;
	// private static PlaceAdapter placeAdapter;
	private static DetailedTrip trip;
	private static EditText adventurerText;
	private String name;
	private Bitmap image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_journ_we);

		MapsInitializer.initialize(this);

		switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
		case ConnectionResult.SUCCESS:
			Log.i("SUCCESS", "SUCCESS");
			break;
		case ConnectionResult.SERVICE_MISSING:
			Log.i("SERVICE MISSING", "SERVICE MISSING");
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Log.i("UPDATE REQUIRED", "UPDATE REQUIRED");
			break;
		default:
			Toast.makeText(getApplicationContext(),
					GooglePlayServicesUtil.isGooglePlayServicesAvailable(this),
					Toast.LENGTH_SHORT).show();
		}

		Intent intent = getIntent();

		name = intent.getExtras().getString(CreateActivity.NAME);
		Uri uri = intent.getParcelableExtra(CreateActivity.IMAGE);

		InputStream imageStream;

		if (uri == null) {
			Log.i("uri", "null");
		}

		else {
			try {
				imageStream = getContentResolver().openInputStream(uri);
				image = BitmapFactory.decodeStream(imageStream);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Toast.makeText(this, "create " + name, Toast.LENGTH_SHORT).show();

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle(name);
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

		format = new SimpleDateFormat();

		if (trip == null) {
			trip = new DetailedTrip("id", name, "link", 0, Status.GOING, null,
					"", "", "", "");
		}

		dateAdapter = new DateAdapter(this, R.id.datelist, trip.getDates());

		adventurerAdapter = new AdventurerAdapter(this, R.id.adventurerlist,
				trip.getAdventurers());

		// placeAdapter = new PlaceAdapter(this, R.id.placelist,
		// trip.getPlaces());

		// map = ((MapFragment)
		// getFragmentManager().findFragmentById(R.id.mapview))
		// .getMap();
	}

	public void buttonClick(View v) {
		switch (v.getId()) {
		case R.id.invite:
			Log.i("buttonClick", "invite");
			mViewPager.setCurrentItem(1);
			break;
		case R.id.pick:
			Log.i("buttonClick", "pick");
			mViewPager.setCurrentItem(2);
			break;
		case R.id.start:
			Log.i("buttonClick", "start");
			create();
			break;
		case R.id.addDate:
			Log.i("buttonClick", "addDate");
			if (startDate != null && endDate != null) {
				trip.addDate(new JournWeDate(startDate, endDate, 0, "false"));
				dateAdapter.notifyDataSetChanged();
				startDate = null;
				endDate = null;
				startButton.setText(R.string.timeStartHint);
				endButton.setText(R.string.timeEndHint);
			}
			break;
		case R.id.addAdventurer:
			Log.i("buttonClick", "addAdventurer");
			if (adventurerText != null
					&& !adventurerText.getText().equals(null)
					&& adventurerText.getText().length() > 0) {
				trip.addAdventurer(new JournWeAdventurer("", Status.UNDECIDED
						.toString(), adventurerText.getText().toString(), "",
						""));
				adventurerAdapter.notifyDataSetChanged();
				adventurerText.setText("");
			}
			break;
		case R.id.addPlace:
			Log.i("buttonClick", "addPlace");
			// if (adventurerText != null &&
			// !adventurerText.getText().equals(null) &&
			// adventurerText.getText().length() > 0) {
			// trip.addAdventurer(new JournWeAdventurer("",
			// Status.UNDECIDED.toString(), adventurerText.getText().toString(),
			// "", ""));
			// adventurerAdapter.notifyDataSetChanged();
			// adventurerText.setText("");
			// }
			break;
		}
	}

	public void dateClick(View v) {
		Log.i("dateClick", (String) v.getTag());
		d = new Dialog((Context) this);
		d.setContentView(R.layout.date_time_picker);
		dialog = (String) v.getTag();
		d.setTitle(dialog);
		Button ok = (Button) d.findViewById(R.id.ok);
		// if button is clicked, close the custom dialog
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePicker datePicker = ((DatePicker) d
						.findViewById(R.id.datePicker));
				TimePicker timePicker = ((TimePicker) d
						.findViewById(R.id.timePicker));

				if (dialog.equals("Start")) {
					startDate = new Date(datePicker.getYear() - 1900,
							datePicker.getMonth(), datePicker.getDayOfMonth(),
							timePicker.getCurrentHour(), timePicker
									.getCurrentMinute());

					startButton.setText(format.format(startDate));
				}

				else if (dialog.equals("End")) {
					endDate = new Date(datePicker.getYear() - 1900, datePicker
							.getMonth(), datePicker.getDayOfMonth(), timePicker
							.getCurrentHour(), timePicker.getCurrentMinute());

					endButton.setText(format.format(endDate));
				}

				d.dismiss();
			}
		});

		d.show();
	}

	private void create() {
		if (startButton != null) {
			startDate = null;
		}

		if (endButton != null) {
			endDate = null;
		}

		trip = null;

		Toast.makeText(getApplicationContext(), "JournWe wird erstellt",
				Toast.LENGTH_SHORT).show();

		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_journ_we, menu);
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
				return getString(R.string.create_section1).toUpperCase(l);
			case 1:
				return getString(R.string.create_section2).toUpperCase(l);
			case 2:
				return getString(R.string.create_section3).toUpperCase(l);
			}
			return null;
		}

		View.OnClickListener myhandler1 = new View.OnClickListener() {
			public void onClick(View v) {
				// it was the 1st button
			}
		};
		View.OnClickListener myhandler2 = new View.OnClickListener() {
			public void onClick(View v) {
				// it was the 2nd button
			}
		};
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

			View rootView = null;
			TextView textView;

			if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
				rootView = inflater.inflate(
						R.layout.fragment_create_journ_we_place, container,
						false);
				textView = (TextView) rootView.findViewById(R.id.section_label);

				// ListView lv = (ListView)
				// rootView.findViewById(R.id.placelist);
				// lv.setAdapter(placeAdapter);

				// map = (((MapFragment) getFragmentManager().findFragmentById(
				// R.id.mapview)).getMap());
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
				rootView = inflater.inflate(
						R.layout.fragment_create_journ_we_adventurer,
						container, false);
				textView = (TextView) rootView.findViewById(R.id.section_label);

				ListView lv = (ListView) rootView
						.findViewById(R.id.adventurerlist);
				lv.setAdapter(adventurerAdapter);

				if (adventurerText == null) {
					adventurerText = (EditText) rootView
							.findViewById(R.id.adventurertext);
				}
			}

			else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
				rootView = inflater.inflate(
						R.layout.fragment_create_journ_we_date, container,
						false);
				textView = (TextView) rootView.findViewById(R.id.section_label);

				startButton = (Button) rootView.findViewById(R.id.startButton);

				if (startDate != null) {
					startButton.setText(format.format(startDate));
				}

				endButton = (Button) rootView.findViewById(R.id.endButton);

				if (endDate != null) {
					endButton.setText(format.format(endDate));
				}

				ListView lv = (ListView) rootView.findViewById(R.id.datelist);
				lv.setAdapter(dateAdapter);
			}

			return rootView;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
