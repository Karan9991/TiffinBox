package com.tiff.tiffinbox.Seller.Model

class AddRecipe {
    var imageURL: String? = null
    var price: String? = null
    var desc: String? = null

    constructor() {}
    constructor(imageURL: String?, price: String?, desc: String?) {
        this.imageURL = imageURL
        this.price = price
        this.desc = desc
    }
}