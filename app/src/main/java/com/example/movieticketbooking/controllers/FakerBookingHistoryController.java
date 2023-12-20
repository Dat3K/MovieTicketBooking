package com.example.movieticketbooking.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movieticketbooking.models.BookingHistory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakerBookingHistoryController {
    private FirebaseFirestore db;
    private Context context;

    public FakerBookingHistoryController(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void addDummyBookingHistoryListToDb() {
        List<BookingHistory> dummyList = generateDummyBookingHistoryList();
        addBookingHistoryList(dummyList);
    }

    private List<BookingHistory> generateDummyBookingHistoryList() {
        List<BookingHistory> dummyList = new ArrayList<>();
        List<Integer> tickets = new ArrayList<>();
        tickets.add(1);
        tickets.add(5);
        tickets.add(10);

        // Add dummy data
        dummyList.add(new BookingHistory("1", "1", "2", "Ant Man And The Wasp", "Cinema 1", "2023-11-26", "9:30 AM", tickets, 3.00, "url1", "Not Paid"));
        dummyList.add(new BookingHistory("2", "1", "3", "Avengers: Endgame", "Cinema 2", "2023-11-27", "2:00 PM", tickets, 2.50, "url2", "Not Paid"));
        // Add more dummy data as needed

        return dummyList;
    }

    private void addBookingHistoryList(List<BookingHistory> bookingHistoryList) {
        // Create a new batch
        WriteBatch batch = db.batch();

        // Reference to the "bookingHistory" collection
        CollectionReference bookingHistoryCollection = db.collection("booking_histories");

        // Iterate through the list of bookingHistory and add each one to the batch
        for (BookingHistory bookingHistory : bookingHistoryList) {
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

            DocumentReference newBookingHistoryRef = bookingHistoryCollection.document();
            batch.set(newBookingHistoryRef, bookingHistoryData);
        }

        // Commit the batch
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("BookingHistory", "Batch write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("BookingHistory", "Error batch writing documents" + e);
                    }
                });
    }
}
