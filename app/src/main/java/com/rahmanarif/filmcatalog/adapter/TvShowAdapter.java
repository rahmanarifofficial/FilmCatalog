package com.rahmanarif.filmcatalog.adapter;

import android.app.Activity;
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
import com.rahmanarif.filmcatalog.model.TvShow;
import com.rahmanarif.filmcatalog.ui.activity.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvViewHolder> {

    private List<TvShow> tvShows;
    public static final String TYPE_TV = "fromTv";

    public TvShowAdapter(List<TvShow> tvShows) {
        this.tvShows = tvShows;
    }

    @NonNull
    @Override
    public TvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_film, viewGroup, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvViewHolder tvViewHolder, final int i) {
        tvViewHolder.judulTv.setText(tvShows.get(i).getName());
        tvViewHolder.deskripsiTv.setText(tvShows.get(i).getOverview());
        tvViewHolder.deskripsiTv.setMaxLines(2);
        Picasso.get().load(BuildConfig.BASE_IMAGE_URL_w92 + tvShows.get(i).getPosterPath())
                .into(tvViewHolder.posterTv);
        tvViewHolder.cardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDetail = new Intent(v.getContext(), DetailActivity.class);
                toDetail.putExtra(DetailActivity.EXTRA_MOVIE_ID, tvShows.get(i).getId().toString());
                toDetail.putExtra(DetailActivity.EXTRA_TYPE, TYPE_TV);
                ((Activity) v.getContext()).startActivityForResult(toDetail, 100);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (tvShows != null) ? tvShows.size() : 0;
    }

    public class TvViewHolder extends RecyclerView.ViewHolder {
        private CardView cardTv;
        private ImageView posterTv;
        private TextView judulTv, deskripsiTv;

        public TvViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTv = itemView.findViewById(R.id.cardFilm);
            posterTv = itemView.findViewById(R.id.posterFilm);
            judulTv = itemView.findViewById(R.id.judulFilm);
            deskripsiTv = itemView.findViewById(R.id.deskripsiFilm);
        }
    }
}