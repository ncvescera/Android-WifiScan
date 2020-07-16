package com.example.wifiscan.Handlers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.example.wifiscan.R;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class LocationHandler {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private ArrayList<Rete> dati;
    private Button button;
    private View mainView;
    private Button buttonScan;

    public LocationHandler(final Context context, final ArrayList<Rete> dati, final View view) {
        this.context = context;
        this.dati = dati;
        this.mainView = view;

        this.button = (Button) mainView.findViewById(R.id.button);
        this.buttonScan = (Button) mainView.findViewById(R.id.btn_scan);

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

                // riabilita il bottone per risolvere problemi di sincronizzazione
                button.setEnabled(true);
                buttonScan.setEnabled(true);
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
