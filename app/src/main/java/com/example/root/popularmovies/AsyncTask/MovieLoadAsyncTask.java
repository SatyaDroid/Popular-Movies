package com.example.root.popularmovies.AsyncTask;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceFragment;

import com.example.root.popularmovies.Constants;
import com.example.root.popularmovies.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/11/16.
 */

public class MovieLoadAsyncTask extends AsyncTask<String, Void, List<Movie>> {

    private HttpURLConnection mHttpURLConnection;
    private URL mURL;
    private BufferedReader mBufferedReader;
    private String movieResultString;
    private MovieLoadListener mMovieLoadListener;
    private boolean isErrorOccured = false;

    @Override
    protected void onPreExecute() {
        mMovieLoadListener.onStart();
    }

    @Override
    protected List<Movie> doInBackground(String... strings) {
        String mFilterOption = strings[0];
        Uri mUri = Uri.parse(Constants.BASE_URL + mFilterOption).buildUpon()
                .appendQueryParameter(Constants.API_KEY_TEXT, Constants.API_KEY_V3).build();

        try {
            mURL = new URL(mUri.toString());
            mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
            InputStream mInputStream = mHttpURLConnection.getInputStream();
            if (mInputStream != null) {
                mBufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
                StringBuilder mStringBuilder = new StringBuilder();
                String line = null;
                if ((line = mBufferedReader.readLine()) != null) {
                    mStringBuilder.append(line + " /n");
                }
                if (mStringBuilder.length() == 0) {
                    return null;
                }
                movieResultString = mStringBuilder.toString();
            }
        } catch (MalformedURLException e) {
            isErrorOccured = true;
            e.printStackTrace();
        } catch (IOException e) {
            isErrorOccured = true;
            e.printStackTrace();
        }

        return (movieResultString != null) ? parseStringToList(movieResultString) : null;
    }


    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (!isErrorOccured) {
            mMovieLoadListener.onFinish(movies);
            super.onPostExecute(movies);
        } else {
            mMovieLoadListener.onErrorOccured();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public List<Movie> parseStringToList(String movieResult) {
        List<Movie> movieList = new ArrayList<Movie>();
        try {
            JSONObject jsonObject = new JSONObject(movieResult);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                Movie movie = parseToMovie((JSONObject) jsonArray.get(i));
                if (movie != null) {
                    movieList.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieList;
    }

    public Movie parseToMovie(JSONObject obj) {

        Movie movie = new Movie();
        try {
            movie.adult = obj.getBoolean("adult");
            movie.video = obj.getBoolean("video");
            movie.poster_path = obj.getString("poster_path");
            movie.overview = obj.getString("overview");
            movie.release_date = obj.getString("release_date");
            movie.original_title = obj.getString("original_title");
            movie.original_language = obj.getString("original_language");
            movie.backdrop_path = obj.getString("backdrop_path");
            movie.title = obj.getString("title");
            movie.popularity = obj.getLong("popularity");
            movie.vote_count = obj.getInt("vote_count");
            movie.vote_average = obj.getLong("vote_average");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }

    public interface MovieLoadListener {
        void onFinish(List<Movie> moviesList);

        void onErrorOccured();

        void onProgress();

        void onStart();
    }

    public void setMovieListener(MovieLoadListener movieListener) {
        mMovieLoadListener = movieListener;
    }
}
