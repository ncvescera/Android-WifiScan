package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.MainActivity;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.AlertBoxManager;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class ScanRecyclerViewAdapter extends RecyclerView.Adapter<ScanRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Rete> reti;

    public ScanRecyclerViewAdapter(ArrayList<Rete> reti) {
        setReti(reti);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_mainactivity_recyclerview, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rete tmp = reti.get(position);

        holder.SSID.setText(tmp.getSSID());
        holder.info.setText(tmp.getDettagli());
        holder.level.setText(tmp.getLevel());

        holder.pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertBoxManager.displayEditPasswordAlertBox((Activity)view.getContext(), tmp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reti.size();
    }

    public void setReti(ArrayList<Rete> reti) {
        this.reti = reti;
        notifyDataSetChanged(); // aggiorna la RecyclerView quando i dati sono stati modificati
        MainActivity.recyclerView.scheduleLayoutAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView SSID;
        private TextView info;
        private TextView level;
        private Button pwd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            SSID       = (TextView) itemView.findViewById(R.id.SSID);
            info       = (TextView) itemView.findViewById(R.id.dettagli);
            level      = (TextView) itemView.findViewById(R.id.level);
            pwd        = (Button)   itemView.findViewById(R.id.pwd);

            // per rendere l'elemento cliccabile ed avviare l'animazione del tocco
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
