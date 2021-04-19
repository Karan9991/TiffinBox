package com.tiff.tiffinbox.authentication.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User {
    var name: String? = null
    var mobile: String? = null
    @JvmField
    var email: String? = null
    var password: String? = null
    var address: String? = null
    var userType: String? = null
    var imageURL: String? = null
    var status: String? = null
    var id: String? = null
    var username: String? = null
    var search: String? = null

    constructor() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    constructor(email: String?) {
        this.email = email
    }

    constructor(name: String?, mobile: String?, email: String?, password: String?, address: String?, userType: String?) {
        this.name = name
        this.mobile = mobile
        this.email = email
        this.password = password
        this.address = address
        this.userType = userType
    }

    constructor(name: String?, mobile: String?, email: String?, password: String?, address: String?, userType: String?, imageURL: String?, status: String?, id: String?) {
        this.name = name
        this.mobile = mobile
        this.email = email
        this.password = password
        this.address = address
        this.userType = userType
        this.imageURL = imageURL
        this.status = status
        this.id = id
    }

    constructor(name: String?, mobile: String?, email: String?, password: String?, address: String?, userType: String?, imageURL: String?, status: String?, id: String?, username: String?, search: String?) {
        this.name = name
        this.mobile = mobile
        this.email = email
        this.password = password
        this.address = address
        this.userType = userType
        this.imageURL = imageURL
        this.status = status
        this.id = id
        this.username = username
        this.search = search
    }
}