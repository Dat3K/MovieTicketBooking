package com.example.movieticketbooking.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movieticketbooking.models.BookingHistory;
import com.example.movieticketbooking.models.Movie;
import com.example.movieticketbooking.models.Theater;
import com.example.movieticketbooking.utils.SeatStatusEnum;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMovieController {
    FirebaseFirestore db;
    CollectionReference movieRef;
    Context context;
    private static final int MAX_SELECT_MOVIE = 1000;

    public DataMovieController(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.movieRef = db.collection("movies");
        this.context = context;
    }

    public interface OnMovieDataReceivedListener {
        void onMovieDataReceived(Movie movie);

        void onMovieDataError(String errorMessage);
    }

    public interface OnMovieDataListReceivedListener {
        void onMovieDataReceived(List<Movie> movie);

        void onMovieDataError(String errorMessage);
    }

    public void getMovieList(int movieCount, OnMovieDataListReceivedListener listener) {

        movieRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int count = 0;
                List<Movie> movieDataList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    if (movieCount != -1 && (++count >= movieCount || count >= MAX_SELECT_MOVIE)) {
                        break;  // Stop fetching more movies once you have 8
                    }
                    String name = documentSnapshot.getString("name");
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    String trailerUrl = documentSnapshot.getString("trailerUrl");
                    Integer duration = documentSnapshot.getLong("duration").intValue();
                    String id = documentSnapshot.getId();

                    Movie movie = new Movie(duration, name, imageUrl, trailerUrl, id, null);
                    movieDataList.add(movie);

                }
                listener.onMovieDataReceived(movieDataList);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
                listener.onMovieDataError("Movie not found");
            }

        });
    }

    public Movie getMovieById(String movieId, OnMovieDataReceivedListener listener) {
        final Movie[] res = {null};
        movieRef.document(movieId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.d("sucess", movieId);
                    // The document exists, you can retrieve the movie data
                    String name = documentSnapshot.getString("name");
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    String trailerUrl = documentSnapshot.getString("trailerUrl");
                    Integer duration = documentSnapshot.getLong("duration").intValue();

                    Map<String, List<Theater>> schedules = new HashMap<>();
                    if (documentSnapshot.contains("schedules")) {
                        Map<String, Object> schedulesData = (Map<String, Object>) documentSnapshot.get("schedules");
                        for (Map.Entry<String, Object> entry : schedulesData.entrySet()) {
                            String date = entry.getKey();
                            List<Map<String, Object>> theatersData = (List<Map<String, Object>>) entry.getValue();
                            List<Theater> theaters = new ArrayList<>();

                            for (Map<String, Object> theaterData : theatersData) {
                                String theaterName = (String) theaterData.get("theaterName");

                                Map<String, List<Integer>> showingTimeMap = new HashMap<>();

                                // Iterate over the entries of the showingTime map
                                Map<String, List<Long>> showingTimeData = (Map<String, List<Long>>) theaterData.get("showingTime");
                                for (Map.Entry<String, List<Long>> timeEntry : showingTimeData.entrySet()) {
                                    String timeKey = timeEntry.getKey();
                                    List<Integer> timeList = new ArrayList<>();

                                    // Convert Long to Integer
                                    for (Long time : timeEntry.getValue()) {
                                        timeList.add(time.intValue());
                                    }

                                    showingTimeMap.put(timeKey, timeList);
                                }

                                Theater theater = new Theater(theaterName, showingTimeMap);
                                theaters.add(theater);
                            }

                            // Add the date and theaters to the schedules map
                            schedules.put(date, theaters);
                        }
                    }
                    Movie movie = new Movie(duration, name, imageUrl, trailerUrl, movieId, schedules);
                    res[0] = movie;
                    listener.onMovieDataReceived(movie);
                } else {
                    // The document doesn't exist
                    listener.onMovieDataError("Movie not found");
                    Log.d("Movie Data", "Movie not found");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle errors
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });
        if (res[0] == null) Log.d("Error", "Can not pass to outside");
        return res[0];
    }

    public void updateSeatList(BookingHistory bookingHistory) {

        String movieId = bookingHistory.getMovieId();
        String date = bookingHistory.getDate();
        String theaterName = bookingHistory.getCinemaName();
        String timeEntryKey = bookingHistory.getTime();
        getMovieById(movieId, new DataMovieController.OnMovieDataReceivedListener() {
            @Override
            public void onMovieDataReceived(Movie movie) {
                DocumentReference movieDoc = movieRef.document(movieId);
                List<Integer> newSeatList = null;
                List<Theater> theaterListData = movie.getSchedules().get(date);
                for (Theater t : theaterListData) {
                    if (t.getName().equals(theaterName)) {
                        newSeatList = t.getSeatList(timeEntryKey);
                        for (Integer pos : bookingHistory.getTickets()) {
                            newSeatList.set(pos, SeatStatusEnum.STATUS_BOOKED);
                        }
                    }
                }
                List<Integer> finalNewSeatList = newSeatList;
                movieDoc.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the current data of the movie
                        Map<String, Object> movieData = documentSnapshot.getData();

                        // Get the schedules data
                        Map<String, List<Map<String, Object>>> schedulesDataByDate = (Map<String, List<Map<String, Object>>>) movieData.get("schedules");

                        // Find the schedule for the specified date
                        List<Map<String, Object>> theaterList = schedulesDataByDate.get(date);

                        if (theaterList != null) {
                            // Find the theater with the specified name
                            for (Map<String, Object> theaterData : theaterList) {
                                String currentTheaterName = (String) theaterData.get("theaterName");

                                if (currentTheaterName != null && currentTheaterName.equals(theaterName)) {
                                    // Update the showing time for the specified timeEntryKey
                                    Map<String, Object> showingTimeData = (Map<String, Object>) theaterData.get("showingTime");

                                    if (showingTimeData != null) {
                                        showingTimeData.put(timeEntryKey, finalNewSeatList);

                                        // Update the schedules data back to Firestore
                                        movieDoc.update("schedules", schedulesDataByDate).addOnSuccessListener(aVoid -> Log.d("Update showing time", "Showing time update successful")).addOnFailureListener(e -> Log.e("Update showing time", "Error updating showing time: " + e));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }).addOnFailureListener(e -> Log.e("Update showing time", "Error getting movie document: " + e));
            }

            @Override
            public void onMovieDataError(String errorMessage) {

            }
        });
    }
}
