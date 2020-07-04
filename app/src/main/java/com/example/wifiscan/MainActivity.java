package com.example.wifiscan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Wifi gestore;
    private LocationManager gps;

    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inizializzazione della ListView
        listView = findViewById(R.id.view_scan);

        // inizializzazione del gestore del wifi
        gestore = new Wifi(MainActivity.this, listView);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        Button buttonScan = findViewById(R.id.btn_scan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gestore.scanWifi();
            }
        });

        // Bottone Temporaneo per vedere password
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("LOG_PASSWORD_0", gestore.arrayList.get(0).getPassword());
               Log.d("PASSWORD_RETE1", gestore.arrayList.get(1).toString());

            }
        });
    }
}