package com.example.movieticketbooking.controllers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.movieticketbooking.models.Movie;
import com.example.movieticketbooking.models.Theater;
import com.example.movieticketbooking.utils.StringUtil;
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
import java.util.Random;

public class FakerMovieController {
    FirebaseFirestore db;
    CollectionReference movieRef;
    Context context;
    public FakerMovieController(Context context){
        this.db = FirebaseFirestore.getInstance();
        this.movieRef = db.collection("movies");
        this.context = context;
    }



    public void addMovieDataList() {
        List<Movie> movieDataList = new ArrayList<>();
        movieDataList.add(new Movie(90, "Inception", "https://occ-0-2794-2219.1.nflxso.net/dnm/api/v6/6AYY37jfdO6hpXcMjf9Yu5cnmO0/AAAABRVyfdUZ3FTgcABYYFO-crpJEfVTK5xAERRGhcixM9s1WaOdyI2RQZSuL7DLAYjRk_X0-Nk1YmCoJaIToQs2lSdrF4zo2tusqBBM.jpg?r=422",
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/YoHD9XEInc0?si=iJn8tpgyIoXR8Tpv\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(100,"Lời nguyền ","https://www.cgv.vn/media/catalog/product/cache/1/image/c5f0a1eff4c394a251036189ccddaacd/s/h/shiraisan_main_poster_1_.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oa4rSoF5_3w?si=qG2ygrzsbhVGbYff\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(100,"Lời nguyền ","https://www.cgv.vn/media/catalog/product/cache/1/image/c5f0a1eff4c394a251036189ccddaacd/s/h/shiraisan_main_poster_1_.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oa4rSoF5_3w?si=qG2ygrzsbhVGbYff\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(157,"ĐẤU TRƯỜNG SINH TỬ: KHÚC HÁT CỦA CHIM CA VÀ RẮN ĐỘC ","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/o/poster_payoff_1_.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/566MCdQI2Ps?si=BinQ-jch2AKLdgK3\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/566MCdQI2Ps?si=BinQ-jch2AKLdgK3\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(94,"ĐIỀU ƯỚC","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/a/payoff_poster-wish.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oyRxxpD3yNw?si=ElF557re9T6_MsX9\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(94,"ĐIỀU ƯỚC","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/a/payoff_poster-wish.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oyRxxpD3yNw?si=ElF557re9T6_MsX9\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(94,"ĐIỀU ƯỚC","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/a/payoff_poster-wish.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oyRxxpD3yNw?si=ElF557re9T6_MsX9\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(94,"ĐIỀU ƯỚC","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/p/a/payoff_poster-wish.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/oyRxxpD3yNw?si=ElF557re9T6_MsX9\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(113,"CHIẾM ĐOẠT","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/6/7/675wx1000h-chiem-doat.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/7fxTYETyjDw?si=tt32_TyX1CGyzmfp\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(119,"Yêu lại vợ ngầu","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/l/r/lr-main-poster-printing.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/wWSzkkoeolE?si=22Sh79DNgileIyMb\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(119,"Cầu hồn","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/7/0/700x1000_24__1.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/RDx_FzYM45Y?si=_mWXT-UvwAK7DDSP\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));
        movieDataList.add(new Movie(143,"Aquaman","https://www.cgv.vn/media/catalog/product/cache/1/thumbnail/190x260/2e2b8cd282892c71872b9e67d2cb5039/a/q/aquaman_poster_9x16.jpg","<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/WDkg3h8PCVU?si=11EKzVz3QHfGEUkk\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>","1",getSampleTheatersMap()));




        addMoviesToFirestore(movieDataList);
    }
    private Map<String,List<Theater>> getSampleTheatersMap(){
        Map<String,List<Theater>> theaterWithDate = new HashMap<>();
        List <String> next10Days = StringUtil.getNext10Days();
        for (String date : next10Days) {
            theaterWithDate.put(date,getSampleTheaters());
        }
        return  theaterWithDate;
    }
    public void addMoviesToFirestore(List<Movie> movies) {


        // Create a new batch
        WriteBatch batch = db.batch();

        // Reference to the "movies" collection
        CollectionReference moviesCollection = db.collection("movies");

        // Iterate through the list of movies and add each one to the batch
        for (Movie movie : movies) {
            Map<String, Object> movieData = new HashMap<>();
            movieData.put("name", movie.getName());
            movieData.put("imageUrl", movie.getImgUrl());
            movieData.put("duration", movie.getDuration());
            movieData.put("trailerUrl", movie.getTrailerUrl());

// Convert schedules to a format suitable for Firestore
            Map<String, List<Map<String, Object>>> schedulesDataByDate = new HashMap<>();
            for (Map.Entry<String, List<Theater>> entry : movie.getSchedules().entrySet()) {
                List<Map<String, Object>> theaterList = new ArrayList<>();
                for (Theater theater : entry.getValue()) {
                    Map<String, Object> theaterData = new HashMap<>();
                    theaterData.put("theaterName", theater.getName());

                    // Inserting showingTime
                    Map<String, List<Integer>> showingTime = theater.getShowingTime();
                    Map<String, Object> showingTimeData = new HashMap<>();
                    for (Map.Entry<String, List<Integer>> timeEntry : showingTime.entrySet()) {
                        showingTimeData.put(timeEntry.getKey(), timeEntry.getValue());
                    }
                    theaterData.put("showingTime", showingTimeData);


                    theaterList.add(theaterData);
                }
                schedulesDataByDate.put(entry.getKey(), theaterList);
            }
            movieData.put("schedules", schedulesDataByDate);


            DocumentReference newMovieRef = moviesCollection.document();
            batch.set(newMovieRef, movieData);
        }

        // Commit the batch
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Batch writing", "Batch write successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Batch writing", "Error batch writing documents"+ e);
                    }
                });
    }


    private List<Theater> getSampleTheaters() {
        // Sample data for testing
        List<Theater> theaters = new ArrayList<>();
        theaters.add(new Theater("Theater 1", getSampleSchedules()));
        theaters.add(new Theater("Theater 2", getSampleSchedules()));
        theaters.add(new Theater("Theater 3", getSampleSchedules()));
        theaters.add(new Theater("Theater 4", getSampleSchedules()));
        theaters.add(new Theater("Theater 5", getSampleSchedules()));
        // Add more theaters as needed
        return theaters;
    }

    private Map<String,List<Integer>> getSampleSchedules() {
        // Sample data for testing
        Map<String,List<Integer>> schedules = new HashMap<>();
        Random random = new Random();

        schedules.put("10:00 AM",generateRandomSeatData(50));
        schedules.put("2:00 PM",generateRandomSeatData(50));
        schedules.put("8:00 PM",generateRandomSeatData(50));
        schedules.put("3:00 PM",generateRandomSeatData(50));
        schedules.put("7:00 AM",generateRandomSeatData(50));
        schedules.put("3:00 AM",generateRandomSeatData(50));
        // Add more schedules as needed
        return schedules;
    }

    private List<Integer> generateRandomSeatData(int count) {
        List<Integer> randomSeatList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            // Generates 0, 1, or 2 randomly
            int randomStatus = random.nextInt(3)  ;
            if(randomStatus == 2) randomStatus = 1;
            randomSeatList.add(randomStatus - 1 ); // Maps 0 to -1, 1 to 0, and 2 to 1

        }

        return randomSeatList;
    }
}
