package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class TripLoader extends
		AsyncTask<JournWeListActivity, Void, ArrayList<Trip>> {

	private String urlstring;
	private final String URL_BASE = "http://www.journwe.com";
	private JournWeListActivity a;
	private ProgressDialog progress;
	private int width;

	@Override
	protected ArrayList<Trip> doInBackground(JournWeListActivity... activity) {
		this.a = activity[0];
		
		width = JournWeListActivity.width/10;
		
		HttpURLConnection connection;
		InputStream input;

		JSONArray jsonArray;

		this.urlstring = URL_BASE + a.getUrl();
		ArrayList<Trip> myTrips = new ArrayList<Trip>();

		String re = "";

		URL url;
		try {
			url = new URL(urlstring);

			Log.i("url", url.toString());

			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// download the file
			input = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String read = "";
			String in = "";

			while ((read = br.readLine()) != null) {
				in += read;
			}
			
			re = in;

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			jsonArray = new JSONArray(re);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				com.journwe.android.Status s = null;
				String stat = jsonObject.getString("status");

				if (stat.equals("GOING")) {
					s = com.journwe.android.Status.GOING;
				}

				else if (stat.equals("BOOKED")) {
					s = com.journwe.android.Status.BOOKED;
				}

				else if (stat.equals("NOTGOING")) {
					s = com.journwe.android.Status.NOTGOING;
				}

				else {
					s = com.journwe.android.Status.UNDECIDED;
				}

				Bitmap b = null;

				if (jsonObject.getString("image").toString() != null && jsonObject.getString("image") != "null") {
					b = download_Image(jsonObject.getString("image"));
				}

				Log.i("image", jsonObject.get("image").toString());
				Log.i("place", jsonObject.get("favoritePlace").toString());

				String place = "";

				if (jsonObject.getString("favoritePlace") == null
						|| jsonObject.getString("favoritePlace") == "null") {
					place = "No place selected";
				}

				else {
					JSONObject json = new JSONObject(
							jsonObject.getString("favoritePlace"));
					place = json.getString("address");
				}

				String time = "";

				if (jsonObject.getString("favoriteTime") == null
						|| jsonObject.getString("favoriteTime") == "null") {
					time = "No time selected";
				}

				else {
					JSONObject json = new JSONObject(
							jsonObject.getString("favoriteTime"));
					SimpleDateFormat d = new SimpleDateFormat("dd/MM");
					time = (d.format(json.getInt("startDate")) + " - " + d
							.format(json.getInt("endDate")));
				}

				Trip t = new Trip(jsonObject.getString("id"),
						jsonObject.getString("name"),
						jsonObject.getString("link"),
						Integer.parseInt(jsonObject.getString("peopleCount")),
						s, b, jsonObject.getString("imageTimestamp"), place,
						time, jsonObject.get("image").toString());

				myTrips.add(t);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return myTrips;
	}

	@Override
	protected void onPostExecute(ArrayList<Trip> result) {
		a.setTrips(result);
	}

	private Bitmap download_Image(String url) {
		Bitmap re = null;
		
		String thumbnail = URL_BASE + "/thumbnail?w=" + width + "&u=" + url;
		
		Log.i("thumbnail", thumbnail);
		
		try {
			URL urln = new URL(thumbnail);
			re = BitmapFactory.decodeStream(urln.openConnection()
					.getInputStream());
		} catch (IOException e) {
			Log.e("HUE", "Error downloading the image from server : "
					+ e.getMessage().toString());
		}
		return re;
	}
}