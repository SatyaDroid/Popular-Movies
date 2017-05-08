package com.example.root.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.root.popularmovies.Contract.CompanyContract;
import com.example.root.popularmovies.Contract.GenreContract;
import com.example.root.popularmovies.Contract.LanguageContract;
import com.example.root.popularmovies.Contract.MovieContract;

/**
 * Created by satyaa on 12/5/2016.
 */

public class DBHandler extends SQLiteOpenHelper {
    Context mContext;
    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 2;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MovieContract.CREATE_TABLE_MOVIE_MASTER);
        sqLiteDatabase.execSQL(MovieContract.CREATE_TABLE_MOVIE_DETAIL);
        sqLiteDatabase.execSQL(GenreContract.CREATE_TABLE_GENRE_MASTER);
        sqLiteDatabase.execSQL(GenreContract.CREATE_TABLE_MOVIE_GENRE);
        sqLiteDatabase.execSQL(CompanyContract.CREATE_TABLE_COMPANY_MASTER);
        sqLiteDatabase.execSQL(CompanyContract.CREATE_TABLE_MOVIE_COMPANY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
