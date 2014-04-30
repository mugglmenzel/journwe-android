package com.journwe.android;

import java.io.IOException;
import java.net.URL;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class DownloadTask extends AsyncTask<ImageView, Void, Bitmap> {

// reference to our imageview
private ImageView mImage;
private View mContent;
private View mSpinner;
private int mDuration;

public DownloadTask(View content, View spinner, int duration) {
  mContent = content;
  mSpinner = spinner;
  mDuration = duration;
}

    /** 
     * The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute()
     */
    protected Bitmap doInBackground(ImageView... images) {
        mImage = images[0];
        String url = (String)mImage.getTag();
        Log.i("load", "start");
        return loadImageFromNetwork(url);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
    	Log.i("load", "set image");
        // Set the image view before the content is shown.
        mImage.setImageBitmap(result);

        // Set the "show" view to 0% opacity but visible, so that it is visible
        mContent.setAlpha(0f);
        mContent.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on the view.
        mContent.animate()
                .alpha(1f)
                .setDuration(mDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity.
        mSpinner.animate()
                .alpha(0f)
                .setDuration(mDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mSpinner.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * download an image from the internet!
     * <a href="/param">@param</a> url
     * @return
     */
    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bm = null;
        try {
            URL urln = new URL(url);
            Log.i("load", url);
            Log.i("load", "loading Image...");
            bm = BitmapFactory.decodeStream(urln.openConnection().getInputStream());
            Log.i("load", "done");
        } catch (IOException e) {
            Log.e("HUE","Error downloading the image from server : " + e.getMessage().toString());
        } 
        return bm;
    }
}