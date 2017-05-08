package com.example.root.popularmovies.Model;

import java.util.List;

/**
 * Created by satyaa on 12/6/2016.
 */

public class MovieDetail {
    public boolean adult;
    public String backdrop_path;
    public MovieCollection belongs_to_collection;
    public long budget;
    public List<MovieAttributes> genres;
    public String homepage;
    public long id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public Double popularity;
    public String poster_path;
    public List<MovieAttributes> production_companies;
    public List<MovieAttributes> production_countries;
    public String release_date;
    public String revenue;
    public String runtime;
    public List<MovieAttributes> spoken_languages;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public Double vote_average;
    public long vote_count;
}
