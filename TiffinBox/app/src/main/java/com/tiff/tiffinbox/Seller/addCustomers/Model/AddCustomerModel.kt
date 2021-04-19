package com.tiff.tiffinbox.Seller.addCustomers.Model

import java.io.Serializable

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */
class AddCustomerModel : Serializable {
    var id: String? = null
    var name: String? = null
    @JvmField
    var email: String? = null
    @JvmField
    var mobile: String? = null
    @JvmField
    var address: String? = null

    constructor() {}
    constructor(name: String?, email: String?, mobile: String?) {
        this.name = name
        this.email = email
        this.mobile = mobile
    }

    constructor(id: String?, name: String?, email: String?, mobile: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.mobile = mobile
    }

    constructor(id: String?, name: String?, email: String?, mobile: String?, address: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.mobile = mobile
        this.address = address
    }
}