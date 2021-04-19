package com.tiff.tiffinbox.Customer.Model

class ModelDemo {
    var image = 0
    var title: String? = null
    var desc: String? = null
    var price: String? = null
    @JvmField
    var imageURL: String? = null

    constructor() {}
    constructor(image: Int, title: String?, desc: String?) {
        this.image = image
        this.title = title
        this.desc = desc
    }

    constructor(imageURL: String?, title: String?, desc: String?) {
        this.imageURL = imageURL
        this.title = title
        this.desc = desc
    }

    constructor(imageURL: String?, title: String?, desc: String?, price: String?) {
        this.imageURL = imageURL
        this.title = title
        this.desc = desc
        this.price = price
    }
}