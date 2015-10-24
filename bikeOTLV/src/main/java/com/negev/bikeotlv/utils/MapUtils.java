package com.negev.bikeotlv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;
import com.negev.bikeotlv.screens.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MapUtils {

	static public Boolean hasLocation(MainActivity activity) {
		if (activity.manager!=null) {
			if (activity.manager.getLastKnownLocation(activity.provider) != null) {
				return true;
			}
		}
		return false;
	}
	
	public static Boolean hasGPSConnection(Context context) {
		LocationManager manager = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
		return manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ;
	}
	
	static public void changeMapFocus(LatLng location, float zoom, GoogleMap map) {
		if (location!=null) {
			// Move the camera instantly to OXFORD with a zoom
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
		}
		else{

			// Move the camera instantly to OXFORD with a zoom
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.DEF_LOCATION, zoom));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
		}
	}


	public static void createStationsMarkers(Resources appResources) {
		//Trying to deal with the ConcurrentModificationException  
		MarkerOptions markerOptions  = null;

		List<MarkerOptions> thingsToBeAdd = new ArrayList<MarkerOptions>();
		for(Iterator<String> it = appResources.getStations().keySet().iterator(); it.hasNext();) {
			String key = it.next();
			BikeStation station = (BikeStation) appResources.getStations().get(key);
			markerOptions = new MarkerOptions()
			.position(station.getLocation())
			.title("shop")
			.draggable(false)
			.snippet(station.getId());

			thingsToBeAdd.add(markerOptions);
		}
		appResources.stationsMarkers.addAll(thingsToBeAdd);
	}

	static public void addIconsToMarkers(Resources appResources){
		Bitmap activeIcon = BitmapFactory.decodeResource(appResources.getResources(), R.drawable.station_icon);
		Bitmap activebhalfsize= Utilities.getResizedBitmap(activeIcon, getIconSize(appResources),(int) (getIconSize(appResources)/1.06));

		Bitmap notActiveIcon = BitmapFactory.decodeResource(appResources.getResources(), R.drawable.station_icon_gray);
		Bitmap notActivebhalfsize= Utilities.getResizedBitmap(notActiveIcon, getIconSize(appResources),(int) (getIconSize(appResources)/1.06));

		for (MarkerOptions markerOptions : appResources.stationsMarkers) {
			BikeStation bikeStation = appResources.getStations().get(markerOptions.getSnippet());
			Bitmap icon = activebhalfsize;
			if (!bikeStation.getActive()) {
				icon = notActivebhalfsize;
			}
			markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
		}
	}
	static public void addMarkersToMap(GoogleMap map, Resources app) {

		for (MarkerOptions marker : app.stationsMarkers) {
			map.addMarker(marker);
		}
	}

	private static int getIconSize(Resources app) {
		DisplayMetrics outMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) app
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		float density  = app.getResources().getDisplayMetrics().density;
		int res =  (int) (app.getResources().getInteger(R.integer.map_marker_size_multiplier) * density);
		return res;
	}

}
