package com.example.root.popularmovies;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.root.popularmovies.AsyncTask.MovieLoadAsyncTask;
import com.example.root.popularmovies.Common.RetrofitAPIBuilder;
import com.example.root.popularmovies.Contract.CompanyContract;
import com.example.root.popularmovies.Contract.GenreContract;
import com.example.root.popularmovies.Contract.MovieContract;
import com.example.root.popularmovies.Model.APIResponse;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.Services.MovieLoadService;
import com.example.root.popularmovies.views.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.root.popularmovies.Contract.MovieContract.MovieEntry.MOVIE_MASTER_COLUMNS;

public class MovieListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIES_LIST = "movies_list";
    EmptyRecyclerView mEmptyRecyclerView;
    GridLayoutManager mLayoutManager;
    MoviesListAdapter adapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MovieLoadAsyncTask mMovieLoadAsyncTask;
    View mProgressView, mEmptyView, mView, mRootView;
    TextView mEmptyViewTitle;
    Button mRetryButton;
    View.OnClickListener mOnClickListener;
    ArrayList<Movie> moviesList = new ArrayList<>();
    MoviesListActivity mActivity;


    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MoviesListActivity) getActivity();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        intializeViews();
        addListeners();
        setUpRecyclerView();
        loadMoviesList(false);
        getAllFavoriteMovies();
        return mRootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void intializeViews() {
        mEmptyRecyclerView = (EmptyRecyclerView) mRootView.findViewById(R.id.movie_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mProgressView = mRootView.findViewById(R.id.progress_view);
        mEmptyView = mRootView.findViewById(R.id.movie_list_empty_view);
        mEmptyViewTitle = (TextView) mEmptyView.findViewById(R.id.empty_title_text_view);
        mRetryButton = (Button) mEmptyView.findViewById(R.id.empty_view_retry_button);
        mView = mRootView.findViewById(R.id.activity_movies_list);
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
                switch (view.getId()) {
                    case R.id.empty_view_retry_button:
                        if (CommonUtils.isNetworAvailable(getActivity())) {
                            loadMoviesList(false);
                        } else {
                            CommonUtils.getNetworkDialog(getActivity()).show();
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
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mEmptyRecyclerView.setLayoutManager(mLayoutManager);
        mEmptyRecyclerView.setEmptyView(mEmptyView);
    }

    private void refreshAdapter(ArrayList<Movie> list) {
        adapter = new MoviesListAdapter(list);
        mEmptyRecyclerView.setAdapter(adapter);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this.getActivity(),
                MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendQueryParameter(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, String.valueOf(1)).build(),
                new String[]{MovieContract.MovieEntry._ID, MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, MovieContract.MovieEntry.COLUMN_TITLE}, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        parseCursorData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }

    public class MoviesListAdapter extends RecyclerView.Adapter<ViewHolder> {
        ArrayList<Movie> mMoviesList;

        MoviesListAdapter(ArrayList<Movie> list) {
            mMoviesList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Movie mMovie = mMoviesList.get(position);
            Picasso.with(holder.mMovieImage.getContext()).load(Constants.IMAGE_BASE_URL + "w185/" + mMovie.poster_path).into(holder.mMovieImage);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mActivity.mTwoPane) {
                        loadFragment(mMovie);
                    } else {
                        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                        intent.putExtra(Constants.MOVIE_OBJ, mMovie);
                        startActivity(intent);
                    }
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

    private void loadMoviesList(final boolean isFromRefresh) {
        if (CommonUtils.isNetworAvailable(getActivity())) {
            mEmptyView.setVisibility(View.GONE);
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            MovieLoadService mMovieLoadService = retrofit.create(MovieLoadService.class);
            Call call = mMovieLoadService.getMoviesList("movie", "popular");
            call.enqueue(new retrofit2.Callback<APIResponse<Movie>>() {
                @Override
                public void onResponse(Call<APIResponse<Movie>> call, Response<APIResponse<Movie>> response) {
                    APIResponse response1 = response.body();
                    showLoader(false);
                    moviesList = (ArrayList<Movie>) response1.results;
//                    insertMovieDetails(moviesList);
                    refreshAdapter(moviesList);
                    if (mActivity.mTwoPane) {
                        loadFragment(moviesList.get(0));
                    }
                    Log.e("response", String.valueOf(response1.results));
                }

                @Override
                public void onFailure(Call<APIResponse<Movie>> call, Throwable t) {
                    Log.e("response", t.toString());
                    showLoader(false);
                    if (isFromRefresh) {
                        if (t != null) {
                            showSnackbar(t.toString());
                        } else {
                            if (!TextUtils.isEmpty(Constants.API_KEY_V3)) {
                                showSnackbar(getResources().getString(R.string.no_network_connection));
                            } else {
                                showSnackbar(getResources().getString(R.string.api_key_not_given));
                            }
                        }
                    } else {
                        showErrorView(true, true, t.toString());
                    }
                }
            });
        } else {
            showLoader(false);
            if (isFromRefresh) {
                showSnackbar(getResources().getString(R.string.no_network_connection));
            } else {
                showErrorView(true, true, getResources().getString(R.string.no_network_connection));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private ArrayList<Movie> parseCursorData(Cursor cursor) {
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Movie movie = new Movie();
                movie.id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                movie.title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        return movieList;
    }

    private void loadFragment(Movie movie) {
        MovieDetailFragment mMovieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MOVIE_OBJ, movie);
        mMovieDetailFragment.setArguments(bundle);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_container, mMovieDetailFragment).addToBackStack(null).commit();
    }

    private void insertMovieDetails(ArrayList<Movie> list) {
        if (list != null && list.size() > 0) {
            long delete = mActivity.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "=?", new String[]{String.valueOf(0)});
            if (delete != -1) {
                long bulkInsert = mActivity.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, parseToContentValues(list));
            }
        }
    }

    private ContentValues[] parseToContentValues(ArrayList<Movie> mMoviesList) {
        ContentValues[] mContentValues = new ContentValues[mMoviesList.size()];
        ArrayList<ContentValues> contentValuesVector = new ArrayList<>();
        for (Movie movie : mMoviesList) {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry._ID, movie.id);
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.title);
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
            values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.original_title);
            values.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.adult);
            values.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.video);
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.vote_average);
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.vote_count);
            values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.backdrop_path);
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.poster_path);
            contentValuesVector.add(values);
        }
        return contentValuesVector.toArray(mContentValues);
    }

    private void getFavoriteMovie() {
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieDetailEntry.CONTENT_URI,
                new String[]{MovieContract.MovieDetailEntry.COLUMN_IMDB_ID, GenreContract.GenreEntry.COLUMN_GENRE_NAME, CompanyContract.CompanyEntry.COLUMN_COMPANY_NAME, MovieContract.MovieDetailEntry.COLUMN_BUDGET},
                null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String a = cursor.getString(cursor.getColumnIndex(GenreContract.GenreEntry.COLUMN_GENRE_NAME));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void getAllFavoriteMovies() {
        Cursor cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(1)).build(), MOVIE_MASTER_COLUMNS,
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + "=?", new String[]{String.valueOf(1)}, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String a = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
