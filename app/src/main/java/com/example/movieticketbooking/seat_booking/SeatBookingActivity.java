package com.example.movieticketbooking.seat_booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.booking_history.BookingHistoryActivity;
import com.example.movieticketbooking.controllers.DataBookingHistoryController;
import com.example.movieticketbooking.controllers.DataMovieController;
import com.example.movieticketbooking.models.BookingHistory;
import com.example.movieticketbooking.models.Movie;
import com.example.movieticketbooking.models.Theater;
import com.example.movieticketbooking.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SeatBookingActivity  extends AppCompatActivity {

    private List<Integer> seatList;
    private FirebaseAuth mAuth;
    private SeatAdapter seatAdapter;
    private String theater;
    private String time;
    private String date;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_booking);
        mAuth = FirebaseAuth.getInstance();

        seatList = new ArrayList<>();
        RecyclerView seatRecyclerView = findViewById(R.id.sb_seat_grid_recycler);
        seatAdapter = new SeatAdapter(seatList, this);
        initializeRecyclerView(seatRecyclerView);

        ImageView backBtn = findViewById(R.id.sb_back_activity);
        ImageView nextBtn = findViewById(R.id.sb_next_activity);

        backBtn.setOnClickListener(v -> {
            handleBackButtonClick();
        });

        nextBtn.setOnClickListener(v -> {
            handleNextButtonClick();
        });

        getMovieDataFromIntent();
    }

    private void initializeRecyclerView(RecyclerView seatRecyclerView) {
        seatAdapter.setOnSeatSelectedListener(selectedSeatCount -> {
            updateUI(selectedSeatCount);
        });

        int numColumns = 10;
        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        seatRecyclerView.setLayoutManager(layoutManager);
        seatRecyclerView.setAdapter(seatAdapter);
    }

    private void updateUI(int selectedSeatCount) {
        TextView ticketNum = findViewById(R.id.sb_ticket_num);
        TextView ticketCost = findViewById(R.id.sb_ticket_cost);

        ticketNum.setText(String.valueOf(selectedSeatCount));
        ticketCost.setText('$' + String.valueOf(selectedSeatCount * 10));
    }

    private void handleBackButtonClick() {
        List<Integer> selectedList = seatAdapter.getSelectedSeats();

        if (!selectedList.isEmpty()) {
            addBookingHistory(selectedList);
        }

        finish();
    }

    private void handleNextButtonClick() {
        List<Integer> selectedList = seatAdapter.getSelectedSeats();

        if (selectedList.isEmpty()) {
            Toast.makeText(SeatBookingActivity.this, "Hãy chọn một ghế", Toast.LENGTH_SHORT).show();
            return;
        }

        double cost = selectedList.size() * 10;

        BookingHistory bookingHistory = createBookingHistory(selectedList, cost);
        showConfirmationDialog(bookingHistory);
    }

    private BookingHistory createBookingHistory(List<Integer> selectedList, double cost) {
        String userId = mAuth.getCurrentUser().getUid();
        return new BookingHistory("1", userId, movie.getId(), movie.getName(), theater, date, time, selectedList, cost, movie.getImgUrl(), BookingHistory.STATUS_PAID);
    }

    private void showConfirmationDialog(BookingHistory bookingHistory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirmation_title);
        builder.setMessage(R.string.confirmation_message_tickets);

        builder.setPositiveButton("Yes", (dialog, which) -> {
            handleConfirmationYes(bookingHistory);
        });

        builder.setNegativeButton("No", (dialog, which) ->{
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void handleConfirmationYes(BookingHistory bookingHistory) {
        DataBookingHistoryController dataBookingHistoryController = new DataBookingHistoryController(SeatBookingActivity.this);

        dataBookingHistoryController.addBookingHistory(bookingHistory, new DataBookingHistoryController.OnBookingAddedListener() {
            @Override
            public void onBookingHistoryDataSuccess() {
                Log.d("Add data", "Success");
            }

            @Override
            public void onBookingHistoryDataError(String errorMessage) {
                Log.d("Add data", "Failed");
            }
        });

        Intent intent = new Intent(SeatBookingActivity.this, BookingHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void addBookingHistory(List<Integer> selectedList) {
        String userId = mAuth.getCurrentUser().getUid();
        double cost = selectedList.size() * 10;

        BookingHistory bookingHistory = new BookingHistory("1", userId, movie.getId(), movie.getName(), theater, date, time, selectedList, cost, movie.getImgUrl(), BookingHistory.STATUS_NOT_PAID);

        DataBookingHistoryController dataBookingHistoryController = new DataBookingHistoryController(SeatBookingActivity.this);

        dataBookingHistoryController.addBookingHistory(bookingHistory, new DataBookingHistoryController.OnBookingAddedListener() {
            @Override
            public void onBookingHistoryDataSuccess() {
                Log.d("Add data", "Success");
            }

            @Override
            public void onBookingHistoryDataError(String errorMessage) {
                Log.d("Add data", "Failed");
            }
        });
    }

    private void getMovieDataFromIntent() {
        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movieId");
        theater = intent.getStringExtra("theater");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");

        DataMovieController dataMovieController = new DataMovieController(this);
        dataMovieController.getMovieById(movieId, new DataMovieController.OnMovieDataReceivedListener() {
            @Override
            public void onMovieDataReceived(Movie movieData) {
                handleMovieData(movieData, date, theater, time);
            }

            @Override
            public void onMovieDataError(String errorMessage) {
                // Handle the error
            }
        });
    }

    private void handleMovieData(Movie mv, String date, String theaterName, String time) {
        movie = mv;

        List<Theater> theaterList = movie.getSchedules().get(date);
        Theater selectedTheater = null;
        for (Theater t : theaterList) {
            if (t.getName().equals(theaterName)) {
                selectedTheater = t;
                break;
            }
        }

        if (selectedTheater != null) {
            seatList.clear();
            seatList.addAll(selectedTheater.getSeatList(time));
            seatAdapter.notifyDataSetChanged();

            ImageView img = findViewById(R.id.mp_image);
            Picasso.get().load(movie.getImgUrl()).into(img);

            TextView nameTv = findViewById(R.id.mp_movie_name);
            nameTv.setText(movie.getName());

            Pair<String, Integer> parsedDate = StringUtil.parseDateString(date);
            TextView dateTv = findViewById(R.id.sb_date);
            dateTv.setText(parsedDate.first + ", " + String.valueOf(parsedDate.second));

            TextView timeTv = findViewById(R.id.sb_time);
            timeTv.setText(time);

            TextView theaterTv = findViewById(R.id.sb_theater_name);
            theaterTv.setText(theaterName);
        }
    }

    @Override
    protected void onDestroy() {
        releaseResources();
        super.onDestroy();
    }

    private void releaseResources() {
        if (seatAdapter != null) {
            Log.d("Cleat RS", "Seat Adapter");
            seatAdapter.releaseResources();
        }
    }
}

