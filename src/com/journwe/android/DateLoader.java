package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

public class DateLoader extends AsyncTask<JournWeDetail, Void, ArrayList<Date>> {

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
	protected ArrayList<Date> doInBackground(JournWeDetail... params) {
		String url = "http://www.journwe.com/api/json/adventure/" + id + "/times.json";
		Log.i("load date", url);
		ArrayList<Date> re = loadDate(url);
		return re;
	}

	private ArrayList<Date> loadDate(String url) {
		ArrayList<Date> re = new ArrayList();

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
			
			for (int i = 0; i < json.length(); i ++) {
				JSONObject j = json.getJSONObject(i);
				
				Date d = new Date(j.get("startDate").toString(), j.get("endDate").toString(), j.get("voteGroup").toString());
				
				re.add(d);
				Log.i("set date", d.getStart()+"");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	@Override
	protected void onPostExecute(ArrayList<Date> result) {
		content.setDate(result);
	}
}