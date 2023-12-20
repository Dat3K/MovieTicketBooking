package com.example.movieticketbooking.movie_page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.models.Theater;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>  {

    private List<Theater> theaterList;
    private static View lastSelectedView = null;
    private static List<ScheduleAdapter> scheduleAdapters;
    private static String  chosenTheater, chosenTime;

    public TheaterAdapter(List<Theater> theaterList) {

        this.theaterList = theaterList;
        this.scheduleAdapters = new ArrayList<>();
    }
    public List<Theater> getTheaterList(){
        return theaterList;
    };

    public String getChosenTheater() {
        return TheaterAdapter.chosenTheater;
    }

    public static String getChosenTime() {
        return TheaterAdapter.chosenTime;
    }

    public void setTheaterList(List<Theater> theaterList){
        this.theaterList.clear();
        this.theaterList.addAll(theaterList);
        notifyDataSetChanged();
    };
    public View getLastSelectedView(){
        return lastSelectedView;
    }
    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mp_theater_recycler_item, parent, false);
        return new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        Theater theater = theaterList.get(position);

        // Set theater details to the views in the TheaterViewHolder

        holder.theaterNameTextView.setText(theater.getName());
        // Set up inner RecyclerView for schedules
        holder.setupSchedulesRecyclerView(theater,position);
    }

    @Override
    public int getItemCount() {
        return  theaterList != null ? theaterList.size() : 0;
    }

    public void releaseResources() {
        if (scheduleAdapters != null) {
            for (ScheduleAdapter scheduleAdapter : scheduleAdapters) {
                scheduleAdapter.clearResources();
            }
        }

        theaterList = null;
    }

    static class TheaterViewHolder extends RecyclerView.ViewHolder  {
        // Declare views for theater details

        // Declare inner RecyclerView for schedules
        RecyclerView schedulesRecyclerView;
        TextView theaterNameTextView;
        public TheaterViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            theaterNameTextView = itemView.findViewById(R.id.mp_theater_recylcer_item_name);
            // Initialize inner RecyclerView for schedules
            schedulesRecyclerView = itemView.findViewById(R.id.mp_theater_schedule_item);

        }

        public void setupSchedulesRecyclerView(Theater theater, int position) {

            Set<String> schedulesSet = theater.getShowingTime().keySet();

            List<String> schedules = new ArrayList<>(schedulesSet);
            // Set up the inner RecyclerView for schedules
            ScheduleAdapter schedulesAdapter = new ScheduleAdapter(schedules);

            schedulesAdapter.setOnScheduleClickListener((selected) -> {
                if (((String)selected.getTag()).equals("disable"))
                    return;

                // Update background of the last selected view to "available"

                if (lastSelectedView != null  ) {
                    ((LinearLayout)lastSelectedView).setBackgroundResource(R.drawable.btn_avaiable
                    );
                }
                TheaterAdapter.chosenTheater = theater.getName();
                TheaterAdapter.chosenTime = schedulesAdapter.getChosenSchedule();

                // Update background of the newly selected view to "selected"
                ((LinearLayout)selected).setBackgroundResource(R.drawable.btn_selected);
                // Update the last selected view
                lastSelectedView = selected;
            });

            if (scheduleAdapters != null && position < scheduleAdapters.size()) {
                scheduleAdapters.set(position, schedulesAdapter);
            } else {
                scheduleAdapters.add(schedulesAdapter);
            }


            schedulesRecyclerView.setAdapter(schedulesAdapter);
            schedulesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
        }
    }
}

