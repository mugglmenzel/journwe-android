package com.journwe.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateAdapter extends ArrayAdapter<Date> {

	private Context context;
	private ArrayList<Date> dates;

	public DateAdapter(Context context, int ViewResourceId, List<Date> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		if (objects.size() > 0) {
			Log.i("size", "> 0");
		}
		Log.i("date", "vor");
		this.dates = (ArrayList<Date>) objects;
		Log.i("date", "nach");
	}

	public int getCount() {
		Log.i("log size", dates.size()+"");
		return dates.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View content = inflater.inflate(R.layout.date_list, parent, false);
		
		if (dates.size() >= position) {
			Log.i("date", ""+dates.get(position).getStart());

			TextView date = (TextView) content.findViewById(R.id.datetext);
			TextView vote = (TextView) content.findViewById(R.id.datevote);

			date.setText(dates.get(position).getStart() + " - "
					+ dates.get(position).getEnd());
			vote.setText(dates.get(position).getVote());
		}

		

		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
