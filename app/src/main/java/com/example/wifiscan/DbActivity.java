package com.example.wifiscan;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;

import com.example.wifiscan.DBManager.DBManager;

import java.util.ArrayList;

public class DbActivity extends AppCompatActivity {
    private ListView listView;
    private DBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        listView = (ListView) findViewById(R.id.cursor_listview);
        manager = new DBManager(getApplicationContext());

        Cursor c = manager.query();

        ArrayList<Rete> dati = manager.cursorToArray(c);

        WifiCursorAdapter adapter = new WifiCursorAdapter(getApplicationContext(), R.layout.layout_cursoradapter, dati);
        listView.setAdapter(adapter);
    }
}