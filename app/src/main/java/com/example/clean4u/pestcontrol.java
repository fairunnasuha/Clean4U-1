package com.example.clean4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class pestcontrol extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ServiceAdapterBasic serviceAdapter;
    private List<Service> basicCleaningServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pestcontrol);

        recyclerView = findViewById(R.id.recyclerViewBasicCleaning);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        basicCleaningServices = new ArrayList<>();
        serviceAdapter = new ServiceAdapterBasic(this, basicCleaningServices);
        recyclerView.setAdapter(serviceAdapter);

        serviceAdapter.setOnItemClickListener(new ServiceAdapterBasic.OnItemClickListener() {
            @Override
            public void onItemClick(Service service) {
                // Navigate to the BookingPage along with required data
                Intent intent = new Intent(pestcontrol.this, bookingpage.class);
                intent.putExtra("companyName", service.getCompanyName());
                intent.putExtra("companyImageUrl", service.getImageUrl());
                intent.putExtra("companyType", service.getServiceType());
                startActivity(intent);
            }
        });

        loadBasicCleaningServicesFromFirebase();
    }

    private void loadBasicCleaningServicesFromFirebase() {
        DatabaseReference servicesReference = FirebaseDatabase.getInstance().getReference().child("services").child("Pest Control");
        servicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                basicCleaningServices.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming your Service model class has appropriate getters and setters
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        basicCleaningServices.add(service);
                    }
                }
                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }
}
