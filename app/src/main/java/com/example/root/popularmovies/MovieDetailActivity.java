package com.example.root.popularmovies;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    Movie mMovie;
    ActionBar mActionBar;
    ImageView mMovieLogo;
    TextView mMovieRatingText, mMovieReleaseText, mMovieDescriptionText, mMovieTitleText, mMoviePopularityText, mMovieVoteText, mMovieLanguageText,mMovieCertificateText;
    View mOverviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent() != null) {
            mMovie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_OBJ);
        }

        intializeViews();
        setUpActionBar();
        bindData();
    }

    private void intializeViews() {
        mMovieLogo = (ImageView) findViewById(R.id.poster);
        mMovieRatingText = (TextView) findViewById(R.id.movie_rating_text);
        mMovieTitleText = (TextView) findViewById(R.id.movie_title_text);
        mMovieDescriptionText = (TextView) findViewById(R.id.movie_description_text);
        mMovieReleaseText = (TextView) findViewById(R.id.movie_release_text);
        mOverviewLayout = findViewById(R.id.overview_layout);
        mMovieVoteText = (TextView) findViewById(R.id.movie_vote_text);
        mMoviePopularityText = (TextView) findViewById(R.id.movie_popularity_text);
        mMovieLanguageText = (TextView) findViewById(R.id.movie_language_text);
        mMovieCertificateText = (TextView) findViewById(R.id.movie_certificate);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mMovie.title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void bindData() {
        Picasso.with(mMovieLogo.getContext()).load(Constants.IMAGE_BASE_URL + "w185/" + mMovie.poster_path).into(mMovieLogo);
        mMovieRatingText.setText(String.format(getResources().getString(R.string.rating_text), String.valueOf(mMovie.vote_average)));
        mMovieTitleText.setText(mMovie.original_title);
        mMovieDescriptionText.setText(mMovie.overview);
        mMovieReleaseText.setText(mMovie.release_date);
        mMoviePopularityText.setText(String.valueOf(mMovie.popularity));
        mMovieVoteText.setText(String.valueOf(mMovie.vote_count));
        if(getResources().getString(R.string.lang_en).equalsIgnoreCase(mMovie.original_language)){
            mMovieLanguageText.setText(getResources().getString(R.string.english_text));
        }else{
            mMovieLanguageText.setText(getResources().getString(R.string.other));
        }
        if(mMovie.adult){
            mMovieCertificateText.setText(getResources().getString(R.string.adult));
        }else{
            mMovieCertificateText.setText(getResources().getString(R.string.under_adult));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
