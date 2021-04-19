package com.tiff.tiffinbox.Seller.network

import com.tiff.tiffinbox.Seller.Model.Message
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by abhisheksisodia on 2017-10-06.
 */
//import com.abhi.inbox.model.Message;
interface ApiInterface {
    @get:GET("inbox.json")
    val inbox: Call<List<Message?>?>?
}