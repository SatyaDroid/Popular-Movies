package com.example.root.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FavoriteMovieFragment extends Fragment {

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }

    public static FavoriteMovieFragment newInstance(String param1, String param2) {
        FavoriteMovieFragment fragment = new FavoriteMovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        return mView;
    }

}
