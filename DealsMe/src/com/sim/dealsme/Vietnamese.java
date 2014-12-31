package com.sim.dealsme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

public class Vietnamese extends Fragment implements OnItemSelectedListener {
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
	String[] spinner_list = { "Sort By", "Review: High to Low ", "DISTANCE : Near to Far" };
	NetworkChangeReceiver nwrReceiver;
	ArrayAdapter<String> spinnerAdapter;
	DownloadJSON startTask;

	public static Vietnamese newInstance() {
		Vietnamese fVietnamese = new Vietnamese();
		return fVietnamese;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		view = inflater.inflate(R.layout.fragment_vietnamese, container, false);
		spinner = (Spinner) view.findViewById(R.id.main_spinner);

		spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, spinner_list);
		if (!MyApplication.isNetWorkExist(getActivity())) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			AlertDialog dialog;
			dialog = builder.create();
			dialog.setMessage("FOODasian can not find Internet available, please check internet status.");
			dialog.setCancelable(false);
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close FOODasian", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					System.exit(0);
				}
			});

			dialog.show();
		} else {
			startTask = new DownloadJSON();
			startTask.execute();
		}
		setRetainInstance(true);
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("XXXXXX", "onPausedCALLED");
		startTask.cancel(true);
	}

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
			nwrReceiver = new NetworkChangeReceiver();
			getActivity().registerReceiver(nwrReceiver, filter);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			String consumerKey = MyApplication.getConsumerkey();
			String consumerSecret = MyApplication.getConsumerSecret();
			String token = MyApplication.getToken();
			String tokenSecret = MyApplication.getTokenSecret();
			Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
			String response = yelp.search("vietnamese", MyApplication.getLocation().getLatitude(), MyApplication.getLocation().getLongitude());
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
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			mGridviewAdaptor = new GridviewAdaptor(getActivity(), arrayList);
			gridView = (GridView) view.findViewById(R.id.gridview_vietnamese);
			gridView.setAdapter(mGridviewAdaptor);
			spinner.setAdapter(spinnerAdapter);
			spinner.setOnItemSelectedListener(Vietnamese.this);
			getActivity().unregisterReceiver(nwrReceiver);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (debugmode) {
			Log.d("spinner item:", position + "");
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
				Collections.sort(arrayList, new CustomComparatorRating());
				mGridviewAdaptor.notifyDataSetChanged();
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
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
