package com.example.clean4u;

public class Booking {

    private String customerName,customerImgUrl,customerEmail,customerPhone;
    private String bookingKey;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private double latitude;
    private double longitude;
    private String serviceType;
    private String companyName;
    private String imgUrl;

    // Constructor
    public Booking(){
        //firebase used
    }
    public Booking(int year, int month, int day, int hour, int minute, double latitude, double longitude,
                   String serviceType, String companyName, String imgUrl, String bookingKey,String customerName,String customerImgUrl,String customerEmail,String customerPhone) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.latitude = latitude;
        this.longitude = longitude;
        this.serviceType = serviceType;
        this.companyName = companyName;
        this.imgUrl = imgUrl;
        this.bookingKey = bookingKey;
        this.customerName = customerName;
        this.customerImgUrl = customerImgUrl;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    // Getters and Setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
    public String getBookingKey() {
        return bookingKey;
    }

    public void setBookingKey(String bookingKey) {
        this.bookingKey = bookingKey;
    }
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerImgUrl() {
        return customerImgUrl;
    }

    public void setCustomerImgUrl(String customerImgUrl) {
        this.customerImgUrl = customerImgUrl;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
