package com.example.clean4u;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView profileName,profileName1;
    private TextView profilePhoneNumber;
    private TextView profileEmail;
    private CircularImageView profileImage;
    private DatabaseReference usersReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileName1 = findViewById(R.id.profileName1);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        profileEmail = findViewById(R.id.profileEmail);
        profileImage = findViewById(R.id.profileImage);

        usersReference = FirebaseDatabase.getInstance().getReference().child("users").child("customers");
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_images");

        loadUserDataFromFirebase();

        // Set click listener for the profile image to open the gallery
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Upload the selected image to Firebase Storage and get the download URL
        StorageReference imageRef = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Save the image URL to the user's profile data in the Firebase Realtime Database
                DatabaseReference userReference = usersReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                userReference.child("imageUrl").setValue(imageUrl).addOnSuccessListener(aVoid -> {
                    // Reload the user's profile data to display the updated profile image
                    loadUserDataFromFirebase();
                    Toast.makeText(this, "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserDataFromFirebase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            usersReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class); // Assuming the image URL is stored in the "imageUrl" field

                        String fullName = name;
                        String[] nameParts = fullName.split("\\s+");

                        // The first element in the array will be the first name
                        String firstName = nameParts[0];


                        profileName.setText(name);
                        profileName1.setText("Welcome " + firstName);
                        profilePhoneNumber.setText(phoneNumber);
                        profileEmail.setText(email);

                        // Load the user's profile image using Glide
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(Profile.this)
                                    .load(imageUrl)
                                    .into(profileImage);
                        } else {
                            // If the image URL is empty, you can set a default image or hide the profile image view
                            profileImage.setImageResource(R.drawable.person);
                            // profileImage.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }
}
