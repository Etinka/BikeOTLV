package com.negev.bikeotlv.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;

import android.content.Context;

public class LocalDB {

	static public void archiveStations(Resources app){
		try {
			archiveLists(app.getApplicationContext(), app.getStations(), "getStations");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	static public void archiveFavStations(Resources app){
		try {
			archiveLists(app.getApplicationContext(), app.getFavStations(), "getFavStations");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	@SuppressWarnings("unchecked")
	public static void readStations(Resources app) {
		try {
			app.setStations((HashMap<String, BikeStation>) readLists(app.getApplicationContext(), "getStations"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	@SuppressWarnings("unchecked")
	public static void readFavStations(Resources app) {
		try {
			app.setFavStations((ArrayList<String>) readLists(app.getApplicationContext(), "getFavStations"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static Object readLists(Context context, String fileName) throws IOException, ClassNotFoundException {

		File file = new File(context.getDir("data", 0), fileName);

		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
		Object object = inputStream.readObject();
		inputStream.close();

		return object;
	}

	public static void archiveLists(Context context, Object object, String fileName) throws IOException {

		File file = new File(context.getDir("data", 0), fileName);    
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
		outputStream.writeObject(object);
		outputStream.flush();
		outputStream.close();

	}
}
