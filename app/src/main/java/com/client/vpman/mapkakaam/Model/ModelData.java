package com.client.vpman.mapkakaam.Model;

public class ModelData
{
    private String vicinity,height,width,photo_reference;
    Double rating;

    public ModelData(String vicinity, String height, String width, String photo_reference, Double rating) {
        this.vicinity = vicinity;
        this.height = height;
        this.width = width;
        this.photo_reference = photo_reference;
        this.rating = rating;
    }

    public ModelData() {
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
