package com.example.osama.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by osama on 3/29/2018.
 */

public  class MovieContract  {
    public static final  String AUTHORITY ="com.example.osama.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);
    public static final  String PATH_MOVIES ="movies";

    public static final class MovieEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_VOTEAVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_BACKDROP = "backdrop_path";
        public static final String COLUMN_IMAGEPATH = "image_path";
        public static final String COLUMN_IDD = "idd";
    }

}
