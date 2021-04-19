package com.tiff.tiffinbox.Seller.addCustomers.map

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.map.collection2.MarkerCollection
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.*
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.Client.getClient
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.FirebaseEventListenerHelper
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.GoogleMapHelper
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.MarkerAnimationHelper
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.UiHelper
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.IPositiveNegativeListener
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.IPositiveNegativeListener.DefaultImpl.onNegative
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolator
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolator.Spherical
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolatorNew
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolatorNew.LinearFixed
import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.util.*

//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
///import android.support.v7.app.AppCompatActivity;
//import com.tiff.tiffinbox.Seller.addCustomers.map.DatabaseHelper;
//import com.tiff.tiffinbox.Seller.addCustomers.map.MapsActivity;
//import com.example.sandhu.Student.MyProvider;
//import com.tiff.tiffinbox.Seller.addCustomers.map.UserModel;
//import com.google.maps.android.SphericalUtil;
//import com.google.maps.android.geometry.*;
//import com.example.sandhu.R;
//import com.example.sandhu.R.id;
//import com.tiff.tiffinbox.Seller.addCustomers.map.MyProvider;
class Map : AppCompatActivity(), FirebaseDriverListener, TaskLoadedCallback, ValueEventListener {
    private var googleMap: GoogleMap? = null
    private var locationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var locationFlag = true
    private var valueEventListener: FirebaseEventListenerHelper? = null
    private val uiHelper = UiHelper()
    private val googleMapHelper = GoogleMapHelper()
    private val databaseReference: DatabaseReference

    //  public static final com.spartons.passengerapp.MainActivity.Companion Companion = new com.spartons.passengerapp.MainActivity.Companion((DefaultConstructorMarker)null);
    private var `_$_findViewCachee`: HashMap<*,*>? = null
    var gpsTracker: GpsTracker? = null
    private var place1: MarkerOptions? = null
    private var place2: MarkerOptions? = null
    private val place3: MarkerOptions? = null
    private val place4: MarkerOptions? = null
    private var currentPolyline: Polyline? = null
    var driver: Driver? = null
    var contentProvider: String? = null
    var cursorLoader: CursorLoader? = null
    var res: StringBuilder? = null
    var data: String? = null
    var name: String? = null
    var address: String? = null
    var custid: String? = null
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var mdatabaseReference = firebaseDatabase.reference
    var mdata = mdatabaseReference.child("passengerpicked")

    //cancel ride
    var firebaseCancelR = FirebaseDatabase.getInstance()
    var databaseRefCancelR = firebaseCancelR.reference
    var cancelRdata = databaseRefCancelR.child("Cancelride")
    var rideCanceldata: String? = null
    private var mMap: GoogleMap? = null
    var bp: Bitmap? = null
    lateinit var photo: ByteArray
    var adddr = false
    var notificationDelivery = false
    var markeragain: Marker? = null
    var locationn: Location? = null
    private var apiService: APIService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        //  Toast.makeText(getApplicationContext(),"Tcp"+contentProvider,Toast.LENGTH_SHORT).show();
        res = StringBuilder()
        mdata.addValueEventListener(this)
        cancelRdata.addValueEventListener(this)
        //inserting content provider
        gpsTracker = GpsTracker(this)
        //       databaseHelper = new DatabaseHelper(getApplicationContext());
        // driver = new Driver();
        //   bp = convertToBitmap(databaseHelper.getMoviep(1).getImage());
        // bp=decodeUri(databaseHelper.getMoviep(1).getImage(), 400);
        adddr = false
        notificationDelivery = true
        apiService = getClient("https://fcm.googleapis.com/")!!.create(APIService::class.java)
        UpdateToken()

//        getValues();
//        ContentValues values = new ContentValues();
//        values.put(MyProvider.name, databaseHelper.getMoviep(1).getFname());
//        values.put(MyProvider.sourcce, 43.6532);
//        values.put(MyProvider.destinationn, 79.3832);
//        values.put(MyProvider.photo, databaseHelper.getMoviep(1).getImage());
//        // Uri uri =
//        //  getContentResolver().insert(MyProvider.CONTENT_URI, values);
//        Cursor countCursor = getContentResolver().query(MyProvider.CONTENT_URI,
//                new String[] {"count(*) AS count"},
//                null,
//                null,
//                null);
//
//        countCursor.moveToFirst();
//        int count = countCursor.getInt(0);
//
//        if (count==0){
//            getContentResolver().insert(MyProvider.CONTENT_URI, values);
//            Toast.makeText(getBaseContext(), "inser "+count, Toast.LENGTH_SHORT).show();
//
//        }
//        if (count>=1) {
//            getContentResolver().update(MyProvider.CONTENT_URI, values,null,null);
//            Toast.makeText(getBaseContext(), "upda "+count+values, Toast.LENGTH_SHORT).show();
//
//        }
//        Toast.makeText(getBaseContext(), "New "+count, Toast.LENGTH_SHORT)
//                .show();

////////////////////////////  1   43.658038, -79.760535
//        place1 = new MarkerOptions().position(new LatLng(43.658038, -79.760535)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(gpsTracker.latitude, gpsTracker.longitude)).title("Driver");
        //////////////////////////////////////////// driver.getLat(), driver.getLng()
        val var10000 = this.supportFragmentManager.findFragmentById(R.id.supportMap)
        val mapFragment = var10000 as SupportMapFragment?
        mapFragment!!.getMapAsync(OnMapReadyCallback { it ->
            val var10000 = this@Map
            var10000.googleMap = it
            mMap = it
            ////////////////////////////////////////////3
//                    googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                    new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
////////////////////////////////////////
        })
        createLocationCallback()
        val var10001 = LocationServices.getFusedLocationProviderClient((this as Activity))
        locationProviderClient = var10001
        locationRequest = uiHelper.locationRequest
        if (!uiHelper.isPlayServicesAvailable(this as Context)) {
            Toast.makeText(this as Context, "Play Services did not installed!" as CharSequence, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            requestLocationUpdate()
        }
        valueEventListener = FirebaseEventListenerHelper((this as FirebaseDriverListener))
        val var3 = databaseReference
        val var4 = valueEventListener
        if (var4 == null) {
        }
        var3.addChildEventListener((var4 as ChildEventListener?)!!)
        //  getSupportLoaderManager().initLoader(1, null, this);
        mdatabaseReference.child("online_drivers").child("0000").child("lat").setValue(Companion.currentLatitude)
        mdatabaseReference.child("online_drivers").child("0000").child("lng").setValue(Companion.currentLongitude)
        val bb = intent
        if (bb != null) {
            name = bb.getStringExtra("name")
            address = bb.getStringExtra("address")
            custid = bb.getStringExtra("custid")
            Log.i("Intentttttttttttt", " $custid")
            //sendDeliveryNotification();
        }
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(application, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocationName(address, 1)
            Companion.customerLat = addresses[0].latitude
            Companion.customerLng = addresses[0].longitude
            // Toast.makeText(this, " "+dlat+dlng,Toast.LENGTH_LONG).show();
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.getValue(String::class.java) != null) {
            val key = dataSnapshot.key
            if (key == "passengerpicked") {
                data = dataSnapshot.getValue(String::class.java)
                adddr = true
                // Toast.makeText(getApplicationContext(),"firebase "+data,Toast.LENGTH_SHORT).show();
            } else if (key == "Cancelride") {
                rideCanceldata = dataSnapshot.getValue(String::class.java)
                googleMap!!.clear()
                //Toast.makeText(getApplicationContext(),"firebase "+rideCanceldata,Toast.LENGTH_SHORT).show();
            } else if (key == "Clear") {
                googleMap!!.clear()
                //Toast.makeText(getApplicationContext(),"firebase "+rideCanceldata,Toast.LENGTH_SHORT).show();
            } else {
                data = null
            }
        } else {
            data = null
        }
    }

    override fun onCancelled(databaseError: DatabaseError) {}

    // convert image to store in content provider database
    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    switch(requestCode) {
    //        case 2:
    //            if(resultCode == RESULT_OK){
    //                Uri choosenImage = data.getData();
    //
    //                if(choosenImage !=null){
    //
    //                    bp=decodeUri(choosenImage, 400);
    //                    //pic.setImageBitmap(bp);
    //                }
    //            }
    //    }
    //    if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
    //        Uri choosenImage = data.getData();
    //
    //        if(choosenImage !=null){
    //
    //            bp=decodeUri(choosenImage, 400);
    //            //  pic.setImageBitmap(bp);
    //        }
    //
    //    }
    //}
    protected fun decodeUri(selectedImage: Uri?, REQUIRED_SIZE: Int): Bitmap? {
        try {

            // Decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage!!), null, o)

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            var width_tmp = o.outWidth
            var height_tmp = o.outHeight
            var scale = 1
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break
                }
                width_tmp /= 2
                height_tmp /= 2
                scale *= 2
            }

            // Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(contentResolver.openInputStream(selectedImage), null, o2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private fun profileImage(b: Bitmap?): ByteArray {
        val bos = ByteArrayOutputStream()
        b!!.compress(Bitmap.CompressFormat.PNG, 0, bos)
        return bos.toByteArray()
    }

    private val values: Unit
        private get() {
            photo = profileImage(bp)
        }

    //    //Getting content provider values from diver app
    //    @Override
    //    public androidx.loader.content.Loader
    //            <Cursor> onCreateLoader(int arg0, Bundle arg1){
    //        cursorLoader = new CursorLoader(this, Uri.parse("content://com.example.StudentDriver.Activity.MyProvider/cte"), null, null, null, null);
    //        return cursorLoader;
    //    }
    //
    //    @Override
    //    public void onLoadFinished
    //            (@NonNull androidx.loader.content.Loader < Cursor > loader, Cursor cursor){
    //        cursor.moveToFirst();
    //        while (!cursor.isAfterLast()) {
    //            res.append("\n" + cursor.getString(cursor.getColumnIndex("id")) + "-" + cursor.getString(cursor.getColumnIndex("name")) + "-" + cursor.getString(cursor.getColumnIndex("source")) + "-" + cursor.getString(cursor.getColumnIndex("destination")));
    //            cursor.moveToNext();
    //        }
    //
    //        Toast.makeText(getApplicationContext(), "cp" + res, Toast.LENGTH_SHORT).show();
    //        contentProvider = res.toString();
    //
    //
    //        new AlertDialog.Builder(MainActivity.this)
    //                .setTitle("One Passenger Found")
    //                .setMessage("m"+contentProvider)
    //                // .setIcon(R.drawable.ninja)
    //                .setPositiveButton("Request Accept",
    //                        new DialogInterface.OnClickListener() {
    //                            @TargetApi(11)
    //                            public void onClick(DialogInterface dialog, int id) {
    //                                Toast.makeText(getApplicationContext(),"Tcp"+contentProvider,Toast.LENGTH_SHORT).show();
    //
    //                            }
    //                        })
    //                .setNegativeButton("Request Reject", new DialogInterface.OnClickListener() {
    //                    @TargetApi(11)
    //                    public void onClick(DialogInterface dialog, int id) {
    //
    //                    }
    //                }).show();
    //
    //    }
    //
    //    @Override
    //    public void onLoaderReset (@NonNull androidx.loader.content.Loader < Cursor > loader) {
    //
    //    }
    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        if (!uiHelper.isHaveLocationPermission(this as Context)) {
            ActivityCompat.requestPermissions((this as Activity), arrayOf("android.permission.ACCESS_FINE_LOCATION"), 6161)
        } else {
            if (uiHelper.isLocationProviderEnabled((this as Context))) {
                val var10000 = uiHelper
                val var10001 = this as Context
                val var10002 = this.resources.getString(R.string.need_location)
                val var10003 = this.resources.getString(R.string.location_content)
                var10000.showPositiveDialogWithListener(var10001, var10002, var10003, (object : IPositiveNegativeListener {
                    override fun onPositive() {
                        this@Map.startActivity(Intent("android.settings.LOCATION_SOURCE_SETTINGS"))
                    }

                    override fun onNegative() {
                        onNegative(this)
                    }
                } as IPositiveNegativeListener), "Turn On", false)
            }
            val var1 = locationProviderClient
            if (var1 == null) {
            }
            val var2 = locationRequest
            if (var2 == null) {
            }
            val var3 = locationCallback
            if (var3 == null) {
            }
            var1!!.requestLocationUpdates(var2, var3, Looper.myLooper())
        }
    }

    private fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult == null) {
                }
                if (locationResult.lastLocation != null) {
                    val var10002 = locationResult.lastLocation
                    val var3 = var10002.latitude
                    val var10003 = locationResult.lastLocation
                    val latLng = LatLng(var3, var10003.longitude)
                    Log.e("Locationn", latLng.latitude.toString() + " , " + latLng.longitude)
                    Companion.currentLatitude = latLng.latitude
                    Companion.currentLongitude = latLng.longitude
                    mdatabaseReference.child("online_drivers").child("0000").child("lat").setValue(latLng.latitude)
                    mdatabaseReference.child("online_drivers").child("0000").child("lng").setValue(latLng.longitude)
                    Log.i("distanceeeeeeeeeeeeeeeeeeeee", " " + findDistance())
                    if (notificationDelivery && findDistance() > 70) {
                        sendDeliveryNotification()
                        notificationDelivery = false
                    }
                    if (locationFlag) {
                        locationFlag = false
                        animateCamera(latLng)
                    }
                }
            }
        }
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraUpdate = googleMapHelper.buildCameraUpdate(latLng)
        val var10000 = googleMap
        if (var10000 == null) {
        }
        var10000!!.animateCamera(cameraUpdate, 10, null as CancelableCallback?)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 6161) {
            val value = grantResults[0]
            if (value == -1) {
                Toast.makeText(this as Context, "Location Permission denied" as CharSequence, Toast.LENGTH_SHORT).show()
                finish()
            } else if (value == 0) {
                requestLocationUpdate()
            }
        }
    }

    ///////////////////////////////////2
    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key)
    }

    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline!!.remove()
        currentPolyline = googleMap!!.addPolyline(values[0] as PolylineOptions)
        currentPolyline!!.setColor(applicationContext.resources.getColor(R.color.colorAccent))
    }

    //////////////////////////////////////////////
    override fun onDriverAdded(var1: Driver?) {
        val markerOptions = googleMapHelper.getDriverMarkerOptions(LatLng(var1!!.lat, var1.lng)).flat(true)
        val var10000 = googleMap
        val marker = var10000!!.addMarker(markerOptions)
        marker.tag = "ghghg"
        marker.showInfoWindow()
        MarkerCollection.INSTANCE!!.insertMarker(marker)
        val var4 = `_$_findCachedViewByIdd`(R.id.totalOnlineDrivers) as TextView?
        Toast.makeText(applicationContext, "n $name", Toast.LENGTH_SHORT).show()
        var4!!.text = name
        //var4.setText((CharSequence)(this.getResources().getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE.allMarkers().size()));
        markeragain = marker
        // Toast.makeText(getBaseContext(), "driver added"+var1.getLat()+var1.getLng(), Toast.LENGTH_SHORT).show();
//        place1 = new MarkerOptions().position(new LatLng(43.658038, -79.760535)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(var1.getLat(), var1.getLng())).title("Driver");
//       // googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//       // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        new FetchURL(MainActivity.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");
        //      MarkerAnimationHelper var100000 = MarkerAnimationHelper.INSTANCE;
//var100000.createAnimation(marker, mMap);

        // Animating marker
//        Intent bb = getIntent();
//        if (bb!=null)
//        {
//            destinationn = bb.getStringExtra("destinationn");
//        }
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(getApplication(), Locale.getDefault());
//        try {
//            addresses = geocoder.getFromLocationName(destinationn,1);
//            dlat = addresses.get(0).getLatitude();
//            dlng = addresses.get(0).getLongitude();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Marker markerr = MarkerCollection.INSTANCE.getMarker(var1.getDriverId());
        val var10000r = MarkerAnimationHelper.INSTANCE
        //       if (data==null) {
        val request = LocationRequest()
        request.interval = 10000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val path = "location"
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    locationn = locationResult.lastLocation
                    if (location != null) {
                        Log.i("sdf", location.toString())
                        place1 = MarkerOptions().position(LatLng(Companion.customerLat, Companion.customerLng)).title(address)
                        place2 = MarkerOptions().position(LatLng(var1.lat, var1.lng)).title("Driver")
                        googleMap!!.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        FetchURL(this@Map).execute(getUrl(place2!!.getPosition(), place1!!.getPosition(), "driving"), "driving")
                    }
                }
            }, null)
        } else {
            Log.i("Asdfsdfsdfsdf", "nooooooooooooo")
        }
        //            place1 = new MarkerOptions().position(new LatLng(44.638730, -63.630210)).title("Customer");
        //          place2 = new MarkerOptions().position(new LatLng(locationn.getLatitude(), locationn.getLongitude())).title("Driver");
        //        googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //      new FetchURL(Map.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");
//            Toast.makeText(getBaseContext(), "database" + data, Toast.LENGTH_SHORT)
//                    .show();
        //     var10000r.createAnimation(marker,mMap);
        //     }else {

        //          place3 = new MarkerOptions().position(new LatLng(44.638730,-63.630210)).title("source");
        //          place4 = new MarkerOptions().position(new LatLng(dlat, dlng)).title("Destination");
        //          new FetchURL(Map.this).execute(getUrl(place3.getPosition(), place4.getPosition(), "driving"), "driving");
        //  Toast.makeText(getBaseContext(), "database61" + data, Toast.LENGTH_SHORT).show();
        //   var10000r.createAnimation(marker,mMap);
        //     }
    }

    override fun onDriverRemoved(var1: Driver?) {
        MarkerCollection.INSTANCE!!.removeMarker(var1!!.driverId)
        val var10000 = `_$_findCachedViewByIdd`(R.id.totalOnlineDrivers) as TextView?
        var10000!!.text = (this.resources.getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE!!.allMarkers()!!.size)
    }

    //    public void onDriverUpdated(@NotNull Driver driver) {
    //        Intrinsics.checkParameterIsNotNull(driver, "driver");
    //        Marker marker = MarkerCollection.INSTANCE.getMarker(driver.getDriverId());
    //        MarkerAnimationHelper var10000 = MarkerAnimationHelper.INSTANCE;
    //        if (marker == null) {
    //            Intrinsics.throwNpe();
    //        }
    //    final LatLng startPosition = marker.getPosition();
    //
    //        var10000.animateMarkerToGB(marker, new LatLng(driver.getLat(), driver.getLng()), (LatLngInterpolator)(new Spherical()));
    //        place1 = new MarkerOptions().position(new LatLng(43.658038, -79.760535)).title("Location 1");
    //        place2 = new MarkerOptions().position(new LatLng(driver.getLat(), driver.getLng())).title("Driver");
    //        new FetchURL(MainActivity.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");
    //
    //        //   marker.setRotation((float) getBearingBetweenTwoPoints1(startPosition, new LatLng(driver.getLat(), driver.getLng())));
    ////        var heading = google.maps.geometry.spherical.computeHeading(lastPosn,p);
    //
    //     //   marker.setPosition(new LatLng(gpsTracker.latitude, gpsTracker.longitude));
    ////marker.setRotation(getBearing(new LatLng(gpsTracker.latitude, gpsTracker.longitude), new LatLng(driver.getLat(), driver.getLng())));
    //   //marker.setRotation((float) SphericalUtil.computeHeading( new LatLng(driver.getLat(), driver.getLng()),new LatLng(driver.getLat(), driver.getLng())));
    //   //marker.setRotation(getBearing(new LatLng(gpsTracker.latitude, gpsTracker.longitude),SphericalUtil.interpolate(new LatLng(driver.getLat(), driver.getLng()),new LatLng(driver.getLat(), driver.getLng()),1.566)));
    //    }
    override fun onDriverUpdated(var1: Driver?) {
        val latLngInterpolator: LatLngInterpolatorNew = LinearFixed()
        val marker = MarkerCollection.INSTANCE!!.getMarker("ghghg")
        val var10000 = MarkerAnimationHelper.INSTANCE
        val startPosition = marker!!.position
        val newPosition = LatLng(var1!!.lat, var1.lng)
        val endPosition = LatLng(43.658038, -79.760535)
        //var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolatorNew)(new LatLngInterpolatorNew.LinearFixed()));
        var10000!!.animateMarkerToGB(marker, LatLng(var1.lat, var1.lng), (Spherical() as LatLngInterpolator))
        place1 = MarkerOptions().position(LatLng(Companion.customerLat, Companion.customerLng)).title(address)
        place2 = MarkerOptions().position(LatLng(var1.lat, var1.lng)).title("Driver")
        googleMap!!.addMarker(place1).showInfoWindow()
        // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        FetchURL(this@Map).execute(getUrl(place2!!.getPosition(), place1!!.getPosition(), "driving"), "driving")

        /////////vv

        // var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolator)(new LatLngInterpolator.Spherical()));
        //   var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolator)(new Spherical()));
//marker.setRotation(latLngInterpolator.getBearing(startPosition,  new LatLng(var1.getLat(), var1.getLng())));
        //     marker.setRotation(latLngInterpolator.getBearing(startPosition,  newPosition));
//     marker.setRotation((float) getBearingBetweenTwoPoints1(startPosition, new LatLng(var1.getLat(), var1.getLng())));
        //  Toast.makeText(getBaseContext(), "driver updated"+var1.getLat()+var1.getLng(), Toast.LENGTH_SHORT)
        //        .show();

        //  uncomment this
        //      if (data==null) {
        //            Toast.makeText(getBaseContext(), "database" + data, Toast.LENGTH_SHORT)
//                    .show();
        //     }else {
        // callDriveradd(var1);
//43.645458, -79.750877  61 michigan ave
        //        place3 = new MarkerOptions().position(new LatLng(44.638730,-63.630210)).title("source");
        //        place4 = new MarkerOptions().position(new LatLng(dlat, dlng)).title("Destination");
        //         new FetchURL(Map.this).execute(getUrl(place3.getPosition(), place4.getPosition(), "driving"), "driving");
        //  Toast.makeText(getBaseContext(), "database61" + data, Toast.LENGTH_SHORT).show();

        //    }
    }

    fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 10000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val client = LocationServices.getFusedLocationProviderClient(this)
        val path = "location"
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        Toast.makeText(applicationContext, "s$location", Toast.LENGTH_LONG).show()
                        Log.i("sdf", location.toString())
                    }
                }
            }, null)
        } else {
            Log.i("Asdfsdfsdfsdf", "nooooooooooooo")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val var10000 = databaseReference
        val var10001 = valueEventListener
        if (var10001 == null) {
        }
        var10000.removeEventListener((var10001 as ChildEventListener?)!!)
        val var1 = locationProviderClient
        if (var1 == null) {
        }
        val var2 = locationCallback
        if (var2 == null) {
        }
        var1!!.removeLocationUpdates(var2)
        MarkerCollection.INSTANCE!!.clearMarkers()
    }

    fun `_$_findCachedViewByIdd`(var1: Int): View? {
        if (`_$_findViewCachee` == null) {
            `_$_findViewCachee` = HashMap<Any?, Any?>()
        }
        var var2 = `_$_findViewCachee`!![var1] as View?
        if (var2 == null) {
            var2 = findViewById(var1)
            //`_$_findViewCachee`!![var1] = var2
        }
        return var2
    }

    fun `_$_clearFindViewByIdCachee`() {
        if (`_$_findViewCachee` != null) {
            `_$_findViewCachee`!!.clear()
        }
    }

   // class Companion private constructor() // $FF: synthetic method
    //        public Companion(DefaultConstructorMarker $constructor_marker) {
    //            this();
    //        }

    private fun convertToBitmap(b: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.cancel_ride, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        switch (item.getItemId()) {
//            case R.id.cancel_ride:
//                new AlertDialog.Builder(Map.this)
//                        //.setTitle("Passenger : "+contentProvider)
//                        .setMessage("Are you sure?")
//                        // .setIcon(image)
//                        .setPositiveButton("Yes",
//                                new DialogInterface.OnClickListener() {
//                                    @TargetApi(11)
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        cancelRdata.setValue("RideCanceled");
//
//                                    }
//                                })//.setView(image)
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @TargetApi(11)
//                            public void onClick(DialogInterface dialog, int id) {
//
//                            }
//                        }).show();
//                break;
////            case R.id.menuitem2:
////                Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT).show();
////                break;
//        }
        return false
    }

    private fun findDistance(): Double {
        val startPoint = Location("locationA")
        startPoint.latitude = Companion.currentLatitude
        startPoint.longitude = Companion.currentLongitude
        val endPoint = Location("locationA")
        endPoint.latitude = Companion.customerLat
        endPoint.longitude = Companion.customerLng
        return startPoint.distanceTo(endPoint).toDouble()
    }

    private fun sendDeliveryNotification() {
        FirebaseDatabase.getInstance().reference.child("Tokens").child(custid!!).child("token").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val usertoken = dataSnapshot.getValue(String::class.java)
                sendNotifications(usertoken, "Tiffin Delivered", "Your Tiffin has been delivered")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun UpdateToken() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        val token = Token(refreshToken)
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(token)
    }

    fun sendNotifications(usertoken: String?, title: String?, message: String?) {
        Log.i("yyyyyyyyyyyyyyyes", "yes")
        val data = Data(title, message)
        val sender = NotificationSender(data, usertoken)
        apiService!!.sendNotifcation(sender)!!.enqueue(object : Callback<MyResponse?> {
            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.code() == 200) {
                    if (response.body()!!.success != 1) {
                        Toast.makeText(this@Map, "Failed ", Toast.LENGTH_LONG)
                    }
                }
            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable) {}
        })
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6161
        private const val ONLINE_DRIVERS = "online_drivers"

        //   DatabaseHelper databaseHelper;
        private const val CAMERA_REQUEST = 200
        var currentLatitude = 0.0
        var currentLongitude = 0.0
        var customerLat = 0.0
        var customerLng = 0.0

        // $FF: synthetic method
        fun `access$getGoogleMap$p`(`$this`: Map): GoogleMap? {
            val var10000 = `$this`.googleMap
            if (var10000 == null) {
            }
            return var10000
        }
    }

    init {
        val var10001 = FirebaseDatabase.getInstance()
        databaseReference = var10001.reference.child("online_drivers")
    }
}


