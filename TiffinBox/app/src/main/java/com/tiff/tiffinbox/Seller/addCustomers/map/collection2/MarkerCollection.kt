package com.tiff.tiffinbox.Seller.addCustomers.map.collection2

import com.google.android.gms.maps.model.Marker
import java.util.*

class MarkerCollection private constructor() {
    fun insertMarker(marker: Marker?) {
        //change marker as nothing
        markers!!.add(marker)
    }

    fun getMarker(driverId: String): Marker? {
        //Intrinsics.checkParameterIsNotNull(driverId, "driverId");
        val var3: Iterator<*> = markers!!.iterator()
        var marker: Marker
        do {
            if (!var3.hasNext()) {
                return null
            }
            marker = var3.next() as Marker
        } while (marker.tag !== driverId)
        return marker
    }

    fun clearMarkers() {
        markers!!.clear()
    }

    fun removeMarker(driverId: String) {
        //   Intrinsics.checkParameterIsNotNull(driverId, "driverId");
        val marker = getMarker(driverId)
        marker?.remove()
        if (marker != null) {
            markers!!.remove(marker)
        }
    }

    fun allMarkers(): List<*>? {
        return markers
    }

    companion object {
        private var markers: MutableList<*>? = null
        @JvmField
        var INSTANCE: MarkerCollection? = null

        init {
            val var0 = MarkerCollection()
            INSTANCE = var0
            markers = LinkedList<Any?>()
        }
    }
}