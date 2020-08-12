package com.example.wifiscan.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifiscan.DbActivity;
import com.example.wifiscan.R;
import com.example.wifiscan.Utils.AESCrypt;
import com.example.wifiscan.Utils.AlertBoxManager;
import com.example.wifiscan.Utils.HumanPosition;
import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class DatabaseRecyclerViewAdapter extends RecyclerView.Adapter<DatabaseRecyclerViewAdapter.ViewHolder> {
    ArrayList<Rete> reti;

    public DatabaseRecyclerViewAdapter(ArrayList<Rete> reti) {
        setReti(reti);
    }

    public DatabaseRecyclerViewAdapter() {
        reti = new ArrayList<Rete>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dbactivity_recyclerview, parent, false);
        DatabaseRecyclerViewAdapter.ViewHolder holder = new DatabaseRecyclerViewAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Rete tmp = reti.get(position);

        //holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(holder.view.getContext(), R.anim.item_animation_fall_down));

        // updating elements text
        holder.SSID.setText(tmp.getSSID());
        holder.info.setText(tmp.getDettagli());
        holder.level.setText(tmp.getLevel());

        // decripta la password
        try {
            holder.password.setText(AESCrypt.decrypt(tmp.getPassword()));
        } catch (Exception e) {
            holder.password.setText("");
            Log.d("ENCRIPTION", "FAIL: " + e);
        }

        // makes password field clickable
        holder.password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // printing the alert box used to edit the password
                AlertBoxManager.displayUpdatePasswordAlertBox((Activity) view.getContext() , holder.password, holder.SSID.getText().toString());
            }
        });

        // getting lat & lon
        Double lat = tmp.getLat();
        Double lon = tmp.getLon();

        // commute coordinates into human readable string
        HumanPosition converter = new HumanPosition(holder.view.getContext());

        String positionText = ""; // variabile che verrà usata per cambiare il testo della TextView position
        String readablePosition = converter.coordToString(lat, lon);    // variabile temporanea per capire se le coordinate sono state convertite

        // controlla se le coordinate sono state covertite
        if (!readablePosition.equals("")) {
            positionText = readablePosition;
        } else {
            positionText = ""+ lat + ", " + lon;
        }

        holder.position.setText(positionText);
    }

    @Override
    public int getItemCount() {
        return reti.size();
    }

    public void setReti(ArrayList<Rete> reti) {
        this.reti = reti;
        notifyDataSetChanged();
        DbActivity.recyclerView.scheduleLayoutAnimation();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView SSID;
        private TextView info;
        private TextView level;
        private TextView password;
        private TextView position;
        private LinearLayout linearLayout;

        private View view;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            view        = itemView;
            //linearLayout = (LinearLayout) itemView.findViewById(R.id.dbRecyclerViewLinearLayout);

            SSID        = (TextView) itemView.findViewById(R.id.cursor_SSID);
            info        = (TextView) itemView.findViewById(R.id.cursor_dettagli);
            level       = (TextView) itemView.findViewById(R.id.cursor_level);
            password    = (TextView) itemView.findViewById(R.id.cursor_password);
            position    = (TextView) itemView.findViewById(R.id.cursor_position);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertBoxManager.displayDeleteReteAlertBox((Activity) view.getContext(), SSID.getText().toString(), reti);

                    return false;
                }
            });
        }


    }
}
