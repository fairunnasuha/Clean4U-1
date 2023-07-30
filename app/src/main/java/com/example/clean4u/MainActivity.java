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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class   MainActivity extends AppCompatActivity {

        Button logIn;
        Button register;
        EditText emailf;
        EditText passwordf;
        EditText name;
        private FirebaseAuth mAuth;

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

        private void loginUser(){
            String email = emailf.getText().toString();
            String password = passwordf.getText().toString();

            if (TextUtils.isEmpty(email)){
                emailf.setError("Email cannot be empty");
                emailf.requestFocus();
            }else if (TextUtils.isEmpty(password)){
                passwordf.setError("Password cannot be empty");
                passwordf.requestFocus();
            }else{
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, Home.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Log in error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


        void navigateToSecondActivity(){
            finish();
            Intent intent = new Intent(MainActivity.this,Home.class);
            startActivity(intent);
        }

    }