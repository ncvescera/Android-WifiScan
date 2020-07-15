package com.example.wifiscan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.AlertBoxes.DeleteReteAlterBox;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.Utils.HumanPosition;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {
    private ListView listView;
    private DBManager manager;
    private Button elimina;
    private WifiCursorAdapter adapter;
    private Cursor cursor;
    private Activity contesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        this.contesto = this;

        elimina = findViewById(R.id.elimina_db);
        listView = (ListView) findViewById(R.id.cursor_listview);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Cursor tmp = (Cursor) listView.getItemAtPosition(pos);
                String ssid = tmp.getString(cursor.getColumnIndex(DBStrings.FIELD_SSID));

                Log.d("LONG_CLICK_TEST", ssid);

                new DeleteReteAlterBox(contesto, ssid, adapter);

                return true;
            }
        });

        manager = new DBManager(getApplicationContext());

        cursor = manager.query();

        adapter = new WifiCursorAdapter(this, cursor, 0);
        listView.setAdapter(adapter);

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // aggiungere alertbox
                manager.deleteAllDataTable(DBStrings.TBL_NAME);
                listView.setAdapter(null);

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.db_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Find by name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.d("Query_test", s);
                cursor = manager.search(s);
                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();

                HumanPosition converter = new HumanPosition(contesto);
                ArrayList<Double> dati = converter.stringToCoord(s);
                if(dati != null) {
                    Log.d("CONVERSIONE_TEST", "" + dati.get(0));
                    Log.d("CONVERSIONE_TEST", "" + dati.get(1));
                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
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