package com.sim.dealsme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ActionBarActivity implements TabListener {
	// actionbarActivity
	// is
	// subClass
	// of
	// FragmentActivity
	//initial
	boolean debugmode = true;
	GridView gridView;
	GridviewAdaptor mGridviewAdaptor;
	LocationManager locManager;
	android.location.LocationListener locationListener;
	Location lastknowLoc;
	LatLng latLngDes;
	LatLng latLngOrigin;
	ArrayList<Bitmap> list_bitmap;
	ProgressDialog pDialog;
	JSONArray jsonArray_deals;
	JSONObject jsonObject;
	ArrayList<HashMap<String, String>> arrayList;
	ViewPager viewPager = null;
	android.support.v7.app.ActionBar actionBar;
	FragmentManager fragmentManager;
	FragmentTransaction fragmentTransaction;
	android.support.v4.app.Fragment fragment;
	android.support.v4.app.Fragment newfragment;
	AlertDialog.Builder alertDialogBuilder;
	String[] tab_list = { "ALL RESTAURANTS", "CHINESE", "JAPANESE", "KOREAN", "TAIWANESE", "VIETNAMESE" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		requestLocation rLocation = new requestLocation();
		rLocation.findCurrentLocation(); // find lastknownLocation
		viewPager = (ViewPager) findViewById(R.id.pager);
		//set params for yelp api
		MyApplication.setLimit(20);

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
		for (int i = 0; i < tab_list.length; i++) {
			android.support.v7.app.ActionBar.Tab tab = actionBar.newTab();
			tab.setText(tab_list[i]);
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}

		fragmentManager = getSupportFragmentManager();
		viewPager.setAdapter(new myAdaptor(fragmentManager));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		Log.d("onTabReselected", "Called" + arg0.getPosition());
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());
		Log.d("onTabselected", "Called" + arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		Log.d("onTabUnselected", "Called" + arg0.getPosition());
	}

	class myAdaptor extends FragmentStatePagerAdapter {

		public myAdaptor(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public android.support.v4.app.Fragment getItem(int i) { // determine
																// what fragment
			// to be
			// called..
			// TODO Auto-generated method stub
			fragment = null;
			if (debugmode) {
				Log.d("DEBUGMODE ON", "getItem");
			}
			if (i == 0) {
				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 0");
				}
				fragment = AROUNDME.newInstance();

			} else if (i == 1) {
				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 1");
				}
				fragment = Chinese.newInstance();
			} else if (i == 2) {

				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 2");
				}
				fragment = Japanese.newInstance();

			} else if (i == 3) {

				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 3");
				}
				fragment = Korean.newInstance();

			} else if (i == 4) {

				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 4");
				}
				fragment = Taiwanese.newInstance();

			} else if (i == 5) {

				if (debugmode) {
					Log.d("DEBEGMDOE ON", "GET ITEM 5");
				}
				fragment = Vietnamese.newInstance();

			}

			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (debugmode) {
				// Log.d("DEBUGMODE ON", "getCount");
			}
			return 6; // how many tabs you have in app.
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			if (position == 0)
				return "ALL RESTAURANTS";
			else if (position == 1)
				return "CHINESE";
			else if (position == 2)
				return "JAPANESE";
			else if (position == 3)
				return "KOREAN";
			else if (position == 4)
				return "TAIWANESE";
			else if (position == 5)
				return "VIETNAMESE";
			return null;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub

			return POSITION_NONE;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		searchView.setQueryHint("TYPE IN A CITY NAME");
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();
				if (MyApplication.isNetWorkExist(getApplicationContext())) {
					Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
					List<LatLng> ll = null;
					try {
						List<Address> addresses = geocoder.getFromLocationName(query, 1);
						ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
						//		    Log.d("XXXXX","~~~~~~~~~~~"+addresses.get(0).getAddressLine(0).toString());
						for (Address a : addresses) {
							if (a.hasLatitude() && a.hasLongitude()) {
								ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
							}
						}

						Intent i = new Intent(MainActivity.this, Search_Activity.class);
						i.putExtra("location", ll.get(0));
						i.putExtra("locationName", addresses.get(0).getAddressLine(0).toString());

						startActivity(i);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						Toast.makeText(MainActivity.this, "Location InValid", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "No Network Available, Please retry later", Toast.LENGTH_LONG).show();
				
				}

				return false;
			}

			@Override
			public boolean onQueryTextChange(String arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			makeAboutMeDialog();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class requestLocation {
		public void findCurrentLocation() {
			locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationListener = new myLocationListener();
			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			lastknowLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			Log.d("my location was", lastknowLoc.getLatitude() + " vs " + lastknowLoc.getLongitude());
			MyApplication.setLocation(lastknowLoc);
		}
	}

	class myLocationListener implements android.location.LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			// if (location != null) {
			//
			// location.getLongitude();
			// location.getLatitude();
			// Log.d("LAT: ", Double.toString(lat));
			// Log.d("LNG: ", Double.toString(lng));
			// }
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	public void makeAboutMeDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		AlertDialog dialog;
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.search_dialog, (ViewGroup) findViewById(R.id.SearchDialogLayout));
		builder.setView(layout);
		dialog = builder.create();
		dialog.setCancelable(true);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "AGREE", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();

			}
		});

		dialog.show();

	}

	public void checkNetworkDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		AlertDialog dialog;
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.search_dialog, (ViewGroup) findViewById(R.id.SearchDialogLayout));
		builder.setView(layout);
		dialog = builder.create();
		dialog.setCancelable(false);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "network", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				System.exit(0);

			}
		});

		dialog.show();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Notification");
		alertDialogBuilder.setMessage("DO YOU WANT TO EXIT ?").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// exit
				finish();
			}
		}).setNegativeButton("NO", null).show();

	}

	private boolean isNetworkOnline() {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
