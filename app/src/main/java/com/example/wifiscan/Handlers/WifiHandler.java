package com.example.wifiscan.Handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.wifiscan.MainActivity;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.Rete;
import com.example.wifiscan.Adapters.WifiAdapter;

import java.util.ArrayList;
import java.util.List;

public class WifiHandler {
    private WifiManager wifiManager;        // manager del wifi
    private BroadcastReceiver receiver;     // classe che gestirà le connessioni trovate

    public ArrayList<Rete> data;            // array che andrà stampato a schermo
    private List<ScanResult> results;       // array temporaneo che conterrà il risultato della wifiscan

    private MainActivity context;           // contesto dove andrà ad operare la classe

    public WifiHandler(final MainActivity context, final ArrayList<Rete> data) {
        this.context = context;
        this.data = data;

        this.wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        check_wifi_state();

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context local_context, Intent intent) {
                // acquisizione del risultato della scanzione
                results = wifiManager.getScanResults();

                local_context.unregisterReceiver(this);   // boh

                // prende i risultati e li aggiunge all'array per la stampa
                // è qui che andranno creati gli oggetti per il custom adapter
                for (ScanResult scanResult : results) {
                    // esclude le reti che non hanno il campo SSID
                    if (scanResult.SSID.equals("")) {
                        Log.d("NO_SSID_VALUE", scanResult.toString());

                        continue;
                    } else {
                        data.add(new Rete(scanResult.SSID, scanResult.capabilities, Integer.toString(scanResult.level)));
                        Log.d("NETWORK_VALUE", scanResult.toString());
                        //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " - " + scanResult.level);
                    }
                }

                // avvisa che la geolocalizzazione sta iniziando
                Toast.makeText(context, "Getting location ...", Toast.LENGTH_SHORT).show();

                // crea l'handler per la geolocalizzazione e acquisisce i dati
                LocationHandler locationHandler = new LocationHandler(context, data);
                locationHandler.requestUpdate();

                // aggiorna la ListView con il nuovo Adapter
                WifiAdapter adapter = new WifiAdapter(context,R.layout.layout_arrayadapter, data);
                MainActivity.listView.setAdapter(adapter);
            }
        };
    }

    public void scanWifi() {
        // controlla se il WiFi e la posizione sono attive
        check_wifi_state();
        LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "GPS Disabilitato, abilitarlo !", Toast.LENGTH_SHORT).show();

            // riabilita il bottone per la scanzione
            MainActivity.buttonScan.setEnabled(true);

            return;
        }

        // pulizia dell'array
        this.data.clear();

        this.context.registerReceiver(this.receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // inizio scanzione
        this.wifiManager.startScan();

        Toast.makeText(this.context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();

        // Acquisizione dei risultati (sembra che può essere omesso dato che sta già in broadcast reciver)
        //results = wifiManager.getScanResults();
    }

    private void check_wifi_state() {
        if (!this.wifiManager.isWifiEnabled()) {
            Toast.makeText(this.context, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            this.wifiManager.setWifiEnabled(true);
        }
    }
}
