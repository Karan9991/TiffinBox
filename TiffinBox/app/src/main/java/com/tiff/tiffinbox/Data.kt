package com.tiff.tiffinbox

import android.content.Context
import android.net.ConnectivityManager

class Data private constructor() {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    fun dem(): Boolean {
        return false
    }

    companion object {
        private var data: Data? = null
        val instance: Data?
            get() {
                if (data == null) {
                    data = Data()
                }
                return data
            }
    }
}