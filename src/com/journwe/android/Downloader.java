package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

public class Downloader extends AsyncTask<String, Integer, String> {

	private Context context;
	private PowerManager.WakeLock mWakeLock;
	private final String URL_BASE = "http://www.journwe.com";

	public Downloader(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... sUrl) {
		InputStream input = null;
		OutputStream output = null;
		HttpURLConnection connection = null;
		String in;
		try {
			URL url = new URL(URL_BASE + sUrl[0]);
			
			Log.i("url", url.toString());
			
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();

			// expect HTTP 200 OK, so we don't mistakenly save error report
			// instead of the file
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return "Server returned HTTP "
						+ connection.getResponseCode() + " "
						+ connection.getResponseMessage();
			}

			// download the file
			input = connection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					input));
			String read = "";
			in = "";

			while ((read = br.readLine()) != null) {
				in += read;
			}

			if (isCancelled()) {
				input.close();
				return null;
			}
		} catch (Exception e) {
			return e.toString();
		} finally {
			try {
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			} catch (IOException ignored) {
			}

			if (connection != null)
				connection.disconnect();
		}
		return in;
	}
}
