package com.rahmanarif.favoritelist.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultTvShow {
    @SerializedName("results")
    @Expose
    private ArrayList<TvShow> results = null;

    public ArrayList<TvShow> getResults() {
        return results;
    }
}
