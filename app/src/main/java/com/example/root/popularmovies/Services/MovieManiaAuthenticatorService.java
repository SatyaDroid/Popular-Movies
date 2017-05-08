package com.example.root.popularmovies.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by satyaa on 12/20/2016.
 */

public class MovieManiaAuthenticatorService extends Service {

    private MovieManiaAuthenticator mMovieManiaAuthenticator;

    @Override
    public void onCreate() {
        mMovieManiaAuthenticator = new MovieManiaAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMovieManiaAuthenticator.getIBinder();
    }
}
