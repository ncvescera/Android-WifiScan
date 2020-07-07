package com.example.wifiscan.DBManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

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
            crs = db.query(DBStrings.TBL_NAME, null, null, null, null, null, null, null);
        } catch(SQLiteException sqle) {
            return null;
        }

        return crs;
    }
}
