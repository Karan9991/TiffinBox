package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

class NotificationSender {
    var data: Data? = null
    var to: String? = null

    constructor(data: Data?, to: String?) {
        this.data = data
        this.to = to
    }

    constructor() {}
}