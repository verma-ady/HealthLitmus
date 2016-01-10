package com.healthlitmus.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Mukesh on 1/10/2016.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public static String CityTable = "City";
    public static String AreaTable = "Area";
    public static String DatabaseName = "Location";
    public static String CityCol1 = "CityID";
    public static String CityCol2 = "CityName";
    public static String AreaCol1 = "AreaID";
    public static String AreaCol2 = "CityID";
    public static String AreaCol3 = "AreaName";

    public DatabaseManager(Context context) {
        super(context, DatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String executeCity = "create table " + CityTable + " ( " + CityCol1 + " INTEGER PRIMARY KEY, " +
                CityCol2 + " TEXT );";
        Log.v("MyApp", getClass().toString() + " City Table : " + executeCity);
        db.execSQL(executeCity);

        String executeArea = "create table " + AreaTable + " ( " + AreaCol1 + " INTEGER PRIMARY KEY, " +
                AreaCol2 + " INTEGER, " + AreaCol3 + " TEXT );";
        Log.v("MyApp", getClass().toString() + " Area Table : " + executeArea);
        db.execSQL(executeArea);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addCity(int cityID, String cityName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityCol1, cityID);
        contentValues.put(CityCol2, cityName);

        if( db.insert(CityTable, null, contentValues ) == -1 ){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addArea(int areaID, int cityID, String areaName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AreaCol1, areaID);
        contentValues.put(AreaCol2, cityID);
        contentValues.put(AreaCol3, areaName);

        if( db.insert(AreaTable, null, contentValues ) == -1 ){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor showCity(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + CityTable, null );
        return res;
    }

    public Cursor showArea(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + AreaTable, null );
        return res;
    }
}
