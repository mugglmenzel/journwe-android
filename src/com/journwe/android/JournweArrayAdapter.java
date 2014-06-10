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
import android.widget.TextView;

public class JournweArrayAdapter extends ArrayAdapter<Trip> {

	private HashMap<Trip, Integer> mIdMap = new HashMap<Trip, Integer>();
	private Context context;
	private ArrayList<Trip> values;

	public JournweArrayAdapter(Context context, int ViewResourceId,
			List<Trip> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		this.values = (ArrayList) objects;
		for (int i = 0; i < objects.size(); i++) {
			mIdMap.put(objects.get(i), i);
		}
	}
	
	public int getCount() {
		return values.size() + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView;
		
		if (position == 0) {
			rowView = inflater.inflate(R.layout.journwe_list_create, parent, false);
		}
		
		else {
			rowView = inflater.inflate(R.layout.journwe_list_element, parent, false);
			
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
			
			Trip t = values.get(position-1);
			
			TextView textView1 = (TextView) rowView.findViewById(R.id.t1);
			TextView textView2 = (TextView) rowView.findViewById(R.id.t2);
			TextView textView3 = (TextView) rowView.findViewById(R.id.t3);
			TextView textView4 = (TextView) rowView.findViewById(R.id.t4);
			TextView textView5 = (TextView) rowView.findViewById(R.id.t5);
			
			textView1.setText(t.getName());
			textView2.setText("Status: " + t.getStatus().toString());
			textView3.setText("Adventurers: " + t.getPeople());
			textView4.setText("Place: " + t.getFavPlace());
			textView5.setText("Time: " + t.getFavTime());

			if (t.getImage() == null) {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
			
			else {
				imageView.setImageBitmap(t.getImage());
			}
		}
		
		
		return rowView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
