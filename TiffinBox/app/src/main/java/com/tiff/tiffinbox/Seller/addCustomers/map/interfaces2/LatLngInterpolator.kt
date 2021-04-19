package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2

import com.google.android.gms.maps.model.LatLng

//1.0E-6D
interface LatLngInterpolator {
    fun interpolate(var1: Float, var2: LatLng, var3: LatLng): LatLng
    class Spherical : LatLngInterpolator {
        override fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng {
            val fromLat = Math.toRadians(a.latitude)
            val fromLng = Math.toRadians(a.longitude)
            val toLat = Math.toRadians(b.latitude)
            val toLng = Math.toRadians(b.longitude)
            val cosFromLat = Math.cos(fromLat)
            val cosToLat = Math.cos(toLat)
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle = Math.sin(angle)
            return if (sinAngle < 1.0E-6) {
                a
            } else {
                val temp1 = Math.sin((1.toFloat() - fraction).toDouble() * angle) / sinAngle
                val temp2 = Math.sin(fraction.toDouble() * angle) / sinAngle
                val x = temp1 * cosFromLat * Math.cos(fromLng) + temp2 * cosToLat * Math.cos(toLng)
                val y = temp1 * cosFromLat * Math.sin(fromLng) + temp2 * cosToLat * Math.sin(toLng)
                val z = temp1 * Math.sin(fromLat) + temp2 * Math.sin(toLat)
                val lat = Math.atan2(z, Math.sqrt(x * x + y * y))
                val lng = Math.atan2(y, x)
                LatLng(Math.toDegrees(lat), Math.toDegrees(lng))
            }
        }

        private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {
            val dLat = fromLat - toLat
            val dLng = fromLng - toLng
            //   Log.d("vvvvvvvvvvvvvv","vvvvvvvvvvvvvvvvvvv");
            return 2.toDouble() * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2.toDouble()), 2.0) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin(dLng / 2.toDouble()), 2.0)))
        }
    }
}