package com.tiff.tiffinbox.Seller.addCustomers.map.helpers2

import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tiff.tiffinbox.R

//import kotlin.jvm.internal.DefaultConstructorMarker;
class GoogleMapHelper {
    //   public static final Companion Companion = new Companion((DefaultConstructorMarker)null);
    fun buildCameraUpdate(latLng: LatLng?): CameraUpdate {
        val cameraPosition = CameraPosition.Builder().target(latLng).tilt(25.toFloat()).zoom(18.toFloat()).build()
        return CameraUpdateFactory.newCameraPosition(cameraPosition)
    }

    fun getDriverMarkerOptions(position: LatLng): MarkerOptions {
        val options = getMarkerOptions(R.drawable.ic_car, position)
        options.flat(true)
        return options
    }

    private fun getMarkerOptions(resource: Int, position: LatLng): MarkerOptions {
        return MarkerOptions().icon(BitmapDescriptorFactory.fromResource(resource)).position(position)
    }
//comment below line
    //class Companion private constructor() // $FF: synthetic method
    //        public Companion(DefaultConstructorMarker $constructor_marker) {
    //            this();
    //        }

    companion object {
        private const val ZOOM_LEVEL = 18
        private const val TILT_LEVEL = 25
    }
}