package com.example.root.popularmovies;

import android.net.Uri;

/**
 * Created by root on 1/11/16.
 */

public class Constants {

    /*API_KEY should be taken from themoviedb.org by creating an account*/
    public static final String API_KEY_V3 = "b7864a9771b23213efb30c9a9bb8371a";

    /*Main base url for calling themoviedb.org*/
    public static final String BASE_URL = "https://api.themoviedb.org/";
    public static final String API_KEY_QUERY_PARAMETER = "api_key";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String MOVIE_OBJ = "movie_obj";
    public static final String APPEND_TO_RESPONSE_PARAMETER = "append_to_response";
    public static final String IMAGES_AND_VIDEOS = "videos,images";

    public static final String CONTENT_AUTHORITY = "com.example.root.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
