package com.negev.bikeotlv.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.utils.CustomDialogInterface;
import com.negev.bikeotlv.utils.LocalDB;
import com.negev.bikeotlv.utils.UIUtils;
import com.negev.bikeotlv.utils.WebServices;

import apps.sunshade.com.utilitislibs.UtilitiesGeneral;

public class SplashScreen extends Activity {
	Resources appResources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		this.appResources = (Resources)getApplication();


		new StartAppDownloads().execute();

	}

	private class StartAppDownloads extends AsyncTask<Void, Integer, Boolean>{
		private Boolean result;

		@Override
		protected Boolean doInBackground(Void... params) {
			result = false;
			LocalDB.readFavStations(appResources);
			LocalDB.readStations(appResources);
			if(UtilitiesGeneral.isNetworkConnected(appResources)) {
				WebServices.downloadingStations(appResources);
				result = true;
			}
			com.negev.bikeotlv.utils.MapUtils.createStationsMarkers(appResources);
			return result;			
		}

		protected void onPostExecute(Boolean result) 
		{
			if (result) {
				if (appResources.getStations().size()==0) {
					UIUtils.getCustomAppDialog(SplashScreen.this, null, SplashScreen.this.getResources().getString(R.string.wehavesomedificulties), SplashScreen.this.getResources().getString(R.string.ok), null, false, new CustomDialogInterface() {

						@Override
						public void PositiveMethod() {
							finish();
						}
					}).show();
				}
				else{
					startActivity(new Intent(SplashScreen.this, MainActivity.class));
					finish();
				}
			}
			else{
				if(appResources.getStations().size() == 0){
					UIUtils.getCustomAppDialog(SplashScreen.this, null, SplashScreen.this.getResources().getString(R.string.noInternetConnectionForSplashFirstLaunch), SplashScreen.this.getResources().getString(R.string.ok), null, false, new CustomDialogInterface() {

						@Override
						public void PositiveMethod() {
							finish();
						}
					}).show();
				}
				else{
					UIUtils.getCustomAppDialog(SplashScreen.this, null, SplashScreen.this.getResources().getString(R.string.noInternetConnectionForSplash), SplashScreen.this.getResources().getString(R.string.ok), null, false, new CustomDialogInterface() {

						@Override
						public void PositiveMethod() {

							startActivity(new Intent(SplashScreen.this, MainActivity.class));
							finish();
						}
					}).show();
				}
			}

		}
	}



}

