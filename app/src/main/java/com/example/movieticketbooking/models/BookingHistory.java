package com.example.movieticketbooking.models;

import java.util.List;

public class BookingHistory {
    public static final String STATUS_PAID = "PAID";
    public static final String STATUS_NOT_PAID = "NOT PAID";
    private String bookingId, movieId, userId; // Unique identifier for each booking
    // Add other fields
    private String movieName;
    private String cinemaName;
    private String date;
    private String time;
    List<Integer> tickets;
    private double ticketCost;
    private String imageUrl;

    // "Paid" or "Not Paid"
    private String status;

    // Empty constructor needed for Firestore

    public List<Integer> getTickets() {
        return tickets;
    }

    // Constructor
    public BookingHistory(String bookingId, String userId, String movieId, String movieName, String cinemaName,
                          String date, String time, List<Integer> tickets, double ticketCost,
                          String imageUrl, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.movieId = movieId;
        this.movieName = movieName;
        this.cinemaName = cinemaName;
        this.date = date;
        this.time = time;
        this.tickets = tickets;
        this.ticketCost = ticketCost;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public double getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(double ticketCost) {
        this.ticketCost = ticketCost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
