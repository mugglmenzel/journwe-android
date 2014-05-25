package com.journwe.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdventurerAdapter extends ArrayAdapter<JournWeAdventurer> {

	private JournWeDetail context;
	private ArrayList<JournWeAdventurer> adventurers;

	public AdventurerAdapter(Context context, int ViewResourceId, List<JournWeAdventurer> objects) {
		super(context, ViewResourceId, objects);
		this.context = (JournWeDetail) context;
		this.adventurers = (ArrayList<JournWeAdventurer>) objects;
	}
	
	public void setAdventurer(ArrayList<JournWeAdventurer> a) {
		context.setAdventurer(a);
	}

	public int getCount() {
		Log.i("log size", adventurers.size()+"");
		
		if (adventurers.size() == 0) {
			return 1;
		}
		
		return adventurers.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View content = inflater.inflate(R.layout.adventurer_list, parent, false);
		
		if (adventurers.size() == 0) {
			TextView name = (TextView) content.findViewById(R.id.adventurername);
			TextView status = (TextView) content.findViewById(R.id.adventurerstatus);
			
			name.setText("no people invited jet");
			status.setText("");
			
			Log.i("adventurer", "!!!!!!!!!!!0!!!!!!!!!");
			
			new AdventurerLoader(this, context.getTrip().getId(), 0).execute(this);
		}
		
		else if (adventurers.size() >= position) {
			Log.i("place", ""+adventurers.get(position).getName());

			TextView name = (TextView) content.findViewById(R.id.adventurername);
			TextView status = (TextView) content.findViewById(R.id.adventurerstatus);

			name.setText(adventurers.get(position).getName());
			
			status.setText(String.valueOf(adventurers.get(position).getStatus()));
		}

		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
