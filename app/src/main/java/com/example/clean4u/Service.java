package com.example.clean4u;

public class Service {
    private String companyName;
    private String serviceType;
    private String imageUrl;
    private String serviceId; // Unique key for each service
    private String locationlat;
    private String locationlng;
    private String servicePrice;

    // Required empty constructor for Firebase
    public Service() {}

    public String getLocationlat() {
        return locationlat;
    }

    public void setLocationlat(String locationlat) {
        this.locationlat = locationlat;
    }

    public String getLocationlng() {
        return locationlng;
    }

    public void setLocationlng(String locationlng) {
        this.locationlng = locationlng;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Service(String companyName, String serviceType, String imageUrl, String serviceId, String location, String price) {
        this.companyName = companyName;
        this.serviceType = serviceType;
        this.imageUrl = imageUrl;
        this.serviceId = serviceId;
        this.locationlat = locationlat;
        this.locationlng = locationlng;
        this.servicePrice = servicePrice;
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
