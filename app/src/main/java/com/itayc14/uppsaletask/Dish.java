package com.itayc14.uppsaletask;

import com.google.gson.annotations.SerializedName;

/**
 * Created by itaycohen on 16.5.2017.
 */

public class Dish {
    @SerializedName("MenuItemId")
    private String dishID;
    @SerializedName("DefaultPrice")
    private String price;
    @SerializedName("ItemDesc")
    private String description;
    @SerializedName("Img")
    private String imgLink;

    public Dish(String price, String description, String imgLink, String dishID){
        this.price = price;
        this.description = description;
        this.imgLink = imgLink;
        this.dishID = dishID;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getDishID() {
        return dishID;
    }

    public void setDishID(String dishID) {
        this.dishID = dishID;
    }
}
