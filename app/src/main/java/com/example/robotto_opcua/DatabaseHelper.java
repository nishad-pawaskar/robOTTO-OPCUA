package com.example.robotto_opcua;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String OPCRobotto_DB = "opcRobotto_database.db";

    public DatabaseHelper(Context context) {
        super(context, OPCRobotto_DB, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + OPCRobottoContract.Connections.CONNECTIONS_TABLE + " (" +
                OPCRobottoContract.Connections._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                OPCRobottoContract.Connections.SERVER_NAME + " TEXT NOT NULL, " +
                OPCRobottoContract.Connections.SERVER_URI + " TEXT NOT NULL " +
                //OPCRobottoContract.Connections.SECURITY + "BOOLEAN NOT NULL, " +
                //OPCRobottoContract.Connections.SECURITY_POLICIES + "TEXT NOT NULL" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + OPCRobottoContract.Connections.CONNECTIONS_TABLE);
        onCreate(db);
    }

    //public boolean newconnectiondata(String serverName, String serverUri, Boolean security,
    //                                  String securityPolicies){
    public boolean newconnectiondata(String serverName, String serverUri){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(OPCRobottoContract.Connections.SERVER_NAME, serverName);
        cv.put(OPCRobottoContract.Connections.SERVER_URI, serverUri);
        //cv.put(OPCRobottoContract.Connections.SECURITY, security);
        //cv.put(OPCRobottoContract.Connections.SECURITY_POLICIES, securityPolicies);
        long ncresult =db.insert(
                OPCRobottoContract.Connections.CONNECTIONS_TABLE, null, cv);
        if(ncresult == -1)
            return false;
        else
            return true;
    }

    public Cursor getConnectionList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " +
                OPCRobottoContract.Connections.CONNECTIONS_TABLE, null);
        return data;
    }
}
