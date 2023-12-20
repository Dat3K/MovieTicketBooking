package com.example.movieticketbooking.all_movie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.booking_history.BookingHistoryActivity;
import com.example.movieticketbooking.controllers.DataMovieController;
import com.example.movieticketbooking.home.HomeActivity;
import com.example.movieticketbooking.home.MovieListAdapter;
import com.example.movieticketbooking.models.Movie;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AllMovieActivity extends AppCompatActivity {
    private RecyclerView listMoviesRecyclerView;
    private List<Movie> movieDataListInAdapter;
    private List<Movie> movieDataList;
    MovieListAdapter listMovieAdapter;
    FirebaseFirestore db;
    CollectionReference movieRef;
    private boolean isAscendingOrder = true;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //List<Movie> movieDataList = getMovieDataList();
        setContentView(R.layout.activity_all_movie);
        listMoviesRecyclerView = findViewById(R.id.home_list_movies);
        db = FirebaseFirestore.getInstance();
        movieRef = db.collection("movies");


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        listMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        movieDataListInAdapter = new ArrayList<>();
        movieDataList = new ArrayList<>();
        listMovieAdapter = new MovieListAdapter(this, movieDataListInAdapter);
        listMoviesRecyclerView.setAdapter(listMovieAdapter);
        ImageView homeBtn = findViewById(R.id.back_home);
        homeBtn.setOnClickListener(view ->{
            Intent intent = new Intent(AllMovieActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        ImageView bookingHistoryBtn = findViewById(R.id.browse_menu);
        bookingHistoryBtn.setOnClickListener(view ->{
            Intent intent = new Intent(AllMovieActivity.this, BookingHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        searchView = findViewById(R.id.browse_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query when the user presses "Enter" on the keyboard
                searchMovies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle the search query as the user types
                searchMovies(newText);
                return true;
            }
        });
        ImageView sortIcon = findViewById(R.id.browse_sorting);
        sortIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSortingOrder(view);
                sortMovies();
            }
        });
        DataMovieController dataMovieController = new DataMovieController(this);
        dataMovieController.getMovieList(-1, new DataMovieController.OnMovieDataListReceivedListener() {
            @Override
            public void onMovieDataReceived(List<Movie> movieData) {
                // Do something with the movie data
                movieDataList.clear();
                movieDataListInAdapter.addAll(movieData);
                movieDataList.addAll(movieData);
                listMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMovieDataError(String errorMessage) {
                // Handle the error
            }
        });

//        FakerMovieController fakerMovieControllerManager = new FakerMovieController(this);
//        fakerMovieControllerManager.addMovieDataList();

    }

    private void searchMovies(String query) {
        // Create a new list to store the filtered movies
        List<Movie> filteredMovies = new ArrayList<>();
        // Iterate through the original movie list and add matching movies to the filtered list
        for (Movie movie : movieDataList) {
            if (movie.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.add(movie);
            }
        }
        movieDataListInAdapter.clear();
        movieDataListInAdapter.addAll(filteredMovies);
        // Update the RecyclerView with the filtered list
        listMovieAdapter.notifyDataSetChanged();
    }



    private void toggleSortingOrder(View sortIcon) {
        isAscendingOrder = !isAscendingOrder;
        Integer sortIconId = isAscendingOrder?R.drawable.asc_sorting:R.drawable.desc_sorting;
        ((ImageView)sortIcon).setImageResource(sortIconId);

    }

    private void sortMovies() {
        Collections.sort(movieDataListInAdapter, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                // Compare based on your criteria, for example, movie name
                int result = movie1.getName().compareTo(movie2.getName());

                // Adjust result based on sorting order
                return isAscendingOrder ? result : -result;
            }
        });

        // Notify the adapter that the data set has changed
        listMovieAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        releaseResources();
        super.onDestroy();
    }

    private void releaseResources() {
        // Release any resources used in AllMoviesActivity
        // For example, release adapters, unregister listeners, etc.
        if (listMovieAdapter != null) {
            Log.d("Cleat RS","List Movie Adapter");

            listMovieAdapter.releaseResources(); // You need to implement releaseResources in MovieListAdapter
        }
    }
}

