package com.journwe.android;

import java.io.IOException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<ImageView, Void, Bitmap> {

	ImageView imageView = null;

	@Override
	protected Bitmap doInBackground(ImageView... imageViews) {
		this.imageView = imageViews[0];
		return download_Image((String) imageView.getTag());
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (result == null) {
			imageView.setImageResource(R.drawable.ic_action_group);
		}

		else {
			imageView.setImageBitmap(result);
		}
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