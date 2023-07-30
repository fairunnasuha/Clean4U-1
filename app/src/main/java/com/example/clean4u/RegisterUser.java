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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

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
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = mReference.child("User").child("posts").child(mAuth.getCurrentUser().getUid()).child("profile");
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("useremail", emailu);
                            hashMap.put("full name", name);
                            hashMap.put("age", phone);

                            databaseReference.setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //Data successfully stored
                                            Toast.makeText(RegisterUser.this, "Data Stored", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Handle any errors
                                        }
                                    });
                        }
                        Toast.makeText(RegisterUser.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                    }else{
                        Toast.makeText(RegisterUser.this, "Registration error:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}