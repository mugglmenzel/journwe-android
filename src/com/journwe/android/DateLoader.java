package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DateLoader extends
		AsyncTask<JournWeDetail, Void, ArrayList<JournWeDate>> {

	// reference to our imageview
	private String id;
	private JournWeDetail content;
	private int mDuration;

	public DateLoader(JournWeDetail content, String id, int duration) {
		this.content = content;
		mDuration = duration;
		this.id = id;
	}

	@Override
	protected ArrayList<JournWeDate> doInBackground(JournWeDetail... params) {
		String url = "http://www.journwe.com/api/json/adventure/" + id
				+ "/times.json";
		Log.i("load date", url);
		ArrayList<JournWeDate> re = loadDate(url);
		return re;
	}

	private ArrayList<JournWeDate> loadDate(String url) {
		ArrayList<JournWeDate> re = new ArrayList();

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.connect();

			// download the file
			InputStream input = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String read = "";
			String in = "";

			while ((read = br.readLine()) != null) {
				in += read;
			}

			JSONArray json = new JSONArray(in);

			for (int i = 0; i < json.length(); i++) {
				JSONObject j = json.getJSONObject(i);
				JournWeDate d = new JournWeDate(null, null, 0, "");
				try {
					long start = Long.parseLong(j.getString("startDate"));
					long end = Long.parseLong(j.getString("endDate"));

					d = new JournWeDate(new Date(start), new Date(end), 5*Double.parseDouble(j.getString(
							"voteGroup")), j.getString("favorite"));

				} catch (NumberFormatException e) {
					Log.i("date exception", e.getStackTrace().toString());
				}

				re.add(d);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	@Override
	protected void onPostExecute(ArrayList<JournWeDate> result) {
		content.setDate(result);
	}
}