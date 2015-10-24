package com.negev.bikeotlv.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.negev.bikeotlv.R;
import com.negev.bikeotlv.Resources;
import com.negev.bikeotlv.classes.BikeStation;

public class UIUtils {

	public static Dialog getCustomMapDialog(final Context mContext, final BikeStation station, boolean isCancelable, final Resources appResources) {


		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.map_station_alert);

		dialog.setCancelable(isCancelable);

		((TextView)dialog.findViewById(R.id.txtName)).setText(station.getName());
		((TextView)dialog.findViewById(R.id.txtInfo)).setText(station.getInfoString(mContext));

		ImageButton buttonDirections = (ImageButton)dialog.findViewById(R.id.btnDirections);
		final ToggleButton buttonFav = (ToggleButton)dialog.findViewById(R.id.toggleFavBtnDetails);
		ImageButton buttonOK = (ImageButton)dialog.findViewById(R.id.btnOk);
		ImageButton buttonReport = (ImageButton)dialog.findViewById(R.id.btnReport);
		buttonFav.setChecked(station.getIsFav());
		buttonDirections.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
						Uri.parse("http://maps.google.com/maps?daddr=" + station.getLatDouble() + "," + station.getLonDouble()));
				mContext.startActivity(intent);
			}
		});

		buttonFav.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(buttonFav.isChecked()){
					station.setIsFavAndList(true, appResources);
				}
				else{
					station.setIsFavAndList(false,appResources);
				}

			}
		});

		buttonOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();				
			}
		});

		buttonReport.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder localBuilder = new AlertDialog.Builder(mContext);

				localBuilder.setTitle(mContext.getResources().getString(R.string.spWhere)).setItems(mContext.getResources().getStringArray(R.array.reportArray), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
					{
						String subjectString = "";
						String mailString = "";
						switch (paramAnonymous2Int)	{
						case 0:
							subjectString = mContext.getResources().getString(R.string.reportStation) + " " + station.getId();
							mailString = mContext.getResources().getString(R.string.mailBodyStationStart) + " " + station.getId(); 
							break;
						case 1:
							subjectString = mContext.getResources().getString(R.string.reportBike) + " " + station.getId();
							mailString = mContext.getResources().getString(R.string.mailBodyBikeStart) + " " + station.getId(); 
							break;
						}
						mailString += " " + mContext.getResources().getString(R.string.mailBodyStationAddress) + " " +station.getAddress() + ".";
						sendEmail(appResources, mContext, subjectString, mailString);

					}
				});

				localBuilder.create();
				localBuilder.show();
			}	
		});



		return dialog;
	}
	public static void sendEmail(Resources paramResources, Context paramContext, String paramString1, String paramString2)
	{
		Intent localIntent = new Intent("android.intent.action.SEND");
		localIntent.setType("text/plain");
		localIntent.putExtra("android.intent.extra.EMAIL", new String[] { "info@fsm-tlv.com" });
		localIntent.putExtra("android.intent.extra.CC", new String[] { "BikeOTLV@gmail.com" });
		localIntent.putExtra("android.intent.extra.SUBJECT", paramString1);
		localIntent.putExtra("android.intent.extra.TEXT", paramString2 + "\n\n\n" + paramContext.getResources().getString(R.string.endOfMail));
		paramContext.startActivity(Intent.createChooser(localIntent, "Send mail..."));
	}

	public static Dialog getCustomAppDialog(Context mContext,String title, String msg, String positiveBtnCaption, String negativeBtnCaption, boolean isCancelable, final CustomDialogInterface target) {

		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.bikeo_alert);
		dialog.setCancelable(isCancelable);

		if (title!=null) {
			((TextView)dialog.findViewById(R.id.title)).setText(title);
		}
		else {
			((TextView)dialog.findViewById(R.id.title)).setVisibility(View.GONE);
		}
		if (msg==null) {
			((TextView)dialog.findViewById(R.id.message)).setVisibility(View.GONE);
		}
		else{
			((TextView)dialog.findViewById(R.id.message)).setText(msg);
		}

		Button button = (Button)dialog.findViewById(R.id.positive_button);
		button.setText(positiveBtnCaption);
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (target!=null) {
					target.PositiveMethod();
				}
			}
		});
		Button button2 = (Button)dialog.findViewById(R.id.negative_button);

		if(negativeBtnCaption!=null){

			button2.setText(negativeBtnCaption);
			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
		else{
			button2.setVisibility(View.GONE);
		}

		return dialog;
	}
	
	public static void showNoInternetDialog(Context context){
		getCustomAppDialog(context, null, context.getResources().getString(R.string.noInternetConnection), context.getResources().getString(R.string.ok), null, false, null).show();
	}
}
