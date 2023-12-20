package com.example.movieticketbooking.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Movie implements Serializable {
    private int duration; // Assuming duration is in minutes
    private String name;
    private String imgUrl;
    private String trailerUrl;
    private String id;
    private Map<String, List<Theater>> schedules; // On date(Fri, 17) having a list of theater, each theater having list of schedule of showwing
    public Movie(int duration, String name, String imgUrl,String trailerUrl, String id, Map<String, List<Theater>> schedules) {
        this.duration = duration;
        this.name = name;
        this.imgUrl = imgUrl;
        this.trailerUrl = trailerUrl;
        this.id = id;
        this.schedules = schedules;
    }
    public String getId() {
        return id;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public Map<String, List<Theater>> getSchedules() {
        return schedules;
    }
    public String getFormattedDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;

        // Format the duration as "1h20m" or "1h30m"
        return hours + "h" + (minutes > 0 ? minutes + "m" : "");
    }
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
    public Integer getDuration() {
        return duration;
    }
    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }


}