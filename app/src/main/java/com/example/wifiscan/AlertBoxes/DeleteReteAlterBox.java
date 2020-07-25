package com.example.wifiscan.AlertBoxes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;

import com.example.wifiscan.Adapters.WifiCursorAdapter;
import com.example.wifiscan.DBManager.DBManager;

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


        // AlertBox init
        builder = new AlertDialog.Builder(this.context);

        // adding elems to AlertBox
        builder.setTitle("Elimina Rete");
        builder.setMessage("Sei sicuro di voler eliminare la rete ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete the network
                dbManager.deleteRete(wifiName);

                // getting all data from db to update the cursor
                Cursor cursor = dbManager.query();
                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_EXIT", "Annulla");
            }
        });

        // creating the AlertBox
        box = builder.create();

        // edit buttons color
        // have to use onShowListener because buttons don't exists until show() method is called
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
