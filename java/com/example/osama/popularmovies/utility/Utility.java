package com.example.osama.popularmovies.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.osama.popularmovies.BuildConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/*this class for utility functin which app need */
public class Utility {
    public static final String API_KEY = BuildConfig.IMDB_API_KEY;
    final static String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    final static String API_KEY_PARAM = "api_key";
    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE = "w500";
    private static final String PATH_SEPARATOR = "/";
    private static String mSort_By;


    /* to set sorting method for movies*/
    public static void setSort_By (String sort_by){
        mSort_By = sort_by;
    }

    /* to build needed url*/
    public static URL buildUrl() {

        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(mSort_By)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url =null;

        try {

             url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static URL buildReviewAndTrailerUrl(String id,String s) {

        Uri uri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(id)
                .appendPath(s)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url =null;
        try {

            url = new URL(uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*to make stable connection and get response*/
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /* to get array of posters path*/
    public static String[] getStingArrayOfPostersURL (String json  )throws Exception{

        JSONObject jsonObject = new JSONObject(json);

        JSONArray jsonArray = jsonObject.getJSONArray("results");

        String[] list =new String[20];
        for(int i = 0; i < jsonArray.length(); i++){
         JSONObject object = jsonArray.getJSONObject(i);
         String imagePath=object.getString("poster_path");
         StringBuilder imageURL = new StringBuilder();
         imageURL.append(IMAGE_BASE_URL);
         imageURL.append(IMAGE_SIZE);
         imageURL.append(PATH_SEPARATOR);
         imageURL.append(imagePath);
         list[i] = imageURL.toString();
        }
       return list;
    }

    /*to get  string of specific movie json data */
    public static String getMovieDetails (int position , String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        JSONObject object = jsonArray.getJSONObject(position);
        return object.toString();
    }
    public static String[][] getReviewDetails(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        String[][] list = new String[jsonArray.length()][2];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    list[i][j] = object.optString("author");
                } else {
                    list[i][j] = object.optString("content");
                }

            }
        }
        return list;

    }

    /*
    to get trailers details
     */
    public static String[][] getTrailersDetails(String json) throws JSONException {


        JSONObject jsonObject = new JSONObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        String[][] list = new String[jsonArray.length()][2];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    list[i][j] = object.optString("key");
                } else {
                    list[i][j] = object.optString("name");
                }

            }
        }
        return list;


    }

}
