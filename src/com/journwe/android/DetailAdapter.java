package com.journwe.android;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailAdapter extends ArrayAdapter<Trip> {

	private Context context;
	private DetailedTrip trip;
	private JournWeDetail detail;
	private View view;
	private boolean loaded;
//	private View spinner;

	public DetailAdapter(Context context, int ViewResourceId, List<Trip> objects) {
		super(context, ViewResourceId, objects);
		this.context = context;
		this.trip = new DetailedTrip(objects.get(0));
		detail = (JournWeDetail) context;
		loaded = false;
	}
	
	public void reload() {
//		trip.reset();
		
		loaded = true;
		
		clear();
		notifyDataSetChanged();
		
		new DetailLoader(this, context).execute(this);
	}
	
	public Context getContext() {
		return (Context) detail;
	}

	public void setDetail(Detail d) {
		// Set the image view before the content is shown.
		
		trip.setImage(d.getBitmap());
		ImageView i = (ImageView) view.findViewById(R.id.image);
		

		if (d.getBitmap() == null) {
			i.getLayoutParams().height = 0;
		}
		
		else {
			i.setImageBitmap(d.getBitmap());
		}

		 trip.setDescription(d.getString());
		TextView t = (TextView) view.findViewById(R.id.text);
		
		if (d.getString().equals("null") || d.getString().equals(null)) {
			t.setText(R.string.no_description);
		}
		
		else {
			t.setText(d.getString());
		}
		
		// Set the "show" view to 0% opacity but visible, so that it is visible
		view.setAlpha(0f);
		view.setVisibility(View.VISIBLE);

		// Animate the "show" view to 100% opacity, and clear any animation
		// listener set on the view.
		view.animate().alpha(1f).setDuration(400).setListener(null);
	}

	public View getView() {
		return view;
	}

	public int getCount() {
		return 4;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View content = null;

		if (position == 0) {
			content = inflater.inflate(R.layout.journwe_detail, parent, false);

			view = content.findViewById(R.id.detail);

			ImageView i = (ImageView) content.findViewById(R.id.image);
			i.setTag(trip.getImageURL());

			final TextView t = (TextView) content.findViewById(R.id.text);
			t.setTag("http://www.journwe.com/api/json/adventure/"
					+ trip.getId() + "/info.json");
			
			t.setOnClickListener(new OnClickListener() {
				boolean expanded = false;
				
				@Override
				public void onClick(View v) {
					if (expanded) {
						t.setMaxLines(10);
						t.setEllipsize(TruncateAt.END);
						expanded = false;
					}
					
					else {
						t.setMaxLines(Integer.MAX_VALUE);
						t.setEllipsize(null);
						expanded = true;
					}
					
				}
			});

			if (loaded == false) {
				reload();
			}

			else {
				t.setText(trip.getDescription());
				i.setImageBitmap(trip.getImage());
			}

			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			layoutParams.setMargins(10, 10, 10, 0);

			view.setLayoutParams(layoutParams);
		}

		else if (position == 1) {
			content = inflater.inflate(R.layout.place_view, parent, false);
			detail.setPlaceView((ListView)content.findViewById(R.id.placeList));

			detail.setFavPlaceView((TextView) content.findViewById(R.id.location));
			detail.setMap((ImageView) content.findViewById(R.id.map));
			
			
		}

		else if (position == 2) {
			content = inflater.inflate(R.layout.adventurer_view, parent, false);
			detail.setAdventurerView((ListView)content.findViewById(R.id.adventurerList));
			TextView t = (TextView) content.findViewById(R.id.status);
			t.setText("You are: " + trip.getStatus().toString());
		}

		else if (position == 3) {
			content = inflater.inflate(R.layout.date_view, parent, false);
			detail.setDateView((ListView)content.findViewById(R.id.dateList));
			detail.setFavDateView((TextView) content.findViewById(R.id.favtime));
			
			detail.setButton((Button) content.findViewById(R.id.startButton), (Button) content.findViewById(R.id.endButton));
		}

		return content;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
