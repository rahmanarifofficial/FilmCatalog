package com.rahmanarif.filmcatalog.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.model.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<Genre> genres;

    public GenreAdapter(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_genre, viewGroup, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder genreViewHolder, int i) {
        genreViewHolder.genreFilm.setText(genres.get(i).getName());
        Log.d("genrename", genres.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return (genres != null) ? genres.size() : 0;
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {

        private TextView genreFilm;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreFilm = itemView.findViewById(R.id.genreFilm);
        }
    }
}
