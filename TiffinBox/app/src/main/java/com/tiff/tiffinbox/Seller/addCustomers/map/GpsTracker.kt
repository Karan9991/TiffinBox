package com.tiff.tiffinbox.Seller.addCustomers.map

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import com.tiff.tiffinbox.Seller.addCustomers.map.GpsTracker
import java.util.*

class GpsTracker(private val mContext: Context) : Service(), LocationListener {
    // flag for GPS status
    var isGPSEnable = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false
    var locationn: Location? = null
    var latitudee = 0.0
    var longitudee = 0.0
    var addr: String? = null

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null
    fun isGPSEnabled(): Boolean {
        locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        return isGPSEnable
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }

    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnable = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnable && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                canGetLocation = true
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    //check the network permission
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((mContext as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
                    }
                    locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        locationn = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (locationn != null) {
                            latitudee = locationn!!.latitude
                            longitudee = locationn!!.longitude
                        }
                    }
                }

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnable) {
                    if (locationn == null) {
                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((mContext as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 101)
                        }
                        locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            locationn = locationManager!!
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (locationn != null) {
                                latitudee = locationn!!.latitude
                                longitudee = locationn!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return locationn
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }

    /**
     * Function to get latitude
     */
    fun getLatitude(): Double {
        if (locationn != null) {
            latitudee = locationn!!.latitude
        }

        // return latitude
        return latitudee
    }

    /**
     * Function to get longitude
     */
    fun getLongitude(): Double {
        if (locationn != null) {
            longitudee = locationn!!.longitude
        }

        // return longitude
        return longitudee
    }

    // Here 1 represent max location result to returned, by documents it recommended 1 to 5
    val address: String?
        get() {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(application, Locale.getDefault())
            try {
                addresses = geocoder.getFromLocation(locationn!!.latitude, locationn!!.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addr = addresses[0].getAddressLine(0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return addr
        }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS or Internet not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings") { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun testLoc(): Location? {
        return locationn
    }

    override fun onLocationChanged(location: Location) {
        latitudee = location.latitude
        longitudee = location.longitude
        //        ContentValues values = new ContentValues();
//        values.put(MyProvider.name, "Karan");
//        values.put(MyProvider.sourcce, latitude);
//        values.put(MyProvider.destinationn, longitude);
//        Uri uri = getContentResolver().insert(MyProvider.CONTENT_URI, values);
//        Toast.makeText(getApplicationContext(),"Inserted Karan",Toast.LENGTH_SHORT).show();
    }

    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()
    }

    init {
        getLocation()
    }
}