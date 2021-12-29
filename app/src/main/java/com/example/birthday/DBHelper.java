package com.example.birthday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    String LOG_TAG = "Kostia_Birthday";

    public DBHelper(Context context) {
        // конструктор суперкласа
        super(context, "birthday_database_kostia", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Create database");
        // Створив нову таблицю з полями
        db.execSQL("create table birthday_database_kostia ("
                + "id integer primary key autoincrement,"
                + "data text,"
                + "name text,"
                + "more text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Database upgraded");
    }
//ARTEM HERE!
}

