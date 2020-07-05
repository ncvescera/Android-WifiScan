package com.example.wifiscan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class WifiAdapter extends ArrayAdapter<Rete> {

    private List<Rete> objects;
    private MainActivity context;

    public WifiAdapter(MainActivity context, int textViewResourceId, List<Rete> objects) {
        super(context, textViewResourceId, objects);

        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_row_rete, null);

        // inizializzo gli elementi della tabella
        TextView SSID       = (TextView) convertView.findViewById(R.id.SSID);
        TextView dettagli   = (TextView) convertView.findViewById(R.id.dettagli);
        TextView level      = (TextView) convertView.findViewById(R.id.level);
        Button pwd          = (Button) convertView.findViewById(R.id.pwd);

        // elemento della lista
        Rete obj = getItem(position);

        // setto il testo di ogni textview al relativo valore
        SSID.setText(obj.getSSID());
        dettagli.setText(obj.getDettagli());
        level.setText(obj.getLevel());

        // clickListener per bottone che modifica la password
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prendo il relativo oggetto del bottone
                Rete tmp = objects.get(position);

                // inizializza l'AlertBox che modificherà la password dell'oggetto
                AlertBox a = new AlertBox(context, tmp);

                //Log.d("BOTTONE_PWD", "STR: " + a.getValue());
            }
        });

        return convertView;
    }

}