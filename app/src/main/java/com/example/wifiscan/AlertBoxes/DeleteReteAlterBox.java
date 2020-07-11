package com.example.wifiscan.AlertBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.R;

public class DeleteReteAlterBox {
    private WifiCursorAdapter adapter;
    private AlertDialog box;
    private AlertDialog.Builder builder;
    private Activity context;
    private DBManager dbManager;
    private String wifiName;

    public DeleteReteAlterBox(final Activity context, final String wifiName, final WifiCursorAdapter adapter) {
        this.context = context;

        this.adapter = adapter;
        this.dbManager = new DBManager(context);
        this.wifiName = wifiName;


        // inizializzazione dell'AlertBox
        builder = new AlertDialog.Builder(this.context);


        // aggiunta di elementi all'AlertBox
        builder.setTitle("Elimina Rete");
        builder.setMessage("Sei sicuro di voler eliminare la rete ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbManager.deleteRete(wifiName);
                
                Cursor dati = dbManager.query();
                adapter.changeCursor(dati);
                adapter.notifyDataSetChanged();
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

        // modifica il colore dei bottoni
        // si fa con il metodo onShow perchè i bottoni non esistono fino all'invocazione del metodo show
        box.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                box.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#9C27B0"));
                box.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#9C27B0"));
            }
        });


        box.show();
    }
}
