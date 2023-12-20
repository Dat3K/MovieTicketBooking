package com.example.movieticketbooking.seat_booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import static com.example.movieticketbooking.utils.SeatStatusEnum.STATUS_AVAILABLE;
import static com.example.movieticketbooking.utils.SeatStatusEnum.STATUS_BOOKED;
import static com.example.movieticketbooking.utils.SeatStatusEnum.STATUS_SELECTED;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticketbooking.R;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter  extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private List<Integer> seatList;
    private Context context;
    List<Integer> selectedSeats ;
    private int selectedSeatCount = 0;

    public SeatAdapter(List<Integer> seatList, Context context) {
        this.seatList = seatList;
        this.context = context;
        selectedSeats = new ArrayList<>();
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sb_seat_item, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        int seatStatus = seatList.get(position);

        holder.itemView.setTag(position); // Set the seat index as the tag for the itemView

        // Set the initial status of the seat
        if (seatStatus == STATUS_BOOKED) {
            holder.sbImg.setImageResource(R.drawable.seat_booked);
            holder.itemView.setClickable(false);
        } else if (seatStatus == STATUS_SELECTED) {
            holder.sbImg.setImageResource(R.drawable.seat_selected);
            selectedSeatCount++;
            holder.itemView.setOnClickListener(v -> onSeatClick(position, holder));
        } else {
            holder.sbImg.setImageResource(R.drawable.seat_available);
            holder.itemView.setOnClickListener(v -> onSeatClick(position, holder));
        }

    }

    public List<Integer> getSelectedSeats() {
        return selectedSeats;
    }

    private void onSeatClick(int position, SeatViewHolder holder) {
        int seatStatus = seatList.get(position);

        if (seatStatus == STATUS_AVAILABLE) {
            seatList.set(position, STATUS_SELECTED); // Set as selected
            holder.sbImg.setImageResource(R.drawable.seat_selected);
            selectedSeatCount++;
        } else if (seatStatus == STATUS_SELECTED) {
            seatList.set(position, STATUS_AVAILABLE); // Set as available
            holder.sbImg.setImageResource(R.drawable.seat_available);
            selectedSeatCount--;
        }
        selectedSeats.clear();
        for (int i = 0; i < seatList.size(); i++) {
            if (seatList.get(i) == STATUS_SELECTED) {
                selectedSeats.add(i);
            }
        }
        // Notify any listeners about the change in selected seat count
        if (onSeatSelectedListener != null) {
            onSeatSelectedListener.onSeatSelected(selectedSeatCount);
        }
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public void releaseResources() {
        context = null;
        selectedSeats = null;
        onSeatSelectedListener = null;
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        ImageView sbImg;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            sbImg = itemView.findViewById(R.id.sb_img);
        }
    }

    // Interface to notify the activity/fragment about the change in selected seat count
    public interface OnSeatSelectedListener {
        void onSeatSelected(int selectedSeatCount);
    }

    private OnSeatSelectedListener onSeatSelectedListener;

    public void setOnSeatSelectedListener(OnSeatSelectedListener listener) {
        this.onSeatSelectedListener = listener;
    }
}

