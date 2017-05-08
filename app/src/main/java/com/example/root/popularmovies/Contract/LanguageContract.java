package com.example.root.popularmovies.Contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.root.popularmovies.Constants;

/**
 * Created by satyaa on 12/10/2016.
 */

public class LanguageContract {

    public static final String PATH_LANGUAGE_MASTER = "mst_tbl_lang";
    public static final String PATH_MOVIE_LANG = "tran_movie_lang";

    public static final String TABLE_LANGUAGE_MASTER = "mst_tbl_lang";
    public static final String TABLE_MOVIE_LANG = "tran_movie_lang";

    public static final String CONTENT_AUTHORITY = "com.example.root.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String CREATE_TABLE_LANGUAGE_MASTER = "CREATE TABLE IF NOT EXISTS " + TABLE_LANGUAGE_MASTER + "("
            + LanguageEntry._ID + " INTEGER PRIMARY KEY," + LanguageEntry.COLUMN_LANGUAGE_NAME + " TEXT);";
    public static final String CREATE_TABLE_MOVIE_LANGUAGE = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE_LANG + "(" + MovieLanguageEntry._ID +
            " INTEGER AUTO INCREMENT PRIMARY KEY," + MovieLanguageEntry.COLUMN_MOVIE_ID + " INTEGER," + MovieLanguageEntry.COLUMN_LANGUAGE_ID + " INTEGER);";

    public static class LanguageEntry implements BaseColumns {
        public static final String TABLE_NAME = "mst_tbl_movie";
        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_LANGUAGE_MASTER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_LANGUAGE_MASTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_LANGUAGE_MASTER;

        public static final String COLUMN_LANGUAGE_NAME = "language_name";
    }

    public static class MovieLanguageEntry implements BaseColumns {
        public static final String TABLE_NAME = "mst_tbl_movie";
        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_LANG).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_LANG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_LANG;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_LANGUAGE_ID = "language_id";
    }
}
