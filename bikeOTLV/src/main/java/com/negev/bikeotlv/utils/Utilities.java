package com.negev.bikeotlv.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;
import com.negev.bikeotlv.screens.MainActivity;

public class Utilities {

	public static void calculateDistanceForList(Resources appResources, Location location) {
		if (location != null) {
			for (String key : appResources.getStations().keySet()) {
				BikeStation bikeStation = appResources.getStations().get(key);
				float[] results = new float[4];
				Location.distanceBetween(location.getLatitude(), location.getLongitude(), bikeStation.getLatDouble(), bikeStation.getLonDouble(), results);
				bikeStation.setDistance(results[0]);
			}
			LocalBroadcastManager.getInstance(appResources).sendBroadcast(new Intent(Constants.LIST_CHANGED));
		}
	}
	public static void arrangeList(ArrayList<String> array, final Resources appResources, MainActivity activity) {

		if (MapUtils.hasLocation(activity)) {
			//--> sort by distance
			Collections.sort(array, new Comparator<String>() {

				@Override
				public int compare(String lhs, String rhs) {
					BikeStation bikeStation1 = appResources.getStations().get(lhs);
					BikeStation bikeStation2 = appResources.getStations().get(rhs);
					//TODO
					return (int)( bikeStation1.getDistance() -  bikeStation2.getDistance());
				}
			});
		}
		else{
			Collections.sort(array, new Comparator<Object>() {

				@Override
				public int compare(Object lhs, Object rhs) {
					BikeStation bikeStation1 = appResources.getStations().get(lhs);
					BikeStation bikeStation2 = appResources.getStations().get(rhs);
					String id1 = bikeStation1.getName();
					String id2 = bikeStation2.getName();
					return id1.compareToIgnoreCase(id2);
				}
			});
		}

	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}

	public static Bitmap decodeSampledBitmapFromResource(Context context,  String fileName, int reqWidth, int reqHeight) {
		//android.content.res.Resources res, int resId,


		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		//BitmapFactory.decodeResource(res, resId, options);
		String path = context.getFilesDir().toString() + "/" + fileName;

		BitmapFactory.decodeFile(path, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}
		return inSampleSize;
	}
	public static Dialog getLoadingDialog(String msg, Context context) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.progress_dialog);
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(msg);
		return dialog;
	}

	

	public static void setListViewScrollWithSwipe(final ListView listview, final SwipeRefreshLayout swipeRefreshLayout) {
		listview.setOnScrollListener(new AbsListView.OnScrollListener() {  
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int topRowVerticalPosition = 
						(listview == null || listview.getChildCount() == 0) ? 
								0 : listview.getChildAt(0).getTop();
				swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
			}
		});

	}
}
