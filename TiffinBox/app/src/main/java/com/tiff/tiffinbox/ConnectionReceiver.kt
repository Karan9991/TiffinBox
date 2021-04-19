package com.tiff.tiffinbox

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast

class ConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("API123", "" + intent.action)
        if (intent.action == "com.tiff.tiffinbox.SOME_ACTION") {
            // Toast.makeText(context, "SOME_ACTION is received", Toast.LENGTH_LONG).show();
        } else {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            val isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting
            if (isConnected) {
                try {
                    //Toast.makeText(context, "Network is connected", Toast.LENGTH_LONG).show();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show()
            }
        }
    }
}