package com.example.wifiscan.Utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CsvExporter {
    private Activity contesto;
    private ArrayList<Rete> dati;
    private String filename = "database.csv";

    private static final int PERMISSION_REQUEST_CODE = 100;

    public CsvExporter(Activity contesto, ArrayList<Rete> dati) {
        this.contesto = contesto;
        this.dati = dati;
    }

    public void export() {
        String toWrite = "";

        for (Rete elem:dati) {
            String line = elem.getSSID() + ";"+
                    elem.getDettagli() + ";" +
                    elem.getLevel() + ";" +
                    elem.getPassword().replace("\n", "").replace("\r", "") + ";" +
                    elem.getLat() + ";" +
                    elem.getLon();

            toWrite += line + "\n";
        }

        writeToSDFile(toWrite);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(contesto, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(contesto, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(contesto, "Abilita il permesso alla memoria nelle impostazioni !", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(contesto, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void writeToSDFile(String content){
        String enterText = content;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdcard.getAbsolutePath() + "/Download/");
                    dir.mkdirs();
                    File file = new File(dir, filename);
                    FileOutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(enterText.getBytes());
                        os.close();

                        Toast.makeText(contesto, "Il database è stato esportato in Downloand !", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(contesto, "C'è stato un problema :/", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    requestPermission(); // Code for permission
                }
            } else {
                File sdcard = Environment.getExternalStorageDirectory();
                File dir = new File(sdcard.getAbsolutePath() + "/Download/");
                dir.mkdirs();
                File file = new File(dir, filename);
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(file);
                    os.write(enterText.getBytes());
                    os.close();

                    Toast.makeText(contesto, "Il database è stato esportato in Downloand !", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(contesto, "C'è stato un problema :/", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }
}
