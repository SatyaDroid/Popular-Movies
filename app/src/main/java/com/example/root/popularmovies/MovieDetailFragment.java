package com.example.root.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.root.popularmovies.Common.RetrofitAPIBuilder;
import com.example.root.popularmovies.Contract.CompanyContract;
import com.example.root.popularmovies.Contract.GenreContract;
import com.example.root.popularmovies.Contract.MovieContract;
import com.example.root.popularmovies.Model.APIResponse;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.Model.MovieAttributes;
import com.example.root.popularmovies.Model.MovieDetail;
import com.example.root.popularmovies.Model.Review;
import com.example.root.popularmovies.Model.Video;
import com.example.root.popularmovies.Services.MovieLoadService;
import com.example.root.popularmovies.views.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class MovieDetailFragment extends Fragment {

    Movie mMovie;
    MovieDetail mMovieDetail;
    ActionBar mActionBar;
    ImageView mMovieLogo, mMovieFavorite;
    TextView mMovieRatingText, mMovieReleaseText, mMovieDescriptionText, mMovieTitleText, mMoviePopularityText, mMovieVoteText, mMovieLanguageText, mMovieCertificateText;
    View mOverviewLayout, mReviewsLayout, mTrailersLayout, mRootView;
    View.OnClickListener mOnClickListener;
    LinearLayout mReviewParentView;
    LayoutInflater mLayoutInflater;
    String youTubeBaseUrl = "http://img.youtube.com/vi/[video_id]/mqdefault.jpg";
    String regexPattern = "\\[video_id\\]";
    LinearLayout mTrailerParentView;

    static final int GENRE = 0;
    static final int MOVIE_GENRE = 1;
    static final int COMPANY = 2;
    static final int MOVIE_COMPANY = 3;



    public MovieDetailFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(Constants.MOVIE_OBJ);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        try {
            intializeViews();
            addListeners();
            bindData();
            loadMovieData();
            loadTrailers();
            loadReviews();
        }catch (Exception e){
            Log.e("exception" , e.toString());
        }
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
        mMovieLogo = (ImageView) mRootView.findViewById(R.id.poster);
        mMovieRatingText = (TextView) mRootView.findViewById(R.id.movie_rating_text);
        mMovieTitleText = (TextView) mRootView.findViewById(R.id.movie_title_text);
        mMovieDescriptionText = (TextView) mRootView.findViewById(R.id.movie_description_text);
        mMovieReleaseText = (TextView) mRootView.findViewById(R.id.movie_release_text);
        mOverviewLayout = mRootView.findViewById(R.id.overview_layout);
        mMovieVoteText = (TextView) mRootView.findViewById(R.id.movie_vote_text);
        mMoviePopularityText = (TextView) mRootView.findViewById(R.id.movie_popularity_text);
        mMovieLanguageText = (TextView) mRootView.findViewById(R.id.movie_language_text);
        mMovieCertificateText = (TextView) mRootView.findViewById(R.id.movie_certificate);
        mMovieFavorite = (ImageView) mRootView.findViewById(R.id.movie_favorite);
        mTrailerParentView = (LinearLayout) mRootView.findViewById(R.id.movie_trailers);
        mReviewParentView = (LinearLayout) mRootView.findViewById(R.id.movie_reviews);
        mReviewsLayout = mRootView.findViewById(R.id.review_layout);
        mTrailersLayout = mRootView.findViewById(R.id.trailer_layout);

        mLayoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
    }


    private void addListeners() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.movie_favorite:
                        if (!mMovie.is_favorite) {
                            mMovie.is_favorite = true;
                            mMovieFavorite.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_24dp));
                            addToFavorite();
                        } else {
                            mMovie.is_favorite = false;
                            mMovieFavorite.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_border_black_24dp));
                            removeFromFavorite();
                        }
                        break;
                }
            }
        };
        mMovieFavorite.setOnClickListener(mOnClickListener);
    }

    private void bindData() {
        Picasso.with(mMovieLogo.getContext()).load(Constants.IMAGE_BASE_URL + "w185/" + mMovie.poster_path).into(mMovieLogo);
        mMovieRatingText.setText(String.format(getResources().getString(R.string.rating_text), String.valueOf(mMovie.vote_average)));
        mMovieTitleText.setText(mMovie.original_title);
        mMovieDescriptionText.setText(mMovie.overview);
        mMovieReleaseText.setText(mMovie.release_date);
        mMoviePopularityText.setText(String.valueOf(mMovie.popularity));
        mMovieVoteText.setText(String.valueOf(mMovie.vote_count));
        if(mMovie.is_favorite){
            mMovieFavorite.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_black_24dp));
        }else{
            mMovieFavorite.setImageDrawable(getResources().getDrawable(R.mipmap.ic_favorite_border_black_24dp));
        }
        if (getResources().getString(R.string.lang_en).equalsIgnoreCase(mMovie.original_language)) {
            mMovieLanguageText.setText(getResources().getString(R.string.english_text));
        } else {
            mMovieLanguageText.setText(getResources().getString(R.string.other));
        }
        if (mMovie.adult) {
            mMovieCertificateText.setText(getResources().getString(R.string.adult));
        } else {
            mMovieCertificateText.setText(getResources().getString(R.string.under_adult));
        }
    }

    private void loadMovieData() {
        if (CommonUtils.isNetworAvailable(getActivity())) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            MovieLoadService mMovieLoadService = retrofit.create(MovieLoadService.class);
            Call call = mMovieLoadService.getMovieDetail("movie", String.valueOf(mMovie.id));
            call.enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    Log.e("response", response.body().toString());
                    mMovieDetail = response.body();
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("response", t.toString());
                }
            });
        } else {

        }
    }

    private void addToFavorite() {
        insertMovie();
        insertMovieDetail();
        insertGenres(mMovieDetail.genres);
    }

    private void removeFromFavorite() {
        deleteMovie();
        deleteMovieDetail();
    }

    private void loadReviews() {
        if (CommonUtils.isNetworAvailable(getActivity())) {
            Retrofit retrofit = RetrofitAPIBuilder.getJSONEnabledInstance();
            MovieLoadService mMovieLoadService = retrofit.create(MovieLoadService.class);
            Call call = mMovieLoadService.getMovieReviews("movie", String.valueOf(mMovie.id));
            call.enqueue(new Callback<APIResponse<Review>>() {
                @Override
                public void onResponse(Call<APIResponse<Review>> call, Response<APIResponse<Review>> response) {
                    APIResponse<Review> apiResponse = response.body();
                    updateReviewViews((ArrayList<Review>) apiResponse.results);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("review error---", t.toString());
                }
            });
        } else {

        }
    }

    private void loadTrailers() {
        if (CommonUtils.isNetworAvailable(getActivity())) {
            Retrofit retrofit = RetrofitAPIBuilder.getJSONEnabledInstance();
            MovieLoadService mMovieLoadService = retrofit.create(MovieLoadService.class);
            Call call = mMovieLoadService.getMovieTrailers("movie", String.valueOf(mMovie.id));
            call.enqueue(new Callback<APIResponse<Video>>() {
                @Override
                public void onResponse(Call<APIResponse<Video>> call, Response<APIResponse<Video>> response) {
                    APIResponse<Video> apiResponse = response.body();
                    updateTrailerViews((ArrayList<Video>) apiResponse.results);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("trailer error---", t.toString());
                }
            });
        } else {


        }
    }

    private void updateTrailerViews(final ArrayList<Video> videoList) {
        try {
            mTrailerParentView.removeAllViews();
            if (videoList != null || videoList.size() > 0) {
                for (Video video : videoList) {
                    final View mView = mLayoutInflater.inflate(R.layout.movie_trailer_view, null);
                    final TextView mTextView = (TextView) mView.findViewById(R.id.trailer_text);
                    final ImageView mImageView = (ImageView) mView.findViewById(R.id.trailer_image);
                    final String videoId = video.key;
                    mTextView.setText(video.name);
                    loadYouTubeThumbnail(videoId, mImageView);
                    mTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewTrailer(videoId);
                        }
                    });
                    mTrailerParentView.addView(mView);
                }
            } else {
                mTrailersLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            mTrailersLayout.setVisibility(View.GONE);
        }
    }

    private void updateReviewViews(ArrayList<Review> reviewList) {
        try {
            mReviewParentView.removeAllViews();
            if (reviewList != null) {
                int count = reviewList.size();
                for (final Review review : reviewList) {
                    final View mView = mLayoutInflater.inflate(R.layout.movie_review_view, null);
                    final TextView txtAuthor = (TextView) mView.findViewById(R.id.review_author);
                    final TextView txtReview = (TextView) mView.findViewById(R.id.review_text);
                    final View mDivider = mView.findViewById(R.id.divider);
                    txtAuthor.setText(review.author);
                    txtReview.setText(Html.fromHtml(review.content));
                    if (reviewList.indexOf(review) == (count - 1)) {
                        mDivider.setVisibility(View.GONE);
                    }
                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(review.url));
                            startActivity(intent);
                        }
                    });
                    mReviewParentView.addView(mView);
                }
            } else {
                mReviewsLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            mReviewsLayout.setVisibility(View.GONE);
        }
    }

    private void viewTrailer(String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.youtube.com/watch?v=" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void insertMovie() {
        Uri uri = null;
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, mMovie.id);
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.title);
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.overview);
        values.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, mMovie.original_title);
        values.put(MovieContract.MovieEntry.COLUMN_ADULT, mMovie.adult);
        values.put(MovieContract.MovieEntry.COLUMN_VIDEO, mMovie.video);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.vote_average);
        values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, mMovie.vote_count);
        values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, mMovie.backdrop_path);
        values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.poster_path);
        values.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, mMovie.is_favorite);
        getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
    }

    private void insertMovieDetail() {
        ContentValues movieDetailValues = new ContentValues();
        movieDetailValues.put(MovieContract.MovieDetailEntry._ID, mMovieDetail.id);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_BUDGET, mMovieDetail.budget);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_HOME_PAGE, mMovieDetail.homepage);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_IMDB_ID, mMovieDetail.imdb_id);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_REVENUE, mMovieDetail.revenue);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_RUNTIME, mMovieDetail.runtime);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_STATUS, mMovieDetail.status);
        movieDetailValues.put(MovieContract.MovieDetailEntry.COLUMN_TAGLINE, mMovieDetail.tagline);
        getActivity().getContentResolver().insert(MovieContract.MovieDetailEntry.CONTENT_URI, movieDetailValues);
    }

    public final void loadYouTubeThumbnail(String videoId, ImageView imageView) {
        String url = buildYouTubeUrl(videoId);
        Picasso.with(imageView.getContext()).load(url)
                .into(imageView);
    }

    private String buildYouTubeUrl(String videoId) {
        return youTubeBaseUrl.replaceAll(regexPattern, videoId);
    }

    private ContentValues[] parseToContentValues(List<MovieAttributes> companies, int mode){
        ContentValues[] mContentValues = new ContentValues[companies.size()];
        ArrayList<ContentValues> contentValuesVector = new ArrayList<>();
        for(MovieAttributes movieAttributes : companies){
            ContentValues contentValues = new ContentValues();
            switch (mode){
                case GENRE:
                    contentValues.put(GenreContract.GenreEntry._ID, movieAttributes.id);
                    contentValues.put(GenreContract.GenreEntry.COLUMN_GENRE_NAME, movieAttributes.name);
                    break;
                case MOVIE_GENRE:
                    contentValues.put(GenreContract.MovieGenreEntry.COLUMN_GENRE_ID, movieAttributes.id);
                    contentValues.put(GenreContract.MovieGenreEntry.COLUMN_MOVIE_ID, mMovie.id);
                    break;
                case COMPANY:
                    contentValues.put(CompanyContract.CompanyEntry._ID, movieAttributes.id);
                    contentValues.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_NAME, movieAttributes.name);
                    break;
                case MOVIE_COMPANY:
                    contentValues.put(CompanyContract.MovieCompanyEntry.COLUMN_COMPANY_ID, movieAttributes.id);
                    contentValues.put(CompanyContract.MovieCompanyEntry.COLUMN_MOVIE_ID, mMovie.id);
                    break;
            }

            contentValuesVector.add(contentValues);
        }
        return contentValuesVector.toArray(mContentValues);

    }

    private void insertGenres(List<MovieAttributes> genres){
        getActivity().getContentResolver().bulkInsert(GenreContract.GenreEntry.CONTENT_URI, parseToContentValues(genres,GENRE));
        getActivity().getContentResolver().bulkInsert(GenreContract.MovieGenreEntry.CONTENT_URI, parseToContentValues(genres,MOVIE_GENRE));
        getActivity().getContentResolver().bulkInsert(CompanyContract.CompanyEntry.CONTENT_URI, parseToContentValues(genres,COMPANY));
        getActivity().getContentResolver().bulkInsert(CompanyContract.MovieCompanyEntry.CONTENT_URI, parseToContentValues(genres,MOVIE_COMPANY));
    }

    private void deleteMovie(){
        int deletedrows = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieEntry._ID +"=?", new String[]{String.valueOf(mMovie.id)});
        Log.e("deleted movies",String.valueOf(deletedrows));
    }

    private void deleteMovieDetail(){
        int deletedrows = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, MovieContract.MovieDetailEntry._ID +"=?", new String[]{String.valueOf(mMovie.id)});
        int deletedgenrerows = getActivity().getContentResolver().delete(GenreContract.MovieGenreEntry.CONTENT_URI, GenreContract.MovieGenreEntry.COLUMN_MOVIE_ID +"=?", new String[]{String.valueOf(mMovie.id)});
        int deletedcompanyrows = getActivity().getContentResolver().delete(CompanyContract.MovieCompanyEntry.CONTENT_URI, CompanyContract.MovieCompanyEntry.COLUMN_MOVIE_ID +"=?", new String[]{String.valueOf(mMovie.id)});
        Log.e("deleted movie details",String.valueOf(deletedrows)+"---"+String.valueOf(deletedgenrerows)+"---"+String.valueOf(deletedcompanyrows));
    }

}
