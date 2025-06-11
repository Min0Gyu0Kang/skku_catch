package com.example.pa2;

public class UserInfo {
    private String restaurantName;
    private String restaurantImage;
    private String time;
    private String date;
    private int numberOfPeople;

    public UserInfo(String restaurantName, String restaurantImage, String time, String date, int numberOfPeople) {
        this.restaurantName = restaurantName;
        this.restaurantImage = restaurantImage;
        this.time = time;
        this.date = date;
        this.numberOfPeople = numberOfPeople;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getRestaurantImage() {
        return restaurantImage;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }
}