package com.example.wifiscan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Wifi {
    private Context context;                // contesto dove andrà ad operare la classe
    private WifiManager manager;            // manager del wifi
    private BroadcastReceiver receiver;     // classe che gestirà le connessioni trovate
    private ArrayList<Rete> arrayList;    // array che andrà stampato a schermo
    private List<ScanResult> results;       // array temporaneo che conterrà il risultato della wifiscan
    private ListView listView;              // View a cui verranno aggiunti i dati

    public Wifi(Context context, final ListView listView) {
        this.context = context;

        this.listView = listView;
        this.arrayList = new ArrayList<>();

        this.manager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);

        check_wifi_state();

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // acquisizione del risultato della scanzione
                results = manager.getScanResults();

                context.unregisterReceiver(this);   // boh

                // prende i risultati e li aggiunge all'array per la stampa
                // è qui che andranno creati gli oggetti per il custom adapter
                for (ScanResult scanResult : results) {
                    arrayList.add(new Rete(scanResult.SSID, scanResult.capabilities, Integer.toString(scanResult.level)));
                    //arrayList.add(scanResult.SSID + " - " + scanResult.capabilities + " - " + scanResult.level);
                }

                // aggiorna la ListView con il nuovo Adapter
                WifiAdapter adapter = new WifiAdapter(context,R.layout.listview_row_rete, arrayList);
                //ArrayAdapter<Rete> adapter = new ArrayAdapter<Rete>(context, android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);
            }
        };

    }

    public void scanWifi() {
        check_wifi_state();

        // pulizia dell'array
        this.arrayList.clear();

        this.context.registerReceiver(this.receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // inizio scanzione
        this.manager.startScan();

        Toast.makeText(this.context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();

        // Acquisizione dei risultati (sembra che può essere omesso dato che sta già in broadcast reciver)
        //results = wifiManager.getScanResults();
    }

    private void check_wifi_state() {
        if (!this.manager.isWifiEnabled()) {
            Toast.makeText(this.context, "WiFi is disabled ... We need to enable it", Toast.LENGTH_LONG).show();
            this.manager.setWifiEnabled(true);
        }
    }
}
