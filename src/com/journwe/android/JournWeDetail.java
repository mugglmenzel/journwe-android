package com.journwe.android;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class JournWeDetail extends Activity {

	private static ListView lv;
	private static Trip trip;
	private static DetailAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_trip);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Intent i = getIntent();
		trip = (Trip) i.getSerializableExtra(JournWeListActivity.SEND_TRIP);
		
		getActionBar().setTitle(trip.getName());
		
		ArrayList<Trip> a = new ArrayList();
		a.add(trip);
		a.add(null);
		a.add(null);
		a.add(null);
		adapter = new DetailAdapter(this, R.layout.fragment_show_trip, a);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_journ_we, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_show_trip,
					container, false);
			
			lv = (ListView) rootView.findViewById(R.id.listView1);
			
			lv.setAdapter(adapter);
			
			return rootView;
		}
	}

}
