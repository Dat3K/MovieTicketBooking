package com.example.movieticketbooking.home_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarouselAdapter  extends  RecyclerView.Adapter<CarouselAdapter.MyViewHolder> {
    private Context context;
    private List<String> imageUrlList;

    public CarouselAdapter(Context context, List<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_carousel_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView); // Use Picasso or your preferred image loading library
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_carousel_item);
        }
    }
}
