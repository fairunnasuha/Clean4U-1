package com.example.clean4u;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class basic_cleaning extends AppCompatActivity {

    CardView cardBC1;
    CardView cardBC2;
    CardView cardBC3;
    CardView cardBC4;
    CardView cardBC5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_cleaning);

        cardBC1 = findViewById(R.id.cardBC1);
        cardBC1.setOnClickListener(view -> {
            startActivity(new Intent(basic_cleaning.this, bookingpage.class));
        });

        cardBC2 = findViewById(R.id.cardBC2);
        cardBC2.setOnClickListener(view -> {
            startActivity(new Intent(basic_cleaning.this, bookingpage.class));
        });

        cardBC3 = findViewById(R.id.cardBC3);
        cardBC3.setOnClickListener(view -> {
            startActivity(new Intent(basic_cleaning.this, bookingpage.class));
        });

        cardBC4 = findViewById(R.id.cardBC4);
        cardBC4.setOnClickListener(view -> {
            startActivity(new Intent(basic_cleaning.this, bookingpage.class));
        });

        cardBC5 = findViewById(R.id.cardBC5);
        cardBC5.setOnClickListener(view -> {
            startActivity(new Intent(basic_cleaning.this, bookingpage.class));
        });
    }
}