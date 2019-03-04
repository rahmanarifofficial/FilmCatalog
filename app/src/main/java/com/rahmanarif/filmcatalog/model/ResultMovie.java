package com.rahmanarif.filmcatalog.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResultMovie {

    @SerializedName("results")
    public ArrayList<Movie> results;

    public ResultMovie(ArrayList<Movie> results) {
        this.results = results;
    }

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }


}
