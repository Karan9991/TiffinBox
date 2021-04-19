package com.tiff.tiffinbox.Seller.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
class SellerProfile {
    var name: String? = null
    var mobile: String? = null
    var address: String? = null

    constructor() {}
    constructor(name: String?, mobile: String?, address: String?) {
        this.name = name
        this.mobile = mobile
        this.address = address
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        val result = HashMap<String, Any?>()
        result["name"] = name
        result["mobile"] = mobile
        result["address"] = address
        return result
    }
}