package com.sim.dealsme;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GridviewAdaptor extends BaseAdapter {
	Context context;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> result = new HashMap<String, String>();
	ImageLoader imageLoader;
	LayoutInflater inflater;

	public GridviewAdaptor(Context context, ArrayList<HashMap<String, String>> arrayList) {

		this.context = context;
		data = arrayList;
		imageLoader = new ImageLoader(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(context, "item clicked :" + position, Toast.LENGTH_LONG).show();
		Log.d("getItem", position + "");
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolderPag {
		public ImageView StoreIMG;
		public TextView StoreName;
		public TextView StoreReviewCount;
		public ImageView StoreRating;
		public TextView StoreDistance;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//		TextView gridTitle;
		//		ImageView  gridImage;
		ViewHolderPag view;

		result = data.get(position);
		if (convertView == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.activity_gridview_adaptor, parent, false);
			view = new ViewHolderPag();
			view.StoreName = (TextView) convertView.findViewById(R.id.grid_header);
			view.StoreReviewCount = (TextView) convertView.findViewById(R.id.grid_reviewCount);
			view.StoreIMG = (ImageView) convertView.findViewById(R.id.picture);
			view.StoreRating = (ImageView) convertView.findViewById(R.id.grid_storeRating);
			view.StoreDistance = (TextView) convertView.findViewById(R.id.grid_distance);
			// view.imgViewFlag.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// view.imgViewFlag.setLayoutParams(new GridView.LayoutParams());
			convertView.setTag(view);
		} else {
			view = (ViewHolderPag) convertView.getTag();
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				result = data.get(position);
				// fire and open another activity for gridview item.
				
				Intent intent = new Intent(context, GridViewItemActivity.class);
				// Pass all data rank
				intent.putExtra("StoreName", result.get("StoreName"));
				intent.putExtra("StoreIMG", result.get("StoreIMG"));
				intent.putExtra("StoreReviewCount", result.get("StoreReviewCount"));
				intent.putExtra("StoreRating", result.get("StoreRating"));
				intent.putExtra("StoreLocation", result.get("StoreLocation"));
				intent.putExtra("StoreCell", result.get("StoreCell"));
				intent.putExtra("StoreMenu", result.get("StoreMenu"));
				intent.putExtra("StoreDistance", result.get("StoreDistance"));
				intent.putExtra("StoreUrl", result.get("StoreUrl"));
				// Start SingleItemView Class
				context.startActivity(intent);

			}
		});
		//		view.txtViewTitle.setText(result.get(MainActivity.gridTitle));
		//		imageLoader.DisplayImage(result.get(MainActivity.gridIMG), view.GridImage);

		view.StoreName.setText(result.get("StoreName")); //get Title back;
		Log.d("GridTitle", result.get("StoreName"));
		imageLoader.DisplayImage(result.get("StoreIMG"), view.StoreIMG);
		Log.d("GridImage", result.get("StoreIMG"));
		imageLoader.DisplayImage(result.get("StoreRating"), view.StoreRating);
		Log.d("GridRating", result.get("StoreRating"));
		view.StoreReviewCount.setText(result.get("StoreReviewCount") + " Reviews ");
		Log.d("GridCount", "" + result.get("StoreReviewCount"));
		if(round(Double.parseDouble(result.get("StoreDistance")), 1)==0.0){
			view.StoreDistance.setText("0.1 mi");
		}else{
			view.StoreDistance.setText(round(Double.parseDouble(result.get("StoreDistance")), 1)+" mi");
		}
		
		Log.d("GridDistance", result.get("StoreDistance"));
		Log.d("XXXXXXXXXXXX", round(Double.parseDouble(result.get("StoreDistance")), 1)+"");

		// handle when item click event here....
		// code here;

		//
		return convertView;
	}
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
}
