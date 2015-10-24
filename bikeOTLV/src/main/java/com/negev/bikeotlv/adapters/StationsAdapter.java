package com.negev.bikeotlv.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;

public class StationsAdapter extends ArrayAdapter<String> {

	private final ArrayList<String> values;
	Context context;
	String availableBikes;
	String availablePoles;
	Resources appResources;


	public StationsAdapter(Context context,  Resources app, ArrayList<String> values) {
		super(context, R.layout.cell_station, values);
		this.context = context;
		this.values = values;
		this.appResources = app;
		this.availableBikes = context.getResources().getString(R.string.availableBikes) + " ";
		this.availablePoles = context.getResources().getString(R.string.availablePoles) + " ";
	}

	@SuppressLint("ViewHolder") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.cell_station, parent, false);
		rowView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BikeStation station = appResources.getStations().get(values.get(position));
				station.popMapAlertDialog(context, appResources);		
			}
		});
		TextView textView = (TextView) rowView.findViewById(R.id.stationName);
		final BikeStation station = appResources.getStations().get(this.values.get(position));
		textView.setText(station.getName());
		((TextView)rowView.findViewById(R.id.availableBikes)).setText(this.availableBikes + station.getAvailableBikes());
		((TextView)rowView.findViewById(R.id.availablePoles)).setText(this.availablePoles + station.getAvailablePoles());

		ToggleButton fav = (ToggleButton)rowView.findViewById(R.id.toggleFavBtnDetails);
		fav.setChecked(station.getIsFav());
		fav.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(((ToggleButton)v).isChecked()){
					station.setIsFavAndList(true, appResources);
				}
				else{		
					station.setIsFavAndList(false, appResources);
				}
			}
		});
		return rowView;
	}
}