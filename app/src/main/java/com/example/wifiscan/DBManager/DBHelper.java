package com.example.wifiscan.DBManager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME="WiFiData";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = "CREATE TABLE " + DBStrings.TBL_NAME +
                " ( "
                + DBStrings.FIELD_SSID + " TEXT NOT NULL PRIMARY KEY,"
                + DBStrings.FIELD_Tipo + " TEXT NOT NULL,"
                + DBStrings.FIELD_Db + " INTEGER NOT NULL,"
                + DBStrings.FIELD_Password + " TEXT,"
                + DBStrings.FIELD_Latitude + " REAL,"
                + DBStrings.FIELD_Longitude + " REAL)";

        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}