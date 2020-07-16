package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.wifiscan.AlertBoxes.UpdatePasswordAlertBox;
import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.HumanPosition;


public class WifiCursorAdapter extends CursorAdapter {
    private Activity activityContext;

    public WifiCursorAdapter(Activity context, Cursor c, int flags) {
        super(context, c, flags);

        this.activityContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.layout_cursoradapter, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // prendo i riferimenti ai vari elementi
        final TextView SSID       = (TextView) view.findViewById(R.id.cursor_SSID);
        TextView dettagli   = (TextView) view.findViewById(R.id.cursor_dettagli);
        TextView level      = (TextView) view.findViewById(R.id.cursor_level);
        TextView password   = (TextView) view.findViewById(R.id.cursor_password);
        TextView position   = (TextView) view.findViewById(R.id.cursor_position);

        // aggiorno il testo dei vari elementi
        SSID.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_SSID)));
        dettagli.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Tipo)));
        level.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Db)));
        password.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Password)));

        // rendo il campo password cliccabile per poterla modificare tramite un AlertBox
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // prendo il riferimento del campo password
                TextView tmp = view.findViewById(R.id.cursor_password);

                // mostro l'AlertBox per modificare la password
                new UpdatePasswordAlertBox(activityContext , tmp, SSID.getText().toString());
            }
        });

        // prendo la latitudine e la longitudine
        Double lat = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Latitude));
        Double lon = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Longitude));

        // trasformo le coordinate in una stringa leggibile
        HumanPosition converter = new HumanPosition(context);
        position.setText(converter.coordToString(lat, lon));

    }
}
