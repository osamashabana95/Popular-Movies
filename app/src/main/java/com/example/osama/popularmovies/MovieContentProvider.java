package com.example.osama.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.osama.popularmovies.MovieContract.MovieEntry.TABLE_NAME;

/**
 * Created by osama on 3/29/2018.
 */
/*
* class to implement special content provider to app
* */
public class MovieContentProvider extends ContentProvider {


    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mMovieDBHelper;

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher =new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES,MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_MOVIES +"/#",MOVIES_WITH_ID);
        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);
        return true;
    }



    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db= mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match){
            case MOVIES:
                retCursor = db.query(TABLE_NAME,null,s,null,null,null, s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db= mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME,null,contentValues);
                if(id>0){
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,id);
                }else {
                    throw new android.database.SQLException("Faild to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int res;
        switch (match){
            case MOVIES:
                res =  db.delete
                        (TABLE_NAME,
                                s,
                                strings
                        );
                break;

            case MOVIES_WITH_ID:
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as you would normally, passing in the selection/args
                res =  db.delete
                        (TABLE_NAME,
                                mSelection,
                                mSelectionArgs
                        );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return res;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
