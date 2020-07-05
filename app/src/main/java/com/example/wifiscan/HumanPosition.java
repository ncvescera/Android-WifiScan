package com.example.wifiscan;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

// Classe per convertire delle coordinate (Double) in una Stringa Human-Readable (String)
public class HumanPosition {
    private Double lon;
    private Double lat;
    private Context context;

    private String position;

    public HumanPosition(Context context, Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
        this.context = context;

        // inizializza l'attrezzo che trasformerà le coordinate in una stringa
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault()); //it is Geocoder
        StringBuilder builder = new StringBuilder();

        // prova la conversione, in caso di errore la stringa sarà nulla ("")
        try {
            // tutta sta roba si fa perchè l'indirizzo potrebbe essere troppo lungo e quindi viene diviso in varie righe (?)
            // di solito dentro la lista address c'è solo un elemento
            List<Address> address = geoCoder.getFromLocation(this.lat, this.lon, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i <= maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            this.position = builder.toString(); // sarà la stringa finale

        } catch (IOException e) {
            Log.d("TEST_COORD", "ERRIR:IO");
            this.position = "";
        } catch (NullPointerException e) {
            Log.d("TEST_COORD", "ERRIR:NULLPOINTER");
            this.position = "";
        }
    }

    public String getPosition() {
        return position;
    }
}
