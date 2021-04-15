package com.tiff.tiffinbox.Seller.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ViewRecipe {
    public String desc;
    public String imageURL;
    public String price;
    public String recipe;
    public ViewRecipe() {
    }

    public ViewRecipe(String desc, String imageURL, String price) {
        this.desc = desc;
        this.imageURL = imageURL;
        this.price = price;
    }

    public ViewRecipe(String desc, String imageURL, String price, String recipe) {
        this.desc = desc;
        this.imageURL = imageURL;
        this.price = price;
        this.recipe = recipe;
    }
//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("uid", uid);
//        result.put("author", author);
//        result.put("title", title);
//        result.put("body", body);
//        result.put("starCount", starCount);
//        result.put("stars", stars);
//
//        return result;
//    }
    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
