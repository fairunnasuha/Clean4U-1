package com.example.clean4u;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.clean4u.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;
    private DatabaseReference servicesReference;

    public ServiceAdapter(Context context, List<Service> serviceList, DatabaseReference servicesReference) {
        this.context = context;
        this.serviceList = serviceList;
        this.servicesReference = servicesReference;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView companyNameTextView;
        private TextView serviceTypeTextView;
        private ImageView companyImageView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            companyNameTextView = itemView.findViewById(R.id.companyNameTextView);
            serviceTypeTextView = itemView.findViewById(R.id.serviceTypeTextView);
            companyImageView = itemView.findViewById(R.id.companyImageView);

            // Set a click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Service service = serviceList.get(position);
                        showDeleteConfirmationDialog(service);
                    }
                }
            });
        }

        public void bind(Service service) {
            companyNameTextView.setText(service.getCompanyName());
            serviceTypeTextView.setText(service.getServiceType());
            Glide.with(context).load(service.getImageUrl()).into(companyImageView);
        }
    }

    private void showDeleteConfirmationDialog(Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Service");
        builder.setMessage("Are you sure you want to delete this service?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteService(service);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Dismiss the dialog
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void deleteService(Service service) {
        String serviceType = service.getServiceType();
        String serviceId = service.getServiceId();

        System.out.println(serviceId);
        if (serviceId != null) {
            servicesReference.child(serviceType).child(serviceId).removeValue()
                    .addOnSuccessListener(aVoid -> {
                        // Item deleted successfully
                    })
                    .addOnFailureListener(e -> {
                        // Failed to delete item
                    });
        }
    }

    public void setServiceList(List<Service> serviceList) {
        this.serviceList = serviceList;
    }
}
