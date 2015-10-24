package com.negev.bikeotlv.classes;

import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;
import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.utils.LocalDB;
import com.negev.bikeotlv.utils.UIUtils;


public class BikeStation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String id;	
	String name;
	String address;
	Double latDouble;

	Double lonDouble;
//	int lon;
//	int lat;
	//	GeoPoint point;
	Location location;
	float distance;


	Boolean favourite;
	int availableBikes;
	int availablePoles;
	Boolean active;
	int ageSeconds;
	Boolean isFav;

	public BikeStation(String id, String name, String address,
			Double latDouble, Double lonDouble, int availableBikes,
			int availablePoles, Boolean active, int ageSeconds) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.latDouble = latDouble;
		this.lonDouble = lonDouble;
		this.availableBikes = availableBikes;
		this.availablePoles = availablePoles;
		this.active = active;
		this.ageSeconds = ageSeconds;
		this.isFav = false;
		this.active = true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Boolean getIsFav() {
		return isFav;
	}

	public void setIsFav(Boolean isFav) {
		this.isFav = isFav;
	}
	public void setIsFavAndList(Boolean isFav, Resources appResources) {
		this.isFav = isFav;
		if (isFav) {
			appResources.favStations.add(this.id);
			LocalDB.archiveFavStations(appResources);
			LocalBroadcastManager.getInstance(appResources).sendBroadcast(new Intent(Constants.FAVS_CHANGED));
		}
		else{
			appResources.favStations.remove(appResources.favStations.indexOf(this.id));
			LocalDB.archiveFavStations(appResources);
			LocalBroadcastManager.getInstance(appResources).sendBroadcast(new Intent(Constants.FAVS_CHANGED));
		}

	}
	public int getAvailableBikes() {
		return availableBikes;
	}

	public void setAvailableBikes(int availableBikes) {
		this.availableBikes = availableBikes;
	}

	public int getAvailablePoles() {
		return availablePoles;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setAvailablePoles(int availablePoles) {
		this.availablePoles = availablePoles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLatDouble() {
		return latDouble;
	}

	public void setLatDouble(Double latDouble) {
		this.latDouble = latDouble;
	}

	public Double getLonDouble() {
		return lonDouble;
	}

	public void setLonDouble(Double lonDouble) {
		this.lonDouble = lonDouble;
	}

//	public int getLon() {
//		return lon;
//	}
//
//	public void setLon(int lon) {
//		this.lon = lon;
//	}
//
//	public int getLat() {
//		return lat;
//	}
//
//	public void setLat(int lat) {
//		this.lat = lat;
//	}

	public LatLng getLocation() {
		return new LatLng(this.latDouble, this.lonDouble);
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public int getAgeSeconds() {
		return ageSeconds;
	}

	public void setAgeSeconds(int ageSeconds) {
		this.ageSeconds = ageSeconds;
	}

	public void popMapAlertDialog(Context context, Resources appResources) {
		UIUtils.getCustomMapDialog(context, this, true, appResources).show();
	}

	public String getInfoString(Context mContext) {
		String infoString = ""; 
		if (!this.getAddress().isEmpty()) {
			infoString = this.getAddress() +  "\n";	
		}
		infoString += "\n" + mContext.getResources().getString(R.string.availableBikes) + " " + this.getAvailableBikes() + 
				"\n" + mContext.getResources().getString(R.string.availablePoles) + " " + this.getAvailablePoles();	
		return infoString;
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass().equals(BikeStation.class)) {
			if(((BikeStation)o).id.equals(this.id)){
				return true;
			}
		}
		return false;
	}
}
