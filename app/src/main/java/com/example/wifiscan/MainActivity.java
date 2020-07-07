package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

        // prende il riferimento della view attuale
        View actualView = getWindow().getDecorView().findViewById(android.R.id.content);

        // inizializzazione del gestore del wifi e della posizione
        gestore = new Wifi(MainActivity.this,  actualView, dati);

        // inizializzazione del bottone per la scanzione e dell'evento onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabilita i bottoni
                btn.setEnabled(false);
                buttonScan.setEnabled(false);

                // elimina le righie vecchie per evitare di poter far casino con i bottoni
                listView.setAdapter(null);

                // avvia la scnazione del wifi
                gestore.scanWifi();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = 0;
        switch (item.getItemId()){
            case R.id.dtab1:
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                break;;
            case R.id.dtab2:
                switch (i){
                    case 0:
                        Toast.makeText(this,"è in arrivo, scappa!", Toast.LENGTH_SHORT);
                        i = i+1;
                        break;
                    case 1:
                        Toast.makeText(this,"ti avevo avvertito...", Toast.LENGTH_SHORT);
                        i = i+1;
                        break;
                    case 2:
                        Toast.makeText(this,"Ricevuto F.C", Toast.LENGTH_SHORT);
                        i = i+1;
                        break;

                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
        return true;
    }

}