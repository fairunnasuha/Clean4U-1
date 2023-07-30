package com.example.clean4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class disinfection extends AppCompatActivity {

    CardView cardDI1;
    CardView cardDI2;
    CardView cardDI3;
    CardView cardDI4;
    CardView cardDI5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_cleaning);

        cardDI1 = findViewById(R.id.cardDC1);
        cardDI1.setOnClickListener(view -> {
            startActivity(new Intent(disinfection.this, bookingpage.class));
        });

        cardDI2 = findViewById(R.id.cardDC2);
        cardDI2.setOnClickListener(view -> {
            startActivity(new Intent(disinfection.this, bookingpage.class));
        });

        cardDI3 = findViewById(R.id.cardDC3);
        cardDI3.setOnClickListener(view -> {
            startActivity(new Intent(disinfection.this, bookingpage.class));
        });

        cardDI4 = findViewById(R.id.cardDC4);
        cardDI4.setOnClickListener(view -> {
            startActivity(new Intent(disinfection.this, bookingpage.class));
        });

        cardDI5 = findViewById(R.id.cardDC5);
        cardDI5.setOnClickListener(view -> {
            startActivity(new Intent(disinfection.this, bookingpage.class));
        });
    }
}