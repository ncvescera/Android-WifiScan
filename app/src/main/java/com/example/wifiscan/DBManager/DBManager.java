package com.example.wifiscan.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.wifiscan.Utils.Rete;

import java.util.ArrayList;

public class DBManager {
    private DBHelper dbhelper;

    public DBManager(Context ctx) {
        dbhelper=new DBHelper(ctx);

    }

    public boolean save(String SSID, String tipo, int level, String password, Double lat, Double lon) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(DBStrings.FIELD_SSID, SSID);
        cv.put(DBStrings.FIELD_Tipo, tipo);
        cv.put(DBStrings.FIELD_Db, level);
        cv.put(DBStrings.FIELD_Password, password);
        cv.put(DBStrings.FIELD_Latitude, lat);
        cv.put(DBStrings.FIELD_Longitude, lon);

        try {
            db.insert(DBStrings.TBL_NAME, null, cv);
        } catch (SQLiteException sqle) {
            // Gestione delle eccezioni
            Log.d("INSERT_DATA", "ERROR: SQLEXCEPTION");
            Log.d("INSERT_DATA", sqle.getLocalizedMessage());

            return false;
        }

        return true;
    }

    public void deleteAllDataTable(String tableName) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        db.execSQL("DELETE FROM " + tableName);
    }

    public void deleteRete(String ssid) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM " + DBStrings.TBL_NAME + " WHERE " + DBStrings.FIELD_SSID + " = '" + ssid + "'");
        } catch (SQLiteException e) {
            return;
        }
    }

    public Cursor query() {
        Cursor crs=null;

        // seleziona tutti i dati dal database
        try {
            // prendo l'istanza del database
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            // eseguo la query
            crs = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME, null);
            //crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
        } catch(SQLiteException sqle) {
            return null;
        }

        return crs;
    }

    public Cursor search(String s){
        Cursor cursor = null;

        // cerca per SSID le reti
        try{
            // prendo l'istanza del database
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME + " WHERE " + DBStrings.FIELD_SSID + " LIKE '%"+s +"%'",null);
            //crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
        } catch (SQLiteException e){
            return null;
        }
        return cursor;
    }

    public Cursor search(Double lat, Double lon){
        Cursor cursor = null;

        // cerca le reti più vicine alla posizione data
        try{
            // prendo l'istanza del database
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME + " ORDER BY " + "((" + DBStrings.FIELD_Latitude + "-" + lat + ")*("+DBStrings.FIELD_Latitude + "-" + lat + ") + (" + DBStrings.FIELD_Longitude + "-" + lon + ")*(" + DBStrings.FIELD_Longitude + "-" + lon + ")) ASC",null);
            //crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
        } catch (SQLiteException e){
            return null;
        }

        return cursor;
    }

    public void update(String campo, String data){
        // prendo l'istanza del database
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // modifica la password di una rete
        try{
            Log.d("Query", "UPDATE " + DBStrings.TBL_NAME + " SET " + DBStrings.FIELD_Password + " = " + data + " WHERE " + DBStrings.FIELD_SSID + " = " + "'" + campo + "'");

            db.execSQL("UPDATE " + DBStrings.TBL_NAME + " SET " + DBStrings.FIELD_Password + " = " + "'" + data + "'" + " WHERE " + DBStrings.FIELD_SSID + " = " + "'" + campo + "'");
        } catch(SQLiteException exec){
            return;
        }
    }

    public ArrayList<Rete> cursorToArray(Cursor c) {
        ArrayList<Rete> array = new ArrayList<Rete>();
        String[] colonne = c.getColumnNames();

        String ssid;
        String dettagli;
        String level;
        String password;
        Double lat;
        Double lon;

        while (c.moveToNext()) {
            try {
                ssid = c.getString(c.getColumnIndexOrThrow(DBStrings.FIELD_SSID));
            } catch (IllegalArgumentException e) {
                ssid = "";
            }

            try {
                dettagli = c.getString(c.getColumnIndexOrThrow(DBStrings.FIELD_Tipo));
            } catch (IllegalArgumentException e) {
                dettagli = "";
            }

            try {
                level = c.getString(c.getColumnIndexOrThrow(DBStrings.FIELD_Db));
            } catch (IllegalArgumentException e) {
                level = "";
            }

            try {
                password = c.getString(c.getColumnIndexOrThrow(DBStrings.FIELD_Password));
            } catch (IllegalArgumentException e) {
                password = "";
            }

            try {
                lat = c.getDouble(c.getColumnIndexOrThrow(DBStrings.FIELD_Latitude));
            } catch (IllegalArgumentException e) {
                lat = 0.;
            }

            try {
                lon = c.getDouble(c.getColumnIndexOrThrow(DBStrings.FIELD_Longitude));
            } catch (IllegalArgumentException e) {
                lon = 0.;
            }

            Rete tmp = new Rete(ssid, dettagli, level);
            tmp.setPassword(password);
            tmp.setLat(lat);
            tmp.setLon(lon);

            array.add(tmp);
        }

        return array;
    }
}
