package com.tiff.tiffinbox.Seller.Model

/**
 * Created by abhisheksisodia on 2017-10-06.
 */
class Message {
    var id = 0
    var from: String? = null
    var subject: String? = null
    var message: String? = null
    var timestamp: String? = null
    var picture: String? = null
    var isImportant = false
    var isRead = false
    var color = -1
    var imageTitle: String? = null
    var imageURL: String? = null

    constructor() {}
    constructor(message: String?) {
        this.message = message
    }

    constructor(imageTitle: String?, imageURL: String?) {
        this.imageTitle = imageTitle
        this.imageURL = imageURL
    }
}