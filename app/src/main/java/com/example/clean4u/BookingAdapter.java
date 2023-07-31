package com.example.clean4u;// BookingAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    private List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Load the image using Glide library
        Glide.with(holder.itemView.getContext())
                .load(booking.getImgUrl())
                .placeholder(R.drawable.person)
                .into(holder.imageView);

        holder.textDate.setText("Date: " + booking.getYear() + "-" + (booking.getMonth() + 1) + "-" + booking.getDay());
        holder.textTime.setText("Time: " + booking.getHour() + ":" + booking.getMinute());
        holder.textLocation.setText("Location: " + booking.getLatitude() + ", " + booking.getLongitude());

        // Set other TextViews with additional booking information if needed
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textDate, textTime, textLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
            textLocation = itemView.findViewById(R.id.textLocation);
        }
    }
}
