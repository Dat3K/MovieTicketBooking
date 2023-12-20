package com.example.movieticketbooking.models;

import java.util.List;
import java.util.Map;

public class Theater {
    Map<String, List<Integer>> showingTime;
    String name;
    public Theater(String name,   Map<String,List<Integer>> showingTime) {
        this.name = name;
        this.showingTime = showingTime;
    }
    public  Map<String,List<Integer>> getSchedules() {
        return showingTime;
    }
    public String getName() {
        return name;
    }

    public Map<String, List<Integer>> getShowingTime() {
        return showingTime;
    }

    public List<Integer> getSeatList(String time) {
        return showingTime.get(time);
    }

}
