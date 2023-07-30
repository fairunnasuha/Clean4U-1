package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    CardView cardBCu;
    CardView cardDCu;
    CardView cardDIu;
    CardView  cardPCu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cardBCu = findViewById(R.id.cardBC);
        cardDCu = findViewById(R.id.cardDC);
        cardDIu = findViewById(R.id.cardDI);
        cardPCu = findViewById(R.id.cardPC);

        cardBCu.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, basic_cleaning.class));
        });

        cardDCu.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, deep_cleaning.class));
        });

        cardDIu.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, disinfection.class));
        });

        cardPCu.setOnClickListener(view -> {
            startActivity(new Intent(Home.this, pestcontrol.class));
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.service:
                        startActivity(new Intent(getApplicationContext(), Service2.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
