package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.Handlers.WifiHandler;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private WifiHandler gestore;
    private ArrayList<Rete> dati;
    private Button buttonScan;
    private Button btn;

    private DBManager database;

    // PER IL WIFI SCAN DEVE ESSERE ABILITATA LA GEOLOCALIZZAZIONE E IL WIFI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DBManager(MainActivity.this.getApplicationContext());

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
        gestore = new WifiHandler(MainActivity.this,  actualView, dati);

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

        // Bottone per salvare i dati nel database
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn.setEnabled(false);
                for(Rete elem : dati) {
                    Log.d("DATI", elem.toString());

                    Toast.makeText(MainActivity.this,"Salvo i dati ...", Toast.LENGTH_SHORT).show();
                    boolean result = database.save(elem.getSSID(), elem.getDettagli(), Integer.parseInt(elem.getLevel()), elem.getPassword(), elem.getLat(), elem.getLon());
                }

                /* Per vedere il contenuto del database

                Cursor a = database.query();

                while (a.moveToNext()) {
                    Log.d("DATA_QUERY", a.getString(a.getColumnIndex(DBStrings.FIELD_SSID)) + " " + a.getString(a.getColumnIndex(DBStrings.FIELD_Password)));
                }

                a.close();
                */

            }
        });
    }

    int fc_counter = 0;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dtab1:
                Intent intent = new Intent(MainActivity.this, DbActivity.class);
                startActivity(intent);

                break;
            case R.id.dtab2:
                switch (fc_counter) {
                    case 0:
                        Toast.makeText(MainActivity.this,"è in arrivo, scappa!", Toast.LENGTH_SHORT).show();
                        fc_counter = fc_counter +1;
                        break;

                    case 1:
                        Toast.makeText(MainActivity.this,"ti avevo avvertito...", Toast.LENGTH_SHORT).show();
                        fc_counter = fc_counter +1;
                        break;

                    case 2:
                        Intent intent2 = new Intent(MainActivity.this, FrocioCoroActivity.class);
                        startActivity(intent2);
                        fc_counter = fc_counter +1;
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