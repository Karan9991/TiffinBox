package com.tiff.tiffinbox.Customer.Model

import java.io.Serializable

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */
class CardModel : Serializable {
    var isSelected = false
    var imageId = 0
        private set
    var title = 0
        private set
    var imageTitle: String? = null
    @JvmField
    var imageURL: String? = null
    var name: String? = null
    var address: String? = null
    @JvmField
    var email: String? = null
    var image: String? = null
    @JvmField
    var mobile: String? = null

    constructor(imageURL: String?) {
        this.imageURL = imageURL
    }

    constructor(name: String?, address: String?) {
        this.name = name
        this.address = address
    }

    constructor(isSelected: Boolean, imageTitle: String?) {
        this.isSelected = isSelected
        this.imageTitle = imageTitle
    }

    constructor() {}
    constructor(name: String?, address: String?, imageURL: String?) {
        this.name = name
        this.address = address
        this.imageURL = imageURL
    }

    constructor(name: String?, address: String?, imageURL: String?, email: String?) {
        this.name = name
        this.address = address
        this.imageURL = imageURL
        this.email = email
    }

    constructor(name: String?, address: String?, imageURL: String?, email: String?, mobile: String?) {
        this.name = name
        this.address = address
        this.imageURL = imageURL
        this.email = email
        this.mobile = mobile
    }

    constructor(imageId: Int, titleId: Int) {
        this.imageId = imageId
        title = titleId
    }
}