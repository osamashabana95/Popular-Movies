package com.example.osama.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerOnClickHandler {


    LinearLayoutManager layoutManager;
    LinearLayoutManager layoutManager2;
            private ImageView imageView;;
    private TextView title;
private TextView overview__;
    private TextView r_date;
    private TextView vote;
    private TextView trailer_title;
private TextView review_title;
    private RecyclerView rRecyclerView;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView tRecyclerView;
    private  TrailerAdapter mTrailerAdapter;
    private ImageButton favorite;
    private String movie_id;
    private String imagePath;
    private String backdrop_path;
    private String originaltitle;
    private String relaeseDate;
    private String overview;
    private String vote_average;
    private Parcelable tListState;
    private Parcelable rListState;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", originaltitle);
        outState.putString("releasedate", relaeseDate);
        outState.putString("overview", originaltitle);
        outState.putString("voteaverage", vote_average);
        tListState = layoutManager2.onSaveInstanceState();
        outState.putParcelable("tt", tListState);
        rListState = layoutManager.onSaveInstanceState();
        outState.putParcelable("rr", rListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
        originaltitle = savedInstanceState.getString("title");
        relaeseDate = savedInstanceState.getString("releasedate");
        overview = savedInstanceState.getString("overview");
        vote_average = savedInstanceState.getString("voteaverage");
        tListState = savedInstanceState.getParcelable("tt");
        rListState=savedInstanceState.getParcelable("rr");}

    }
    /*In this function the activity is created
     * Intialize variables
      * and get intent and it's data
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        favorite =(ImageButton) findViewById(R.id.button_favorite);
        rRecyclerView =(RecyclerView) findViewById(R.id.review_list);
        imageView =(ImageView) findViewById(R.id.imageView);
        title = (TextView)findViewById(R.id.originalTitle_text);
        overview__=(TextView)findViewById(R.id.overview_text);
        trailer_title=(TextView)findViewById(R.id.textView6);
        review_title=(TextView)findViewById(R.id.textView8);
        r_date=(TextView)findViewById(R.id.releaseDate_text);
        vote= (TextView)findViewById(R.id.voteAverage_text);


        rRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rRecyclerView.setLayoutManager(layoutManager);
        mReviewAdapter=new ReviewAdapter();
        rRecyclerView.setAdapter(mReviewAdapter);
        tRecyclerView =(RecyclerView) findViewById(R.id.trailer_list);
        tRecyclerView.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(this);
        tRecyclerView.setLayoutManager(layoutManager2);
        mTrailerAdapter=new TrailerAdapter(this);
        tRecyclerView.setAdapter(mTrailerAdapter);



        Intent intent = getIntent();
        if(intent.hasCategory(Intent.EXTRA_CHOOSER_TARGETS)){
             favorite.setVisibility(View.INVISIBLE);
             trailer_title.setVisibility(View.INVISIBLE);
             review_title.setVisibility(View.INVISIBLE);
             rRecyclerView.setVisibility(View.INVISIBLE);
             tRecyclerView.setVisibility(View.INVISIBLE);
            if(intent.hasExtra(Intent.EXTRA_TITLE)){
                String titlee = intent.getStringExtra(Intent.EXTRA_TITLE);
                title.setText(titlee);
            }
            if(intent.hasExtra(Intent.EXTRA_SUBJECT)){
                String date = intent.getStringExtra(Intent.EXTRA_SUBJECT);
                r_date.setText(date);
            }
            if(intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)){
                String image = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
                StringBuilder imageURL = new StringBuilder();
                imageURL.append("http://image.tmdb.org/t/p/");
                imageURL.append("w500");
                imageURL.append("/");
                imageURL.append(image);
                Picasso.with(this)
                        .load(imageURL.toString())
                        .into(imageView);
            }
            if(intent.hasExtra(Intent.EXTRA_TEXT)){
                String overView_Text = intent.getStringExtra(Intent.EXTRA_TEXT);
                overview__.setText(overView_Text);
            }
            if(intent.hasExtra(Intent.EXTRA_RETURN_RESULT)){
                String voteAverage_Text = intent.getStringExtra(Intent.EXTRA_RETURN_RESULT);
                vote.setText(voteAverage_Text);
            }

        }
        else {
            favorite.setVisibility(View.VISIBLE);
            trailer_title.setVisibility(View.VISIBLE);
            review_title.setVisibility(View.VISIBLE);
            rRecyclerView.setVisibility(View.VISIBLE);
            tRecyclerView.setVisibility(View.VISIBLE);
            if (intent.hasExtra(Intent.EXTRA_INDEX)) {
                int position = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
                new DetailsTask().execute(position);
            }
        }
    }


    /* function for fill views with data and extract data from json
    * specify state of image Button
    * */
    public  void display(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
         imagePath=jsonObject.getString("poster_path");
         backdrop_path = jsonObject.optString("backdrop_path");
         originaltitle = jsonObject.optString("original_title");
         relaeseDate = jsonObject.optString("release_date");
        overview = jsonObject.optString("overview");
         vote_average = jsonObject.optString("vote_average");
        movie_id = jsonObject.optString("id");



        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String state=sharedPreferences.getString(movie_id,"false");
        if(state=="false"){

            favorite.setImageResource(R.drawable.heartgray); }
        else{


            favorite.setImageResource(R.drawable.heart);
        }




        title.setText(originaltitle);
        r_date.setText(relaeseDate);
        vote .setText(vote_average);
        overview__.setText(overview);

        StringBuilder imageURL = new StringBuilder();
        imageURL.append("http://image.tmdb.org/t/p/");
        imageURL.append("w500");
        imageURL.append("/");
        imageURL.append(backdrop_path);
        Picasso.with(this)
                .load(imageURL.toString())
                .into(imageView);
        new ReviewAndTrailerTask().execute(movie_id);
    }


/*
* to handle click on trailers and open youtube to open trailer
* */
    @Override
    public void onListItemClck(String s) {
        Uri uri = Uri.parse("http://www.youtube.com/watch?v="+s);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }


    /*
    to handle click in favorite button
    to add or delet data from database
     */
    public void onClick(View view) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String state=sharedPref.getString(movie_id,"false");
        if(state=="false"){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(movie_id, "true");
        editor.commit();
        favorite.setImageResource(R.drawable.heart);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE,originaltitle);
            contentValues.put(MovieContract.MovieEntry.COLUMN_DATE,relaeseDate);
            contentValues.put(MovieContract.MovieEntry.COLUMN_IMAGEPATH,imagePath);
            contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP,backdrop_path);
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW,overview);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE,vote_average);
            contentValues.put(MovieContract.MovieEntry.COLUMN_IDD,movie_id);
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,contentValues);
            if(uri != null){
                Toast.makeText(getBaseContext(),uri.toString(),Toast.LENGTH_LONG).show();
            }

            }
        else{
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(movie_id, "false");
            editor.commit();
            favorite.setImageResource(R.drawable.heartgray);
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            String where = "idd=?";
            String[] args = new String[] { movie_id };
            getContentResolver().delete(uri,where,args);



        }
    }


    /*class for make connection and return response json data*/
    public class DetailsTask extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            if (integers.length == 0) {
                return null;
            }

            int position = integers[0];
            URL requestUrl = Utility.buildUrl();

            try {
                String jsonResponse = Utility
                        .getResponseFromHttpUrl(requestUrl);

                String simpleJsonData = Utility
                        .getMovieDetails( position,jsonResponse);

                return simpleJsonData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                try {
                    display(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
        /*class for make connection and return response json data*/

    public class ReviewAndTrailerTask extends AsyncTask<String,Void,String[]>{

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            String id = strings[0];
            URL requestReviewUrl = Utility.buildReviewAndTrailerUrl(id,"reviews");
            URL requestTrailerUrl = Utility.buildReviewAndTrailerUrl(id,"videos");

            try {
                String[] jsonResponse = new String[2];
                jsonResponse[0] = Utility
                        .getResponseFromHttpUrl(requestReviewUrl);
                jsonResponse[1] = Utility
                        .getResponseFromHttpUrl(requestTrailerUrl);


                return jsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(String[] s) {
            if (s[0] != null&&s[1] != null) {
                try {
                    String[][] reviewArray = Utility.getReviewDetails(s[0]);
                    String[][] trailerArray = Utility.getTrailersDetails(s[1]);
                    mReviewAdapter.setReviews(reviewArray);
                    mTrailerAdapter.setTrailers(trailerArray);
                    layoutManager2.onRestoreInstanceState(tListState);
                    layoutManager.onRestoreInstanceState(rListState);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
