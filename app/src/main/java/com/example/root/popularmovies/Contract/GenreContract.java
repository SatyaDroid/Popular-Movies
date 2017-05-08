package com.example.root.popularmovies.Contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.root.popularmovies.Constants;

/**
 * Created by satyaa on 12/7/2016.
 */

public class GenreContract {

    public static final String PATH_GENRE_MASTER = "mst_tbl_genre";
    public static final String PATH_MOVIE_GENRE = "tran_movie_genre";

    public static final String TABLE_GENRE_MASTER = "mst_tbl_genre";
    public static final String TABLE_MOVIE_GENRE = "tran_movie_genre";

    public static final String CREATE_TABLE_GENRE_MASTER = "CREATE TABLE IF NOT EXISTS " + TABLE_GENRE_MASTER + "("
            + GenreEntry._ID + " INTEGER PRIMARY KEY," + GenreEntry.COLUMN_GENRE_NAME + " TEXT);";
    public static final String CREATE_TABLE_MOVIE_GENRE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE_GENRE + "(" + MovieGenreEntry._ID +
            " INTEGER AUTO INCREMENT PRIMARY KEY," + MovieGenreEntry.COLUMN_MOVIE_ID + " INTEGER," + MovieGenreEntry.COLUMN_GENRE_ID + " INTEGER);";

    public static class GenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "mst_tbl_genre";

        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRE_MASTER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_GENRE_MASTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_GENRE_MASTER;

        public static final String COLUMN_GENRE_NAME = "genre_name";
    }

    public static class MovieGenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "tran_movie_genre";

        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_GENRE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_GENRE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_GENRE;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_GENRE_ID = "genre_id";
    }
}
