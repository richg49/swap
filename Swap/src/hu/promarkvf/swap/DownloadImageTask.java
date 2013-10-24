package hu.promarkvf.swap;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	Bitmap    bitmapImage;
	
	public DownloadImageTask() {
		this.bmImage = null;
		this.bitmapImage = null;
	}
	
	public DownloadImageTask(ImageView bmImage) {
		this.bmImage = bmImage;
		this.bitmapImage = null;
	}
	
	public DownloadImageTask(Bitmap bm) {
		this.bitmapImage = bm;
		this.bmImage = null;
	}
	
	@Override
	protected Bitmap doInBackground(String... urls) {
		String urldisplay = urls[0];
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}
	
}
