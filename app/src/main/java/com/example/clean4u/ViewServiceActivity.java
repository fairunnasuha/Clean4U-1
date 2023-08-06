package com.example.clean4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.clean4u.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewServiceActivity extends AppCompatActivity implements ServiceAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ServiceAdapter serviceAdapter;
    private DatabaseReference servicesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Database reference
        servicesReference = FirebaseDatabase.getInstance().getReference().child("services");

        // Create an instance of the ServiceAdapter and pass the servicesReference
        serviceAdapter = new ServiceAdapter(this, new ArrayList<>(), servicesReference,this::onItemClick);
        recyclerView.setAdapter(serviceAdapter);

        // Load services from Firebase
        loadServices();
    }



    private void loadServices() {
        // Add a ValueEventListener to retrieve the services from Firebase
        servicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Service> serviceList = new ArrayList<>();
                for (DataSnapshot typeSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot serviceSnapshot : typeSnapshot.getChildren()) {
                        Service service = serviceSnapshot.getValue(Service.class);
                        serviceList.add(service);
                    }
                }
                // Update the serviceList in the adapter and notify the changes
                serviceAdapter.setServiceList(serviceList);
                serviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled if needed
            }
        });
    }

    @Override
    public void onItemClick(Service service) {
        Intent intent = new Intent(ViewServiceActivity.this,ViewLocationCompany.class);
        intent.putExtra("companyName", service.getCompanyName());
        intent.putExtra("locationLat", service.getLocationlat());
        intent.putExtra("locationLng", service.getLocationlng());

        System.out.println("debug 23 "+service.getLocationlat());

        startActivity(intent);
    }
}
