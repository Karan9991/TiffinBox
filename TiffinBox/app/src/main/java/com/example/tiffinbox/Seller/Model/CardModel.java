package com.example.tiffinbox.Seller.Model;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardModel {
    private int imageId;
    private int titleId;
    private String imageTitle;
    private String imageURL;

    public CardModel(String imageTitle, String imageURL) {
        this.imageTitle = imageTitle;
        this.imageURL = imageURL;
    }

    public CardModel() {
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public CardModel(int imageId, int titleId) {
        this.imageId = imageId;
        this.titleId = titleId;
    }

    public CardModel(String imageTitle) {
        this.imageTitle = imageTitle;
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
