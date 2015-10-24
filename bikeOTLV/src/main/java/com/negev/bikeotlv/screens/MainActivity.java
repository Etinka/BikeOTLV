package com.negev.bikeotlv.screens;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.negev.bikeotlv.R;
import com.negev.bikeotlv.RefreshStations;
import com.negev.bikeotlv.RefreshStations.RefreshStationsListener;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.utils.UIUtils;
import com.negev.bikeotlv.utils.Utilities;

import java.util.Locale;

public class MainActivity extends Activity implements ActionBar.TabListener, RefreshStationsListener {


	static int MAP_TAB = 0;
	static int LIST_TAB = 1;
	static int FAV_TAB = 2;
	public LocationManager manager; 
	public String provider;

	SectionsPagerAdapter mSectionsPagerAdapter;
	Resources appResources;
	public Dialog loadingDialog;
	public Boolean mapFirstTime;
	StationsMapFragment stationsMapFragment;

	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.appResources = (Resources)getApplication();
		mapFirstTime = true;
		new LoadView(savedInstanceState).execute();
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_refresh:
			new RefreshStations(appResources,this, this).execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).

			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = StationsMapFragment.newInstance(position);
				stationsMapFragment = (StationsMapFragment)fragment;
				break;
			case 1:
				fragment = ListFrag.newInstance(position);
				break;
			case 2:
				fragment = FavoriteFragment.newInstance(position);
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.map).toUpperCase(l);
			case 1:
				return getString(R.string.list).toUpperCase(l);
			case 2:
				return getString(R.string.favs).toUpperCase(l);
			}
			return null;
		}


	}


	@Override
	public void refreshDone() {
		// TODO Auto-generated method stub

	}

	private class LoadView extends AsyncTask<Void, Intent, Boolean>{
		Context context;
		ActionBar actionBar;
		Bundle savedInstanceState;

		public LoadView(Bundle _savedInstanceState){
			savedInstanceState = _savedInstanceState; 
		}
		protected void onPreExecute() {
			context = MainActivity.this;
			loadingDialog = Utilities.getLoadingDialog(getResources().getString(R.string.justloading), context);
			loadingDialog.show();

			// Set up the action bar.
			actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setTitle("");
			TypedValue tv = new TypedValue();
			context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
			actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_green)));
			actionBar.setIcon(R.drawable.tab_icon);
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
			mViewPager = (ViewPager) findViewById(R.id.pager);
			return true;
		}

		protected void onPostExecute(Boolean result) 
		{


			mViewPager.setAdapter(mSectionsPagerAdapter);

			mViewPager
			.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {				
					actionBar.setSelectedNavigationItem(position);
				}
			});
			int tab = -1;
			if(savedInstanceState!=null){
				tab = savedInstanceState.getInt("tab");
			}
			//			if(tab!=FAV_TAB){

			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title defined by
				// the adapter. Also specify this Activity object, which implements
				// the TabListener interface, as the callback (listener) for when
				// this tab is selected.
				if (tab == -1) {
					actionBar.addTab(actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(MainActivity.this));
				}
				else{
					actionBar.addTab(actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(MainActivity.this)
							,i, tab == i);
				}

			}
			if (tab==FAV_TAB || tab == LIST_TAB) {
				loadingDialog.dismiss();
			}

			//			if(savedInstanceState!=null){
			//				tab = savedInstanceState.getInt("tab");
			//				actionBar.setSelectedNavigationItem(tab);
			//			}

			com.negev.bikeotlv.utils.MapUtils.addIconsToMarkers(appResources);

		}
	}


	@Override
	public void refreshNoInternet() {
		UIUtils.showNoInternetDialog(this);				
	}

}
