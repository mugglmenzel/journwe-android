package com.journwe.android;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class JournWeDetail extends Activity {

	private static TextView tv;
	private static Trip trip;
	
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
			
			tv = (TextView) rootView.findViewById(R.id.textview);

			String text = "";
			text += "Name: " + trip.getName() + "\n";
			text += "Status: " + trip.getStatus() + "\n";
			text += "People: " + trip.getPeople() + "\n";
			text += "Place: " + trip.getFavPlace() + "\n";
			text += "Time: " + trip.getFavTime() + "\n";
			text += "Id: " + trip.getId() + "\n";
			text += "Link: " + trip.getLink() + "\n";
			
			if (tv != null) {
				tv.setText(text);
			}
			Log.i("show", tv.toString());
			return rootView;
		}
	}

}
