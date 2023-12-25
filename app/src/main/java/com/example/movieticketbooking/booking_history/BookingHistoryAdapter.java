package com.example.movieticketbooking.booking_history;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;
import com.example.movieticketbooking.models.BookingHistory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {
    private List<BookingHistory> bookingHistories;
    private Context context;
    private OnPayButtonClickListener onPayButtonClickListener;

    public BookingHistoryAdapter(Context context, List<BookingHistory> bookingHistories) {
        this.context = context;
        this.bookingHistories = bookingHistories;
    }

    public interface OnPayButtonClickListener {
        void onPayButtonClick(int position);
    }

    public void setOnPayButtonClickListener(OnPayButtonClickListener listener) {
        this.onPayButtonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bk_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingHistory bookingHistory = bookingHistories.get(position);

        // Set data to views
        holder.movieNameTextView.setText(bookingHistory.getMovieName());
        holder.cinemaNameTextView.setText(bookingHistory.getCinemaName());
        holder.dateTextView.setText(bookingHistory.getDate());
        holder.timeTextView.setText(bookingHistory.getTime());
        holder.ticketNumberTextView.setText(String.valueOf(bookingHistory.getTickets().size()));
        holder.ticketCostTextView.setText(String.format("$%.1f", bookingHistory.getTicketCost()));
        holder.statusTextView.setText(bookingHistory.getStatus());


        Picasso.get().load(bookingHistory.getImageUrl()).into(holder.movieImageView);

        holder.ticketNumberTextView.setOnClickListener(v -> {
            String ticketStr = bookingHistory.getTickets().toString();
            Toast.makeText(context, "Ghế được chọn: " + ticketStr, Toast.LENGTH_SHORT).show();
        });
        if (!bookingHistory.getStatus().equals(BookingHistory.STATUS_PAID)) {
            holder.payButton.setVisibility(View.VISIBLE);
            holder.payButton.setOnClickListener(v -> {
                if (onPayButtonClickListener != null) {
                    onPayButtonClickListener.onPayButtonClick(position);
                }
            });
        } else {
//            Log.d("Hello","a");
            holder.payButton.setVisibility(View.VISIBLE);
            holder.payButton.setText("QR Code");
            holder.payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Generate QR code for a sample data (booking ID)
                    String bookingId = bookingHistory.getBookingId();
                    Bitmap qrCodeBitmap = QRCodeManager.generateQRCode(context, bookingId);

                    // Show the QR code in a dialog
                    QRCodeManager.showQRCodeDialog(context, qrCodeBitmap);
                }
            });
        }
        TextView mapBtn = holder.itemView.findViewById(R.id.bh_go_ggmap);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?saddr=Đại học Tôn Đức Thắng&daddr=Lotte Mart Quận 7";

                Uri locationUri = Uri.parse(uri);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, locationUri);
                Log.d("btn gg map", "clicked");
                context.startActivity(mapIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingHistories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieNameTextView, cinemaNameTextView, dateTextView, timeTextView, ticketNumberTextView, ticketCostTextView, statusTextView;
        ImageView movieImageView;
        Button payButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            movieNameTextView = itemView.findViewById(R.id.bh_movie_name);
            cinemaNameTextView = itemView.findViewById(R.id.bh_movie_theater);
            dateTextView = itemView.findViewById(R.id.bh_movie_date);
            timeTextView = itemView.findViewById(R.id.bh_movie_time);
            ticketNumberTextView = itemView.findViewById(R.id.bh_movie_ticket_number);
            ticketCostTextView = itemView.findViewById(R.id.bh_ticket_cost);
            statusTextView = itemView.findViewById(R.id.bh_ticket_status);
            movieImageView = itemView.findViewById(R.id.bh_movie_img);
            payButton = itemView.findViewById(R.id.bh_pay_button);
        }
    }
}
