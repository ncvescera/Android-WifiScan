package com.example.wifiscan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wifiscan.R;
import com.example.wifiscan.Utils.Rete;

import java.util.List;

public class WifiCursorAdapter extends ArrayAdapter<Rete> {
    private List<Rete> objects;

    public WifiCursorAdapter(Context context, int textViewResourceId, List<Rete> objects) {
        super(context, textViewResourceId, objects);

        //this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_cursoradapter, null);

        TextView SSID       = (TextView) convertView.findViewById(R.id.cursor_SSID);
        TextView dettagli   = (TextView) convertView.findViewById(R.id.cursor_dettagli);
        TextView level      = (TextView) convertView.findViewById(R.id.cursor_level);
        TextView password   = (TextView) convertView.findViewById(R.id.cursor_password);
        TextView lat   = (TextView) convertView.findViewById(R.id.cursor_lat);
        TextView lon   = (TextView) convertView.findViewById(R.id.cursor_lon);

        Rete obj = getItem(position);

        SSID.setText(obj.getSSID());
        dettagli.setText(obj.getDettagli());
        level.setText(obj.getLevel());
        password.setText(obj.getPassword());
        lat.setText(Double.toString(obj.getLat()));
        lon.setText(Double.toString(obj.getLon()));

        return convertView;
    }
}
