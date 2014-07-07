package com.journwe.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class PlaceAdapter extends ArrayAdapter<JournWePlace> {

	private JournWeDetail detail;
	private ArrayList<JournWePlace> places;
	private Context context;
	

	public PlaceAdapter(Context context, int ViewResourceId, List<JournWePlace> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		
		if (context instanceof JournWeDetail) {
			this.detail = (JournWeDetail) context;
		}
		
		this.places = (ArrayList<JournWePlace>) objects;
	}
	
	public void setPlace(ArrayList<JournWePlace> p, Date deadline) {
		detail.setPlace(p, deadline);
	}

	public int getCount() {
		Log.i("log size", places.size()+"");
		
		if (places.size() == 0) {
			return 1;
		}
		
		return places.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View content = inflater.inflate(R.layout.place_list, parent, false);
		
		if (places.size() == 0) {
			TextView place = (TextView) content.findViewById(R.id.placetext);
			
			place.setText("No places");
			
			if (detail != null) {
				new PlaceLoader(this, detail.getTrip().getId(), 0).execute(this);
			}
		}
		
		else if (places.size() >= position) {
			Log.i("place", ""+places.get(position).getPlace());

			TextView place = (TextView) content.findViewById(R.id.placetext);
			TextView vote = (TextView) content.findViewById(R.id.placevote);

			place.setText(places.get(position).getPlace());
			
			vote.setText(String.valueOf(places.get(position).getVote()));

			RatingBar r = (RatingBar) content.findViewById(R.id.placerating);
			
			r.setRating((float) places.get(position).getVote());
			
			Log.i("rating", places.get(position).getVote()+"");
		}

		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
