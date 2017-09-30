package com.example.who.doittest.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import com.example.who.doittest.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by who on 30.09.2017.
 */

public class LocationUtils {

    private LocationUtils() {}

    public  static String getAddress(Context context, float latitude, float longitude){
        String address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                address = "";
            } else {
                address = addresses.get(0).getAddressLine(0);
                if (address == null) address = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static void askForGps(final Context context){
        LocationManager mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context, R.style.MyDialogTheme);
            alertDialogBuilder
                    .setMessage("GPS is disabled in your device. Enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Enable GPS",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivity(callGPSSettingIntent);
                                }
                            });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }
}
