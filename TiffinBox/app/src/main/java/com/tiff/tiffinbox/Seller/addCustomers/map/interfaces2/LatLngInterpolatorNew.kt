package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2

import com.google.android.gms.maps.model.LatLng

interface LatLngInterpolatorNew {
    fun interpolate(fraction: Float, a: LatLng, b: LatLng): LatLng
    class LinearFixed : LatLngInterpolatorNew {
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
            return 2.toDouble() * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2.toDouble()), 2.0) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin(dLng / 2.toDouble()), 2.0)))
        }
    }

    //Method for finding bearing between two points
    fun getBearing(latLng1: LatLng, latLng2: LatLng): Float {
//        double lat = Math.abs(begin.latitude - end.latitude);
//        double lng = Math.abs(begin.longitude - end.longitude);
//
//        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)));
//        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
//        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
//            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
//        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
//            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
//        return -1;
//    }
//
        val lat1 = degreesToRadians(latLng1.latitude).toDouble()
        val long1 = degreesToRadians(latLng1.longitude).toDouble()
        val lat2 = degreesToRadians(latLng2.latitude).toDouble()
        val long2 = degreesToRadians(latLng2.longitude).toDouble()
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))
        val radiansBearing = Math.atan2(y, x)
        return radiansToDegrees(radiansBearing)
    }

    fun degreesToRadians(degrees: Double): Float {
        return (degrees * Math.PI / 180.0).toFloat()
    }

    fun radiansToDegrees(radians: Double): Float {
        return (radians * 180.0 / Math.PI).toFloat()
    }
}