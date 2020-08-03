package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.AlertBoxManager;
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
        // getting row elements refs.
        final TextView SSID       = (TextView) view.findViewById(R.id.cursor_SSID);
        TextView info   = (TextView) view.findViewById(R.id.cursor_dettagli);
        TextView level      = (TextView) view.findViewById(R.id.cursor_level);
        TextView password   = (TextView) view.findViewById(R.id.cursor_password);
        TextView position   = (TextView) view.findViewById(R.id.cursor_position);

        // updating elements text
        SSID.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_SSID)));
        info.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Tipo)));
        level.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Db)));
        password.setText(cursor.getString(cursor.getColumnIndex(DBStrings.FIELD_Password)));

        // makes password field clickable
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getting password item ref.
                TextView tmp = view.findViewById(R.id.cursor_password);

                // printing the alert box used to edit the password
                AlertBoxManager.displayUpdatePasswordAlertBox(activityContext , tmp, SSID.getText().toString());
            }
        });

        // getting lat & lon
        Double lat = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Latitude));
        Double lon = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Longitude));

        // commute coordinates into human readable string
        HumanPosition converter = new HumanPosition(context);
        position.setText(converter.coordToString(lat, lon));

    }
}
