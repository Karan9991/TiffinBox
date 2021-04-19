package com.tiff.tiffinbox.Seller.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class EditRecipe {
    var uid: String? = null
    var desc: String? = null
    var imageURL: String? = null
    var price: String? = null

    constructor() {}
    constructor(desc: String?, imageURL: String?, price: String?) {
        this.desc = desc
        this.imageURL = imageURL
        this.price = price
    }

    constructor(desc: String?, price: String?) {
        this.desc = desc
        this.price = price
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["uid"] = uid
        result["desc"] = desc
        result["imageURL"] = imageURL
        result["price"] = price
        return result
    }
}