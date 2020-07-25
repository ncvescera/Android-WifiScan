package com.example.wifiscan.Handlers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.wifiscan.MainActivity;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class LocationHandler {
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Context context;
    private ArrayList<Rete> data;


    public LocationHandler(final Context context, final ArrayList<Rete> data) {
        this.context = context;
        this.data = data;

        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // method called when new position is acquired

                Log.d("LOCATION", Double.toString(location.getLatitude()));
                Log.d("LOCATION", Double.toString(location.getLongitude()));

                // the position is acquired only once
                locationManager.removeUpdates(this);

                // update data lat & lon
                for(Rete elem : data) {
                    elem.setLat(location.getLatitude());
                    elem.setLon(location.getLongitude());
                }

                // enable buttons
                MainActivity.buttonScan.setEnabled(true);
                MainActivity.buttonSave.setEnabled(true);
            }
        };

    }

    // boh
    public void requestUpdate() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
