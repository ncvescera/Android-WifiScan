package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.wifiscan.R;
import com.example.wifiscan.Utils.AlertBoxManager;
import com.example.wifiscan.Utils.Rete;

import java.util.List;

public class WifiAdapter extends ArrayAdapter<Rete> {

    private List<Rete> objects;
    private Activity context;

    public WifiAdapter(Activity context, int textViewResourceId, List<Rete> objects) {
        super(context, textViewResourceId, objects);

        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_arrayadapter, null);

        // init row elements
        TextView SSID       = (TextView) convertView.findViewById(R.id.SSID);
        TextView info       = (TextView) convertView.findViewById(R.id.dettagli);
        TextView level      = (TextView) convertView.findViewById(R.id.level);
        Button pwd          = (Button)   convertView.findViewById(R.id.pwd);

        // each list item
        Rete obj = getItem(position);

        // updating row items text
        SSID.setText(obj.getSSID());
        info.setText(obj.getDettagli());
        level.setText(obj.getLevel());

        // adding a onClickListener to the edit password button
        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // getting relative button object
                Rete tmp = objects.get(position);

                // printing the alert box used to getting the new password
                AlertBoxManager.displayEditPasswordAlertBox(context, tmp);
            }
        });

        return convertView;
    }

}