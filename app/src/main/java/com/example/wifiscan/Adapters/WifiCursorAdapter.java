package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wifiscan.AlertBoxes.UpdatePasswordAlertBox;
import com.example.wifiscan.DBManager.DBStrings;
import com.example.wifiscan.DbActivity;
import com.example.wifiscan.MainActivity;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.HumanPosition;
import com.example.wifiscan.Utils.Rete;

import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

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
        final TextView SSID       = (TextView) view.findViewById(R.id.cursor_SSID);
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

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Log.d("Click_test", password.getText().toString());
                TextView tmp = view.findViewById(R.id.cursor_password);
                 new UpdatePasswordAlertBox(activityContext , tmp, SSID.getText().toString());
                //UpdatePasswordAlertBox alertBox = new UpdatePasswordAlertBox(view.getContext().getApplicationContext() , tmp);
                //AlertDialog.Builder builder = new AlertDialog.Builder();
                /*AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
                alertDialog.setTitle("hi");
                alertDialog.setMessage("this is my app");

                alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }


                });

                alertDialog.show();  //<-- See This!*/

            }
        });


        Double lat = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Latitude));
        Double lon = cursor.getDouble(cursor.getColumnIndex(DBStrings.FIELD_Longitude));

        HumanPosition converter = new HumanPosition(context, lat, lon);
        position.setText(converter.getPosition());

    }
}
