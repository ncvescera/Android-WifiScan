package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;

    private WifiManager wifiManager;        // gestore del wifi
    private ArrayList<String> arrayList;    // array che andrà stampato a schermo
    private List<ScanResult> results;       // array temporaneo che conterrà il risultato della wifiscan

    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inizializzazione della ListView
        listView = findViewById(R.id.view_scan);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        Button buttonScan = findViewById(R.id.btn_scan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });

        // inizializzazione dell'ArrayList per la stampa dei risultati della WiFiScan
        arrayList = new ArrayList<>();

        // inizializzazione del WifiManager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // controllo se il wifi è abilitato
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

    }

    // Definizione del wifireciver che gestisce i dati che trova la scanzione
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // acquisizione del risultato della scanzione
            results = wifiManager.getScanResults();

            unregisterReceiver(this);   // boh

            // prende i risultati e li aggiunge all'array per la stampa
            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " - " + scanResult.level);
            }

            // aggiorna la ListView con il nuovo Adapter
            ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(adapter);
        }
    };

    private void scanWifi() {
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        // pulizia dell'array
        arrayList.clear();

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // inizio scanzione
        wifiManager.startScan();

        Toast.makeText(this, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();

        // Acquisizione dei risultati (sembra che può essere omesso dato che sta già in broadcast reciver)
        //results = wifiManager.getScanResults();
    }
}