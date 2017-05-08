package com.example.root.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Common.RetrofitAPIBuilder;
import com.example.root.popularmovies.Contract.MovieContract;
import com.example.root.popularmovies.Model.APIResponse;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.Model.MovieDetail;
import com.example.root.popularmovies.Model.Review;
import com.example.root.popularmovies.Model.Video;
import com.example.root.popularmovies.Services.MovieLoadService;
import com.example.root.popularmovies.views.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetailActivity extends AppCompatActivity {

    Movie mMovie;
    MovieDetailFragment mMovieDetailFragment;
    LayoutInflater mLayoutInflater;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent() != null) {
            mMovie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_OBJ);
        }

        intializeViews();
        setUpActionBar();
        mMovieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MOVIE_OBJ, mMovie);
        mMovieDetailFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, mMovieDetailFragment).disallowAddToBackStack().commit();

    }

    private void intializeViews() {
        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(mMovie.title);
        mActionBar.setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
