package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.OnMapClickListener {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText companyNameEditText;
    private Spinner serviceTypeSpinner;
    private Button addImageButton;
    private Button addServiceButton;
    private ImageView companyImageView;
    private Button adminLocation;
    private EditText adminPrice;

    private Uri imageUri;
    private ProgressDialog progressDialog;
    private DatabaseReference servicesReference;
    private StorageReference storageReference;
    private Button viewServicesButton;

    private FirebaseAuth mAuth;
    private Button logoutadmin;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userLocationMarker;
    private LatLng userLatLng; // Store user's latitude and longitude in a global variable
    private Marker clickedMarker;

    private double longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        companyNameEditText = findViewById(R.id.companyNameEditText);
        serviceTypeSpinner = findViewById(R.id.serviceTypeSpinner);
        addImageButton = findViewById(R.id.addImageButton);
        addServiceButton = findViewById(R.id.addServiceButton);
        companyImageView = findViewById(R.id.companyImageView);
        viewServicesButton = findViewById(R.id.viewServicesButton);
        adminPrice = findViewById(R.id.adminprice);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");

        servicesReference = FirebaseDatabase.getInstance().getReference().child("services");
        storageReference = FirebaseStorage.getInstance().getReference().child("company_images");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.service_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceTypeSpinner.setAdapter(adapter);

        addImageButton.setOnClickListener(view -> pickImageFromGallery());

        Button viewBookingsButton = findViewById(R.id.viewBookingsButton);

        viewBookingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch MapActivity to view all bookings on the map
                startActivity(new Intent(AddServiceActivity.this, MapsActivity.class));
            }
        });
        addServiceButton.setOnClickListener(view -> addService());

        viewServicesButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddServiceActivity.this, ViewServiceActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();
        logoutadmin = findViewById(R.id.logoutadmin);

        logoutadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(AddServiceActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AddServiceActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragmentadmin);
        mapFragment.getMapAsync(this);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            companyImageView.setImageURI(imageUri); // Display the selected image in the ImageView
            companyImageView.setVisibility(View.VISIBLE);

            // Optionally, you can also hide the "Add Image" button after selecting an image
            //addImageButton.setVisibility(View.GONE);
        }
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

        // Set onMapClickListener to listen for map click events
        googleMap.setOnMapClickListener(this);
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
        clickedMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Get the latitude and longitude of the clicked location
         latitude = latLng.latitude;
         longitude = latLng.longitude;

        // You can use latitude and longitude as needed
        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
    }

    private void addService() {
        String companyName = companyNameEditText.getText().toString().trim();
        String serviceType = serviceTypeSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(companyName)) {
            companyNameEditText.setError("Company name cannot be empty");
            companyNameEditText.requestFocus();
        } else if (TextUtils.isEmpty(serviceType) || serviceType.equals("Select Service Type")) {
            Toast.makeText(this, "Please select a valid service type", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(this, "Please select an image for the company", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();

            // Generate a unique key for the service
            String serviceId = servicesReference.child(serviceType).push().getKey();

            // Upload image to Firebase Storage
            String imageName = UUID.randomUUID().toString() + ".jpg";
            StorageReference imageRef = storageReference.child(imageName);
            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                // Get the download URL of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    // Save service details to Realtime Database
                    HashMap<String, Object> serviceData = new HashMap<>();
                    serviceData.put("companyName", companyName);
                    serviceData.put("serviceType", serviceType);
                    serviceData.put("imageUrl", imageUrl);
                    serviceData.put("serviceId", serviceId); // Set the serviceId in the data
                    serviceData.put("locationlng", Double.toString(latitude)); //set the location of branch in the data
                    serviceData.put("locationlat", Double.toString(longitude));
                    serviceData.put("servicePrice", adminPrice.getText().toString().trim());
                    servicesReference.child(serviceType).child(serviceId).setValue(serviceData)
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                companyNameEditText.setText("");
                                companyImageView.setVisibility(View.GONE);
                                Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(this, "Failed to add service: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                });
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

}
