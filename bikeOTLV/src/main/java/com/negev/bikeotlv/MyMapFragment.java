package com.negev.bikeotlv;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MyMapFragment extends MapFragment {

	  private MapCallback callback;

	  public void setMapCallback(MapCallback callback)
	  {
	    this.callback = callback;
	  }

	  public static interface MapCallback{
	     public void onMapReady(GoogleMap map);
	  }

	  @Override public void onActivityCreated(Bundle savedInstanceState){
	     super.onActivityCreated(savedInstanceState);
	     Log.i(Constants.Log_Tag, "MyMapFragment onActivityCreated");
	     if(callback != null) callback.onMapReady(getMap());
	     Log.i(Constants.Log_Tag, "MyMapFragment getMap");
	  }
	
}
