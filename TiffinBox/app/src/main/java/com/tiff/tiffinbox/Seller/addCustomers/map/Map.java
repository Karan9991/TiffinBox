package com.tiff.tiffinbox.Seller.addCustomers.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.addCustomers.map.collection2.MarkerCollection;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.APIService;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.Client;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.Data;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.MyResponse;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.Token;
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.FirebaseEventListenerHelper;
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.GoogleMapHelper;
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.MarkerAnimationHelper;
import com.tiff.tiffinbox.Seller.addCustomers.map.helpers2.UiHelper;
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener;
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.IPositiveNegativeListener;
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolator;
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolator.Spherical;
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.LatLngInterpolatorNew;
import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class Map extends AppCompatActivity implements FirebaseDriverListener, TaskLoadedCallback, ValueEventListener {
    private GoogleMap googleMap;
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean locationFlag = true;
    private FirebaseEventListenerHelper valueEventListener;
    private final UiHelper uiHelper = new UiHelper();
    private final GoogleMapHelper googleMapHelper = new GoogleMapHelper();
    private final DatabaseReference databaseReference;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 6161;
    private static final String ONLINE_DRIVERS = "online_drivers";
    //  public static final com.spartons.passengerapp.MainActivity.Companion Companion = new com.spartons.passengerapp.MainActivity.Companion((DefaultConstructorMarker)null);
    private HashMap _$_findViewCache;
    GpsTracker gpsTracker;
    private MarkerOptions place1, place2, place3, place4;
    private Polyline currentPolyline;
    Driver driver;
    String contentProvider=null;
    CursorLoader cursorLoader;
    StringBuilder res;
    String data, name, address, custid;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference mdatabaseReference = firebaseDatabase.getReference();
    DatabaseReference mdata = mdatabaseReference.child("passengerpicked");
    //cancel ride
    FirebaseDatabase firebaseCancelR = FirebaseDatabase.getInstance();
    DatabaseReference databaseRefCancelR = firebaseCancelR.getReference();
    DatabaseReference cancelRdata = databaseRefCancelR.child("Cancelride");
    String rideCanceldata;
    private GoogleMap mMap;
 //   DatabaseHelper databaseHelper;
    private static final int CAMERA_REQUEST = 200;
    public Bitmap bp;
    public byte[] photo;
    boolean adddr, notificationDelivery;
    Marker markeragain;
    Location locationn = null;
    public static double currentLatitude, currentLongitude, customerLat, customerLng;
    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);
        //  Toast.makeText(getApplicationContext(),"Tcp"+contentProvider,Toast.LENGTH_SHORT).show();
        res = new StringBuilder();
        mdata.addValueEventListener(this);
        cancelRdata.addValueEventListener(this);
//inserting content provider
        gpsTracker = new GpsTracker(this);
 //       databaseHelper = new DatabaseHelper(getApplicationContext());
        // driver = new Driver();
     //   bp = convertToBitmap(databaseHelper.getMoviep(1).getImage());
        // bp=decodeUri(databaseHelper.getMoviep(1).getImage(), 400);
        adddr = false;
        notificationDelivery = true;

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        UpdateToken();

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
        Fragment var10000 = this.getSupportFragmentManager().findFragmentById(R.id.supportMap);

        SupportMapFragment mapFragment = (SupportMapFragment)var10000;
        mapFragment.getMapAsync((OnMapReadyCallback)(new OnMapReadyCallback() {
            public final void onMapReady(GoogleMap it) {
                Map var10000 = Map.this;
                var10000.googleMap = it;

                mMap = it;
                ////////////////////////////////////////////3
//                    googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                    googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//                    new FetchURL(MainActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
////////////////////////////////////////
            }
        }));
        this.createLocationCallback();
        FusedLocationProviderClient var10001 = LocationServices.getFusedLocationProviderClient((Activity)this);
        this.locationProviderClient = var10001;
        this.locationRequest = this.uiHelper.getLocationRequest();
        if (!this.uiHelper.isPlayServicesAvailable((Context)this)) {
            Toast.makeText((Context)this, (CharSequence)"Play Services did not installed!", Toast.LENGTH_SHORT).show();
            this.finish();
        } else {
            this.requestLocationUpdate();
        }

        this.valueEventListener = new FirebaseEventListenerHelper((FirebaseDriverListener)this);
        DatabaseReference var3 = this.databaseReference;
        FirebaseEventListenerHelper var4 = this.valueEventListener;
        if (var4 == null) {
        }

        var3.addChildEventListener((ChildEventListener)var4);
        //  getSupportLoaderManager().initLoader(1, null, this);
        mdatabaseReference.child("online_drivers").child("0000").child("lat").setValue(currentLatitude);
        mdatabaseReference.child("online_drivers").child("0000").child("lng").setValue(currentLongitude);

                Intent bb = getIntent();
        if (bb!=null)
        {
            name = bb.getStringExtra("name");
            address = bb.getStringExtra("address");
            custid = bb.getStringExtra("custid");
            Log.i("Intentttttttttttt"," "+custid);
            //sendDeliveryNotification();

        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplication(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(address,1);
            customerLat = addresses.get(0).getLatitude();
            customerLng = addresses.get(0).getLongitude();
           // Toast.makeText(this, " "+dlat+dlng,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if(dataSnapshot.getValue(String.class)!=null){
            String key = dataSnapshot.getKey();
            if (key.equals("passengerpicked")){
                data = dataSnapshot.getValue(String.class);
                adddr = true;
                // Toast.makeText(getApplicationContext(),"firebase "+data,Toast.LENGTH_SHORT).show();
            }else  if (key.equals("Cancelride")){
                rideCanceldata = dataSnapshot.getValue(String.class);
                googleMap.clear();
                //Toast.makeText(getApplicationContext(),"firebase "+rideCanceldata,Toast.LENGTH_SHORT).show();
            }
            else  if (key.equals("Clear")){
                googleMap.clear();
                //Toast.makeText(getApplicationContext(),"firebase "+rideCanceldata,Toast.LENGTH_SHORT).show();
            }
            else {
                data = null;
            }
        }
        else {
            data = null;
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
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

    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    private void getValues(){
        photo = profileImage(bp);
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
    @SuppressLint({"MissingPermission"})
    private final void requestLocationUpdate() {
        if (!this.uiHelper.isHaveLocationPermission((Context)this)) {
            ActivityCompat.requestPermissions((Activity)this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 6161);
        } else {
            if (this.uiHelper.isLocationProviderEnabled((Context)this)) {
                UiHelper var10000 = this.uiHelper;
                Context var10001 = (Context)this;
                String var10002 = this.getResources().getString(R.string.need_location);
                String var10003 = this.getResources().getString(R.string.location_content);
                var10000.showPositiveDialogWithListener(var10001, var10002, var10003, (IPositiveNegativeListener)(new IPositiveNegativeListener() {
                    public void onPositive() {
                        Map.this.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                    }

                    public void onNegative() {
                        DefaultImpls.onNegative(this);
                    }
                }), "Turn On", false);
            }

            FusedLocationProviderClient var1 = this.locationProviderClient;
            if (var1 == null) {
            }

            LocationRequest var2 = this.locationRequest;
            if (var2 == null) {
            }

            LocationCallback var3 = this.locationCallback;
            if (var3 == null) {
            }

            var1.requestLocationUpdates(var2, var3, Looper.myLooper());
        }
    }

    private final void createLocationCallback() {
        this.locationCallback = (LocationCallback)(new LocationCallback() {
            public void onLocationResult( LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                }

                if (locationResult.getLastLocation() != null) {
                    Location var10002 = locationResult.getLastLocation();
                    double var3 = var10002.getLatitude();
                    Location var10003 = locationResult.getLastLocation();
                    LatLng latLng = new LatLng(var3, var10003.getLongitude());
                    Log.e("Locationn", latLng.latitude + " , " + latLng.longitude);
                    currentLatitude = latLng.latitude;
                    currentLongitude = latLng.longitude;
                    mdatabaseReference.child("online_drivers").child("0000").child("lat").setValue(latLng.latitude);
                    mdatabaseReference.child("online_drivers").child("0000").child("lng").setValue(latLng.longitude);
                    Log.i("distanceeeeeeeeeeeeeeeeeeeee"," "+findDistance());
                    if (notificationDelivery && findDistance()>70){
                        sendDeliveryNotification();
                        notificationDelivery = false;
                    }

                    if (Map.this.locationFlag) {
                        Map.this.locationFlag = false;
                        Map.this.animateCamera(latLng);
                    }

                }
            }
        });
    }

    private final void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = this.googleMapHelper.buildCameraUpdate(latLng);
        GoogleMap var10000 = this.googleMap;
        if (var10000 == null) {
        }

        var10000.animateCamera(cameraUpdate, 10, (CancelableCallback)null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6161) {
            int value = grantResults[0];
            if (value == -1) {
                Toast.makeText((Context)this, (CharSequence)"Location Permission denied", Toast.LENGTH_SHORT).show();
                this.finish();
            } else if (value == 0) {
                this.requestLocationUpdate();
            }
        }
    }
    ///////////////////////////////////2
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(getApplicationContext().getResources().getColor(R.color.colorAccent));

    }
    //////////////////////////////////////////////

    @Override
    public void onDriverAdded(Driver var1) {
        MarkerOptions markerOptions = this.googleMapHelper.getDriverMarkerOptions(new LatLng(var1.getLat(), var1.getLng())).flat(true);
        GoogleMap var10000 = this.googleMap;

        Marker marker = var10000.addMarker(markerOptions);
        marker.setTag("ghghg");
        marker.showInfoWindow();
        MarkerCollection.INSTANCE.insertMarker(marker);
        TextView var4 = (TextView)this._$_findCachedViewById(R.id.totalOnlineDrivers);
        Toast.makeText(getApplicationContext(),"n "+name,Toast.LENGTH_SHORT).show();
        var4.setText(name);
        //var4.setText((CharSequence)(this.getResources().getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE.allMarkers().size()));
        markeragain = marker;
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
        MarkerAnimationHelper var10000r = MarkerAnimationHelper.INSTANCE;
 //       if (data==null) {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "location";
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    locationn = locationResult.getLastLocation();
                    if (location != null) {
                        Log.i("sdf",location.toString());
                        place1 = new MarkerOptions().position(new LatLng(customerLat, customerLng)).title(address);
                        place2 = new MarkerOptions().position(new LatLng(var1.getLat(), var1.getLng())).title("Driver");
                        googleMap.addMarker(place1).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        new FetchURL(Map.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");
                    }
                }
            }, null);
        }else {
            Log.i("Asdfsdfsdfsdf","nooooooooooooo");
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

    @Override
    public void onDriverRemoved(Driver var1) {
        MarkerCollection.INSTANCE.removeMarker(var1.getDriverId());
        TextView var10000 = (TextView)this._$_findCachedViewById(R.id.totalOnlineDrivers);
        var10000.setText((CharSequence)(this.getResources().getString(R.string.total_online_drivers) + " " + MarkerCollection.INSTANCE.allMarkers().size()));
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
    @Override
    public void onDriverUpdated(Driver var1) {

        final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();
        Marker marker = MarkerCollection.INSTANCE.getMarker("ghghg");

        MarkerAnimationHelper var10000 = MarkerAnimationHelper.INSTANCE;
        final LatLng startPosition = marker.getPosition();
        final LatLng newPosition = new LatLng(var1.getLat(), var1.getLng());


        final LatLng endPosition = new LatLng(43.658038, -79.760535);
//var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolatorNew)(new LatLngInterpolatorNew.LinearFixed()));

                        var10000.animateMarkerToGB(marker, new LatLng(var1.getLat(), var1.getLng()), (LatLngInterpolator)(new Spherical()));
                        place1 = new MarkerOptions().position(new LatLng(customerLat, customerLng)).title(address);
                        place2 = new MarkerOptions().position(new LatLng(var1.getLat(), var1.getLng())).title("Driver");
                        googleMap.addMarker(place1).showInfoWindow();
                        // googleMap.addMarker(place2).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        new FetchURL(Map.this).execute(getUrl(place2.getPosition(), place1.getPosition(), "driving"), "driving");

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
    public void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "location";

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Toast.makeText(getApplicationContext(),"s"+location, Toast.LENGTH_LONG).show();
                        Log.i("sdf",location.toString());
                    }
                }
            }, null);
        }else {
            Log.i("Asdfsdfsdfsdf","nooooooooooooo");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();


        DatabaseReference var10000 = this.databaseReference;
        FirebaseEventListenerHelper var10001 = this.valueEventListener;
        if (var10001 == null) {
        }

        var10000.removeEventListener((ChildEventListener)var10001);
        FusedLocationProviderClient var1 = this.locationProviderClient;
        if (var1 == null) {
        }

        LocationCallback var2 = this.locationCallback;
        if (var2 == null) {
        }

        var1.removeLocationUpdates(var2);
        MarkerCollection.INSTANCE.clearMarkers();
    }

    public Map() {
        FirebaseDatabase var10001 = FirebaseDatabase.getInstance();
        this.databaseReference = var10001.getReference().child("online_drivers");
    }

    // $FF: synthetic method
    public static final GoogleMap access$getGoogleMap$p(Map $this) {
        GoogleMap var10000 = $this.googleMap;
        if (var10000 == null) {
        }

        return var10000;
    }

    public View _$_findCachedViewById(int var1) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }

        View var2 = (View)this._$_findViewCache.get(var1);
        if (var2 == null) {
            var2 = this.findViewById(var1);
            this._$_findViewCache.put(var1, var2);
        }

        return var2;
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }

    }

    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
//        public Companion(DefaultConstructorMarker $constructor_marker) {
//            this();
//        }
    }
    private Bitmap convertToBitmap(byte[] b){

        return BitmapFactory.decodeByteArray(b, 0, b.length);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.cancel_ride, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
        return false;
    }
    private double findDistance(){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(currentLatitude);
        startPoint.setLongitude(currentLongitude);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(customerLat);
        endPoint.setLongitude(customerLng);

        double distance=startPoint.distanceTo(endPoint);
        return distance;
    }
    private void sendDeliveryNotification(){
        FirebaseDatabase.getInstance().getReference().child("Tokens").child(custid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String usertoken=dataSnapshot.getValue(String.class);
                sendNotifications(usertoken, "Tiffin Delivered","Your Tiffin has been delivered");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Log.i("yyyyyyyyyyyyyyyes","yes");
        Data data = new Data(title, message);
        com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.NotificationSender sender = new com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(Map.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }
            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
