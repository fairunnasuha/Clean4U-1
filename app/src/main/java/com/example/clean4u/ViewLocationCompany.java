package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ViewLocationCompany extends AppCompatActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userLocationMarker;
    private LatLng userLatLng; // Store user's latitude and longitude in a global variable
    private Marker clickedMarker;
    private double locationLat,locationLng;


    private double longitude,latitude;
    private String companyname;
    private double x;

    public ViewLocationCompany() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location_company);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragmentview);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
         companyname = intent.getStringExtra("companyName");
         locationLat = Double.parseDouble(intent.getStringExtra("locationLat"));
         locationLng = Double.parseDouble(intent.getStringExtra("locationLng"));
         String debug = intent.getStringExtra("locationLat");
         System.out.println("debug 24 "+debug);
          x = Double.parseDouble(debug);
        System.out.println("debug 24 "+x);
    }


    private void showUserCurrentLocation() {
        if (mMap == null) {
            return;
        }

        // Check for location permission, and request if necessary
        // ... (add code to handle location permission if needed)

        // Get the user's last known location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        // Add a marker at the user's current location and move the camera
                        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        userLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(userLatLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .title("Your Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(this, "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at the user's current location and move the camera
        showUserCurrentLocation();

        addMarkerToFixedLocation();
    }

    private void addMarkerToFixedLocation() {
        if (clickedMarker != null) {
            clickedMarker.remove();
        }

        final LatLng companyLatLng = new LatLng(locationLat, locationLng);

        System.out.println("123123 " + companyLatLng);

        // Add a new marker at the clicked location with a blue color
        clickedMarker = mMap.addMarker(new MarkerOptions()
                .position(companyLatLng)
                .title(companyname)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Optionally, move the camera to the marker location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(companyLatLng, 15));
    }

}