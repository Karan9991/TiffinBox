package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Client {
    private var retrofit: Retrofit? = null
    @JvmStatic
    fun getClient(url: String?): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit
    }
}