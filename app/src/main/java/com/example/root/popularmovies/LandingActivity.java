package com.example.root.popularmovies;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

import com.example.root.popularmovies.Contract.MovieContract;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.Services.MovieLoadIntentService;
import com.example.root.popularmovies.Services.MovieManiaSyncAdapter;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.example.root.popularmovies.Contract.MovieContract.MovieEntry.MOVIE_MASTER_COLUMNS;

public class LandingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

//        loadMovies();
//        MovieManiaSyncAdapter.syncImmediately(this);

        if (CommonUtils.isNetworAvailable(this)) {
            loadMovies();
        } else {
            loadFavoriteMovies();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LandingActivity.this, MoviesListActivity.class));
                finish();
            }
        }, 5000);
    }

    private void loadMovies() {
        Intent alarmIntent = new Intent(this, MovieLoadIntentService.AlarmReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pi);
        Intent intent = new Intent(LandingActivity.this, MovieLoadIntentService.class);
        startService(intent);
    }

    private void loadFavoriteMovies() {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Intent intent = new Intent(LandingActivity.this, MoviesListActivity.class);
        parseCursorData(data, Movie.class);
        startActivity(new Intent(LandingActivity.this, MoviesListActivity.class));
        finish();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(1)).build(), MOVIE_MASTER_COLUMNS,
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "=?", new String[]{String.valueOf(1)}, null);
    }

    private void parseCursorData(Cursor cursor, Class mClass){
        Field[] fields = mClass.getDeclaredFields();
        String[] columnNames = cursor.getColumnNames();
        ArrayList<Object> objectList = new ArrayList<>();
        do{
            Object obj = null;
            try {
                obj = mClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            for(Field field : fields){
                objectList.add(obj);
            }
        }while(cursor.moveToNext());
    }
}
