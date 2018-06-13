package com.example.osama.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int MOVIE_LOADER_ID = 44;
    private static final int MOVIE_LOADER_ID_2 = 55;
    public String sort_by ;
    GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private    MoviesAdapter mMoviesAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private FavoriteAdapter mFavoriteAdapter;
    private Parcelable mListState;
    /*
                                Loader to fetch json data and get array of images path
                                 */
         private LoaderManager.LoaderCallbacks<String[]> LoadData =new LoaderManager.LoaderCallbacks<String[]>() {

           @Override
           public Loader<String[]> onCreateLoader(int id, final Bundle args) {

               return new AsyncTaskLoader<String[]>(MainActivity.this) {
                  String[] mWeatherData = null;
                   @Override
                   protected void onStartLoading() {
                       if (mWeatherData != null) {
                           deliverResult(mWeatherData);
                       } else {
                           forceLoad();
                       }
                   }

                   @Override
                   public String[] loadInBackground() {
                       String s = args.getString(SEARCH_QUERY_URL_EXTRA);
                       if(s == null || TextUtils.isEmpty(s)){
                           return null;
                       }
                       Utility.setSort_By(s);
                       URL requestUrl = Utility.buildUrl();


                       try {

                           String jsonResponse = Utility
                                   .getResponseFromHttpUrl(requestUrl);

                          String[] pathArray =Utility.getStingArrayOfPostersURL(jsonResponse);

                           return pathArray;


                       } catch (Exception e) {
                           e.printStackTrace();
                           return null;
                       }



                   }

                   @Override
                   public void deliverResult(String[] data) {

                       mWeatherData = data;
                       super.deliverResult(data);
                   }
               };




           }
           @Override
            public void onLoadFinished(Loader<String[]> loader, String[] data) {
                  if (data != null) {
                      mMoviesAdapter.setMoviesPosters(data);
                      mLayoutManager.onRestoreInstanceState(mListState);
                  }
           }

           @Override
           public void onLoaderReset(Loader<String[]> loader) {

           }
    };
    /*
    Loader to get image path from database in case of favorite movies
     */
    private LoaderManager.LoaderCallbacks<Cursor> LoadFavoriteData =new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            switch(id){
                case MOVIE_LOADER_ID:
                    String sortOrder = MovieContract.MovieEntry._ID + " ASC";
                    return new CursorLoader(MainActivity.this,
                            MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            sortOrder);


                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
            }

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mFavoriteAdapter.swapCursor(data);

            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

            mRecyclerView.smoothScrollToPosition(mPosition);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mFavoriteAdapter.swapCursor(null);

        }
    };
    /*implement MoviesOnClickHandler to make intent to details Activity*/
    private  MoviesAdapter.MoviesOnClickHandler moviesOnClickHandler = new MoviesAdapter.MoviesOnClickHandler() {
        @Override
        public void onListItemClck(int position) {
            Context context = MainActivity.this;
            Class dClass =  DetailsActivity.class;
            Intent startDetailsActivityIntent = new Intent(context,dClass);
            startDetailsActivityIntent.putExtra(Intent.EXTRA_INDEX,position);
            startActivity(startDetailsActivityIntent);
        }
    };
    /*implement FavoriteOnClickHandler to make intent to details Activity*/
    private FavoriteAdapter.FavoriteOnClickHandler favoriteOnClickHandler = new FavoriteAdapter.FavoriteOnClickHandler() {
        @Override
        public void onListItemClck(String title, String date, String image, String overview, String voteAverage) {
            Context context = MainActivity.this;
            Class detailClass =  DetailsActivity.class;
            Intent startDetailsIntent = new Intent(context,detailClass);
            startDetailsIntent.addCategory(Intent.EXTRA_CHOOSER_TARGETS);
            startDetailsIntent.putExtra(Intent.EXTRA_TITLE,title);
            startDetailsIntent.putExtra(Intent.EXTRA_SUBJECT,date);
            startDetailsIntent.putExtra(Intent.EXTRA_PROCESS_TEXT,image);
            startDetailsIntent.putExtra(Intent.EXTRA_TEXT,overview);
            startDetailsIntent.putExtra(Intent.EXTRA_RETURN_RESULT,voteAverage);

            startActivity(startDetailsIntent);
        }
    };

    /*for count numbers of columns is better for screen size
    * */
   public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable("kk", mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable("kk");
    }


/* for relating menu with activity*/

    /*In this function the activity is created
     * Intialize variables
     * and start the correct loader
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = (RecyclerView) findViewById(R.id.movies_list);
        mRecyclerView.setHasFixedSize(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_popular));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
         mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (sort_by != getString(R.string.pref_favorite)) {


            mMoviesAdapter = new MoviesAdapter(this, moviesOnClickHandler);

            mRecyclerView.setAdapter(mMoviesAdapter);
            Bundle queryBundle = new Bundle();
            queryBundle.putString(SEARCH_QUERY_URL_EXTRA, sort_by);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER_ID_2);
            if (loader == null) {
                loaderManager.initLoader(MOVIE_LOADER_ID_2, queryBundle, LoadData);
            }
            loaderManager.restartLoader(MOVIE_LOADER_ID_2, queryBundle, LoadData);

        } else {
            mFavoriteAdapter =new FavoriteAdapter(this,favoriteOnClickHandler);

            mRecyclerView.setAdapter(mFavoriteAdapter);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER_ID);
            if (loader == null) {
                loaderManager.initLoader(MOVIE_LOADER_ID, null, LoadFavoriteData);
            }
            loaderManager.restartLoader(MOVIE_LOADER_ID, null, LoadFavoriteData);
           }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies, menu);
        return true;
    }

    /* for handlind menu item selection*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.pref_sort_key))){
           sort_by = sharedPreferences.getString(getString(R.string.pref_sort_key),getString(R.string.pref_popular));
        }
    }



}


