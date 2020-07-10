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

    public boolean delete(long id) {
    /*
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            if (db.delete(DBStrings.TBL_NAME, DBStrings.FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
        */
        return true;
    }

    public Cursor query() {
        Cursor crs=null;
        try {
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            //crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
            crs = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME, null);
        } catch(SQLiteException sqle) {
            return null;
        }

        return crs;
    }

    public Cursor search(String s){
        Cursor cursor = null;
        try{
            SQLiteDatabase db=dbhelper.getReadableDatabase();
            //crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
            cursor = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME + " WHERE " + DBStrings.FIELD_SSID + " LIKE '%"+s +"%'",null);
        }catch (SQLiteException e){
            return null;
        }
        return cursor;
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
