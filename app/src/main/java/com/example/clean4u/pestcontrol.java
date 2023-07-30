package com.example.clean4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class pestcontrol extends AppCompatActivity {

    CardView cardPC1u;
    CardView cardPC2u;
    CardView cardPC3u;
    CardView cardPC4u;
    CardView cardPC5u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pestcontrol);

        cardPC1u = findViewById(R.id.cardPC1);
        cardPC1u.setOnClickListener(view -> {
            startActivity(new Intent(pestcontrol.this, bookingpage.class));
        });

        cardPC2u = findViewById(R.id.cardPC2);
        cardPC2u.setOnClickListener(view -> {
            startActivity(new Intent(pestcontrol.this, bookingpage.class));
        });

        cardPC3u = findViewById(R.id.cardPC3);
        cardPC3u.setOnClickListener(view -> {
            startActivity(new Intent(pestcontrol.this, bookingpage.class));
        });

        cardPC4u = findViewById(R.id.cardPC4);
        cardPC4u.setOnClickListener(view -> {
            startActivity(new Intent(pestcontrol.this, bookingpage.class));
        });

        cardPC5u = findViewById(R.id.cardPC5);
        cardPC5u.setOnClickListener(view -> {
            startActivity(new Intent(pestcontrol.this, bookingpage.class));
        });
    }
}