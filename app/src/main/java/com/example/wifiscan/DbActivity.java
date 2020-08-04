package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.SearchView;
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

        // inizializzo il dbManager
        manager = DBManager.getDbInstance(getApplicationContext());

        // prendo tutti i dati dal database per inizializzare la listview
        cursor = manager.query();

        // creo l'adapter e lo aggiungo alla listview
        adapter = new WifiCursorAdapter(this, cursor, 0);
        listView.setAdapter(adapter);

        // esegue la ricerca in base al contenuto delle EditText
        cercaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prende il contenuto delle EditTExt
                String ssidText = SSIDEditText.getText().toString();
                String positionText = PositionEditText.getText().toString();

                if ((positionText == null || positionText.equals("")) && (ssidText == null || ssidText.equals(""))) { // controlla se tutte e 2 le EditText sono vuote
                    cursor = manager.query();

                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();
                } else if (positionText == null || positionText.equals("")) { // controlla se la EditText della posizione è vuota
                    // effettuo la query e aggiorno la ListView
                    cursor = manager.search(ssidText);

                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();

                } else if (ssidText == null || ssidText.equals("")) {   // controlla se la EditText del SSID è vuota
                    // effettua la conversione da Stringa a Coordinate e aggiorna la ListView
                    HumanPosition converter = new HumanPosition(contesto);
                    ArrayList<Double> dati = converter.stringToCoord(positionText);

                    if(dati != null) {
                        Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                        Log.d("CONVERSIONE_TEST", "" + dati.get(1));

                        // cerco nel database le reti più vicine alla posizione data dall'utente
                        cursor = manager.search(dati.get(0), dati.get(1));
                        adapter.changeCursor(cursor);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(contesto,"Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("SEARCH", "ASDASD");
                    // effettua la conversione da Stringa a Coordinate e aggiorna la ListView
                    HumanPosition converter = new HumanPosition(contesto);
                    ArrayList<Double> dati = converter.stringToCoord(positionText);

                    if(dati != null) {
                        Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                        Log.d("CONVERSIONE_TEST", "" + dati.get(1));

                        // cerco nel database le reti più vicine alla posizione data dall'utente
                        cursor = manager.search(ssidText, dati.get(0), dati.get(1));
                        adapter.changeCursor(cursor);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(contesto,"Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                    }
                }

                // aggiungere alertbox
                //manager.deleteAllDataTable(DBStrings.TBL_NAME);
                //listView.setAdapter(null);

            }
        });

        // con una lunga pressione del tasto viene eliminato il contenuto delle EditText
        cercaBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SSIDEditText.setText("");
                PositionEditText.setText("");
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // prendo il riferimento del layout del menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.db_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);

        // inizializzo la searchView
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Cerca per posizione");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("Query_test", s);

                // trasformo la stringa in coordinate (latitudine e longitudine)
                HumanPosition converter = new HumanPosition(contesto);
                ArrayList<Double> dati = converter.stringToCoord(s);

                if(dati != null) {
                    Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                    Log.d("CONVERSIONE_TEST", "" + dati.get(1));

                    // cerco nel database le reti più vicine alla posizione data dall'utente
                    cursor = manager.search(dati.get(0), dati.get(1));
                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(contesto,"Posizione inesistente :/", Toast.LENGTH_SHORT).show();
                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                // quando la stringa viene cancellata del tutto faccio vedere tutti i dati del database
                if( s.equals("")){
                    cursor = manager.query();
                    adapter.changeCursor(cursor);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        return true;
    }
}