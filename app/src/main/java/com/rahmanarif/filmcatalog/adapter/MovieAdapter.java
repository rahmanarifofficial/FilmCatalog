package com.rahmanarif.filmcatalog.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public void refill(List<Movie> movie){
        this.movies = new ArrayList<>();
        this.movies.addAll(movie);
        notifyDataSetChanged();
    }
    public List<Movie> getMovies() {
        return movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_film, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, final int i) {
        movieViewHolder.judulFilm.setText(movies.get(i).getTitle());
        movieViewHolder.deskripsiFilm.setText(movies.get(i).getOverview());
        movieViewHolder.deskripsiFilm.setMaxLines(2);
        Picasso.get().load(BuildConfig.BASE_IMAGE_URL_w92 + movies.get(i).getPosterPath())
                .into(movieViewHolder.posterFilm);
        movieViewHolder.cardFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), DetailActivity.class).
                        putExtra(DetailActivity.EXTRA_MOVIE_ID, movies.get(i).getId().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movies != null) ? movies.size() : 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private CardView cardFilm;
        private ImageView posterFilm;
        private TextView judulFilm, deskripsiFilm;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            cardFilm = itemView.findViewById(R.id.cardFilm);
            posterFilm = itemView.findViewById(R.id.posterFilm);
            judulFilm = itemView.findViewById(R.id.judulFilm);
            deskripsiFilm = itemView.findViewById(R.id.deskripsiFilm);
        }
    }
}
