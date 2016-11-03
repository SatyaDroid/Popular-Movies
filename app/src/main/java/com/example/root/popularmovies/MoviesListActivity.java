package com.example.root.popularmovies;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.views.EmptyRecyclerView;

public class MoviesListActivity extends AppCompatActivity {

    EmptyRecyclerView mEmptyRecyclerView;
    GridLayoutManager mLayoutManager;
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        intializeViews();
        addListeners();
        setUpActionBar();
        setUpRecyclerView();
        loadMoviesList();
    }

    private void intializeViews() {
        mEmptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.movie_recycler_view);
    }

    private void setUpActionBar() {
        mActionBar = getSupportActionBar();
    }

    private void addListeners() {

    }

    private void setUpRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mEmptyRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void loadMoviesList() {
        MovieLoadAsyncTask movieLoadAsyncTask = new MovieLoadAsyncTask();
        movieLoadAsyncTask.setMovieListener(new MovieLoadAsyncTask.MovieLoadListener() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onErrorOccured() {

            }

            @Override
            public void onProgress() {

            }
        });
        movieLoadAsyncTask.execute("popular");
    }

    public class MoviesListAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
