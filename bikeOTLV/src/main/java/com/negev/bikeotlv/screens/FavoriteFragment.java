package com.negev.bikeotlv.screens;

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
import android.widget.RelativeLayout;

import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.RefreshStations;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.RefreshStations.RefreshStationsListener;
import com.negev.bikeotlv.adapters.StationsAdapter;
import com.negev.bikeotlv.utils.UIUtils;
import com.negev.bikeotlv.utils.Utilities;

public class FavoriteFragment extends Fragment implements OnRefreshListener, RefreshStationsListener{

	Resources appResources;
	StationsAdapter adapter;
	View rootView;
	SwipeRefreshLayout swipeLayout;

	public FavoriteFragment() {

	}

	public static FavoriteFragment newInstance(int sectionNumber) {
		FavoriteFragment fragment = new FavoriteFragment();
		Bundle args = new Bundle();
		args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fav_fragment, container,
				false);
		this.appResources = (Resources)getActivity().getApplication();
		changeView(this.appResources.getFavStations().size()>0, rootView);
		swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(R.color.app_green, 
				R.color.white, R.color.app_green, R.color.white);
		if(this.appResources.getFavStations().size()!=0){
			Utilities.arrangeList(this.appResources.getFavStations(), appResources, (MainActivity)getActivity());

			ListView listView = (ListView) rootView.findViewById(R.id.favList);
			adapter = new StationsAdapter(getActivity(), appResources, this.appResources.getFavStations());
			listView.setAdapter(adapter);
			Utilities.setListViewScrollWithSwipe(listView, swipeLayout);
		}
		
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(Constants.FAVS_CHANGED));

		return rootView;
	}

	public void refreshData() {
		changeView(appResources.getFavStations().size()>0, rootView);
		Utilities.arrangeList(appResources.getFavStations(), appResources, (MainActivity)getActivity());

		adapter.notifyDataSetChanged();
	}

	private void changeView(Boolean showList, View rootvView) {
		if (showList) {
			((ListView)rootvView.findViewById(R.id.favList)).setVisibility(View.VISIBLE);
			((RelativeLayout)rootvView.findViewById(R.id.no_fav)).setVisibility(View.GONE);
		}
		else{
			((ListView)rootvView.findViewById(R.id.favList)).setVisibility(View.GONE);
			((RelativeLayout)rootvView.findViewById(R.id.no_fav)).setVisibility(View.VISIBLE);
		}
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(Constants.FAVS_CHANGED)) {
				if(adapter!=null)
					refreshData();
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
