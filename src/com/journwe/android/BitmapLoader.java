package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.R.drawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;
import android.widget.ImageView;

public class BitmapLoader extends AsyncTask<String, Integer, Bitmap> {

	private Context context;
	private final String URL_BASE = "http://www.journwe.com";

	public BitmapLoader(Context context) {
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(String... sUrl) {
		Bitmap re = null;

		Log.v("url", sUrl[0]);
		URL imageURL = null;
		
		if (sUrl[0] == null) {
			
		}
		
		else {
			try {
				imageURL = new URL(sUrl[0]);

				HttpURLConnection connection = (HttpURLConnection) imageURL
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream inputStream = connection.getInputStream();

				re = BitmapFactory.decodeStream(inputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		
		
		if (context instanceof Trip) {
			((Trip) context).setImage(re);
		}
		

		return re;
	}
}
