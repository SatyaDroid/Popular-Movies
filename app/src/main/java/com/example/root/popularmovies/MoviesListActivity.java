package com.example.root.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.views.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity {

    EmptyRecyclerView mEmptyRecyclerView;
    GridLayoutManager mLayoutManager;
    ActionBar mActionBar;
    MoviesListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MovieLoadAsyncTask mMovieLoadAsyncTask;
    View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        intializeViews();
        addListeners();
        setUpActionBar();
        setUpRecyclerView();
        loadMoviesList(false);
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

    private void intializeViews() {
        mEmptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.movie_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mProgressView = findViewById(R.id.progress_view);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getResources().getString(R.string.latest_movies_text));
    }

    private void addListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoviesList(true);
            }
        });
    }

    private void setUpRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mEmptyRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void loadMoviesList(final boolean isFromRefresh) {
        if (mMovieLoadAsyncTask != null) {
            mMovieLoadAsyncTask.cancel(true);
        }
        mMovieLoadAsyncTask = new MovieLoadAsyncTask();
        mMovieLoadAsyncTask.setMovieListener(new MovieLoadAsyncTask.MovieLoadListener() {

            @Override
            public void onFinish(List<Movie> moviesList) {
                if (isFromRefresh) {
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    showLoader(false);
                }
                refreshAdapter(moviesList);
            }

            @Override
            public void onErrorOccured() {
                showLoader(false);
            }

            @Override
            public void onProgress() {

            }

            @Override
            public void onStart() {
                if (!isFromRefresh) {
                    showLoader(true);
                }
            }
        });
        mMovieLoadAsyncTask.execute("popular");

    }

    private void refreshAdapter(List<Movie> list) {
        adapter = new MoviesListAdapter(list);
        mEmptyRecyclerView.setAdapter(adapter);
    }

    public class MoviesListAdapter extends RecyclerView.Adapter<ViewHolder> {
        ArrayList<Movie> mMoviesList;

        public MoviesListAdapter(List<Movie> list) {
            mMoviesList = (ArrayList<Movie>) list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Movie mMovie = mMoviesList.get(position);
            Picasso.with(holder.mMovieImage.getContext()).load(Constants.IMAGE_BASE_URL + "w185/" + mMovie.poster_path).into(holder.mMovieImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MoviesListActivity.this, MovieDetailActivity.class);
                    intent.putExtra(Constants.MOVIE_OBJ, mMovie);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return (mMoviesList != null) ? mMoviesList.size() : 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView mMovieImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mMovieImage = (ImageView) itemView.findViewById(R.id.movie_image_poster);
        }
    }

    public void showLoader(boolean opt) {
        if(opt) {
            mProgressView.setVisibility(View.VISIBLE);
        }else{
            mProgressView.setVisibility(View.GONE);
        }
    }


}
