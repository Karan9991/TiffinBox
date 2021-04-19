package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2

import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver

interface FirebaseDriverListener {
    fun onDriverAdded(var1: Driver?)
    fun onDriverRemoved(var1: Driver?)
    fun onDriverUpdated(var1: Driver?)
}