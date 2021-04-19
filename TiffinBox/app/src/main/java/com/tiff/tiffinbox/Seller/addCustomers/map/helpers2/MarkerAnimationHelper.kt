package com.tiff.tiffinbox.Seller.addCustomers.map.helpers2

import android.animation.ValueAnimator
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.tiff.tiffinbox.Seller.addCustomers.map.Map
import com.tiff.tiffinbox.Seller.addCustomers.map.PointsParser
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolator
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolatorNew
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolatorNew.LinearFixed

class MarkerAnimationHelper private constructor() {
    private var startPositionn: LatLng? = null
    private var endPositionn: LatLng? = null
    var newPos: LatLng? = null
    var lat = 0.0
    var lng = 0.0
    private var indexx = 0
    private var nextt = 0
    private var indexca = 0
    private var nextca = 0
    private var startPositionca: LatLng? = null
    private var endPositionca: LatLng? = null
    private var v = 0f
    val handlerca = Handler()
    fun animateMarkerToGB(marker: Marker, finalPosition: LatLng?, latLngInterpolator: LatLngInterpolator) {
        indexx = -1
        nextt = 1
        val duration: Long = 1000
        val startPosition = marker.position
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 2000.0f
        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {
                val latLngInterpolato: LatLngInterpolatorNew = LinearFixed()
                val endPosition = LatLng(Map.currentLatitude, Map.currentLongitude)
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed.toFloat() / durationInMs
                this.v = interpolator.getInterpolation(t)
                val var10002 = this.v
                marker.position = latLngInterpolator.interpolate(var10002, startPosition, finalPosition!!)
                marker.rotation = latLngInterpolato.getBearing(startPosition, endPosition)
                if (t < 1.toFloat()) {
                    handler.postDelayed(this as Runnable, 16L)
                }
            }
        } as Runnable)
        if (indexx < PointsParser.polyLineList.size - 1) {
            indexx++
            nextt = indexx + 1
        }
        if (indexx < PointsParser.polyLineList.size - 1) {
            startPositionn = PointsParser.polyLineList[indexx]
            endPositionn = PointsParser.polyLineList[nextt]
            Log.i("startend", "startenddddddddddddd$startPositionn $endPositionn")
        }
        val elapsedv = SystemClock.uptimeMillis() - start
        val tv = interpolator.getInterpolation(elapsedv.toFloat() / duration
        )
        Log.i("startend", "startenddddddddddddd$startPositionn $endPositionn")
        if (startPositionn != null && endPositionn != null) {
            lng = tv * endPositionn!!.longitude + (1 - tv) * startPositionn!!.longitude
            lat = tv * endPositionn!!.latitude + (1 - tv) * startPositionn!!.latitude
        }
        newPos = LatLng(lat, lng)
        marker.position = newPos!!
        marker.setAnchor(0.5f, 0.5f)
        // if (null != newPos) {
        //  marker.setRotation(getBearing(startPositionn, newPos));
        if (startPositionn != null && newPos != null) {
            marker.rotation = getBearing(startPositionn!!, newPos!!)
            Log.i("bearinggggggggggggg", "bearing one")
        }
    }

    fun createAnimation(marker: Marker, mMap: GoogleMap?) {
        // Collections.reverse(PointsParser.polyLineList);
        indexca = -1
        nextca = 1
        handlerca.postDelayed(object : Runnable {
            override fun run() {
                if (indexca < PointsParser.polyLineList.size - 1) {
                    indexca++
                    nextca = indexca + 1
                }
                if (indexca < PointsParser.polyLineList.size - 1) {
                    startPositionca = PointsParser.polyLineList[indexca]
                    endPositionca = PointsParser.polyLineList[nextca]
                    // Log.i("startend","startenddddddddddddd"+startPosition+" "+endPosition);
                }
                if (indexca == 0) {
//                    BeginJourneyEvent beginJourneyEvent = new BeginJourneyEvent();
//                    beginJourneyEvent.setBeginLatLng(startPositionca);
//                    JourneyEventBus.getInstance().setOnJourneyBegin(beginJourneyEvent);
                }
                if (indexca == PointsParser.polyLineList.size - 1) {
//                    EndJourneyEvent endJourneyEvent = new EndJourneyEvent();
//                    endJourneyEvent.setEndJourneyLatLng(new LatLng(PointsParser.polyLineList.get(indexca).latitude,
//                            PointsParser.polyLineList.get(indexca).longitude));
//                    JourneyEventBus.getInstance().setOnJourneyEnd(endJourneyEvent);
                }
                val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
                valueAnimator.duration = 1000
                valueAnimator.interpolator = LinearInterpolator()
                valueAnimator.addUpdateListener { valueAnimator ->
                    v = valueAnimator.animatedFraction
                    //  Log.i("vv","vvvvvvvvvvvvv"+v);
                    if (endPositionca != null && startPositionca != null) {
                        lng = v * endPositionca!!.longitude + (1 - v) * startPositionca!!.longitude
                        lat = v * endPositionca!!.latitude + (1 - v) * startPositionca!!.latitude
                    }
                    val newPos = LatLng(lat, lng)
                    //                        CurrentJourneyEvent currentJourneyEvent = new CurrentJourneyEvent();
//                        currentJourneyEvent.setCurrentLatLng(newPos);
//                        JourneyEventBus.getInstance().setOnJourneyUpdate(currentJourneyEvent);
                    marker.position = newPos
                    marker.setAnchor(0.5f, 0.5f)
                    if (startPositionca != null && newPos != null) {
                        marker.rotation = getBearing(startPositionca!!, newPos)
                        Log.i("bearinggggggggggggg", "bearing two")
                    }
                    //  Log.i("bearing","bbbbbbbbbbbbbbb"+ getBearing(startPosition, newPos));
                    // Log.i("sd",startPosition +"v"+ newPos);F

//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition
//                                (new CameraPosition.Builder().target(newPos)
//                                        .zoom(15.5f).build()));
                }
                valueAnimator.start()
                if (indexca != PointsParser.polyLineList.size - 1) {
                    handlerca.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    companion object {
        @JvmField
        var INSTANCE: MarkerAnimationHelper? = null

        init {
            val var0 = MarkerAnimationHelper()
            INSTANCE = var0
        }
    }

    private fun getBearing(begin: LatLng, end: LatLng): Float {
        val lat = Math.abs(begin.latitude - end.latitude)
        val lng = Math.abs(begin.longitude - end.longitude)
        if (begin.latitude < end.latitude && begin.longitude < end.longitude) return Math.toDegrees(Math.atan(lng / lat)).toFloat() else if (begin.latitude >= end.latitude && begin.longitude < end.longitude) return (90 - Math.toDegrees(Math.atan(lng / lat)) + 90).toFloat() else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude) return (Math.toDegrees(Math.atan(lng / lat)) + 180).toFloat() else if (begin.latitude < end.latitude && begin.longitude >= end.longitude) return (90 - Math.toDegrees(Math.atan(lng / lat)) + 270).toFloat()
        return (-1).toFloat()
    }
}