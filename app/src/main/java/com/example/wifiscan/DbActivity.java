package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.Utils.AlertBoxManager;
import com.example.wifiscan.Utils.HumanPosition;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {
    private ListView listView;
    private DBManager manager;
    private ImageButton cercaBtn;
    private WifiCursorAdapter adapter;
    private Cursor cursor;
    private Activity contesto;
    private EditText SSIDEditText;
    private EditText PositionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        this.contesto = this;

        // bottone per eliminare il contenuto del database
        cercaBtn = findViewById(R.id.elimina_db);

        // EditText per la ricerca
        SSIDEditText = findViewById(R.id.searchSSID);
        PositionEditText = findViewById(R.id.searchPosition);

        // listview principale
        listView = (ListView) findViewById(R.id.cursor_listview);

        // inizializzo il dbManager
        manager = DBManager.getDbInstance(getApplicationContext());

        // prendo tutti i dati dal database per inizializzare la listview
        cursor = manager.query();

        // creo l'adapter e lo aggiungo alla listview
        adapter = new WifiCursorAdapter(this, cursor, 0);
        listView.setAdapter(adapter);

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
        cursor.close();
        manager.close();

        super.onDestroy();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteDatabase:
                manager.deleteAllDataTable(DBStrings.TBL_NAME);
                cursor = null;

                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();
                break;
            case R.id.degrado:
                // passa al degrado
                Intent intent = new Intent(DbActivity.this, DegradoActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private void setListeners() {
        /** LISTENER PER LA LISTVIEW listView**/
        // con una pressione continua sulla riga si può eliminare
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Cursor tmp = (Cursor) listView.getItemAtPosition(pos);
                String ssid = tmp.getString(cursor.getColumnIndex(DBStrings.FIELD_SSID));

                Log.d("LONG_CLICK_TEST", ssid);

                AlertBoxManager.displayDeleteReteAlertBox(contesto, ssid, adapter);

                return true;
            }
        });

        /** LISTENER PER IL BOTTONE cercaBtn **/
        // con una lunga pressione del tasto viene eliminato il contenuto delle EditText
        cercaBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SSIDEditText.setText("");
                PositionEditText.setText("");

                cursor = manager.query();

                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();

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
                    cursor = manager.query();

                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();
                } else if (positionText == null || positionText.equals("")) { // cerca solo per SSID
                    // effettuo la query e aggiorno la ListView
                    cursor = manager.search(ssidText);

                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();

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
                        cursor = manager.search(dati.get(0), dati.get(1), isPrecisionSearch);

                        adapter.changeCursor(cursor);
                        adapter.notifyDataSetChanged();
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
                        cursor = manager.search(ssidText, dati.get(0), dati.get(1), isPrecisionSearch);

                        adapter.changeCursor(cursor);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(contesto, "Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}