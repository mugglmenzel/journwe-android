package com.journwe.android;

import android.graphics.Bitmap;

public class Detail {
	
	private Bitmap bitmap;
	private String string;
	
	public Detail(Bitmap bitmap, String string) {
		this.bitmap = bitmap;
		this.string = string;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}
}
