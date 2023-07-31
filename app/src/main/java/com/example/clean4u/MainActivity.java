package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clean4u.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Pattern;

public class   MainActivity extends AppCompatActivity {

        Button logIn;
        Button register;
        EditText emailf;
        EditText passwordf;
        EditText name;
        private FirebaseAuth mAuth;

        private String nameInDatabase;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            logIn = findViewById(R.id.logIn);
            register=findViewById(R.id.register);
            mAuth = FirebaseAuth.getInstance();
            name = findViewById(R.id.name);
            emailf = findViewById(R.id.email);
            passwordf = findViewById(R.id.password);

            logIn.setOnClickListener(view -> {
                loginUser();
            });

            register.setOnClickListener(view -> {
                startActivity(new Intent(MainActivity.this,RegisterUser.class));
            });
        }

    private void loginUser() {
        String email = emailf.getText().toString();
        String password = passwordf.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailf.setError("Email cannot be empty");
            emailf.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordf.setError("Password cannot be empty");
            passwordf.requestFocus();
        } else {
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users").child("customers");

            // Check if it's an admin login
            if (EmailValidator.isNotValidEmail(email)) {
                DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("users").child("admin");
                adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String adminPassword = dataSnapshot.child("password").getValue(String.class);
                            if (adminPassword != null && adminPassword.equals(password)) {
                                // Admin login successful
                                startActivity(new Intent(MainActivity.this, AddServiceActivity.class));
                                finish();
                                Toast.makeText(MainActivity.this, "Admin logged in successfully", Toast.LENGTH_SHORT).show();

                                // Proceed with admin-specific actions or activities if needed
                                return;
                            }
                        }
                        // Admin login failed
                        Toast.makeText(MainActivity.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error if needed
                    }
                });
            } else {
                // Regular user login
                usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email found, check password and proceed with login
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String nameInDatabase = userSnapshot.child("name").getValue(String.class);
                                if (nameInDatabase != null) {
                                    // User's name retrieved, proceed with login
                                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, Home.class));
                                            finish();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Log in error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                return; // Stop further iteration, as we found the user
                            }
                        } else {
                            // Regular user login failed
                            Toast.makeText(MainActivity.this, "Invalid user credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error if needed
                    }
                });
            }
        }
    }


}