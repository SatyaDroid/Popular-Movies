package com.example.root.popularmovies.Contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.root.popularmovies.Constants;

/**
 * Created by satyaa on 12/21/2016.
 */

public class CompanyContract {

    public static final String PATH_COMPANY_MASTER = "mst_tbl_company";
    public static final String PATH_MOVIE_COMPANY = "tran_movie_company";

    public static final String TABLE_COMPANY_MASTER = "mst_tbl_company";
    public static final String TABLE_MOVIE_COMPANY = "tran_movie_company";

    public static final String CREATE_TABLE_COMPANY_MASTER = "CREATE TABLE IF NOT EXISTS " + TABLE_COMPANY_MASTER + "("
            + CompanyEntry._ID + " INTEGER PRIMARY KEY," + CompanyEntry.COLUMN_COMPANY_NAME + " TEXT);";
    public static final String CREATE_TABLE_MOVIE_COMPANY = "CREATE TABLE IF NOT EXISTS " + TABLE_MOVIE_COMPANY + "(" + MovieCompanyEntry._ID +
            " INTEGER AUTO INCREMENT PRIMARY KEY," + MovieCompanyEntry.COLUMN_MOVIE_ID + " INTEGER," + MovieCompanyEntry.COLUMN_COMPANY_ID + " INTEGER);";

    public static class CompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = "mst_tbl_company";

        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_COMPANY_MASTER).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_COMPANY_MASTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_COMPANY_MASTER;

        public static final String COLUMN_COMPANY_NAME = "company_name";
    }

    public static class MovieCompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = "tran_movie_company";

        public static final Uri CONTENT_URI =
                Constants.BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_COMPANY).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_COMPANY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + Constants.CONTENT_AUTHORITY + "/" + PATH_MOVIE_COMPANY;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_COMPANY_ID = "company_id";
    }

}
