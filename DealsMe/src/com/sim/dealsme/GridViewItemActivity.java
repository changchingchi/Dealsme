package com.sim.dealsme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GridViewItemActivity extends ActionBarActivity {
	// Declare Variables
	String StoreName;
	String StoreCell;
	String StoreReviewCount;
	String StoreIMG;
	String StoreIMGRating;
	String StoreLocation;
	String StoreUrl;
	ImageLoader imageLoader = new ImageLoader(this);
	final static int check = 111;
	EditText ssms;
	List<Address> directioins;
	LatLng latLngDes;
	buildmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid_view_item);

		ActionBar aB = getActionBar();
		aB.setDisplayHomeAsUpEnabled(true);

		Intent i = getIntent();

		// Get the result 
		StoreName = i.getStringExtra("StoreName");
		StoreIMG = i.getStringExtra("StoreIMG");
		StoreIMGRating = i.getStringExtra("StoreRating");
		StoreCell = i.getStringExtra("StoreCell");
		StoreReviewCount = i.getStringExtra("StoreReviewCount");
		StoreLocation = i.getStringExtra("StoreLocation");
		StoreUrl = i.getStringExtra("StoreUrl");
		// Locate the TextViews in singleitemview.xml
		TextView TV_StoreName = (TextView) findViewById(R.id.TV_StoreName);
		TextView TV_StoreCell = (TextView) findViewById(R.id.TV_StoreNumber);
		TextView TV_StoreLocation = (TextView) findViewById(R.id.TV_StoreLocation);

		// Locate the CallButton
		ImageButton BU_CallButton = (ImageButton) findViewById(R.id.BU_callbutton);
		ImageButton BU_Directions = (ImageButton) findViewById(R.id.BU_directions);
		ImageButton BU_Email = (ImageButton) findViewById(R.id.BU_email);
		//		ImageButton BU_SMS = (ImageButton) findViewById(R.id.BU_SMS);

		// Locate the ImageView in singleitemview.xml
		ImageView IMG_storeIMG = (ImageView) findViewById(R.id.gridview_singleIMG);
		ImageView IMG_storeRating = (ImageView) findViewById(R.id.gridview_singleRating);

		// Set results to the TextViews
		TV_StoreName.setText(StoreName);
		TV_StoreCell.setText(StoreCell);

		if (StoreLocation == null) {
			TV_StoreLocation.setText("NO ADDRESS AVAILABLE");
		} else {
			TV_StoreLocation.setText(StoreLocation);
		}

		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		imageLoader.DisplayImage(StoreIMG, IMG_storeIMG);
		imageLoader.DisplayImage(StoreIMGRating, IMG_storeRating);

		BU_CallButton.setClickable(true);
		BU_CallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_CALL);
				i.setData(Uri.parse("tel:" + StoreCell));
				startActivity(i);
			}
		});

		BU_Directions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("MAP BUTTON", "CLICKS");
				String GOOGLEQUERY = "google.navigation:q=";
				String STOREADDRESS = StoreLocation.replace(", ", "+");
				Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(GOOGLEQUERY + STOREADDRESS));
				startActivity(i);

			}
		});

		BU_Email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				String IMGlink = "<a href=\"" + StoreIMG + "\">" + StoreIMG + "</a>";
				String Storelink = "<a href=\"" + StoreUrl + "\">" + StoreUrl + "</a>";
				intent.putExtra(Intent.EXTRA_SUBJECT, "Check out the Restaurant I Found ! ");
				intent.putExtra(Intent.EXTRA_TEXT, "Check out this Restaurant!\n\n<" + StoreName + ">\n\n@" + StoreLocation + "\n\n" + Html.fromHtml(Storelink));

				startActivity(intent);
			}
		});

		//		BU_SMS.setOnClickListener(new OnClickListener() {
		//
		//			@Override
		//			public void onClick(View v) {
		//
		//				AlertDialog.Builder builder = new AlertDialog.Builder(GridViewItemActivity.this);
		//				AlertDialog dialog;
		//
		//				LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//				final View layout = inflater.inflate(R.layout.sms_alertdialog, (ViewGroup) findViewById(R.id.AlertDialogLayout1));
		//				final EditText number = (EditText) layout.findViewById(R.id.ET_Receiver);
		//				number.requestFocus();
		//				ssms = (EditText) layout.findViewById(R.id.ET_Content);
		//				ssms.setText("Check out this Restaurant!\n\n<" + StoreName + ">\n\n@" + StoreLocation + "\n", TextView.BufferType.EDITABLE);
		//				builder.setView(layout);
		//				dialog = builder.create();
		//				dialog.setCancelable(true);
		//				dialog.setMessage("TYPE in YOUR Messages! ");
		//				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "SEND", new DialogInterface.OnClickListener() {
		//
		//					@Override
		//					public void onClick(DialogInterface dialog, int which) {
		//						// TODO Auto-generated method stub
		//						// Toast.makeText(getApplicationContext(), "SEND",
		//						// Toast.LENGTH_LONG).show();
		//
		//						String numberString = number.getText().toString();
		//
		//						String SMStextString = ssms.getText().toString();
		//						if (!isNum(numberString) || numberString.isEmpty() || SMStextString.isEmpty()) {
		//
		//							if (numberString.isEmpty() || SMStextString.isEmpty())
		//								Toast.makeText(getApplicationContext(), " Do not keep it blank! ", Toast.LENGTH_LONG).show();
		//							else {
		//								Toast.makeText(getApplicationContext(), "Please Type in Only Number for RCVR!", Toast.LENGTH_LONG).show();
		//							}
		//						} else {
		//							sendMSG(numberString, SMStextString);
		//						}
		//					}
		//				});
		//				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
		//
		//					@Override
		//					public void onClick(DialogInterface dialog, int which) {
		//						// TODO Auto-generated method stub
		//						Toast.makeText(getApplicationContext(), "CANCEL", Toast.LENGTH_LONG).show();
		//						dialog.dismiss();
		//					}
		//				});
		//				dialog.show();
		//
		//				ImageButton voice = (ImageButton) dialog.findViewById(R.id.buttonV);
		//				voice.setOnClickListener(new OnClickListener() {
		//
		//					@Override
		//					public void onClick(View v) {
		//
		//						// TODO Auto-generated method stub
		//						Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		//						i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		//						i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Tell Me Your Message");
		//						startActivityForResult(i, check);
		//
		//					}
		//				});
		//
		//			}
		//		});

		// Locate Map and location information

		Geocoder getGeocoder = new Geocoder(GridViewItemActivity.this);
		bm = new buildmap();
		try {
			directioins = getGeocoder.getFromLocationName(StoreLocation, 1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (directioins.isEmpty()) {
			bm.updatemap(MyApplication.getLocation().getLatitude(), MyApplication.getLocation().getLongitude());
			Toast.makeText(this, "Sorry,Can't Find Store On Map.\nUse Navigation Button For Directions", Toast.LENGTH_LONG).show();
		} else {
			latLngDes = new LatLng(directioins.get(0).getLatitude(), directioins.get(0).getLongitude());
			Log.d("result", directioins.get(0).getLatitude() + " vs " + directioins.get(0).getLongitude());

			bm.updatemap(latLngDes.latitude, latLngDes.longitude);
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
				Toast.makeText(GridViewItemActivity.this, query, Toast.LENGTH_LONG).show();

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

					Intent i = new Intent(GridViewItemActivity.this, Search_Activity.class);
					i.putExtra("location", ll.get(0));
					i.putExtra("locationName", addresses.get(0).getAddressLine(0).toString());
					startActivity(i);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					Toast.makeText(GridViewItemActivity.this, "Location InValid", Toast.LENGTH_LONG).show();
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
		switch (id) {
			case R.id.action_settings:
				makeAboutMeDialog();

				break;
			case android.R.id.home:
				Intent i = new Intent(this, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				break;
			default:
				break;
		}

		return super.onOptionsItemSelected(item);
	}

	class buildmap {
		GoogleMap map;
		LatLng Target;
		LatLng CurrentLoc;

		public buildmap() {
			map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

		}

		public void updatemap(Double Lat, Double Lng) {
			Target = new LatLng(Lat, Lng);
			CurrentLoc = new LatLng(MyApplication.getLocation().getLatitude(), MyApplication.getLocation().getLongitude());

			GoogleMap map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment)).getMap();

			map.addMarker(new MarkerOptions().position(Target).title(StoreName).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).flat(true)).showInfoWindow();
			map.addMarker(new MarkerOptions().position(CurrentLoc).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluedot)));

			// Set the camera to the greatest possible zoom level that includes the
			// bounds
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Target, 12));
			//			map.animateCamera(CameraUpdateFactory.zoomTo(10), 1000, null);
		}

	}

	public void makeAboutMeDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(GridViewItemActivity.this);
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

	private boolean isNum(String s) {

		try {
			Double d = Double.parseDouble(s);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}

		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (requestCode == check && resultCode == RESULT_OK) {

			ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			Log.d("result content : ", result.get(0).toString());
			String s = result.get(0).toString();
			// TextView tvTextView = (TextView) findViewById(R.id.textView1);
			// tvTextView.setText(s);
			ssms.append(s);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
