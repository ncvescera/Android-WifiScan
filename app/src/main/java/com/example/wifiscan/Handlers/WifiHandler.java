package com.example.wifiscan.Handlers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wifiscan.MainActivity;
import com.example.wifiscan.Adapters.ScanRecyclerViewAdapter;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;
import java.util.List;

public class WifiHandler {
    private WifiManager wifiManager;
    private BroadcastReceiver receiver;

    private LocationHandler locationHandler;

    public ArrayList<Rete> data;            // array used to populate ListView
    private List<ScanResult> results;       // temp array for storing data. Used to populate ArrayList<Rete> data

    private Activity context;

    public WifiHandler(final Activity context, final ArrayList<Rete> data) {
        this.context = context;
        this.data = data;

        this.wifiManager = (WifiManager) this.context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.locationHandler = new LocationHandler(this.context, this.data);

        check_wifi_state();

        this.receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context local_context, Intent intent) {
                // getting scan results
                results = wifiManager.getScanResults();

                local_context.unregisterReceiver(this);   // boh :D

                // populate data ArrayList from results List
                for (ScanResult scanResult : results) {
                    // exclude Wifi with blank SSID field
                    if (scanResult.SSID.equals("")) {
                        Log.d("NO_SSID_VALUE", scanResult.toString());

                        continue;
                    } else {
                        data.add(new Rete(scanResult.SSID, scanResult.capabilities, Integer.toString(scanResult.level)));
                        Log.d("NETWORK_VALUE", scanResult.toString());
                    }
                }

                Toast.makeText(context, "Accedo alla posizione ...", Toast.LENGTH_SHORT).show();

                // getting latitude and longitude
                locationHandler.requestUpdate();

                // update ListView content with the new data by a CutstomArrayAdapter
                //WifiAdapter adapter = new WifiAdapter(context,R.layout.layout_mainactivity_recyclerview, data);
                ScanRecyclerViewAdapter adapter = new ScanRecyclerViewAdapter(data);

                MainActivity.recyclerView.setAdapter(adapter);
                MainActivity.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        };
    }

    public void scanWifi() {
        // check if wifi is enabled
        check_wifi_state();

        // check if GPS is enabled
        // if GPS is disabled it stops the scan (i don't know how to autoenable GPS like I do with the Wifi)
        if(!locationHandler.isGPSEnabled()) {
            Toast.makeText(context, "GPS Disabilitato, abilitarlo !", Toast.LENGTH_SHORT).show();

            // enable SCAN button
            MainActivity.buttonScan.setEnabled(true);

            return;
        }

        // wipe array content
        this.data.clear();

        this.context.registerReceiver(this.receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // starting scan
        this.wifiManager.startScan();

        Toast.makeText(this.context, "Scanning WiFi ...", Toast.LENGTH_SHORT).show();

        // gets data (it seams useless)
        //results = wifiManager.getScanResults();
    }

    private void check_wifi_state() {
        // if wifi is disabled it enables it
        if (!this.wifiManager.isWifiEnabled()) {
            Toast.makeText(this.context, "Wifi Disabilitato, abilitarlo !", Toast.LENGTH_LONG).show();
            this.wifiManager.setWifiEnabled(true);
        }
    }
}
