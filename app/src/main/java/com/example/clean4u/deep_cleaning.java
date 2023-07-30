package com.example.clean4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class deep_cleaning extends AppCompatActivity {

    CardView cardDC1;
    CardView cardDC2;
    CardView cardDC3;
    CardView cardDC4;
    CardView cardDC5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_cleaning);

        cardDC1 = findViewById(R.id.cardDC1);
        cardDC1.setOnClickListener(view -> {
            startActivity(new Intent(deep_cleaning.this, bookingpage.class));
        });

        cardDC2 = findViewById(R.id.cardDC2);
        cardDC2.setOnClickListener(view -> {
            startActivity(new Intent(deep_cleaning.this, bookingpage.class));
        });

        cardDC3 = findViewById(R.id.cardDC3);
        cardDC3.setOnClickListener(view -> {
            startActivity(new Intent(deep_cleaning.this, bookingpage.class));
        });

        cardDC4 = findViewById(R.id.cardDC4);
        cardDC4.setOnClickListener(view -> {
            startActivity(new Intent(deep_cleaning.this, bookingpage.class));
        });

        cardDC5 = findViewById(R.id.cardDC5);
        cardDC5.setOnClickListener(view -> {
            startActivity(new Intent(deep_cleaning.this, bookingpage.class));
        });
    }
}