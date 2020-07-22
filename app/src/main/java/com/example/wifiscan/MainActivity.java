package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


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
import com.example.wifiscan.Utils.CSVFile;
import com.example.wifiscan.Utils.Rete;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

        // inizializzo il gestore del database
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
                // controlla se ha i permessi per il GPS in caso li abilita chiedendo all'utente
                int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                        return;
                }

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
                // disabilita se stesso
                btn.setEnabled(false);

                Toast.makeText(MainActivity.this,"Salvo i dati ...", Toast.LENGTH_SHORT).show();

                // salva i dati all'interno del database
                for(Rete elem : dati) {
                    Log.d("DATI", elem.toString());

                    boolean result = database.save(elem.getSSID(), elem.getDettagli(), Integer.parseInt(elem.getLevel()), elem.getPassword(), elem.getLat(), elem.getLon());
                }
            }
        });
    }

    int fc_counter = 0;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dtab1:    // passa a DbActivity
                Intent intent = new Intent(MainActivity.this, DbActivity.class);
                startActivity(intent);

                break;
            case R.id.dtab2:
                csvPopulate();
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
                        Intent intent2 = new Intent(MainActivity.this, FCActivity.class);
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

    private void csvPopulate() {
        InputStream inputStream = getResources().openRawResource(R.raw.test_dati_veri);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> scoreList = csvFile.read();

        for (String[] row:scoreList) {
            database.save(row[0], row[1], Integer.valueOf(row[2]), row[3], Double.valueOf(row[4]), Double.valueOf(row[5]));
        }
    }

}