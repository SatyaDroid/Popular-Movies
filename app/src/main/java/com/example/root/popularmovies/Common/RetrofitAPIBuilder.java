package com.example.root.popularmovies.Common;

import android.content.Context;
import android.provider.SyncStateContract;

import com.example.root.popularmovies.Constants;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by satyaa on 11/11/2016.
 */

public class RetrofitAPIBuilder {

    static Retrofit retrofit = null;
    Context mContext;

    public static synchronized Retrofit getInstance() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static synchronized Retrofit getJSONEnabledInstance() {
        final OkHttpClient okHttpClient = new OkHttpClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
