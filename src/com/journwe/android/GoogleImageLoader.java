package com.journwe.android;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class GoogleImageLoader extends AsyncTask<JournWeDetail, Void, Bitmap> {

	JournWeDetail detail = null;

	@Override
	protected Bitmap doInBackground(JournWeDetail... JournWeDetails) {
		this.detail = JournWeDetails[0];
		return download_Image((String) detail.getURL());
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		detail.setGoogleMap(result);
	}

	private Bitmap download_Image(String url) {
		Bitmap re = null;
		try {
			URL urln = new URL(url);
			re = BitmapFactory.decodeStream(urln.openConnection()
					.getInputStream());
		} catch (IOException e) {
			Log.e("HUE", "Error downloading the image from server : "
					+ e.getMessage().toString());
		}
		return re;
	}
}