package com.negev.bikeotlv;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.MarkerOptions;
import com.negev.bikeotlv.classes.BikeStation;


import android.app.Application;

public class Resources extends Application{

	HashMap< String, BikeStation>stations = new HashMap<String, BikeStation>();
	public ArrayList<String> favStations = new ArrayList<String>();
	public ArrayList<MarkerOptions>stationsMarkers = new ArrayList<MarkerOptions>();

	public HashMap<String, BikeStation> getStations() {
		return stations;
	}

	public void setStations(HashMap<String, BikeStation> stations) {
		this.stations = stations;
	}

	public ArrayList<String> getFavStations() {
		return favStations;
	}

	public void setFavStations(ArrayList<String> favStations) {
		this.favStations = favStations;
	}



}
