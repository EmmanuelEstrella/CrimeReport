package com.example.emman.crimereport.Models;

public class Photo {
    private String description;
    private String location;
    private String url;

    public Photo() {

    }

    public Photo(String description, String location, String url) {
        this.description = description;
        this.location = location;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}


