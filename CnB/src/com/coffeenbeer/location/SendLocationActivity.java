package com.coffeenbeer.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

public class SendLocationActivity extends Activity {
	
	private TextView t;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_location);	
	}
	
	
	public void updateLocation(View view) {
		new AsyncTask<String, Void, JSONObject>() {

			Boolean yummyResult = false;
			
			@Override
			protected JSONObject doInBackground(String... input) {

				JSONObject jsonResponse = null;

				try {

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost request = new HttpPost(
							"http://www.coffeenbeer.com/move");
					
					LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
					Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
							2);
					nameValuePairs.add(new BasicNameValuePair("lat",
							String.valueOf(latitude)));
					nameValuePairs.add(new BasicNameValuePair("long",
							String.valueOf(longitude)));
					nameValuePairs.add(new BasicNameValuePair("auth",
							getString(R.string.auth)));
					request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(request);
					if(response.getStatusLine().getStatusCode() == 200)
						yummyResult = true;
					else
						yummyResult = false;
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
				return new JSONObject();

			}

			@Override
			protected void onPostExecute(JSONObject result) {
				System.out.println(result);
				TextView t = (TextView)findViewById(R.id.TextView01);
				if (yummyResult) 
					t.setTextColor(0xFF00FF00);
				else
					t.setTextColor(0xFFFF0000);
			}
		}.execute();
	}
}
