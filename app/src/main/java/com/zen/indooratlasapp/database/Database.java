package com.zen.indooratlasapp.database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IARegion;

import java.util.ArrayList;
import java.util.UUID;


public class Database {
    public static final String TAG = Database.class.getSimpleName();

    public static final String SURVEY_TABLE = "Survey";
    public static final String LOCATION_TABLE = "Location";
    public static final String SURVEY_READING_TABLE = "SurveyReading";

    public static final String ID = "id";
    public static final String SURVEY_ID = "survey_id";
    public static final String SURVEY_NAME = "name";
    public static final String REMARKS = "remarks";
    public static final String CREATE_ON = "created_on";

    public static final String LOCATION_ID = "location_id";
    public static final String LOCATION_NAME = "location_name";

    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String READING = "reading";
    public static final String READING_ON = "reading_on";


    public static final String SQL_CREATE_SURVEY = "create table " + SURVEY_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SURVEY_ID + " TEXT,"
            + SURVEY_NAME + " TEXT,"
            + REMARKS + " TEXT,"
            + CREATE_ON + " TEXT)";

    public static final String SQL_CREATE_LOCATION = "create table " + LOCATION_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SURVEY_ID + " TEXT,"
            + LOCATION_ID + " TEXT,"
            + LOCATION_NAME + " TEXT,"
            + REMARKS + " TEXT)";

    public static final String SQL_CREATE_SURVEY_READING = "create table " + SURVEY_READING_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SURVEY_ID + " TEXT,"
            + LOCATION_ID + " TEXT,"
            + LAT + " LONG,"
            + LNG + " LONG,"
            + READING + " TEXT,"
            + READING_ON + " TEXT)";


    public void addSurvey(Context context) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SURVEY_ID, getUuId());
            contentValues.put(SURVEY_NAME, "");
            contentValues.put(REMARKS, "");
            contentValues.put(CREATE_ON, currentDateTimeInMilli());
            SQLiteDatabase sqLiteDatabase = DBHelper.getDbInstance(context).getWritableDatabase();
            sqLiteDatabase.insert(SURVEY_TABLE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addLocation(Context context, IARegion region) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SURVEY_ID, getUuId());
            contentValues.put(LOCATION_ID, region.getId());
            contentValues.put(LOCATION_NAME, region.getName());
            contentValues.put(REMARKS, "");
            SQLiteDatabase sqLiteDatabase = DBHelper.getDbInstance(context).getWritableDatabase();
            if (isExists(LOCATION_TABLE, region.getId(), sqLiteDatabase)) {
                sqLiteDatabase.update(LOCATION_TABLE, contentValues, LOCATION_ID + " = ?", new String[]{region.getId()});
            } else {
                sqLiteDatabase.insert(LOCATION_TABLE, null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSurveyReading(Context context, IALocation iaLocation, IARegion region) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(SURVEY_ID, getUuId());
            contentValues.put(LOCATION_ID, region.getId());
            contentValues.put(LAT, iaLocation.getLatitude());
            contentValues.put(LNG, iaLocation.getLongitude());
            contentValues.put(READING, "-65");
            contentValues.put(READING_ON, currentDateTimeInMilli());
            SQLiteDatabase sqLiteDatabase = DBHelper.getDbInstance(context).getWritableDatabase();
            long i = sqLiteDatabase.insert(SURVEY_READING_TABLE, null, contentValues);
            Log.e(TAG, "insert = " + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isExists(String tableName, String id, @NonNull SQLiteDatabase db) {
        try {
            //Log.e(TAG, "isExists: " + id);
            String selection = SURVEY_ID + " =?";
            String[] selectionArgs = {id};
            String limit = "1";
            Cursor cursor = db.query(tableName, null, selection, selectionArgs, null, null, null, limit);
            boolean exists = (cursor.getCount() > 0);
            cursor.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUuId() {
        return UUID.randomUUID().toString();
    }

    public String currentDateTimeInMilli() {
        return "" + System.currentTimeMillis();
    }

    @NonNull
    public ArrayList<IALocation> getAllReading(Context context) {
        ArrayList<IALocation> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = DBHelper.getDbInstance(context).getReadableDatabase();
        String query = "select * from " + SURVEY_READING_TABLE;
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    long surveyId = cursor.getLong(cursor.getColumnIndexOrThrow(SURVEY_ID));
                    long locationId = cursor.getLong(cursor.getColumnIndexOrThrow(LOCATION_ID));
                    long lat = cursor.getLong(cursor.getColumnIndexOrThrow(LAT));
                    long lng = cursor.getLong(cursor.getColumnIndexOrThrow(LNG));
                    long reading = cursor.getLong(cursor.getColumnIndexOrThrow(READING));
                    long readingOn = cursor.getLong(cursor.getColumnIndexOrThrow(READING_ON));
                    IALocation location = new IALocation(new IALocation.Builder());
                    location.a.setLatitude(lat);
                    location.a.setLongitude(lng);
                    location.a.setTime(readingOn);
                    arrayList.add(location);
                } while (cursor.moveToNext());
            }
        }
        return arrayList;
    }

}
