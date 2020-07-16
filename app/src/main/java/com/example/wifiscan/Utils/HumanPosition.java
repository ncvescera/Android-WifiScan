package com.example.wifiscan.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Classe per convertire delle coordinate (Double) in una Stringa Human-Readable (String)
public class HumanPosition {
    private Double lon;
    private Double lat;
    private Context context;
    private Geocoder geoCoder;
    private StringBuilder builder;

    private String position;

    public HumanPosition(Context context) {
        this.lat = lat;
        this.lon = lon;
        this.context = context;

        // inizializza l'attrezzo che trasformerà le coordinate in una stringa
        geoCoder = new Geocoder(context, Locale.getDefault()); //it is Geocoder
        builder = new StringBuilder();
    }

    public String coordToString(Double lat, Double lon) {
        // prova la conversione, in caso di errore la stringa sarà nulla ("")
        try {
            // tutta sta roba si fa perchè l'indirizzo potrebbe essere troppo lungo e quindi viene diviso in varie righe (?)
            // di solito dentro la lista address c'è solo un elemento
            List<Address> address = geoCoder.getFromLocation(lat, lon, 1);

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

        return position;
    }

    public ArrayList<Double> stringToCoord(String nome) {
        // inizializzo l'array dove andranno a finire i dati
        ArrayList<Double> result = new ArrayList<Double>();

        try {
            // prova ad effettuare la conversione
            List<Address> address = geoCoder.getFromLocationName(nome, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();

            Log.d("TEST", ""+maxLines);

            // aggiunge all'array prima la latitudine e poi la longitudine
            result.add(address.get(0).getLatitude());
            result.add(address.get(0).getLongitude());

        } catch (IOException e) {
            Log.d("TEST_COORD", "ERRIR:IO");

            return null;
        } catch (NullPointerException e) {
            Log.d("TEST_COORD", "ERRIR:NULLPOINTER");

            return null;
        } catch (IndexOutOfBoundsException e) {
            Log.d("TEST_COORD", "ERRIR:INDEXOUTBOUND");

            return null;
        }

        return result;
    }

}
