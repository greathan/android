package com.example.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.net.http.AndroidHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    new NetworkTask().execute("http://www.google.com");
        setContentView(R.layout.main);
    }

	private class NetworkTask extends AsyncTask<String, Void, HttpResponse> {
		@Override
		protected HttpResponse doInBackground(String... params) {
			String link = params[0];
			HttpGet request = new HttpGet(link);
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			try {
				return client.execute(request);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} finally {
				client.close();
			}
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			//Do something with result
			if (result != null) {

			}
		}
	}
}
