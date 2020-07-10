package com.example.wifiscan.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.HumanPosition;
import com.example.wifiscan.Utils.Rete;

import java.util.List;

public class WifiCursorAdapter extends CursorAdapter {
    private Context context;

    public WifiCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);

        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.layout_cursoradapter, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView SSID       = (TextView) view.findViewById(R.id.cursor_SSID);
        TextView dettagli   = (TextView) view.findViewById(R.id.cursor_dettagli);
        TextView level      = (TextView) view.findViewById(R.id.cursor_level);
        TextView password   = (TextView) view.findViewById(R.id.cursor_password);
        TextView position   = (TextView) view.findViewById(R.id.cursor_position);

        //TextView lat   = (TextView) view.findViewById(R.id.cursor_lat);
        //TextView lon   = (TextView) view.findViewById(R.id.cursor_lon);


        SSID.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_SSID)));
        dettagli.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Tipo)));
        level.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Db)));
        password.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Password)));

        /*password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabilita i bottoni
                btn.setEnabled(false);
                buttonScan.setEnabled(false);

                // elimina le righie vecchie per evitare di poter far casino con i bottoni
                listView.setAdapter(null);

                // avvia la scnazione del wifi
                gestore.scanWifi();
            }
        });*/

        Double lat = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Latitude));
        Double lon = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Longitude));

        HumanPosition converter = new HumanPosition(context, lat, lon);
        position.setText(converter.getPosition());

    }
}
