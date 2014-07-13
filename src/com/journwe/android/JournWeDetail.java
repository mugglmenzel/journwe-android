package com.journwe.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class JournWeDetail extends Activity {

	private static ListView lv;
	private static DetailedTrip trip;
	private static DetailAdapter detailAdapter;
	private static LinearLayout ll1;
	private static LinearLayout ll2;
	private static LinearLayout ll3;
	private static LinearLayout ll4;
	private static LinearLayout ll5;
	private static int selected;
	private static ListView dateList;
	private static ListView placeList;
	private static ListView adventurerList;
	private static DateAdapter dateAdapter;
	private static PlaceAdapter placeAdapter;
	private static AdventurerAdapter adventurerAdapter;
	private static TextView favDate;
	private static TextView favPlace;
	private static SimpleDateFormat dateFormat;
	private static ImageView map;
	private String bitmapURL;
	private static LayoutParams ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_trip);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		Intent i = getIntent();
		trip = new DetailedTrip(
				(Trip) i.getSerializableExtra(JournWeListActivity.SEND_TRIP));

		getActionBar().setTitle(trip.getName());

		ArrayList<Trip> a = new ArrayList<Trip>();

		a.add(trip);

		new DateLoader(this, trip.getId(), 0).execute(this);

		dateAdapter = new DateAdapter(this, R.id.dateList, trip.getDates());
		placeAdapter = new PlaceAdapter(this, R.id.placeList, trip.getPlaces());
		adventurerAdapter = new AdventurerAdapter(this, R.id.adventurerList,
				trip.getAdventurers());

		detailAdapter = new DetailAdapter(this, R.layout.fragment_show_trip, a);

		if (ll1 != null) {
			setColor(1);
		}

		dateFormat = new SimpleDateFormat("dd/MM");

		ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	}
	
	public String getURL() {
		return bitmapURL;
	}
	
	public void setURL(String url) {
		bitmapURL = url;
	}

	public void setMap(ImageView map) {
		this.map = map;

		loadMap();
	}

	public void setGoogleMap(Bitmap result) {
		trip.setGoogleMap(result);
		
		if (result == null) {
			map.getLayoutParams().height = 1;
		}
		
		else {
			map.getLayoutParams().height = adaptSize(350);
			map.setImageBitmap(result);
		}
	}
	
	private void loadMap() {
		Log.i("load map", "map");
		
		if (map != null) {
			if (trip.getPlaces() != null && trip.getPlaces().size() > 0) {
				if (trip.getGoogleMap() == null) {
					String url = "https://maps.google.com/maps/api/staticmap?size=350x350";
					
					for (JournWePlace p : trip.getPlaces()) {
						url += "&markers=color:red|" + p.getLat() + "," + p.getLng();
					}

					url += "&sensor=false&key=AIzaSyCuMcV-IagEP8k3o8K3q7LcgWjT-AKofqE";

					Log.i("google maps url", url);

					this.bitmapURL = url;
					
					new GoogleImageLoader().execute(this);
				}
				
				else {
					if (trip.getGoogleMap() == null) {
						map.getLayoutParams().height = 1;
					}
					
					else {
						map.getLayoutParams().height = adaptSize(350);
						map.setImageBitmap(trip.getGoogleMap());
					}
				}
			}
		}
	}

	public DetailedTrip getTrip() {
		return trip;
	}

	public void setFavDateView(TextView date) {
		favDate = date;

		Log.i("fav date view", "set");

		if (trip.getFavoriteDate() != null) {
			favDate.setText(dateFormat
					.format(trip.getFavoriteDate().getStart())
					+ " - "
					+ dateFormat.format(trip.getFavoriteDate().getEnd()));
			Log.i("set", "fav date");
		}
	}

	public void setFavPlaceView(TextView place) {
		favPlace = place;

		Log.i("fav place view", "set");

		if (trip.getFavoritePlace() != null) {
			favPlace.setText(trip.getFavoritePlace().getPlace());
			Log.i("set", "fav place");
		}
	}
	
	private int adaptSize(int size) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());
	}

	public void setDateView(ListView date) {
		dateList = date;
		
		setDateSize();
	}
	
	private void setDateSize() {
		if (dateList != null) {
			LayoutParams lp = (LayoutParams) dateList.getLayoutParams();
			int height = trip.getDates().size() * 50;

			if (trip.getDates().size() == 0) {
				height = 50;
			}

			Log.i("set date list size", height + "");

			lp.height = adaptSize(height);
			dateList.setLayoutParams(lp);

			dateList.setAdapter(dateAdapter);

			Log.i("set date list size", lp.height + "");
		}
	}

	public void setPlaceView(ListView place) {
		placeList = place;

		setPlaceSize();
	}
	
	private void setPlaceSize() {
		if (placeList != null) {
			LayoutParams lp = (LayoutParams) placeList.getLayoutParams();
			int height = trip.getPlaces().size() * 50;
			
			Log.i("place list length", trip.getPlaces().size() + "");

			if (trip.getPlaces().size() == 0) {
				height = 50;
			}

			Log.i("set place list size", height + "");

			lp.height = adaptSize(height);
			placeList.setLayoutParams(lp);

			placeList.setAdapter(placeAdapter);

			Log.i("set place list size", lp.height + "");
		}
	}

	public void setAdventurerView(ListView adventurer) {
		adventurerList = adventurer;

		setAdventurerSize();
	}
	
	private void setAdventurerSize() {
		LayoutParams lp = (LayoutParams) adventurerList.getLayoutParams();
		int height = trip.getAdventurers().size() * 70;

		if (height == 0) {
			height = 1;
		}

		Log.i("set adventurer list size", height + "");

		lp.height = adaptSize(height);
		adventurerList.setLayoutParams(lp);

		adventurerList.setAdapter(adventurerAdapter);

		Log.i("set adventurer list size", lp.height + "");
	}

	public void setDate(ArrayList<JournWeDate> result) {
		for (JournWeDate d : result) {
			trip.addDate(d);

			Log.i("date values", "s:" + d.getStart() + " e:" + d.getEnd()
					+ " v:" + d.getVote() + " f:" + d.getFavorite());

			if (d.getFavorite().equals("true")) {
				trip.setFavoriteDate(d);

				if (favDate != null) {
					favDate.setText(dateFormat.format(d.getStart()) + " - "
							+ dateFormat.format(d.getEnd()));
				}
			}
		}

		if (dateAdapter != null) {
			dateAdapter.notifyDataSetChanged();
		}
		
		setDateSize();
	}

	public void setPlace(ArrayList<JournWePlace> places, Date deadline) {

		for (JournWePlace p : places) {
			trip.addPlace(p);
			
			Log.i("place values", "p:" + p.getPlace() + " v:" + p.getVote()
					+ " f:" + p.getFavorite());

			if (p.getFavorite().equals("true")) {
				trip.setFavoritePlace(p);

				if (favPlace != null) {
					favPlace.setText(p.getPlace());
				}
			}
		}
		
		trip.setPlaceDeadline(deadline);
		
		if (placeAdapter != null) {
			placeAdapter.notifyDataSetChanged();
		}

		if (placeList != null) {
			LayoutParams lp = (LayoutParams) placeList.getLayoutParams();
			lp.height = trip.getPlaces().size() * 150;
			placeList.setLayoutParams(lp);
		}
		
		loadMap();
		
		setPlaceSize();
	}

	public void setAdventurer(ArrayList<JournWeAdventurer> adventurers) {
		for (JournWeAdventurer a : adventurers) {
			trip.addAdventurer(a);
		}
		
		if (adventurerAdapter != null) {
			adventurerAdapter.notifyDataSetChanged();
		}

		if (adventurerList != null) {
			LayoutParams lp = (LayoutParams) adventurerList.getLayoutParams();
			int height = 212 * trip.getAdventurers().size();

			if (height < 212) {
				height = 212;
			}

			lp.height = height;
			adventurerList.setLayoutParams(lp);
		}
		
		setAdventurerSize();
	}

	public String getId() {
		return trip.getId();
	}

	private static void setColor(int sel) {
		if (selected == 1) {
			ll1.setBackgroundColor(Color.parseColor("#cccfd5d3"));
		}

		else if (selected == 2) {
			ll2.setBackgroundColor(Color.parseColor("#cccfd5d3"));
		}

		else if (selected == 3) {
			ll3.setBackgroundColor(Color.parseColor("#cccfd5d3"));
		}

		else if (selected == 4) {
			ll4.setBackgroundColor(Color.parseColor("#cccfd5d3"));
		}

		else if (selected == 5) {
			ll5.setBackgroundColor(Color.parseColor("#cccfd5d3"));
		}

		if (sel == 1) {
			ll1.setBackgroundColor(Color.parseColor("#bad48a"));
		}

		else if (sel == 2) {
			ll2.setBackgroundColor(Color.parseColor("#bad48a"));
		}

		else if (sel == 3) {
			ll3.setBackgroundColor(Color.parseColor("#bad48a"));
		}

		else if (sel == 4) {
			ll4.setBackgroundColor(Color.parseColor("#bad48a"));
		}

		else if (sel == 5) {
			ll5.setBackgroundColor(Color.parseColor("#bad48a"));
		}

		selected = sel;
	}

	public void onPlace(View view) {
		Log.i("scroll", "place");
		lv.setSelection(1);

		setColor(1);
	}

	public void onAdventurer(View view) {
		Log.i("scroll", "place");
		lv.setSelection(2);

		setColor(2);
	}

	public void onDate(View view) {
		Log.i("scroll", "place");
		lv.setSelection(3);

		setColor(3);
	}

	public void onChat(View view) {
		Log.i("scroll", "chat");

		setColor(4);
	}

	public void onCheck(View view) {
		Log.i("scroll", "check");

		setColor(5);
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
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_refresh:
			
			Log.i("refresh", "clear");
			detailAdapter.reload();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

			ll1 = (LinearLayout) rootView.findViewById(R.id.ll1);
			ll2 = (LinearLayout) rootView.findViewById(R.id.ll2);
			ll3 = (LinearLayout) rootView.findViewById(R.id.ll3);
			ll4 = (LinearLayout) rootView.findViewById(R.id.ll4);
			ll5 = (LinearLayout) rootView.findViewById(R.id.ll5);
			
			setColor(1);

			lv = (ListView) rootView.findViewById(R.id.listView1);

			lv.setAdapter(detailAdapter);

			lv.setOnScrollListener(new OnScrollListener() {
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					if (firstVisibleItem != 0 && firstVisibleItem != selected) {
						setColor(firstVisibleItem);
					}

					if (lv.getLastVisiblePosition() == lv.getAdapter()
							.getCount() - 1
							&& lv.getChildAt(lv.getChildCount() - 1)
									.getBottom() <= lv.getHeight()) {
						setColor(3);
						Log.i("list", "ende!");
					}
				}

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {

				}
			});

			return rootView;
		}
	}
}
