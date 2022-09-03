package com.apps.googlemaps.models;

public class MapLocation {

    private int id;
    private String place;
    private String address;
    private Double latitude;
    private Double longitude;
    private String icon;

    public MapLocation(int id, String place, String address, String icon) {
        this.id = id;
        this.place = place;
        this.address = address;
        this.icon = icon;
    }

    public MapLocation(int id, String place, String address, Double latitude, Double longitude, String icon) {
        this.id = id;
        this.place = place;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
