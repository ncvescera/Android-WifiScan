package com.example.wifiscan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationHandler {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private ArrayList<Rete> dati;
    private Button button;

    public LocationHandler(final Context context, final ArrayList<Rete> dati, final Button button) {
        this.context = context;
        this.dati = dati;
        this.button = button;

        //final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

        // Definisci il listener che risponde agli aggiornamenti delle posizione
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Metodo chiamato quando viene individuata una nuova posizione

                Log.d("LOCATION", Double.toString(location.getLatitude()));
                Log.d("LOCATION", Double.toString(location.getLongitude()));

                locationManager.removeUpdates(this);

                for(Rete elem : dati) {
                    elem.setLat(location.getLatitude());
                    elem.setLon(location.getLongitude());
                }

                // Con questa classe trasformo le coordinate (Double) in una stringa human-readable (String)
                // Potrebbe tornare utile
                /*
                HumanPosition humanPosition = new HumanPosition(context, location.getLatitude(), location.getLongitude());
                Log.d("TEST_HUMAN", humanPosition.getPosition());
                */

                // riabilita il bottone per risolvere problemi di sincronizzazione
                button.setEnabled(true);
            }
        };

    }

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
}
