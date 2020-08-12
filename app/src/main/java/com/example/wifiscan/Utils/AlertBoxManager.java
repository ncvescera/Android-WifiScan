package com.example.wifiscan.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.DbActivity;
import com.example.wifiscan.R;

import java.util.ArrayList;

public class AlertBoxManager {
    private static void setButtonsColor(final AlertDialog box) {
        // edit buttons color
        // have to use onShowListener because buttons don't exists until show() method is called
        box.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                box.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#9C27B0"));
                box.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#9C27B0"));
            }
        });
    }

    public static void displayEditPasswordAlertBox(Activity context, final Rete obj) {
        // AlertBox init.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.layout_alertbox, null);

        // getting EditText ref
        final EditText text = (EditText) dialog_layout.findViewById(R.id.edittext);

        // adding elems to AlertBox
        builder.setTitle("Password");
        builder.setView(dialog_layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_OK", "OK " + text.getText());

                obj.setPassword(text.getText().toString());
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_EXIT", "Annulla");
            }
        });

        // if a password already exists, populate the EditText text with it
        if(!obj.getPassword().equals("")) {
            text.setText(obj.getPassword());
        }

        // creating the AlertBox
        final AlertDialog box = builder.create();

        // set positive & negative buttons color
        setButtonsColor(box);

        box.show();
    }

    public static void displayUpdatePasswordAlertBox(Activity context, final TextView textView, final String wifiName) {
        final DBManager dbManager = DBManager.getDbInstance(context);

        // AlertBox init.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.layout_alertbox, null);

        // getting EditText ref.
        final EditText text = (EditText) dialog_layout.findViewById(R.id.edittext);

        // adding elems to AlertBoxAlertBox
        builder.setTitle("Password");
        builder.setView(dialog_layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_OK", "OK " + text.getText());

                // trying to encrypt the password
                String password = "";

                try {
                    password = AESCrypt.encrypt(text.getText().toString());
                } catch (Exception e) {
                    password = "";
                    Log.d("ENCRIPTION", "FAIL: " + e);
                }

                // update password on database
                dbManager.update(wifiName, password);

                // edit password on the ListView
                textView.setText(text.getText());
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_EXIT", "Annulla");
            }
        });

        // creating AlertBox
        AlertDialog box = builder.create();
        setButtonsColor(box);
        box.show();
    }

    public static void displayDeleteReteAlertBox(final Activity context, final String wifiName, final ArrayList<Rete> dati) {
        final DBManager dbManager = DBManager.getDbInstance(context);

        // AlertBox init
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // adding elems to AlertBox
        builder.setTitle("Elimina Rete");
        builder.setMessage("Sei sicuro di voler eliminare la rete ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete the network
                dbManager.deleteRete(wifiName);

                // trova l'elemento all'interno dei dati che va eliminato
                // così si può mantenere il set di dati attuale senza dover ricaricare ogni volta la query
                for (Rete elem:dati) {
                    if (elem.getSSID().equals(wifiName)) {
                        dati.remove(elem);
                        break;
                    }
                }

                DbActivity.adapter.setReti(dati);
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_EXIT", "Annulla");
            }
        });

        // creating the AlertBox
        AlertDialog box = builder.create();
        setButtonsColor(box);
        box.show();
    }

    public static void displayDeleteAllDataAlertBox(Activity context, final ArrayList<Rete> dati) {
        final DBManager dbManager = DBManager.getDbInstance(context);

        // AlertBox init
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // adding elems to AlertBox
        builder.setTitle("Elimina Database");
        builder.setMessage("Sei sicuro di voler eliminare tutto il contenuto del database ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete the network
                dbManager.deleteAllDataTable(DBStrings.TBL_NAME);
                dati.clear();
                DbActivity.adapter.setReti(dati);
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_EXIT", "Annulla");
            }
        });

        // creating the AlertBox
        AlertDialog box = builder.create();
        setButtonsColor(box);
        box.show();
    }
}
