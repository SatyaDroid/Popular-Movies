package com.example.root.popularmovies.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.root.popularmovies.Common.RetrofitAPIBuilder;
import com.example.root.popularmovies.CommonUtils;
import com.example.root.popularmovies.Constants;
import com.example.root.popularmovies.Model.APIResponse;
import com.example.root.popularmovies.Model.Movie;
import com.example.root.popularmovies.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by satyaa on 12/19/2016.
 */

public class MovieLoadIntentService extends IntentService {

    public MovieLoadIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadMoviesList();
    }

    private void loadMoviesList() {
        if (CommonUtils.isNetworAvailable(this)) {
            Retrofit retrofit = RetrofitAPIBuilder.getInstance();
            MovieLoadService mMovieLoadService = retrofit.create(MovieLoadService.class);
            Call call = mMovieLoadService.getMoviesList("movie", "popular");
            call.enqueue(new retrofit2.Callback<APIResponse<Movie>>() {
                @Override
                public void onResponse(Call<APIResponse<Movie>> call, Response<APIResponse<Movie>> response) {
                    APIResponse response1 = response.body();
                    Log.e("response", String.valueOf(response1.results));
                }

                @Override
                public void onFailure(Call<APIResponse<Movie>> call, Throwable t) {
                    Log.e("response", t.toString());
                }
            });
        }
    }

    public class AlarmReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(context, MovieLoadIntentService.class);
            context.startActivity(intent1);
        }
    }

}
