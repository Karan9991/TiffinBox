package com.example.tiffinbox.Seller.Model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class EditRecipe {
    private String uid;
    private String desc;
    private String imageURL;
    private String price;

    public EditRecipe() {
    }

//    public EditRecipe(String uid, String desc, String imageURL, String price) {
//        this.uid = uid;
//        this.desc = desc;
//        this.imageURL = imageURL;
//        this.price = price;
//    }

//    public EditRecipe(String uid, String desc, String imageURL, String price, String title) {
//        this.uid = uid;
//        this.desc = desc;
//        this.imageURL = imageURL;
//        this.price = price;
//        this.title = title;
//    }

    public EditRecipe(String desc, String imageURL, String price) {
        this.desc = desc;
        this.imageURL = imageURL;
        this.price = price;
    }
    public EditRecipe(String desc, String price) {
        this.desc = desc;
        this.price = price;
    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("desc", desc);
        result.put("imageURL", imageURL);
        result.put("price", price);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
