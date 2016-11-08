package com.example.root.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.views.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesListActivity extends AppCompatActivity {

    private static final String MOVIES_LIST = "movies_list";
    EmptyRecyclerView mEmptyRecyclerView;
    GridLayoutManager mLayoutManager;
    MoviesListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MovieLoadAsyncTask mMovieLoadAsyncTask;
    View mProgressView, mEmptyView, mView;
    TextView mEmptyViewTitle;
    Button mRetryButton;
    View.OnClickListener mOnClickListener;
    ArrayList<Movie> moviesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        try {
            intializeViews();
            addListeners();
            setUpActionBar();
            setUpRecyclerView();
            if (savedInstanceState != null) {
                moviesList = (ArrayList<Movie>) savedInstanceState.getSerializable(MOVIES_LIST);
                if (moviesList != null && moviesList.size() > 0) {
                    refreshAdapter(moviesList);
                } else {
                    loadMoviesList(false);
                }
            } else {
                loadMoviesList(false);
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_list_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MOVIES_LIST, moviesList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(MoviesListActivity.this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intializeViews() {
        mEmptyRecyclerView = (EmptyRecyclerView) findViewById(R.id.movie_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mProgressView = findViewById(R.id.progress_view);
        mEmptyView = findViewById(R.id.movie_list_empty_view);
        mEmptyViewTitle = (TextView) mEmptyView.findViewById(R.id.empty_title_text_view);
        mRetryButton = (Button) mEmptyView.findViewById(R.id.empty_view_retry_button);
        mView = findViewById(R.id.activity_movies_list);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.latest_movies_text));
    }

    private void addListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoviesList(true);
            }
        });
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.empty_view_retry_button:
                        if (CommonUtils.isNetworAvailable(MoviesListActivity.this)) {
                            loadMoviesList(false);
                        } else {
                            CommonUtils.getNetworkDialog(MoviesListActivity.this).show();
                        }
                        break;
                    case R.id.progress_view:
                        break;
                }
            }
        };
        mRetryButton.setOnClickListener(mOnClickListener);
        mProgressView.setClickable(true);
        mProgressView.setOnClickListener(mOnClickListener);
    }

    private void setUpRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mEmptyRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyRecyclerView.setEmptyView(mEmptyView);
    }

    private void loadMoviesList(final boolean isFromRefresh) {
        if (CommonUtils.isNetworAvailable(this)) {
            mEmptyView.setVisibility(View.GONE);
            if (mMovieLoadAsyncTask != null) {
                mMovieLoadAsyncTask.cancel(true);
            }
            mMovieLoadAsyncTask = new MovieLoadAsyncTask();
            mMovieLoadAsyncTask.setMovieListener(new MovieLoadAsyncTask.MovieLoadListener() {

                @Override
                public void onFinish(List<Movie> list) {
                    showLoader(false);
                    moviesList = (ArrayList<Movie>) list;
                    refreshAdapter(moviesList);
                }

                @Override
                public void onErrorOccured(String message) {
                    showLoader(false);
                    if (isFromRefresh) {
                        showSnackbar(message);
                    } else {
                        showErrorView(true, true, message);
                    }
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
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String movieType = preferences.getString(getString(R.string.pref_movie_list_type_key), getString(R.string.pref_movie_list_type_default));
            mMovieLoadAsyncTask.execute(movieType);
        } else {
            showLoader(false);
            if (isFromRefresh) {
                showSnackbar(getResources().getString(R.string.no_network_connection));
            } else {
                showErrorView(true, true, getResources().getString(R.string.no_network_connection));
            }
        }

    }

    private void refreshAdapter(ArrayList<Movie> list) {
        adapter = new MoviesListAdapter(list);
        mEmptyRecyclerView.setAdapter(adapter);
    }

    public class MoviesListAdapter extends RecyclerView.Adapter<ViewHolder> {
        ArrayList<Movie> mMoviesList;

        MoviesListAdapter(ArrayList<Movie> list) {
            mMoviesList = list;
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

        ViewHolder(View itemView) {
            super(itemView);
            mMovieImage = (ImageView) itemView.findViewById(R.id.movie_image_poster);
        }
    }

    public void showLoader(boolean opt) {
        if (opt) {
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mProgressView.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void showErrorView(boolean show, boolean isRetryRequired, String message) {
        if (show) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (isRetryRequired) {
                mRetryButton.setVisibility(View.VISIBLE);
            }
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRetryButton.setVisibility(View.GONE);
        }
        if (message != null) {
            mEmptyViewTitle.setText(message);
        } else {
            mEmptyViewTitle.setText(getResources().getString(R.string.no_network_connection));
        }
    }

    private void showSnackbar(String message) {
        Snackbar mSnackbar = Snackbar.make(mView, message, Snackbar.LENGTH_LONG);
        mSnackbar.setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoviesList(true);
            }
        });
        mSnackbar.show();
    }

}
