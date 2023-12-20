package com.example.movieticketbooking.movie_page;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.models.Movie;
import com.example.movieticketbooking.utils.StringUtil;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private List<String> calendarList;
    private Movie movie;
    TheaterAdapter theaterAdapter;
    private View lastSelectedView = null;
    private String chosenDate = null;
    public CalendarAdapter(List<String> calendarList, Movie movie, TheaterAdapter theaterAdapter) {
        this.calendarList = calendarList;
        this.movie = movie;
        this.theaterAdapter=theaterAdapter;

    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_calender_item, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        Pair<String, Integer> calendarItem = StringUtil.parseDateString(calendarList.get(position));

        // Set calendar item details to the views in the CalendarViewHolder
        holder.calendarNameTextView.setText(calendarItem.first); // Replace with the actual data
        holder.calendarTimeTextView.setText(calendarItem.second.toString());

        holder.itemView.setOnClickListener(view -> {
            if (lastSelectedView != null) {
                // Reset the background of the last selected item
                lastSelectedView.setBackgroundResource(R.drawable.btn_avaiable);
            }
            Log.d("date chosen",String.valueOf(calendarList.get(position)));

            theaterAdapter.setTheaterList(movie.getSchedules().get(calendarList.get(position)));
            chosenDate = calendarList.get(position);
            lastSelectedView = holder.itemView; // Update the last selected View
            // Highlight the current selected item
            holder.itemView.setBackgroundResource(R.drawable.btn_selected);
        });
    }

    public View getLastSelectedView(){
        return lastSelectedView;
    }

    public String getChosenDate() {
        return chosenDate;
    }



    @Override
    public int getItemCount() {
        return calendarList != null ? calendarList.size() : 0;
    }

    public void releaseResources() {
        calendarList = null;
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        // Declare views for calendar item details
        TextView calendarNameTextView;
        TextView calendarTimeTextView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            calendarNameTextView = itemView.findViewById(R.id.mp_theater_calendar_name);
            calendarTimeTextView = itemView.findViewById(R.id.mp_theater_calendar_number);
        }
    }
}

