package com.example.root.popularmovies.Services;

import com.example.root.popularmovies.Constants;
import com.example.root.popularmovies.Model.APIResponse;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.Model.MovieDetail;
import com.example.root.popularmovies.Model.Review;
import com.example.root.popularmovies.Model.Video;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by satyaa on 11/11/2016.
 */

public interface MovieLoadService {

    @GET("3/{movies}/{popular}?" + Constants.API_KEY_QUERY_PARAMETER + "=" + Constants.API_KEY_V3)
    Call<APIResponse<Movie>> getMoviesList(@Path("movies") String movie, @Path("popular") String popular);

    @GET("3/{movies}/{id}?" + Constants.API_KEY_QUERY_PARAMETER + "=" + Constants.API_KEY_V3)
    Call<MovieDetail> getMovieDetail(@Path("movies") String movie, @Path("id") String id);

    @GET("3/{movies}/{id}/videos?" + Constants.API_KEY_QUERY_PARAMETER + "=" + Constants.API_KEY_V3)
    Call<APIResponse<Video>> getMovieTrailers(@Path("movies") String movie, @Path("id") String id);

    @GET("3/{movies}/{id}/reviews?" + Constants.API_KEY_QUERY_PARAMETER + "=" + Constants.API_KEY_V3)
    Call<APIResponse<Review>> getMovieReviews(@Path("movies") String movie, @Path("id") String id);
}
