package com.example.movieticketbooking.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.models.Movie;
import com.example.movieticketbooking.movie_page.MoviePageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie> movieDataList;

    public MovieListAdapter(Context context, List<Movie> movieDataList) {
        this.context = context;
        this.movieDataList = movieDataList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_movie_item, parent, false);
        return new MovieViewHolder(view);
    }
    public void releaseResources() {
        // Cancel any pending network requests
        Picasso.get().cancelTag("MovieListAdapter");
    }
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movieData = movieDataList.get(position);

        holder.tvMovieTime.setText(movieData.getFormattedDuration());
        holder.tvMovieName.setText(movieData.getName());

        Picasso.get().load(movieData.getImgUrl())
                .tag("MovieListAdapter").into(holder.ivMovieImage);

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switch to MoviePageActivity with the selected movie ID
                Intent intent = new Intent(context, MoviePageActivity.class);
                intent.putExtra("movieId", movieData.getId());
                Log.d("movieId",movieData.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieDataList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTime, tvMovieName;
        ImageView ivMovieImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTime = itemView.findViewById(R.id.item_movie_duration);
            tvMovieName = itemView.findViewById(R.id.item_movie_name);
            ivMovieImage = itemView.findViewById(R.id.item_movie_image);
        }
    }
}

