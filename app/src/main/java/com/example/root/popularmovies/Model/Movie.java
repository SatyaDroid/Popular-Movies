package com.example.root.popularmovies.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 2/11/16.
 */

public class Movie extends Object implements Serializable {

    public boolean adult;
    public boolean video;
    public List<Integer> genre_ids;
    public int id;
    public String poster_path;
    public String overview;
    public String release_date;
    public String original_title;
    public String original_language;
    public String title;
    public String backdrop_path;
    public float popularity;
    public int vote_count;
    public float vote_average;

}
