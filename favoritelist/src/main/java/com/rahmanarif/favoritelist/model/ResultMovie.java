package com.rahmanarif.favoritelist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultMovie {

    @SerializedName("results")
    public List<Movie> results;

    public ResultMovie(List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }


}
