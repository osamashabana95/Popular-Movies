package com.example.osama.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.osama.popularmovies.database.MovieContract;

/**
 * Created by osama on 3/29/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "moviesDb.db";


    private static final int VERSION = 2;



    MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /*
    to create database
    */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE "  + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieContract.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_DATE    + " TEXT NOT NULL,"+
                MovieContract.MovieEntry.COLUMN_IMAGEPATH + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_VOTEAVERAGE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IDD + " TEXT NOT NULL); ";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
