package com.example.movieticketbooking.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movieticketbooking.models.BookingHistory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBookingHistoryController {
    FirebaseFirestore db;
    CollectionReference movieRef;
    Context context;
    private static final int MAX_SELECT_MOVIE  = 929;
    public DataBookingHistoryController(Context context){
        this.db = FirebaseFirestore.getInstance();
        this.movieRef = db.collection("movies");
        this.context = context;
    }
    public interface OnBookingAddedListener {
        void onBookingHistoryDataSuccess();
        void onBookingHistoryDataError(String errorMessage);
    }
    public interface OnBookingReceivedListener {
        void onBookingHistoryListReceived(List<BookingHistory> bookingHistories);
        void onBookingHistoryDataError(String errorMessage);
    }
    public interface OnBookingUpdatedListener {
        void onBookingHistoryUpdateSuccess();
        void onBookingHistoryUpdateError(String errorMessage);
    }

    public void getBookingHistoryByUserId(String userId, OnBookingReceivedListener callback) {
        // Reference to the "booking_histories" collection
        CollectionReference bookingHistoryCollection = db.collection("booking_histories");

        // Create a query to retrieve documents where the 'userId' field matches the provided userId
        bookingHistoryCollection.whereEqualTo("userId", userId)
                .orderBy(FieldPath.documentId())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // List to store retrieved booking history documents
                    List<BookingHistory> bookingHistories = new ArrayList<>();

                    // Iterate through the documents and convert them to BookingHistory objects
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String bookingId = document.getId();
                        String movieId = document.getString("movieId");
                        String movieName = document.getString("movieName");
                        String cinemaName = document.getString("theaterName");
                        String date = document.getString("date");
                        String time = document.getString("time");
                        List<Long> ticketsLong = (List<Long>) document.get("tickets");
                        List<Integer> tickets = new ArrayList<>();
                        ticketsLong.forEach(t -> tickets.add(t.intValue()));
                        double ticketCost = document.getDouble("totalPrice");
                        String imageUrl = document.getString("imgUrl");
                        String status = document.getString("status");

                        // Create BookingHistory object
                        BookingHistory bookingHistory = new BookingHistory(
                                bookingId, userId, movieId, movieName, cinemaName,
                                date, time, tickets, ticketCost, imageUrl, status);
                        bookingHistories.add(bookingHistory);
                    }

                    // Callback with the list of booking histories
                    callback.onBookingHistoryListReceived(bookingHistories);
                })
                .addOnFailureListener(e -> {
                    // Callback with an error message
                    Log.e("BookingHistory", "Error getting documents: " + e);
                    callback.onBookingHistoryDataError(e.getMessage());
                });
    }
    public void addBookingHistory(BookingHistory bookingHistory,  OnBookingAddedListener callback) {
        // Reference to the "bookingHistory" collection
        CollectionReference bookingHistoryCollection = db.collection("booking_histories");

        // Create a new document with an auto-generated ID
        DocumentReference newBookingHistoryRef = bookingHistoryCollection.document();

        // Create a map with the data to be added
        Map<String, Object> bookingHistoryData = new HashMap<>();

        bookingHistoryData.put("userId", bookingHistory.getUserId());
        bookingHistoryData.put("movieId", bookingHistory.getMovieId());
        bookingHistoryData.put("movieName", bookingHistory.getMovieName());
        bookingHistoryData.put("theaterName", bookingHistory.getCinemaName());
        bookingHistoryData.put("date", bookingHistory.getDate());
        bookingHistoryData.put("time", bookingHistory.getTime());
        bookingHistoryData.put("tickets", bookingHistory.getTickets());
        bookingHistoryData.put("totalPrice", bookingHistory.getTicketCost());
        bookingHistoryData.put("imgUrl", bookingHistory.getImageUrl());
        bookingHistoryData.put("status", bookingHistory.getStatus());

        // Set the data to the document
        newBookingHistoryRef.set(bookingHistoryData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void Void) {
                        Log.d("BookingHistory", "Document added successfully");
                        callback.onBookingHistoryDataSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BookingHistory", "Error adding document" + e);
                        callback.onBookingHistoryDataError(e.getMessage());
                    }
                });
    }
    public void updateBookingStatus(String bookingId, String newStatus, OnBookingUpdatedListener callback) {
        // Reference to the "booking_histories" collection
        CollectionReference bookingHistoryCollection = db.collection("booking_histories");

        // Reference to the specific document using the bookingId
        DocumentReference bookingHistoryDocRef = bookingHistoryCollection.document(bookingId);

        // Create a map with the data to be updated
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", newStatus);

        // Update the data in the document
        bookingHistoryDocRef.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("BookingHistory", "Document updated successfully");
                        callback.onBookingHistoryUpdateSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BookingHistory", "Error updating document" + e);
                        callback.onBookingHistoryUpdateError(e.getMessage());
                    }
                });
    }
}
