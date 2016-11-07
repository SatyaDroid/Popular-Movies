package com.example.root.popularmovies;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    Movie mMovie;
    ActionBar mActionBar;
    CollapsingToolbarLayout mCollapsingToolBarLayout;
    ImageView mMovieLogo;
    MovieLoadAsyncTask mMovieLoadAsyncTask;
    TextView mMovieRatingText, mMovieTimeText, mMovieYearText, mMovieDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent() != null) {
            mMovie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_OBJ);
        }

        intializeViews();
        setUpActionBar();
        loadMovieData();
    }

    private void intializeViews() {
        mCollapsingToolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mMovieLogo = (ImageView) findViewById(R.id.logo);
        mMovieRatingText = (TextView) findViewById(R.id.movie_rating_text);
        mMovieTimeText = (TextView) findViewById(R.id.movie_time_text);
        mMovieDescriptionText = (TextView) findViewById(R.id.movie_description_text);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mMovie.title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mCollapsingToolBarLayout.setTitle(mMovie.title);
    }

    private void bindData() {
        Picasso.with(mMovieLogo.getContext()).load(Constants.IMAGE_BASE_URL + "w185/" + mMovie.poster_path).into(mMovieLogo);
    }

    private void loadMovieData() {
        mMovieLoadAsyncTask = new MovieLoadAsyncTask();
        mMovieLoadAsyncTask.setImagesAndVideosRequired(true);
        mMovieLoadAsyncTask.setMovieListener(new MovieLoadAsyncTask.MovieLoadListener() {
            @Override
            public void onFinish(List<Movie> moviesList) {

            }

            @Override
            public void onErrorOccured(String message) {

            }

            @Override
            public void onProgress() {

            }

            @Override
            public void onStart() {

            }
        });
        mMovieLoadAsyncTask.execute(String.valueOf(mMovie.id));
    }
}
