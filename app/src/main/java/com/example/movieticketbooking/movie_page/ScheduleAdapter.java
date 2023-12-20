package com.example.movieticketbooking.movie_page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<String> scheduleList;

    String chosenSchedule;
    private OnScheduleClickListener onScheduleClickListener;

    public ScheduleAdapter(List<String> scheduleList) {
        this.scheduleList = scheduleList;
    }

    public String getChosenSchedule() {
        return chosenSchedule;
    }

    public void clearResources() {
        scheduleList.clear();
        notifyDataSetChanged();
        onScheduleClickListener = null;
    }

    public interface OnScheduleClickListener {
        void onScheduleClick(View adapterPosition);
    }
    public void setOnScheduleClickListener(OnScheduleClickListener listener) {
        this.onScheduleClickListener = listener;
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_theater_schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        String schedule = String.valueOf(scheduleList.get(position));

        // Set schedule details to the views in the ScheduleViewHolder
        holder.scheduleTimeTextView.setText(schedule);



        // Handle click events
        holder.itemView.setOnClickListener(view -> {
            chosenSchedule = schedule;
            if (onScheduleClickListener != null) {
                onScheduleClickListener.onScheduleClick(holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        // Declare views for schedule details
        TextView scheduleTimeTextView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            scheduleTimeTextView = itemView.findViewById(R.id.mp_theater_schedule_item_time);
            // Initialize more views as needed
        }
    }
}

