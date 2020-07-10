package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.DBManager.DBStrings;
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

        manager.deleteAllDataTable(DBStrings.TBL_NAME);

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
}