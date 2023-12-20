package com.example.movieticketbooking.utils;

import android.os.Build;
import android.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StringUtil{
    static public Pair<String, Integer> parseDateString(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dateString);

            // Convert the parsed date to day of the week and day of the month
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            String dayOfWeek = new SimpleDateFormat("E", Locale.getDefault()).format(calendar.getTime());
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            return new Pair<>(dayOfWeek, dayOfMonth);
        } catch (ParseException e) {
            // Handle parsing exception
            e.printStackTrace();
            return null;
        }
    }
    static public List<String> getNext10Days() {
        List<String> next10Days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 10; i++) {
            String dateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
            next10Days.add(dateString);

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return next10Days;
    }
    public static List<String> sortDates(List<String> dateStrings) {
        // Convert strings to LocalDate objects
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }
        for (String dateString : dateStrings) {
            LocalDate date = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date = LocalDate.parse(dateString, formatter);
            }
            dates.add(date);
        }

        // Sort the list of LocalDate objects
        Collections.sort(dates);

        // Convert LocalDate objects back to strings
        List<String> sortedDateStrings = new ArrayList<>();
        for (LocalDate date : dates) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                sortedDateStrings.add(date.format(formatter));
            }
        }

        return sortedDateStrings;
    }

}

