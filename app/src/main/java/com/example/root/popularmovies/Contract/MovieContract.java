package com.example.root.popularmovies.Contract;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.root.popularmovies.Constants;
import com.example.root.popularmovies.Model.MovieAttributes;

import java.util.List;

import static com.example.root.popularmovies.Contract.MovieContract.MovieEntry.CONTENT_URI;

/**
 * Created by satyaa on 12/5/2016.
 */

public class MovieContract {

    public static final String PATH_MOVIE_MASTER = "mst_tbl_movie";
    public static final String PATH_MOVIE_DETAIL = "tran_movie_detail";

    public static final String TABLE_MOVIE_MASTER = "mst_tbl_movie";
    public static final String TABLE_MOVIE_DETAIL = "tran_movie_detail";

    public static final String CREATE_TABLE_MOVIE_MASTER = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE_MASTER + "(" + MovieEntry._ID + " INTEGER,"
            + MovieEntry.COLUMN_ADULT + " INTEGER," + MovieEntry.COLUMN_VIDEO + " INTEGER," + MovieEntry.COLUMN_GENRE_IDS + " TEXT,"
            + MovieEntry.COLUMN_POSTER_PATH + " TEXT," + MovieEntry.COLUMN_OVERVIEW + " TEXT," + MovieEntry.COLUMN_RELEASE_DATE + " TEXT,"
            + MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT," + MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT," + MovieEntry.COLUMN_TITLE + " TEXT," + MovieEntry.COLUMN_BACKDROP_PATH + " TEXT,"
            + MovieEntry.COLUMN_POPULARITY + " REAL," + MovieEntry.COLUMN_VOTE_COUNT + " INTEGER," + MovieEntry.COLUMN_VOTE_AVERAGE + " REAL,"+MovieEntry.COLUMN_IS_FAVORITE+" INTEGER);";

    public static final String CREATE_TABLE_MOVIE_DETAIL = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE_DETAIL + "(" + MovieDetailEntry._ID + " INTEGER,"
            + MovieDetailEntry.COLUMN_BELONGS_TO_COLLECTION + " INTEGER," + MovieDetailEntry.COLUMN_BUDGET + " INTEGER,"
            + MovieDetailEntry.COLUMN_GENRES + " TEXT," + MovieDetailEntry.COLUMN_HOME_PAGE + " TEXT," + MovieDetailEntry.COLUMN_IMDB_ID + " TEXT,"
            + MovieDetailEntry.COLUMN_PRODUCTION_COMPANIES + " TEXT," + MovieDetailEntry.COLUMN_PRODUCTION_COUNTRIES + " TEXT," + MovieDetailEntry.COLUMN_REVENUE + " TEXT,"
            + MovieDetailEntry.COLUMN_RUNTIME + " TEXT," + MovieDetailEntry.COLUMN_SPOKEN_LANGUAGES + " TEXT," + MovieDetailEntry.COLUMN_TAGLINE + " REAL," + MovieDetailEntry.COLUMN_STATUS + " REAL);";

    public static final String DROP_TABLE_MOVIE_MASTER = "DROP TABLE " + TABLE_MOVIE_MASTER + ";";

    public static class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "mst_tbl_movie";
        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_MASTER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_MASTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_MASTER;


        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_GENRE_IDS = "genre_ids";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static final String[] MOVIE_MASTER_COLUMNS = new String[]{
                COLUMN_ADULT,COLUMN_VIDEO,COLUMN_GENRE_IDS,COLUMN_POSTER_PATH,COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE,COLUMN_ORIGINAL_TITLE,COLUMN_ORIGINAL_LANGUAGE,COLUMN_TITLE,
                COLUMN_BACKDROP_PATH,COLUMN_POPULARITY,COLUMN_VOTE_COUNT,COLUMN_VOTE_AVERAGE,
                COLUMN_IS_FAVORITE
        };
    }

    public static class MovieDetailEntry implements BaseColumns {

        public static final String TABLE_NAME = "tran_movie_detail";
        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_DETAIL).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAIL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAIL;

        public static final String COLUMN_BELONGS_TO_COLLECTION = "belongs_to_collection";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_HOME_PAGE = "homepage";
        public static final String COLUMN_IMDB_ID = "imdb_id";
        public static final String COLUMN_PRODUCTION_COMPANIES = "production_companies";
        public static final String COLUMN_PRODUCTION_COUNTRIES = "production_countries";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_SPOKEN_LANGUAGES = "spoken_languages";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_STATUS = "status";
    }

    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
    }

    public static Uri buildMovieDetailUri(long id) {
        return ContentUris.withAppendedId(MovieDetailEntry.CONTENT_URI, id);
    }

    public static Uri buildGetFavoriteMovieUri(){
        return MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendQueryParameter(MovieContract.MovieEntry.COLUMN_IS_FAVORITE,String.valueOf(1)).build();
    }

}
