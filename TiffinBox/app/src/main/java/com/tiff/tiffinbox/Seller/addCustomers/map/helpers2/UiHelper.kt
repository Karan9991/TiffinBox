package com.tiff.tiffinbox.Seller.addCustomers.map.helpers2

import android.content.Context
import android.location.LocationManager
import android.os.Build.VERSION
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.IPositiveNegativeListener

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import kotlin.TypeCastException;
class UiHelper {
    fun isPlayServicesAvailable(context: Context?): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return status == 0
    }

    fun isHaveLocationPermission(context: Context?): Boolean {
        return VERSION.SDK_INT < 23 || ActivityCompat.checkSelfPermission(context!!, "android.permission.ACCESS_FINE_LOCATION") == 0 || ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_COARSE_LOCATION") == 0
    }

    fun isLocationProviderEnabled(context: Context): Boolean {
        val var10000 = context.getSystemService(Context.LOCATION_SERVICE)
        val locationManager = var10000 as LocationManager
        return !locationManager.isProviderEnabled("gps") && !locationManager.isProviderEnabled("network")
    }

    fun showPositiveDialogWithListener(callingClassContext: Context, title: String, content: String, positiveNegativeListener: IPositiveNegativeListener, positiveText: String?, cancelable: Boolean) {
        buildDialog(callingClassContext, title, content).builder.positiveText((positiveText as CharSequence?)!!).positiveColor(getColor(-500007, callingClassContext)).onPositive(SingleButtonCallback { `$noName_0`, `$noName_1` -> positiveNegativeListener.onPositive() }).cancelable(cancelable).show()
    }

    private fun buildDialog(callingClassContext: Context, title: String, content: String): MaterialDialog {
        return MaterialDialog.Builder(callingClassContext).title((title as CharSequence)).content((content as CharSequence)).build()
    }

    private fun getColor(color: Int, context: Context): Int {
        return ContextCompat.getColor(context, color)
    }

    val locationRequest: LocationRequest
        get() {
            val locationRequest = LocationRequest.create()
            locationRequest.priority = 100
            locationRequest.interval = 3000L
            return locationRequest
        }
}