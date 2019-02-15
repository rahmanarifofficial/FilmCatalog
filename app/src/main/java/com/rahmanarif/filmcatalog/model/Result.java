package com.rahmanarif.filmcatalog.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {

    @SerializedName("results")
    public ArrayList<Movie> results;

    public Result(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }


}
