package com.sim.dealsme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class Search_Activity extends Activity implements OnItemSelectedListener{
	GridView gridView;
	GridviewAdaptor mGridviewAdaptor;
	ProgressDialog pDialog;
	JSONArray jsonArray_businesses;
	JSONObject jsonObject;
	ArrayList<HashMap<String, String>> arrayList;
	View view;
	String url_aroundMe;
	Spinner spinner;
	boolean debugmode = true;
	boolean isFirst = true;
	Bundle locationDATA;
	LatLng location;
	String locationNameString;
	String[] spinner_list = { "Sort By", "Review: High to Low ", "Distance : Near to Far" };
	ArrayAdapter<String> spinnerAdapter;
	TextView locationName;
	boolean hasLocationName = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_);
		spinner = (Spinner) findViewById(R.id.main_spinnerSearch);
		spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinner_list);
		locationDATA = getIntent().getExtras();
		location = (LatLng) locationDATA.get("location");
		locationName = (TextView) findViewById(R.id.TV_locationName);
		locationNameString = (String) locationDATA.get("locationName");
		new DownloadJSON().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
		if (debugmode) {
			Log.d("spinner item:", position + " callllllllllllled");
		}

		switch (position) {

			case 0:
				// do nothing, by default = all deals...
				// need to handle when user choose case 0 from others cases.
				// put some logic here.
				if (debugmode)
					Log.d("spinner item  :", spinner.getSelectedItem().toString());
				break;
			case 1:

				if (debugmode)
					Log.d("spinner item  :", spinner.getSelectedItem().toString());
				
				
//				new DownloadJSON().execute();
				
				Collections.sort(arrayList, new CustomComparatorRating());
				mGridviewAdaptor.notifyDataSetChanged();
				isFirst = false;

				break;
			case 2:
				if (debugmode)
					Log.d("spinner item  :", spinner.getSelectedItem().toString());
				Collections.sort(arrayList, new CustomComparatorDistance());
				mGridviewAdaptor.notifyDataSetChanged();
				break;

			default:
				
				break;
		}
		// put categories logic here...
		// 1. alldeals-->others. 2.others to alldeals. 3. first time initialize.

	}
	
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Search_Activity.this);
			pDialog.setTitle("LOADING...");
			pDialog.setMessage("We Are Working on it now...");
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.d("XXXXX","doinBGCalledddddd");
			String consumerKey = MyApplication.getConsumerkey();
			String consumerSecret = MyApplication.getConsumerSecret();
			String token = MyApplication.getToken();
			String tokenSecret = MyApplication.getTokenSecret();
			Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
			String response = yelp.search("restaurants",location.latitude,location.longitude);
			arrayList = new ArrayList<HashMap<String, String>>();

			try {
				jsonObject = new JSONObject(response);
				// need to request more information here in order to pass into
				// GridviewItemActivity.class
				// later.
				jsonArray_businesses = jsonObject.getJSONArray("businesses");
				if (!jsonArray_businesses.isNull(0)) { // check if has deals.

					for (int i = 0; i < jsonArray_businesses.length() & jsonArray_businesses != null; i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						String Add = "";

						jsonObject = jsonArray_businesses.getJSONObject(i);

						//the following for brief gridview

						map.put("StoreName", jsonObject.getString("name"));//store name
						map.put("StoreIMG", jsonObject.getString("image_url").replace("ms.jpg", "ls.jpg"));//get a small image. need to convert to large.
						map.put("StoreRatingIndex", jsonObject.getString("rating"));
						map.put("StoreReviewCount", jsonObject.getString("review_count"));
						map.put("StoreRating", jsonObject.getString("rating_img_url_large"));
						// the following for menu showing on webview
						map.put("StoreUrl", jsonObject.getString("mobile_url"));

						// the following for single item activity
						map.put("StoreCell", jsonObject.getString("phone"));//store cell phone number, set action to show dial pad.					

						for (int j = 0; j < jsonObject.getJSONObject("location").getJSONArray("address").length(); j++) {

							Add += (String) jsonObject.getJSONObject("location").getJSONArray("address").get(j) + ", ";

						}
						Add = Add + jsonObject.getJSONObject("location").getString("city") + ", " + jsonObject.getJSONObject("location").getString("state_code") + ", " + jsonObject.getJSONObject("location").getString("postal_code");
						map.put("StoreLocation", Add); // get all address info
						Log.d("location", Add);

						if (jsonObject.getJSONObject("location").has("coordinate")) {
							map.put("StoreGeoLocLAT", jsonObject.getJSONObject("location").getJSONObject("coordinate").getString("latitude"));
							map.put("StoreGeoLocLONG", jsonObject.getJSONObject("location").getJSONObject("coordinate").getString("longitude"));
						}

						map.put("StoreDistance", ((jsonObject.getInt("distance") / 1000) / 1.6) + ""); // from my location to destination

						arrayList.add(map);

					}

				}

			}

			catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("ERROR", "CANNOT FIND JSON ARRAY");
				e.printStackTrace();
				hasLocationName = false;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("onPostEX","calleeeeeeeedonPostEXXXX");
			mGridviewAdaptor = new GridviewAdaptor(Search_Activity.this, arrayList);
			gridView = (GridView) findViewById(R.id.gridview_searchactivity);
			gridView.setAdapter(mGridviewAdaptor);
//			spinner.setSelection()
			if(hasLocationName){
				locationName.setText(locationNameString);
			}
			else {
				locationName.setText("NO RESTAURANT FOUND, CHECK YOUR CITY NAME");
			}
			spinner.setAdapter(spinnerAdapter);
			spinner.setOnItemSelectedListener(Search_Activity.this);
			pDialog.dismiss();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}


}
