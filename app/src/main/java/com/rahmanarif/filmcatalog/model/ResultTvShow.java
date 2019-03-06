package com.rahmanarif.filmcatalog.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResultTvShow {
    @SerializedName("results")
    @Expose
    private ArrayList<TvShow> results = null;

    public ArrayList<TvShow> getResults() {
        return results;
    }

}
