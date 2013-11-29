package com.example.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyActivity extends Activity {

	final private String TAG = "com.example.android.MyActivity";

	private LinearLayout mLoading;
	private Button mButton;
    private Button mButtonSensor;
	private TextView mTxt;
	private EditText mEdit;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		mLoading = (LinearLayout) findViewById(R.id.loading);
	    mButton = (Button) findViewById(R.id.button);
        mButtonSensor = (Button) findViewById(R.id.sensor);
	    mTxt = (TextView) findViewById(R.id.txt);
	    mEdit = (EditText) findViewById(R.id.edit);

	    mButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    String url = mEdit.getText().toString();
			    if (!url.equals("")) {
				    mButton.setEnabled(false);
			        mLoading.setVisibility(View.VISIBLE);
			        new JSONTask().execute(url);
			    }
		    }
	    });

        mButtonSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SensorActivity.class);
                startActivity(intent);
            }

        });
    }

	private void setData(JSONObject jo) {
		try {
			JSONObject data = jo.getJSONObject("data");
			JSONArray items = data.getJSONArray("items");
			String res = "";
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				res += item.getString("time") + "-" + item.getString("PM25") + "\n";
			}

			mTxt.setText(res);

		} catch (JSONException je) {
			Log.e(TAG, je.getMessage());
		}
	}

	private class JSONTask extends AsyncTask<String, Void, JSONObject> {
		@Override
		protected JSONObject doInBackground(String... urls) {
			JSONObject r = null;
			String response = "";
			String link = urls[0];
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpGet request = new HttpGet(link);
			try {
				HttpResponse execute = client.execute(request);
				InputStream content = execute.getEntity().getContent();

				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s;
				while ((s = buffer.readLine()) != null) {
					response += s;
				}
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
			} finally {
				client.close();
			}

			try {
				r = new JSONObject(response);
			} catch (JSONException je) {
				Log.e(TAG, je.getMessage());
				Log.e(TAG, response);
			}

			return r;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			//Do something with result
			if (result != null) {
				mLoading.setVisibility(View.GONE);
				setData(result);
				//mTxt.setText(result);
				mTxt.setVisibility(View.VISIBLE);
				mButton.setEnabled(true);
			}
		}
	}
}
