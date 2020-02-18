package com.example.tiffinbox.Seller.Model;

public class ViewRecipe {
    public String desc;
    public String imageURL;
    public String price;

    public ViewRecipe() {
    }

    public ViewRecipe(String desc, String imageURL, String price) {
        this.desc = desc;
        this.imageURL = imageURL;
        this.price = price;
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
