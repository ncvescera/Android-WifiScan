package com.example.wifiscan.AlertBoxes;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.wifiscan.MainActivity;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.Rete;

public class EditPasswordAlertBox {
    private Context context;
    private AlertDialog box;
    private AlertDialog.Builder builder;
    private Rete obj;

    public EditPasswordAlertBox(MainActivity context, final Rete obj) {
        this.context = context;
        this.obj = obj;

        // AlertBox init.
        builder = new AlertDialog.Builder(this.context);

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View dialog_layout = inflater.inflate(R.layout.dialog_layout, null);

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
