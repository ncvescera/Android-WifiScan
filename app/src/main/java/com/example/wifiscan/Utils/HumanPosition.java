package com.example.wifiscan.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HumanPosition {
    private Context context;
    private Geocoder geoCoder;
    private StringBuilder builder;

    private String position;

    public HumanPosition(Context context) {
        this.context = context;

        // init. the thing that will commute coordinate in to a human readable string
        geoCoder = new Geocoder(this.context, Locale.getDefault()); //it is Geocoder
        builder = new StringBuilder();
    }

    public String coordToString(Double lat, Double lon) {
        // trying the conversion, if error occur the string will be ""
        try {
            // usually address List has only an element but I have to do all this thing because it could have more than 1 elem
            // not really sure
            List<Address> address = geoCoder.getFromLocation(lat, lon, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i <= maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            this.position = builder.toString(); // return string

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
        // init result ArrayList
        ArrayList<Double> result = new ArrayList<Double>();

        try {
            // trying to do the conversion
            List<Address> address = geoCoder.getFromLocationName(nome, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();

            Log.d("TEST", ""+maxLines);

            // adding latitude and longitude to the array
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
