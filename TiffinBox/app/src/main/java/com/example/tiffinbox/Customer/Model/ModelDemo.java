package com.example.tiffinbox.Customer.Model;

public class ModelDemo {
    private int image;
    private String title;
    private String desc;
    private String price;

    public static String imageURL;

    public ModelDemo() {
    }

    public ModelDemo(int image, String title, String desc) {
        this.image = image;
        this.title = title;
        this.desc = desc;
    }

    public ModelDemo(String imageURL, String title, String desc) {
        this.imageURL = imageURL;
        this.title = title;
        this.desc = desc;
    }
    public ModelDemo(String imageURL, String title, String desc, String price) {
        this.imageURL = imageURL;
        this.title = title;
        this.desc = desc;
        this.price = price;
    }
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
