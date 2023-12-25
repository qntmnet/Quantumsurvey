package com.zen.indooratlasapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "location.db";
    public static final int DATABASE_VERSION = 1;
    public static final int DATABASE_VERSION_NEW = 2;

    private static DBHelper dbHelper;

    public static DBHelper getDbInstance(Context context) {
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }
        return dbHelper;
    }

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(Database.SQL_CREATE_SURVEY);
        db.execSQL(Database.SQL_CREATE_LOCATION);
        db.execSQL(Database.SQL_CREATE_SURVEY_READING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {

        }
    }

}
