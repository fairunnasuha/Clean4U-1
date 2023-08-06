package com.example.clean4u;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private FirebaseDatabase firebaseDatabase;
    private List<Booking> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseDatabase = FirebaseDatabase.getInstance();
        bookings = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Set onMarkerClickListener to listen for marker click events
        googleMap.setOnMarkerClickListener(this);

        // Query all bookings from Firebase
        Query bookingQuery = firebaseDatabase.getReference().child("booking_service");
        System.out.println("debug 2 : "+bookingQuery);
        bookingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("debug 2 : "+dataSnapshot);
                bookings.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot bookingSnapshot : userSnapshot.getChildren()) {
                        System.out.println("debug 4 : "+bookingSnapshot);
                        // Parse the booking data
                        Booking booking = bookingSnapshot.getValue(Booking.class);
                        if (booking != null) {
                            double latitude = booking.getLatitude();
                            double longitude = booking.getLongitude();
                            String customerMarkerName = booking.getCustomerName();
                            String serviceType = booking.getServiceType();
                            String imgUrl = booking.getImgUrl();

                            System.out.println("debug 1 : "+latitude+","+longitude);
                            // Add marker for each booking
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude, longitude))
                                    .title(customerMarkerName)
                                    .snippet("Service Type: " + serviceType));

                            // Store the booking data as the marker's tag to use in onMarkerClick
                            marker.setTag(booking);

                            // Add the booking to the list
                            bookings.add(booking);
                        }
                    }
                }

                // Update the camera position to show all markers on the map
                updateCameraPosition();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Booking booking = (Booking) marker.getTag();
        if (booking != null) {
            // Create and show the popup dialog
            new AlertDialog.Builder(this)
                    .setTitle("Booking Information")
                    .setMessage("Company Name: " + booking.getCompanyName() + "\n" +
                            "Customers Name: " + booking.getCustomerName() + "\n" +
                            "Customers Email: " + booking.getCustomerEmail() + "\n" +
                            "Customers Phone: " + booking.getCustomerPhone() + "\n" +
                            "Service Type: " + booking.getServiceType() + "\n" +
                            "Date: " + booking.getYear() + "-" + (booking.getMonth() + 1) + "-" + booking.getDay() + "\n" +
                            "Time: " + booking.getHour() + ":" + booking.getMinute() + "\n") // Add other booking information as needed
                    .setPositiveButton("OK", null)
                    .show();
        }
        return true;
    }

    private void updateCameraPosition() {
        if (!bookings.isEmpty()) {
            // Get the LatLngBounds for all markers
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Booking booking : bookings) {
                double latitude = booking.getLatitude();
                double longitude = booking.getLongitude();
                builder.include(new LatLng(latitude, longitude));
            }

            // Set padding to leave space for any views
            int padding = 100; // Adjust the padding as needed

            // Build the LatLngBounds and create a CameraUpdate to move the camera
            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            // Move the camera to show all markers on the map
            googleMap.animateCamera(cameraUpdate);
        }
    }
}
