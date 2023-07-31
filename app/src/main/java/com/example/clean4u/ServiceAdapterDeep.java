package com.example.clean4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.List;

public class ServiceAdapterDeep extends RecyclerView.Adapter<ServiceAdapterDeep.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Service service);
    }

    public ServiceAdapterDeep(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_b, parent, false);
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

        private ImageView companyImage;
        private TextView companyName;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            companyImage = itemView.findViewById(R.id.cardBasicCleaningCompanyImage);
            companyName = itemView.findViewById(R.id.cardBasicCleaningCompanyName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Service service = serviceList.get(position);
                            itemClickListener.onItemClick(service);
                        }
                    }
                }
            });
        }

        public void bind(Service service) {
            companyName.setText(service.getCompanyName());

            // Load the company image using Glide
            Glide.with(context)
                    .load(service.getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(companyImage);
        }
    }
}
