package com.example.tiffinbox.Seller.Model;

public class AddRecipe {
    private String imageURL;
    private String price;
    private String desc;

    public AddRecipe() {
    }

    public AddRecipe(String imageURL, String price, String desc) {
        this.imageURL = imageURL;
        this.price = price;
        this.desc = desc;
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
