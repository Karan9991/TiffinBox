package com.tiff.tiffinbox.Seller.network

import retrofit2.Retrofit
import com.tiff.tiffinbox.Seller.network.ApiClient
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by abhisheksisodia on 2017-10-06.
 */
object ApiClient {
    const val BASE_URL = "https://api.androidhive.info/json/"
    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return retrofit
        }
}