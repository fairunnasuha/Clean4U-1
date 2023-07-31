package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clean4u.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.integrity.internal.e;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {

    EditText nameu;
    EditText phoneu;
    EditText emailu;
    EditText passwordu;
    FirebaseAuth mAuth;
    Button signUpu;

    FirebaseDatabase db;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        signUpu=findViewById(R.id.signUp);
        nameu =findViewById(R.id.name);
        phoneu=findViewById(R.id.phone);
        emailu = findViewById(R.id.email);
        passwordu = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

        signUpu.setOnClickListener(view ->{
            createUser();
        });


    }

    private void createUser(){
        String name = nameu.getText().toString();
        String phone = phoneu.getText().toString();
        String email = emailu.getText().toString();
        String password = passwordu.getText().toString();

        if (TextUtils.isEmpty(email)){
            emailu.setError("Email cannot be empty");
            emailu.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordu.setError("Password cannot be empty");
            passwordu.requestFocus();
        }else if (TextUtils.isEmpty(phone)) {
            passwordu.setError("Phone Number cannot be empty");
            passwordu.requestFocus();
        } else if (TextUtils.isEmpty(name)) {
            passwordu.setError("Name cannot be empty");
            passwordu.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String uid = FirebaseAuth.getInstance().getUid();
                        if (uid !=null){

                            // User registered successfully, now save user data to Realtime Firebase
                            saveUserDataToFirebase(uid, name, phone, email);
                            Toast.makeText(RegisterUser.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            navigateToMainActivity();
                        }

                    }else{
                        Toast.makeText(RegisterUser.this, "Registration error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void saveUserDataToFirebase(String userId, String name, String phone, String email) {
        DatabaseReference usersRef = mReference.child("users").child("customers").child(userId);
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("phone", phone);
        userData.put("email", email);

        usersRef.setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    // Data saved successfully to Firebase
                    Toast.makeText(RegisterUser.this, "Successful to save user data: ", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(RegisterUser.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterUser.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}