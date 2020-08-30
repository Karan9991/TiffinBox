package com.tiff.tiffinbox.Seller.Profile;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BoundService extends Service {

    private static String LOG_TAG = "Bound Service";
    private IBinder iBinder = new MyBinder();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();

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
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("name").setValue(name);
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("mobile").setValue(mobile);
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("address").setValue(address);
        Toast.makeText(getApplicationContext(),"Profile Updated", Toast.LENGTH_SHORT).show();
    }
}
