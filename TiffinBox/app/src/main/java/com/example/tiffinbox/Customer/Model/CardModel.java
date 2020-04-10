package com.example.tiffinbox.Customer.Model;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardModel {
    private boolean isSelected;
    private int imageId;
    private int titleId;
    private String imageTitle;
    public String imageURL;

    public String name;
    public String address;
    public String email;
    private String image;

    public CardModel(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CardModel(String name, String address, String imageURL) {
        this.name = name;
        this.address = address;
        this.imageURL = imageURL;
    }

    public CardModel(String name, String address, String imageURL,String email) {
        this.name = name;
        this.address = address;
        this.imageURL = imageURL;
        this.email = email;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CardModel(String name, String address) {
        this.name = name;
        this.address = address;
    }


//    public CardModel(String imageTitle, String imageURL) {
//        this.imageTitle = imageTitle;
//        this.imageURL = imageURL;
//    }

    public CardModel(boolean isSelected, String imageTitle) {
        this.isSelected = isSelected;
        this.imageTitle = imageTitle;
    }

    public CardModel() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
    public CardModel(int imageId, int titleId) {
        this.imageId = imageId;
        this.titleId = titleId;
    }


    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTitle() {
        return titleId;
    }

}