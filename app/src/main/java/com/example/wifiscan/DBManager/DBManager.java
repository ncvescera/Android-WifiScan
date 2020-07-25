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

        // select all data from database
        try {
            // getting db instance
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            // executing query
            crs = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME, null);
        } catch(SQLiteException sqle) {
            return null;
        }

        return crs;
    }

    public Cursor search(String s){
        Cursor cursor = null;

        // search by SSID field
        try{
            // getting db instance
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME + " WHERE " + DBStrings.FIELD_SSID + " LIKE '%"+s +"%'",null);
        } catch (SQLiteException e){
            return null;
        }
        return cursor;
    }

    public Cursor search(Double lat, Double lon){
        Cursor cursor = null;

        // getting nearest data from given position
        try{
            // getting db instance
            SQLiteDatabase db=dbhelper.getReadableDatabase();

            cursor = db.rawQuery("SELECT ROWID as _id, * FROM " + DBStrings.TBL_NAME + " ORDER BY " + "((" + DBStrings.FIELD_Latitude + "-" + lat + ")*("+DBStrings.FIELD_Latitude + "-" + lat + ") + (" + DBStrings.FIELD_Longitude + "-" + lon + ")*(" + DBStrings.FIELD_Longitude + "-" + lon + ")) ASC",null);

        } catch (SQLiteException e){
            return null;
        }

        return cursor;
    }

    public void update(String campo, String data){
        // getting db instance
        SQLiteDatabase db = dbhelper.getWritableDatabase();

        // edit wifi password
        try{
            Log.d("Query", "UPDATE " + DBStrings.TBL_NAME + " SET " + DBStrings.FIELD_Password + " = " + data + " WHERE " + DBStrings.FIELD_SSID + " = " + "'" + campo + "'");

            db.execSQL("UPDATE " + DBStrings.TBL_NAME + " SET " + DBStrings.FIELD_Password + " = " + "'" + data + "'" + " WHERE " + DBStrings.FIELD_SSID + " = " + "'" + campo + "'");
        } catch(SQLiteException exec){
            return;
        }
    }
}
