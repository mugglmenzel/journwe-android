package com.journwe.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailAdapter extends ArrayAdapter<Trip> {

	private Context context;
	private ArrayList<Trip> values;
	private Trip trip;

	public DetailAdapter(Context context, int ViewResourceId,
			List<Trip> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		this.values = (ArrayList) objects;
		this.trip = values.get(0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View content = null;
		
		if (position == 0) {
			View rootView = inflater.inflate(R.layout.load_detail, parent, false);
			
			View loadView = rootView.findViewById(R.id.loading_spinner);
			content = rootView.findViewById(R.id.detail);
			
			content.setVisibility(View.GONE);
			
			int duration = 400;
			
			ImageView i = (ImageView) rootView.findViewById(R.id.image);
			i.setTag(values.get(0).getImageURL());
			
			TextView t = (TextView) rootView.findViewById(R.id.text);
			t.setTag("http://www.journwe.com/api/json/adventure/" + values.get(0).getId() + "/info.json");
			
			new DetailLoader(content, loadView, duration).execute(content);
			
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				     LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

				layoutParams.setMargins(10, 10, 10, 0);
			
			content.setLayoutParams(layoutParams);
		}
		
		else if (position == 1) {
			content = inflater.inflate(R.layout.place_view, parent, false);
		}
		
		else if (position == 2) {
			content = inflater.inflate(R.layout.adventurer_view, parent, false);
		}
		
		else if (position == 3) {
			content = inflater.inflate(R.layout.date_view, parent, false);
		}
		
		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
