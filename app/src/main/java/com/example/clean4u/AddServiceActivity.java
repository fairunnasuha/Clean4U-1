package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText companyNameEditText;
    private Spinner serviceTypeSpinner;
    private Button addImageButton;
    private Button addServiceButton;
    private ImageView companyImageView;

    private Uri imageUri;
    private ProgressDialog progressDialog;
    private DatabaseReference servicesReference;
    private StorageReference storageReference;
    private Button viewServicesButton;

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
