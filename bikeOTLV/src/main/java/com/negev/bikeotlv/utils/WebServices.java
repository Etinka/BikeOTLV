package com.negev.bikeotlv.utils;

import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class WebServices {

	public static void downloadingStations(Resources appResources) {
		String urlString = Constants.webService + Constants.getStationList;
		JSONObject json = null;

		try {
			json = new JSONParser().getGetJSONFromUrl(urlString);
			if (json!=null) {
				JSONArray array = json.getJSONArray("StationList");
				appResources.setStations(arrangeBikesArray(array, appResources));
				LocalDB.archiveStations(appResources);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	private static HashMap<String, BikeStation> arrangeBikesArray(JSONArray array, Resources appResources) {
		HashMap<String, BikeStation>stationsList = new HashMap<String, BikeStation>();
		for (int i = 0; i < array.length(); i++) {
			try {
				BikeStation bikeStation = getBikeStation(array.getJSONObject(i));
				if(appResources.getFavStations().contains(bikeStation.getId()))
					bikeStation.setIsFav(true);
				stationsList.put(bikeStation.getId(),bikeStation);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return stationsList;
	}

	private static BikeStation getBikeStation(JSONObject object) {
		BikeStation bikeStation = null;
		try {
			String id = object.getString("Id");
			String name = object.getString("Name");
			String address = object.getString("Address");
			Double latDouble = object.getDouble("Lat");
			Double lonDouble = object.getDouble("Long");
		    Boolean active = false;
		    int availableBikes = 0;
		    int availablePoles = 0;
	        if ((object.get("FreeBikes")!=null)){
	        	availableBikes = object.getInt("FreeBikes");
				availablePoles = object.getInt("FreePolls");
	            active = true;
	        }
			int ageSeconds = object.getInt("AgeSeconds");
			bikeStation = new BikeStation(id, name, address, latDouble, lonDouble, availableBikes, availablePoles, active, ageSeconds);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return bikeStation;
	}

}
