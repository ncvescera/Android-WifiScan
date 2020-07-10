package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;


import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {
    private ListView listView;
    private DBManager manager;
    private Button elimina;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        elimina = findViewById(R.id.elimina_db);
        listView = (ListView) findViewById(R.id.cursor_listview);
        manager = new DBManager(getApplicationContext());

        Cursor c = manager.query();

        ArrayList<Rete> dati = manager.cursorToArray(c);

        WifiCursorAdapter adapter = new WifiCursorAdapter(getApplicationContext(), c, 0);
        listView.setAdapter(adapter);

        elimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }
}