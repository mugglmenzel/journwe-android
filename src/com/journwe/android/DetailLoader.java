package com.journwe.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailLoader extends AsyncTask<DetailAdapter, Void, Detail> {

	private View mView;
	private DetailAdapter detail;
	private ProgressDialog progress;

	public DetailLoader(DetailAdapter content) {
		detail = content;
		progress = new ProgressDialog(content.getContext());
		progress.setTitle("Loading");
		progress.setMessage("Wait while loading...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
	}

	@Override
	protected Detail doInBackground(DetailAdapter... params) {
		detail = params[0];
		mView = detail.getView();
		
//		progress = new ProgressDialog(detail.getContext());
//		progress.setTitle("Loading");
//		progress.setMessage("Wait while loading...");
//		progress.setCanceledOnTouchOutside(false);
//		progress.show();
		
		ImageView i = (ImageView) mView.findViewById(R.id.image);
		String url = (String) i.getTag();
		Log.i("load", "image " + url);
		Bitmap b = loadImageFromNetwork(url);
		Log.i("load", "image");
		TextView t = (TextView) mView.findViewById(R.id.text);
		String s = loadText(t.getTag().toString());
		Log.i("load", "text");
		Detail re = new Detail(b, s);
		return re;
	}

	private String loadText(String url) {
		String re = "";

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

			JSONObject jsonObject = new JSONObject(in);
			re = jsonObject.getString("description");

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

	@Override
	protected void onPostExecute(Detail result) {
		Log.i("load", "set image");
		
		detail.setDetail(result);

		// To dismiss the dialog
		progress.dismiss();
		
		// Set the image view before the content is shown.
//		ImageView i = (ImageView) mView.findViewById(R.id.image);
//		i.setImageBitmap(result.getBitmap());
//
//		TextView t = (TextView) mView.findViewById(R.id.text);
//		t.setText(result.getString());
//
//		// Set the "show" view to 0% opacity but visible, so that it is visible
//		detail.getView().setAlpha(0f);
//		detail.getView().setVisibility(View.VISIBLE);
//
//		// Animate the "show" view to 100% opacity, and clear any animation
//		// listener set on the view.
//		detail.getView().animate().alpha(1f).setDuration(mDuration).setListener(null);
//
//		// Animate the "hide" view to 0% opacity.
//		mSpinner.animate().alpha(0f).setDuration(mDuration)
//				.setListener(new AnimatorListenerAdapter() {
//					@Override
//					public void onAnimationEnd(Animator animation) {
//						mSpinner.setVisibility(View.GONE);
//					}
//				});
	}

	/**
	 * download an image from the internet! <a href="/param">@param</a> url
	 * 
	 * @return
	 */
	private Bitmap loadImageFromNetwork(String url) {
		Bitmap bm = null;
		try {
			URL urln = new URL(url);
			Log.i("load", url);
			Log.i("load", "loading Image...");
			bm = BitmapFactory.decodeStream(urln.openConnection()
					.getInputStream());
			Log.i("load", "done");
		} catch (IOException e) {
			Log.e("HUE", "Error downloading the image from server : "
					+ e.getMessage().toString());
		}
		return bm;
	}
}