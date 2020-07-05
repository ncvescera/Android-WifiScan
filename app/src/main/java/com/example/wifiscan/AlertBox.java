package com.example.wifiscan;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

public class AlertBox {
    private Context context;
    private AlertDialog box;
    private AlertDialog.Builder builder;
    private Rete obj;

    public AlertBox(MainActivity context, final Rete obj) {
        this.context = context;
        this.obj = obj;

        // inizializzazione dell'AlertBox
        builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.dialog_layout, null);

        // prendo riferimento all'EditText
        final EditText text = (EditText) dialog_layout.findViewById(R.id.edittext);

        // aggiunta di elementi all'AlertBox
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

        // creazione e visualizzazione dell'AlertBox
        box = builder.create();
        box.show();
    }
}
