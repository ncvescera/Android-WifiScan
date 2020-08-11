package com.example.wifiscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.Handlers.WifiHandler;
import com.example.wifiscan.Utils.AESCrypt;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private WifiHandler wifiHandler;
    private DBManager dbManager;
    private ArrayList<Rete> data;

    public static RecyclerView recyclerView;
    public static Button buttonScan;
    public static Button buttonSave;

    // Wifi Scanning need Wifi and GPS enabled !!!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // db mangare init.
        dbManager = DBManager.getDbInstance(getApplicationContext());

        // ListView init.
        recyclerView = (RecyclerView) findViewById(R.id.view_scan);
        recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation));

        // data init.
        data = new ArrayList<Rete>();

        // getting button ref.
        buttonScan = findViewById(R.id.btn_scan);
        buttonSave = findViewById(R.id.btn_save);

        // wifihandler and locationhandler init.
        wifiHandler = new WifiHandler(MainActivity.this, data);

        // adding onClickListener on the SCAN button
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check GPS permission and it ask the permission to the user (enable ACCESS_FINE_LOCATION permission if disabled)
                int permissionCheck = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1 );
                        return;
                }

                // disable buttons
                buttonSave.setEnabled(false);
                buttonScan.setEnabled(false);

                // delete old listview rows
                // (the user can't mess up data while new scan is starting)
                recyclerView.setAdapter(null);

                // start wifi scanning
                wifiHandler.scanWifi();
            }
        });

        // adding onClickListener on the SAVA DATA button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disable himself
                buttonSave.setEnabled(false);

                Toast.makeText(MainActivity.this,"Salvo i dati ...", Toast.LENGTH_SHORT).show();

                // saving data in the database
                for(Rete elem : data) {
                    Log.d("DATI", elem.toString());
                    String password = "";

                    // trying to encrypt the password
                    try {
                        password = AESCrypt.encrypt(elem.getPassword());
                    } catch (Exception e) {
                        password = "";
                        Log.d("ENCRIPTION", "FAIL: " + e);
                    }

                    boolean result = dbManager.save(elem.getSSID(), elem.getDettagli(), Integer.parseInt(elem.getLevel()), password, elem.getLat(), elem.getLon());
                }
            }
        });
    }

    // counter for the F.C. easter egg
    int fc_counter = 0;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dtab1:    // change activity to DbActivity
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

}