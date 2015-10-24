package com.negev.bikeotlv;

import com.google.android.gms.maps.model.LatLng;


public class Constants {

	public static String Log_Tag = "AppFlow";
	public static String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER"; 
	public static String FAVS_CHANGED = "favs_changed"; 
	public static String LIST_CHANGED = "list_changed"; 

	/**WEB SERVICES**/
	
	public static String webService = "http://apps.socialebola.com/Telofun/TelofunService.svc/rest/";
	public static String getStationList = "GetStationList";

	/**MAP**/
	static public LatLng DEF_LOCATION = new LatLng(51.51237449814354,-0.14000292867422104);;

}
