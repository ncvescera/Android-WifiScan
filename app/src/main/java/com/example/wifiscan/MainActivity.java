package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Wifi gestore;
    private LocationManager gps;
    private LocationHandler locationHandler;
    private ArrayList<Rete> dati;
    private Button buttonScan;
    private Button btn;

    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inizializzazione della ListView
        listView = findViewById(R.id.view_scan);

        // inizializzazione dei dati
        dati = new ArrayList<Rete>();

        // preso riferimento dei bottoni
        buttonScan = findViewById(R.id.btn_scan);
        btn = findViewById(R.id.button);

        // inizializzazione del gestore del wifi e della posizione
        gestore = new Wifi(MainActivity.this, listView, dati, btn);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabilita i bottoni
                btn.setEnabled(false);
                buttonScan.setEnabled(false);

                // avvia la scnazione del wifi
                gestore.scanWifi();

                // riabilita il bottone per la wifiscan dopo tot secondi
                // per evitare lo spam delle scanzioni
                new Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                buttonScan.setEnabled(true);
                            }
                        }, 5000    //Specific time in milliseconds
                );
            }
        });

        // Bottone Temporaneo per dubg
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Rete elem : dati) {
                    Log.d("DATI", elem.toString());
                }

            }
        });
    }
}