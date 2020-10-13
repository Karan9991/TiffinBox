package com.tiff.tiffinbox.Seller.Profile;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BoundService extends Service {

    private static String LOG_TAG = "Bound Service";
    private IBinder iBinder = new MyBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");
        return iBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in unBind");
        return super.onUnbind(intent);
    }

    class MyBinder extends Binder{
          BoundService getService(){
              return BoundService.this;
          }
    }

    public void updateProfile(String name, String mobile, String address) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference df = database.getReference();
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(name);
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("mobile").setValue(mobile);
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("address").setValue(address);
        Toast.makeText(getApplicationContext(),"Profile Updated", Toast.LENGTH_SHORT).show();
    }

    public void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
//Specify how often your app should request the device’s location//
        request.setInterval(10000);
//Get the most accurate location data available//
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = "location";

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
//If the app currently has access to the location permission...//
        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//

                    //     DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        Toast.makeText(getApplicationContext(),"s"+location, Toast.LENGTH_LONG).show();
                        Log.i("sdf",location.toString());
//Save the location data to the database//

                        //       ref.setValue(location);
                    }
                }
            }, null);
        }else {
            Log.i("Asdfsdfsdfsdf","nooooooooooooo");
        }
    }
}
