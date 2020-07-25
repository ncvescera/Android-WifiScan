package com.example.wifiscan.AlertBoxes;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import android.app.AlertDialog;

import com.example.wifiscan.DBManager.DBManager;
import com.example.wifiscan.R;

public class UpdatePasswordAlertBox {

    private TextView textView;
    private AlertDialog box;
    private AlertDialog.Builder builder;
    private Activity context;
    private DBManager dbManager;
    private String wifiName;


    public UpdatePasswordAlertBox(Activity context, final TextView textView, final String wifiName) {

        this.textView = textView;
        this.dbManager = new DBManager(context);
        this.wifiName = wifiName;


        // AlertBox init.
        builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialog_layout = inflater.inflate(R.layout.dialog_layout, null);

        // getting EditText ref.
        final EditText text = (EditText) dialog_layout.findViewById(R.id.edittext);

        // adding elems to AlertBoxAlertBox
        builder.setTitle("Password");
        builder.setView(dialog_layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("TEST_OK", "OK " + text.getText());

                // update password on database
                dbManager.update(wifiName, text.getText().toString());

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
