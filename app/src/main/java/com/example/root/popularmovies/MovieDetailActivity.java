package com.example.root.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.root.popularmovies.Model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    Movie mMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if(getIntent() != null){
            mMovie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_OBJ);
        }
    }
}
