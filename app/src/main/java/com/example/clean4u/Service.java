package com.example.clean4u;

public class Service {
    private String companyName;
    private String serviceType;
    private String imageUrl;
    private String serviceId; // Unique key for each service

    // Required empty constructor for Firebase
    public Service() {}

    public Service(String companyName, String serviceType, String imageUrl,String serviceId) {
        this.companyName = companyName;
        this.serviceType = serviceType;
        this.imageUrl = imageUrl;
        this.serviceId = serviceId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
