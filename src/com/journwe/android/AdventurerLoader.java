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

public class AdventurerLoader extends
		AsyncTask<AdventurerAdapter, Void, ArrayList<JournWeAdventurer>> {

	// reference to our imageview
	private String id;
	private AdventurerAdapter content;
	private int mDuration;

	public AdventurerLoader(AdventurerAdapter content, String id, int duration) {
		this.content = content;
		mDuration = duration;
		this.id = id;
	}

	@Override
	protected ArrayList<JournWeAdventurer> doInBackground(AdventurerAdapter... params) {
		String url = "http://www.journwe.com/api/json/adventure/" + id
				+ "/adventurers.json";
		Log.i("load date", url);
		ArrayList<JournWeAdventurer> re = loadPlace(url);
		return re;
	}

	private ArrayList<JournWeAdventurer> loadPlace(String url) {
		ArrayList<JournWeAdventurer> re = new ArrayList();

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
				JournWeAdventurer a = new JournWeAdventurer("", "", "", "", "");
				try {
					a = new JournWeAdventurer(j.getString("id"), j.getString("status"), j.getString("name"), j.getString("link"), j.getString("image"));

				} catch (NumberFormatException e) {
					Log.i("date exception", e.getStackTrace().toString());
				}

				re.add(a);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	@Override
	protected void onPostExecute(ArrayList<JournWeAdventurer> result) {
		content.setAdventurer(result);
	}
}