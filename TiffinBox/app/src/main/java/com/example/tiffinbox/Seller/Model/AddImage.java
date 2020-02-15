package com.example.tiffinbox.Seller.Model;

public class AddImage {
    public String imageURL;
    public String imagePrice;
    public String imageDesc;

    public AddImage() {
    }

    public AddImage(String imageURL, String imagePrice, String imageDesc) {
        this.imageURL = imageURL;
        this.imagePrice = imagePrice;
        this.imageDesc = imageDesc;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImagePrice() {
        return imagePrice;
    }

    public void setImagePrice(String imagePrice) {
        this.imagePrice = imagePrice;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }
}
