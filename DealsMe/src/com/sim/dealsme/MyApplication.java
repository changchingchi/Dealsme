package com.sim.dealsme;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class MyApplication extends Application {
	private final static String consumerKey = " ";
	private final static String consumerSecret = " ";
	private final static String token = " ";
	private final static String tokenSecret = "";

	private static ArrayList<HashMap<String, String>> arrayList;
	private static Location location;
	private static String category;
	private static int limit;
	
	

	public ArrayList<HashMap<String, String>> getArrayList() {
		return arrayList;
	}

	public void setArrayList(ArrayList<HashMap<String, String>> arrayList) {
		MyApplication.arrayList = arrayList;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("XXXX", "MY APPLICATION CALLED");
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		//		Toast.makeText(this, "configChanged", Toast.LENGTH_LONG).show();
	}

	public static Location getLocation() {
		return location;
	}

	public static void setLocation(Location location) {
		MyApplication.location = location;
	}

	public static String getCategory() {
		return category;
	}

	public static String getConsumerkey() {
		return consumerKey;
	}

	public static String getConsumerSecret() {
		return consumerSecret;
	}

	public static String getToken() {
		return token;
	}

	public static String getTokenSecret() {
		return tokenSecret;
	}

	public static int getLimit() {
		return limit;
	}

	public static void setLimit(int i) {
		MyApplication.limit = i;
	}

	public static boolean isNetWorkExist(Context context) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

}
