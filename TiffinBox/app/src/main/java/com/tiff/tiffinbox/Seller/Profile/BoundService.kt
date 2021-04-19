package com.tiff.tiffinbox.Seller.Profile

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tiff.tiffinbox.Seller.Profile.BoundService

class BoundService : Service() {
    private val iBinder: IBinder = MyBinder()
    var sharedPreferences: SharedPreferences? = null
    override fun onBind(intent: Intent): IBinder? {
        Log.v(LOG_TAG, "in onBind")
        return iBinder
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.v(LOG_TAG, "in unBind")
        return super.onUnbind(intent)
    }

    internal inner class MyBinder : Binder() {
        val service: BoundService
            get() = this@BoundService
    }

    fun updateProfile(name: String?, mobile: String?, address: String?) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance()
        val df = database.reference
        sharedPreferences = getSharedPreferences("UserType", MODE_PRIVATE)
        if (sharedPreferences!!.getString("UT", null) == "Seller") {
            df.child("Seller").child(firebaseAuth.currentUser!!.uid).child("name").setValue(name)
            df.child("Seller").child(firebaseAuth.currentUser!!.uid).child("mobile").setValue(mobile)
            df.child("Seller").child(firebaseAuth.currentUser!!.uid).child("address").setValue(address)
            Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_SHORT).show()
        } else if (sharedPreferences!!.getString("UT", null) == "Customer") {
            df.child("Customer").child(firebaseAuth.currentUser!!.uid).child("name").setValue(name)
            df.child("Customer").child(firebaseAuth.currentUser!!.uid).child("mobile").setValue(mobile)
            df.child("Customer").child(firebaseAuth.currentUser!!.uid).child("address").setValue(address)
            Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestLocationUpdates() {
        val request = LocationRequest()
        //Specify how often your app should request the device’s location//
        request.interval = 10000
        //Get the most accurate location data available//
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val path = "location"
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        //If the app currently has access to the location permission...//
        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {

//Get a reference to the database, so your app can perform read and write operations//

                    //     DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    val location = locationResult.lastLocation
                    if (location != null) {
                        Toast.makeText(applicationContext, "s$location", Toast.LENGTH_LONG).show()
                        Log.i("sdf", location.toString())
                        //Save the location data to the database//

                        //       ref.setValue(location);
                    }
                }
            }, null)
        } else {
            Log.i("Asdfsdfsdfsdf", "nooooooooooooo")
        }
    }

    companion object {
        private const val LOG_TAG = "Bound Service"
    }
}