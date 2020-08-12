package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wifiscan.Adapters.DatabaseRecyclerViewAdapter;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.Utils.AlertBoxManager;
import com.example.wifiscan.Utils.CsvExporter;
import com.example.wifiscan.Utils.HumanPosition;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {

    private Activity contesto;

    private ImageButton cercaBtn;
    private EditText SSIDEditText;
    private EditText PositionEditText;

    private DBManager manager;
    private ArrayList<Rete> reti;

    public static RecyclerView recyclerView;
    public static DatabaseRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        this.contesto = this;

        reti = new ArrayList<Rete>();

        // inizializzo il dbManager
        manager = DBManager.getDbInstance(getApplicationContext());

        // bottone per eliminare il contenuto del database
        // il bottone viene disabilitato fin quando i dati non sono stati stampati
        cercaBtn = findViewById(R.id.elimina_db);
        cercaBtn.setEnabled(false);

        // EditText per la ricerca
        SSIDEditText = findViewById(R.id.searchSSID);
        PositionEditText = findViewById(R.id.searchPosition);

        // creo l'adapter
        adapter = new DatabaseRecyclerViewAdapter();

        // recyclerview principale
        recyclerView = (RecyclerView) findViewById(R.id.cursor_listview);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thread per popolare la RecyclerView all'avvio
        // serve per velocizzare l'avvio dell'Activity
        new Thread(new Runnable() {
            @Override
            public void run() {
                /* Non sembra essere necessario far attendere dei secondi prefissati...
                // aspetta 0.5 secondi
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */

                // prendo tutti i dati dal database per inizializzare la listview
                reti = manager.query();

                // passa in modalità UI per poter modificare l'adapter
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  aggiorno i dati
                        adapter.setReti(reti);
                        cercaBtn.setEnabled(true);
                    }
                });
            }
        }).start();

        // aggiunge i listeners ai vari oggetti
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // prendo il riferimento del layout del menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.db_menu, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        // quando l'Activity viene chiusa il cursore e la connessione al database vengono chiusi
        manager.close();

        // elimino i dati dalla memoria
        reti.clear();
        adapter.setReti(reti);

        super.onDestroy();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteDatabase:
                AlertBoxManager.displayDeleteAllDataAlertBox(contesto, reti);
                break;
            case R.id.exportCSV:
                new CsvExporter(contesto, manager.query()).export();
                break;
            case R.id.degrado:
                // passa al degrado
                /*Intent intent = new Intent(DbActivity.this, DegradoActivity.class);
                startActivity(intent);*/
                break;
        }

        return true;
    }

    private void setListeners() {
        /** LISTENER PER IL BOTTONE cercaBtn **/
        // con una lunga pressione del tasto viene eliminato il contenuto delle EditText
        cercaBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SSIDEditText.setText("");
                PositionEditText.setText("");

                reti = manager.query();

                adapter.setReti(reti);

                closeKeyboard(v.getContext());

                return true;
            }
        });

        // esegue la ricerca in base al contenuto delle EditText
        cercaBtn.setOnClickListener(new View.OnClickListener() {
            private ArrayList checkPrecisionSearch(String positionText) {
                // controlla se la stringa inizia con *
                String searchinString = "";
                boolean precisionSearch;

                if (positionText.charAt(0) == '*') {
                    precisionSearch = true;
                    int start = 1;

                    if (positionText.charAt(1) == ' ') {
                        start = 2;
                    }
                    searchinString = positionText.substring(start, positionText.length());

                    Log.d("Escaping", searchinString);
                } else {
                    precisionSearch = false;
                    searchinString = positionText;
                }

                ArrayList returnValues = new ArrayList();
                returnValues.add(precisionSearch);
                returnValues.add(searchinString);

                return returnValues;
            }

            @Override
            public void onClick(View view) {
                // prende il contenuto delle EditTExt
                String ssidText = SSIDEditText.getText().toString();
                String positionText = PositionEditText.getText().toString();

                if ((positionText == null || positionText.equals("")) && (ssidText == null || ssidText.equals(""))) { // prende tutti i dati dal database
                    reti = manager.query();

                    adapter.setReti(reti);
                } else if (positionText == null || positionText.equals("")) { // cerca solo per SSID
                    // effettuo la query e aggiorno la ListView
                    reti = manager.search(ssidText);

                    adapter.setReti(reti);

                } else if (ssidText == null || ssidText.equals("")) {   // cerca solo per Posizione
                    // controlla se si tratta di una ricerca precisa e setta i parametri
                    ArrayList datiRicerca = checkPrecisionSearch(positionText);

                    boolean isPrecisionSearch = (boolean) datiRicerca.get(0);
                    String searchinString = (String) datiRicerca.get(1);

                    // effettua la conversione da Stringa a Coordinate e aggiorna la ListView
                    HumanPosition converter = new HumanPosition(contesto);
                    ArrayList<Double> dati = converter.stringToCoord(searchinString);

                    if (dati != null) {
                        Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                        Log.d("CONVERSIONE_TEST", "" + dati.get(1));

                        // ordina tutte le reti dalla più vicina alla più lontana
                        reti = manager.search(dati.get(0), dati.get(1), isPrecisionSearch);

                        adapter.setReti(reti);

                    } else {
                        Toast.makeText(contesto, "Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                    }

                } else {    // cerca sia per Posizione che per SSID
                    Log.d("SEARCH", "ASDASD");

                    // controlla se si tratta di una ricerca precisa e setta i parametri
                    ArrayList datiRicerca = checkPrecisionSearch(positionText);

                    boolean isPrecisionSearch = (boolean) datiRicerca.get(0);
                    String searchinString = (String) datiRicerca.get(1);

                    // effettua la conversione da Stringa a Coordinate e aggiorna la ListView
                    HumanPosition converter = new HumanPosition(contesto);
                    ArrayList<Double> dati = converter.stringToCoord(searchinString);

                    if (dati != null) {
                        Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                        Log.d("CONVERSIONE_TEST", "" + dati.get(1));

                        // cerco nel database le reti più vicine alla posizione data dall'utente
                        reti = manager.search(ssidText, dati.get(0), dati.get(1), isPrecisionSearch);

                        adapter.setReti(reti);
                    } else {
                        Toast.makeText(contesto, "Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                    }
                }

                closeKeyboard(view.getContext());
            }
        });
    }

    private void closeKeyboard(Context context) {
        // chiude la tastiera
        InputMethodManager imm = (InputMethodManager)getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cercaBtn.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

}