package com.negev.bikeotlv.screens;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.negev.bikeotlv.Constants;
import com.negev.bikeotlv.MyMapFragment;
import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;
import com.negev.bikeotlv.utils.MapUtils;
import com.negev.bikeotlv.utils.UIUtils;
import com.negev.bikeotlv.utils.Utilities;

public class StationsMapFragment extends Fragment implements MyMapFragment.MapCallback, LocationListener,
        OnMarkerClickListener, OnMyLocationButtonClickListener, OnMapReadyCallback {

    private static final int REQUEST_LOCATION = 25;
    Resources appResources;
    private MyMapFragment fragment;
    private GoogleMap map;
    MainActivity activity;

    public StationsMapFragment() {

    }

    public static StationsMapFragment newInstance(int sectionNumber) {
        StationsMapFragment fragment = new StationsMapFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (MainActivity)getActivity();
        new LoadView().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.map_fragment, container,
                false);
        this.appResources = (Resources)getActivity().getApplication();
/*        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationServices();
        activity.manager = null;
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null && f.isResumed())
            getFragmentManager().beginTransaction().remove(f).commit();
    }


    @Override
    public void onPause() {
        super.onPause();
        stopLocationServices();
    }

    private void setUpMap() {
        map = fragment.getMap();
        map.setOnMarkerClickListener(this);
        map.getUiSettings().setZoomControlsEnabled(false);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        if (appResources.stationsMarkers.size()!=0) {
            if(map!=null){
                com.negev.bikeotlv.utils.MapUtils.addMarkersToMap(map, appResources);
                map.setMyLocationEnabled(true); // false to disable
            }
        }
        startLocationServices();
    }

    public void startLocationServices() {
        // Get the location manager
        activity.manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        activity.provider = activity.manager.getBestProvider(criteria, true);
        if (activity.provider!=null || MapUtils.hasGPSConnection(getActivity())) {

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Check Permissions Now
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            } else {
          /*  // permission has been granted, continue as usual
            Location myLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);*/
                activity.manager.requestLocationUpdates(((MainActivity)getActivity()).provider, 400, 1, this);
                onLocationChanged(activity.manager.getLastKnownLocation(activity.provider));
            }


        }
        else{
            UIUtils.getCustomAppDialog(getActivity(), getActivity().getResources().getString(R.string.noGPSTitle), getActivity().getResources().getString(R.string.noGPSContent),  getActivity().getResources().getString(R.string.ok),  null, true, null).show();
            onLocationChanged(null);
        }

    }



    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                activity.manager.requestLocationUpdates(((MainActivity)getActivity()).provider, 400, 1, this);
                onLocationChanged(activity.manager.getLastKnownLocation(activity.provider));

            } else {
                // Permission was denied or request was cancelled
            }
        }
    }
    public void stopLocationServices() {
        if(activity!=null && activity.manager!=null &&  ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            activity.manager.removeUpdates(this);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        BikeStation station = appResources.getStations().get(marker.getSnippet());
        station.popMapAlertDialog(getActivity(), appResources);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            Utilities.calculateDistanceForList(appResources, location);
            stopLocationServices();
        }
        else if (!MapUtils.hasGPSConnection(getActivity())) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.086783,34.789731), 15));
            if(activity.mapFirstTime){
                UIUtils.getCustomAppDialog(getActivity(), getActivity().getResources().getString(R.string.noGPSTitle), getActivity().getResources().getString(R.string.noGPSContent),  getActivity().getResources().getString(R.string.ok),  null, true, null).show();
                activity.mapFirstTime = false;
            }
            stopLocationServices();
        }


    }
    @Override
    public void onProviderDisabled(String provider) {
        UIUtils.getCustomAppDialog(getActivity(), getActivity().getResources().getString(R.string.noGPSTitle), getActivity().getResources().getString(R.string.noGPSContent),  getActivity().getResources().getString(R.string.ok),  null, true, null).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        startLocationServices();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onMapReady(GoogleMap map) {
        new LoadMap().execute();
    }

    private class LoadMap extends AsyncTask<Void, Intent, Boolean>{

        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        protected void onPostExecute(Boolean result)
        {
            setUpMap();
            Log.i(Constants.Log_Tag, "onMapReady end");
            if (activity.loadingDialog!=null && activity.loadingDialog.isShowing()) {
                activity.loadingDialog.dismiss();
            }
        }
    }

    private class LoadView extends AsyncTask<Void, Intent, Boolean>{

        protected void onPreExecute() {

            FragmentManager fm = getFragmentManager();
            fragment = (MyMapFragment)fm.findFragmentById(R.id.map);

            if (fragment == null) {
                fragment = new MyMapFragment();
                fragment.setMapCallback(StationsMapFragment.this);
                fm.beginTransaction().replace(R.id.map, fragment).commit();
            }
            else{
                fragment.setMapCallback(StationsMapFragment.this);
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //				com.negev.bikeotlv.utils.MapUtils.createStationsMarkers(appResources);



            return true;
        }

        protected void onPostExecute(Boolean result){
            //Setting the location on the map
            // 				if (loadingDialog!=null && loadingDialog.isShowing()) {
            //					loadingDialog.dismiss();
            //				}


        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        if(MapUtils.hasLocation(activity) || MapUtils.hasGPSConnection(getActivity())
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            onLocationChanged(activity.manager.getLastKnownLocation(activity.provider));
            return true;
        }
        else{
            UIUtils.getCustomAppDialog(getActivity(), getActivity().getResources().getString(R.string.noGPSTitle), getActivity().getResources().getString(R.string.noGPSContent),  getActivity().getResources().getString(R.string.ok),  null, true, null).show();
            return false;
        }
    }

}
