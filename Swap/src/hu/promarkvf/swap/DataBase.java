package hu.promarkvf.swap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DataBase extends AsyncTask<String, Integer, String> {
	private Context        context        = null;
	private ProgressDialog progressDialog = null;
	private long           responseLength;
	
	public DataBase(Context context) {
		super();
		this.context = context;
	}
	
	public DataBase() {
		super();
		this.context = null;
	}
	
	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = null;
		String resp = null;
		List<NameValuePair> nameValuePairs = null;
		if (params.length > 0) {
			try {
				httpClient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				nameValuePairs = new ArrayList<NameValuePair>(2);
				for (String param : params) {
					String[] posts = param.split("=");
					if (posts.length > 1) {
						nameValuePairs.add(new BasicNameValuePair(posts[0], posts[1]));
					}
				}
				if (nameValuePairs.size() > 0) {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				}
				resp = httpClient.execute(httppost, new BasicResponseHandler());
				// System.out.println(resp);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (httpClient != null) {
					// httpClient.close();
				}
			}
		}
		return resp;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		double percent = 0;
		if (this.responseLength != 0) {
			percent = 100 * values[0] / this.responseLength;
		}
		Log.d("AsyncTask progress", "" + percent + " %");
	}
}
