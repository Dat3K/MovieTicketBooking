package com.example.movieticketbooking.booking_history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.controllers.DataBookingHistoryController;
import com.example.movieticketbooking.controllers.DataMovieController;
import com.example.movieticketbooking.home.HomeActivity;
import com.example.movieticketbooking.models.BookingHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingHistoryAdapter bookHistoryAdapter;
    private List<BookingHistory> bookHistoryList;

    private FirebaseFirestore db;
    private FirebaseUser mAuth;

    private FirebaseAuth firebaseAuth;
    private CollectionReference bookingCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = firebaseAuth.getCurrentUser();
        bookingCollection = db.collection("bookings");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.bh_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookHistoryList = new ArrayList<>();
        bookHistoryAdapter = new BookingHistoryAdapter(this, bookHistoryList);
        recyclerView.setAdapter(bookHistoryAdapter);
        ImageView homeBtn = findViewById(R.id.back_home);
        homeBtn.setOnClickListener(view ->{
            Intent intent = new Intent(BookingHistoryActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        // Set the listener for Pay button clicks
        bookHistoryAdapter.setOnPayButtonClickListener(new BookingHistoryAdapter.OnPayButtonClickListener() {
            @Override
            public void onPayButtonClick(int position) {
                handlerPayButtonClicked(position);
            }
        });

        //        FakerMovieController fakerMovieController = new FakerMovieController(this);
//        fakerMovieController.addDummyBookingHistoryListToDb();
        DataBookingHistoryController dataBookingHistoryController = new DataBookingHistoryController(this);
        dataBookingHistoryController.getBookingHistoryByUserId(mAuth.getUid(), new DataBookingHistoryController.OnBookingReceivedListener() {
            @Override
            public void onBookingHistoryListReceived(List<BookingHistory> bookingHistories) {
                bookHistoryList.clear();
                bookHistoryList.addAll(bookingHistories);
                bookHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBookingHistoryDataError(String errorMessage) {

            }
        });
    }
    public void handlerPayButtonClicked(int position) {
        // Create a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(BookingHistoryActivity.this);
        builder.setTitle(R.string.confirmation_title);
        builder.setMessage(R.string.confirmation_message_bookingpaid);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, update the status in Firestore
                updateStatusInFirestore(bookHistoryList.get(position));


                // Notify the adapter that the data has changed
                bookHistoryAdapter.notifyItemChanged(position);

                // Optionally, you can show a toast or perform any other action
                Toast.makeText(BookingHistoryActivity.this, "Booking marked as paid for item at position " + position, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, do nothing
            }
        });

        // Show the dialog
        builder.show();
    }


    private void updateStatusInFirestore(BookingHistory bookHistory) {
        if (bookHistory != null) {
            bookHistory.setStatus(BookingHistory.STATUS_PAID);
            // Update the status to "Paid"
            DataBookingHistoryController dataBookingHistoryController = new DataBookingHistoryController(BookingHistoryActivity.this);
            DataMovieController dataMovieController = new DataMovieController(BookingHistoryActivity.this);
            dataMovieController.updateSeatList(bookHistory);

            dataBookingHistoryController.updateBookingStatus(bookHistory.getBookingId(), BookingHistory.STATUS_PAID, new DataBookingHistoryController.OnBookingUpdatedListener() {
                @Override
                public void onBookingHistoryUpdateSuccess() {
                    Log.d("Update bookinghistory","success");
                }

                @Override
                public void onBookingHistoryUpdateError(String errorMessage) {

                }
            });
        }
    }
}

