package com.tiff.tiffinbox.Seller.addCustomers.Model

import java.io.Serializable

class YourCustomerModel : Serializable {
    var id: String? = null
    var name: String? = null
    var email: String? = null
    var mobile: String? = null
    var address: String? = null

    constructor() {}
    constructor(name: String?, email: String?, mobile: String?) {
        this.name = name
        this.email = email
        this.mobile = mobile
    }

    constructor(name: String?, email: String?, mobile: String?, address: String?) {
        this.name = name
        this.email = email
        this.mobile = mobile
        this.address = address
    }

    constructor(id: String?, name: String?, email: String?, mobile: String?, address: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.mobile = mobile
        this.address = address
    }
}