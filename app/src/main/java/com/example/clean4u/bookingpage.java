package com.example.clean4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class bookingpage extends AppCompatActivity {

    Button bookingbtn;
    TextView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookingpage);

        bookingbtn = findViewById(R.id.bookingbtn);
        bookingbtn.setOnClickListener(view -> {
            startActivity(new Intent(bookingpage.this, Home.class));
        });
    }
}