package com.negev.bikeotlv;

import android.content.Context;
import android.os.AsyncTask;

import com.negev.bikeotlv.utils.MapUtils;
import com.negev.bikeotlv.utils.WebServices;

import apps.sunshade.com.utilitislibs.UtilitiesGeneral;


public class RefreshStations extends AsyncTask<Void, Integer, Boolean>{
	public static interface RefreshStationsListener	{
		public void refreshDone();
		public void refreshNoInternet();

	}
	
	private Boolean result;
	Resources appResources;
	RefreshStationsListener refreshStationsListener;
	Context context;
	public RefreshStations(Resources _appResources, Context _context, RefreshStationsListener _refreshStationsListener){
		appResources = _appResources;
		refreshStationsListener = _refreshStationsListener;
		context = _context;
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		result = false;
		if(UtilitiesGeneral.isNetworkConnected(appResources)) {
			WebServices.downloadingStations(appResources);
			com.negev.bikeotlv.utils.MapUtils.createStationsMarkers(appResources);
			result = true;
		}
		return result;			
	}

	protected void onPostExecute(Boolean result) 
	{
		if (result) {
			MapUtils.addIconsToMarkers(appResources);
			//TODO refresh
			if (refreshStationsListener!=null) {
				refreshStationsListener.refreshDone();
			}
		}
		else{
	
			if (refreshStationsListener!=null) {
				refreshStationsListener.refreshNoInternet();
			}

		}

	}
}
