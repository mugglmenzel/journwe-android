package com.journwe.android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import android.widget.RatingBar;
import android.widget.TextView;

public class DateAdapter extends ArrayAdapter<JournWeDate> {

	private Context context;
	private ArrayList<JournWeDate> dates;
	SimpleDateFormat dateFormat;

	public DateAdapter(Context context, int ViewResourceId, List<JournWeDate> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		this.dates = (ArrayList<JournWeDate>) objects;
		dateFormat = new SimpleDateFormat("dd/MM");
	}

	public int getCount() {
		if (dates.size() == 0) {
			return 1;
		}
		
		return dates.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View content = inflater.inflate(R.layout.date_list, parent, false);
		
		if (dates.size() == 0) {
			TextView date = (TextView) content.findViewById(R.id.datetext);
			
			date.setText("No dates");
		}
		
		else if (dates.size() >= position) {
			TextView date = (TextView) content.findViewById(R.id.datetext);
			TextView vote = (TextView) content.findViewById(R.id.datevote);

			date.setText(dateFormat.format(dates.get(position).getStart()) + " - "
					+ dateFormat.format(dates.get(position).getEnd()));
			
			double v = (double) dates.get(position).getVote();
			
			vote.setText(String.valueOf(v));

			RatingBar r = (RatingBar) content.findViewById(R.id.daterating);
			
			r.setRating((float) dates.get(position).getVote());
		}

		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
