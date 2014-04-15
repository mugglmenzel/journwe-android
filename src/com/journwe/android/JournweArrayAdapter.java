package com.journwe.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
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

	public JournweArrayAdapter(Context context, int textViewResourceId,
			List<Trip> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.values = (ArrayList) objects;
		for (int i = 0; i < objects.size(); i++) {
			mIdMap.put(objects.get(i), i);
		}
	}

	@Override
	public long getItemId(int position) {
		Trip item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.test, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView textView1 = (TextView) rowView.findViewById(R.id.t1);
		TextView textView2 = (TextView) rowView.findViewById(R.id.t2);
		textView1.setText(values.get(position).getName());
		textView2.setText(values.get(position).getStatus().toString());
//		String s = values[position];
//		if (s.startsWith("iPhone")) {
//			imageView.setImageResource(R.drawable.no);
//		} else {
//			imageView.setImageResource(R.drawable.ok);
//		}

		if (values.get(position).getImage() == null) {
			imageView.setImageResource(R.drawable.ic_launcher);
		}
		
		else {
			imageView.setImageBitmap(values.get(position).getImage());
		}
		
		return rowView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
