package com.example.root.popularmovies.Services;

import com.example.root.popularmovies.Constants;
import com.example.root.popularmovies.Model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by satyaa on 11/11/2016.
 */

public interface MovieLoadService {

    @GET("/{movies}/{popular}?" + Constants.API_KEY_QUERY_PARAMETER + "=" + Constants.API_KEY_V3)
    Call<Movie> getMoviesList(@Path("movies") String movie, @Path("popular") String popular);
}
