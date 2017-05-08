package com.example.root.popularmovies.Model;

import java.util.List;

/**
 * Created by satyaa on 11/11/2016.
 */

public class APIResponse<T> {
    public List<T> results;
    int page;
    int total_results;
    int total_pages;
    String status_message;
    int status_code;
    int id;
}
