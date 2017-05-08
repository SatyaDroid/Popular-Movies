package com.example.root.popularmovies;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.root.popularmovies.Contract.CompanyContract;
import com.example.root.popularmovies.Contract.GenreContract;
import com.example.root.popularmovies.Contract.LanguageContract;
import com.example.root.popularmovies.Contract.MovieContract;

import static com.example.root.popularmovies.Contract.MovieContract.MovieEntry.MOVIE_MASTER_COLUMNS;
import static com.example.root.popularmovies.Contract.MovieContract.TABLE_MOVIE_DETAIL;
import static com.example.root.popularmovies.Contract.MovieContract.TABLE_MOVIE_MASTER;

/**
 * Created by satyaa on 12/5/2016.
 */

public class MovieProvider extends ContentProvider {
    public DBHandler mDBHandler;
    public SQLiteDatabase mSQLiteDatabase;
    public static UriMatcher mUriMatcher = buildUriMatcher();
    public static SQLiteQueryBuilder mSQLiteQueryBuilder;

    public static final int MOVIE = 100;
    public static final int MOVIE_DETAIL = 101;
    public static final int MOVIE_FAVOURITE_DETAIL = 102;
    public static final int GENRE = 103;
    public static final int MOVIE_GENRE = 104;
    public static final int COMPANY = 105;
    public static final int MOVIE_COMPANY = 107;


    static {
        mSQLiteQueryBuilder = new SQLiteQueryBuilder();
        mSQLiteQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                MovieContract.MovieDetailEntry.TABLE_NAME + " INNER JOIN " +
                CompanyContract.CompanyEntry.TABLE_NAME + " INNER JOIN " +
                CompanyContract.MovieCompanyEntry.TABLE_NAME + " INNER JOIN " +
                GenreContract.GenreEntry.TABLE_NAME + " INNER JOIN " +
                GenreContract.MovieGenreEntry.TABLE_NAME +
                " WHERE " + MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " = 1 AND "
                + MovieContract.MovieDetailEntry.TABLE_NAME +
                "." + MovieContract.MovieDetailEntry._ID + " = " + CompanyContract.MovieCompanyEntry.TABLE_NAME +
                "." + CompanyContract.MovieCompanyEntry.COLUMN_MOVIE_ID + " AND " + CompanyContract.MovieCompanyEntry.TABLE_NAME +
                "." + CompanyContract.MovieCompanyEntry.COLUMN_COMPANY_ID + " = " + CompanyContract.CompanyEntry.TABLE_NAME +
                "." + CompanyContract.CompanyEntry._ID + " AND " + MovieContract.MovieDetailEntry.TABLE_NAME +
                "." + MovieContract.MovieDetailEntry._ID + " = " + GenreContract.MovieGenreEntry.TABLE_NAME +
                "." + GenreContract.MovieGenreEntry.COLUMN_MOVIE_ID + " AND " + GenreContract.MovieGenreEntry.TABLE_NAME +
                "." + GenreContract.MovieGenreEntry.COLUMN_GENRE_ID + " = " + GenreContract.GenreEntry.TABLE_NAME +
                "." + GenreContract.GenreEntry._ID);
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Constants.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE_MASTER, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAIL, MOVIE_DETAIL);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_MASTER + "/*", MOVIE_FAVOURITE_DETAIL);
        matcher.addURI(authority, GenreContract.PATH_GENRE_MASTER, GENRE);
        matcher.addURI(authority, GenreContract.PATH_MOVIE_GENRE, MOVIE_GENRE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDBHandler = new DBHandler(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase mSQLiteDatabase = mDBHandler.getReadableDatabase();
        final int urimatcher = mUriMatcher.match(uri);
        Cursor returnCursor = null;

        switch (urimatcher) {
            case MOVIE:
                returnCursor = getAllMovies();
                return returnCursor;
            case MOVIE_DETAIL:
                returnCursor = getFavouriteMovie(projection, selection, selectionArgs, sortOrder);
                return returnCursor;
            case MOVIE_FAVOURITE_DETAIL:
                returnCursor = mSQLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, null);
                return returnCursor;
        }
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int urimatcher = mUriMatcher.match(uri);
        switch (urimatcher) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAIL:
                return MovieContract.MovieDetailEntry.CONTENT_TYPE;
            case MOVIE_FAVOURITE_DETAIL:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case GENRE:
                return GenreContract.GenreEntry.CONTENT_TYPE;
            case MOVIE_GENRE:
                return GenreContract.MovieGenreEntry.CONTENT_TYPE;
            case COMPANY:
                return CompanyContract.MovieCompanyEntry.CONTENT_TYPE;
            case MOVIE_COMPANY:
                return CompanyContract.CompanyEntry.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase mSQLiteDatabase = mDBHandler.getWritableDatabase();
        final int urimatcher = mUriMatcher.match(uri);
        long id = -1;
        switch (urimatcher) {
            case MOVIE:
                id = mSQLiteDatabase.insert(MovieContract.TABLE_MOVIE_MASTER, null, contentValues);
                break;
            case MOVIE_DETAIL:
                id = mSQLiteDatabase.insert(MovieContract.TABLE_MOVIE_DETAIL, null, contentValues);
                break;
            case GENRE:
                id = mSQLiteDatabase.insert(GenreContract.TABLE_GENRE_MASTER, null, contentValues);
                break;
            case MOVIE_GENRE:
                id = mSQLiteDatabase.insert(GenreContract.TABLE_MOVIE_GENRE, null, contentValues);
                break;
            case COMPANY:
                id = mSQLiteDatabase.insert(CompanyContract.TABLE_COMPANY_MASTER, null, contentValues);
                break;
            case MOVIE_COMPANY:
                id = mSQLiteDatabase.insert(CompanyContract.TABLE_MOVIE_COMPANY, null, contentValues);
                break;
        }
        return (id > -1) ? MovieContract.buildMovieUri(id) : null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase mSQLiteDatabase = mDBHandler.getWritableDatabase();
        final int urimatcher = mUriMatcher.match(uri);
        int rowsDeleted = 0;
        switch (urimatcher) {
            case MOVIE:
                rowsDeleted = mSQLiteDatabase.delete(MovieContract.TABLE_MOVIE_MASTER, s, strings);
                break;
            case MOVIE_DETAIL:
                rowsDeleted = mSQLiteDatabase.delete(MovieContract.TABLE_MOVIE_DETAIL, s, strings);
                break;
            case MOVIE_GENRE:
                rowsDeleted = mSQLiteDatabase.delete(GenreContract.TABLE_MOVIE_GENRE, s, strings);
                break;
            case MOVIE_COMPANY:
                rowsDeleted = mSQLiteDatabase.delete(CompanyContract.TABLE_MOVIE_COMPANY, s, strings);
                break;
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase mSQLiteDatabase = mDBHandler.getWritableDatabase();
        final int urimatcher = mUriMatcher.match(uri);
        int rowsUpdated = 0;
        switch (urimatcher) {
            case MOVIE:
                rowsUpdated = mSQLiteDatabase.update(TABLE_MOVIE_MASTER, contentValues, s, strings);
                break;
            case MOVIE_DETAIL:
                rowsUpdated = mSQLiteDatabase.update(TABLE_MOVIE_DETAIL, contentValues, s, strings);
                break;
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int urimatcher = mUriMatcher.match(uri);
        int returnCount = 0;
        mSQLiteDatabase = mDBHandler.getWritableDatabase();
        switch (urimatcher) {
            case GENRE:
                mSQLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = mSQLiteDatabase.insert(GenreContract.GenreEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    mSQLiteDatabase.setTransactionSuccessful();
                } finally {
                    mSQLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case MOVIE:
                mSQLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = mSQLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    mSQLiteDatabase.setTransactionSuccessful();
                } finally {
                    mSQLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
        }
        return super.bulkInsert(uri, values);
    }

    public Cursor getFavouriteMovie(String[] projection, String selection, String[] selectionArgs, String sortType) {
        return mSQLiteQueryBuilder.query(mDBHandler.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortType);
    }

    public Cursor getAllMovies() {
        SQLiteDatabase db = mDBHandler.getReadableDatabase();
        return db.rawQuery("select * from " + MovieContract.MovieEntry.TABLE_NAME, null);
    }

}

