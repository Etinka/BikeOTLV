package com.negev.bikeotlv.screens;

import java.util.ArrayList;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.RefreshStations;
import com.negev.bikeotlv.RefreshStations.RefreshStationsListener;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.adapters.StationsAdapter;
import com.negev.bikeotlv.utils.UIUtils;
import com.negev.bikeotlv.utils.Utilities;

public class ListFrag extends Fragment implements OnRefreshListener, RefreshStationsListener{
	Resources appResources;
	StationsAdapter adapter;
	SwipeRefreshLayout swipeLayout;
	MainActivity activity;
	ArrayList<String>stationsList;

	public ListFrag() {

	}
	public static ListFrag newInstance(int sectionNumber) {
		ListFrag fragment = new ListFrag();
		Bundle args = new Bundle();
		args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.list_fragment, container,
				false);

		activity = (MainActivity)getActivity();
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(R.color.app_green, 
				R.color.white, R.color.app_green, R.color.white);
		this.appResources = (Resources)getActivity().getApplication();

		ListView listView = (ListView) rootView.findViewById(R.id.stationsList);
		stationsList = new ArrayList<String>();

		for (String string : appResources.getStations().keySet()) {
			stationsList.add(string);

		}	
		Utilities.arrangeList(stationsList, appResources, activity);


		adapter = new StationsAdapter(getActivity(), appResources, stationsList);
		listView.setAdapter(adapter);
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(Constants.FAVS_CHANGED));
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(Constants.LIST_CHANGED));

		return rootView;
	}


	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Constants.FAVS_CHANGED)) {
				if(adapter!=null)
					adapter.notifyDataSetChanged();
			}		

			if (action.equals(Constants.LIST_CHANGED)) {
				Utilities.arrangeList(stationsList, appResources, activity);

				if(adapter!=null)
					adapter.notifyDataSetChanged();
			}

		}
	};

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(true);
		new RefreshStations(appResources, getActivity(), this).execute();
	}
	@Override
	public void refreshDone() {
		this.adapter.notifyDataSetChanged();
		swipeLayout.setRefreshing(false);
	}
	@Override
	public void refreshNoInternet() {
		swipeLayout.setRefreshing(false);
		UIUtils.showNoInternetDialog(getActivity());		
	}
}
