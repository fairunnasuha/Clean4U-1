package com.example.clean4u;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class bookingpage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker clickedMarker;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private DatabaseReference usersReference;
    Button bookingbtn;
    TextView backarrow;

    String imgUrl, companyName, serviceType;
    private Button selectDate;

    private DatabaseReference bookingServiceRef;
    private String name,email,phoneNumber,imageUrlCust,fullName,firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);

        imgUrl = getIntent().getStringExtra("companyImageUrl");
        companyName = getIntent().getStringExtra("companyName");
        serviceType = getIntent().getStringExtra("companyType");

        bookingbtn = findViewById(R.id.bookingbtn);
        selectDate = findViewById(R.id.selectDate);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        bookingServiceRef = firebaseDatabase.getReference().child("booking_service");

        loadUserDataFromFirebase();
        bookingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if all required information is available
                if (selectedYear == 0 || selectedMonth == 0 || selectedDay == 0 || selectedHour == 0 || selectedMinute == 0) {
                    // Date and time are not selected
                    Toast.makeText(bookingpage.this, "Please select a date and time.", Toast.LENGTH_SHORT).show();
                } else if (clickedMarker == null) {
                    // Location is not selected
                    Toast.makeText(bookingpage.this, "Please select a location.", Toast.LENGTH_SHORT).show();
                } else if (serviceType == null || serviceType.isEmpty()) {
                    // Service type is not available
                    Toast.makeText(bookingpage.this, "Service type is missing.", Toast.LENGTH_SHORT).show();
                } else if (companyName == null || companyName.isEmpty()) {
                    // Company name is not available
                    Toast.makeText(bookingpage.this, "Company name is missing.", Toast.LENGTH_SHORT).show();
                } else if (imgUrl == null || imgUrl.isEmpty()) {
                    // Image URL is not available
                    Toast.makeText(bookingpage.this, "Image URL is missing.", Toast.LENGTH_SHORT).show();
                } else {
                    // All required information is available
                    // You can proceed with booking or store the data in the booking service node
                    // For demonstration purposes, let's show a confirmation toast
                    String confirmationMessage = "Booking confirmed!\n" +
                            "Date: " + selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay + "\n" +
                            "Time: " + selectedHour + ":" + selectedMinute + "\n" +
                            "Location: " + clickedMarker.getPosition() + "\n" +
                            "Service Type: " + serviceType + "\n" +
                            "Company Name: " + companyName + "\n" +
                            "Image URL: " + imgUrl;

                    Toast.makeText(bookingpage.this, confirmationMessage, Toast.LENGTH_LONG).show();

                    // Store the data in the booking service node using Firebase
                    storeBookingData();
                }
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePickerDialog();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Toast.makeText(this, "Error initializing map!", Toast.LENGTH_SHORT).show();
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    private void loadUserDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference = FirebaseDatabase.getInstance().getReference().child("users").child("customers");
            usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                         name = dataSnapshot.child("name").getValue(String.class);
                         phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                         email = dataSnapshot.child("email").getValue(String.class);
                         imageUrlCust = dataSnapshot.child("imageUrl").getValue(String.class); // Assuming the image URL is stored in the "imageUrl" field

                         fullName = name;
                        String[] nameParts = fullName.split("\\s+");

                        // The first element in the array will be the first name
                         firstName = nameParts[0];


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }
    private void showDateTimePickerDialog() {
        // Get the current date and time
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        // Set the selected date
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = day;

                        // Show the time picker dialog
                        showTimePickerDialog();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void storeBookingData() {
        // Create a unique key for the booking entry
        String bookingKey = bookingServiceRef.push().getKey();

        // Create a Booking object with all the data
        Booking booking = new Booking(
                selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute,
                clickedMarker.getPosition().latitude, clickedMarker.getPosition().longitude,
                serviceType, companyName, imgUrl,bookingKey,name,imageUrlCust,email,phoneNumber
        );
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Store the booking data in the Firebase Realtime Database
        assert bookingKey != null;
        assert currentUser != null;
        bookingServiceRef.child(currentUser.getUid()).child(bookingKey).setValue(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Booking data successfully stored

                        Toast.makeText(bookingpage.this, "Booking confirmed!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to store booking data
                        Toast.makeText(bookingpage.this, "Failed to confirm booking. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showTimePickerDialog() {
        // Get the current time
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Show the time picker dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        // Set the selected time
                        selectedHour = hour;
                        selectedMinute = minute;

                        // Show the selected date and time in a Toast (you can use it as needed)
                        Toast.makeText(bookingpage.this, "Selected Date: " + selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay +
                                "\nSelected Time: " + selectedHour + ":" + selectedMinute, Toast.LENGTH_SHORT).show();
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, get the last known location and focus the camera
            getCurrentLocationAndFocusCamera();
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set onMapClickListener to listen for map click events
        googleMap.setOnMapClickListener(this);
    }

    private void getCurrentLocationAndFocusCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Get the user's current location
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            // Add a marker for the user's current location
                            LatLng currentLocation = new LatLng(latitude, longitude);
                            googleMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));

                            // Move the camera to the user's current location
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        } else {
                            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the last known location and focus the camera
                getCurrentLocationAndFocusCamera();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Create and show the confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Confirm Selection")
                .setMessage("Do you want to select this location?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User confirmed, add the marker and get latitude and longitude
                        handleMarkerSelection(latLng);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User declined, do nothing
                    }
                })
                .show();
    }

    private void handleMarkerSelection(LatLng latLng) {
        // Remove the previously clicked marker (if any)
        if (clickedMarker != null) {
            clickedMarker.remove();
        }

        // Add a new marker at the clicked location with a blue color
        clickedMarker = googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Get the latitude and longitude of the clicked location
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        // You can use latitude and longitude as needed
        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
    }
}
