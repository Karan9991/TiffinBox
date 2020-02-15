package com.example.tiffinbox.Seller.Model;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardModel {
    private int imageId;
    private int titleId;

    public CardModel(int imageId, int titleId) {
        this.imageId = imageId;
        this.titleId = titleId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getTitle() {
        return titleId;
    }

}
