package com.journwe.android;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateActivity extends Activity {

	private static EditText name;
	private static ImageView image;
	
	private Intent intent;
	public final static String NAME = "com.journwe.android.name";
	public final static String IMAGE = "com.journwe.android.image";
	private static final int SELECT_PHOTO = 100;
	private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		intent = new Intent(this, CreateJournWe.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void click(View v) {
		switch (v.getId()) {
		case R.id.image:
			Log.i("click", "image");
			
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			
			break;
		case R.id.setdetail:
			Log.i("click", "setdetail");
			if (name != null) {
				String n = name.getText().toString();
				
				if (n.equals(null) || n.equals("")) {
					Toast.makeText(this, "Choose a name", Toast.LENGTH_LONG).show();
					break;
				}
				
				else {
					setDetail(n);
				}
			}
			
			
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case SELECT_PHOTO:
	        if(resultCode == RESULT_OK){  
	            imageUri = imageReturnedIntent.getData();
	            InputStream imageStream;

				image.setImageURI(imageUri);
	        }
	    }
	}
	
	private void setDetail(String s) {
		intent.putExtra(NAME, s);
		intent.putExtra(IMAGE, imageUri);
		startActivity(intent);
		finish();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_create,
					container, false);
			
			name = (EditText) rootView.findViewById(R.id.editname);
			image = (ImageView) rootView.findViewById(R.id.image);
			
			return rootView;
		}
	}
}
