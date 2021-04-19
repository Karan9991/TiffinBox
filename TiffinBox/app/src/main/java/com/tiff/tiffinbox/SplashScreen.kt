package com.tiff.tiffinbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiff.tiffinbox.Customer.ui.Customer
import com.tiff.tiffinbox.Seller.AddView
import com.tiff.tiffinbox.authentication.ui.SignIn

class SplashScreen : AppCompatActivity() {
    var l1: TextView? = null
    var l2: TextView? = null
    var uptodown: Animation? = null
    var downtoup: Animation? = null
    var sharedPref: SharedPreferences? = null
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var dfOnline: DatabaseReference? = null
    var dfOnline2: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPref = getSharedPreferences("UserType", MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser
        l1 = findViewById(R.id.l1)
        l2 = findViewById(R.id.l2)
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown)
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup)
        l1!!.setAnimation(uptodown)
        l2!!.setAnimation(downtoup)
        //        getSupportActionBar().hide();
        val timerThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
//                    Intent intent = new Intent(SplashScreen.this, SignIn.class);
//                    startActivity(intent);
                    findUT()
                }
            }
        }
        timerThread.start()
    }

    fun findUT() {
        //If User already signed in
        if (firebaseUser != null && firebaseAuth!!.currentUser!!.isEmailVerified) {
            dfOnline = FirebaseDatabase.getInstance().reference
            dfOnline2 = FirebaseDatabase.getInstance().reference
            val myTopPostsQuery = dfOnline!!.child("Customer").orderByChild("email").equalTo(firebaseUser!!.email)
            val myTopPostsQuery2 = dfOnline2!!.child("Seller").orderByChild("email").equalTo(firebaseUser!!.email)
            if (sharedPref!!.getString("UT", null) == "Customer") {
                startActivity(Intent(this@SplashScreen, Customer::class.java))
                finish()
                //               myTopPostsQuery.addValueEventListener(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       Log.i("qqqqqqqqqqqqqqqqqqqqqqqqqsplashscreen","q"+dataSnapshot.getKey());
//                       if (dataSnapshot.getKey().equals("Customer")){
//                           dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
//                           startActivity(new Intent(SplashScreen.this, Customer.class));
//                           finish();
//                       }
//                   }
//
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                   }
//               });
            } else if (sharedPref!!.getString("UT", null) == "Seller") {
                startActivity(Intent(this@SplashScreen, AddView::class.java))
                finish()
                //               myTopPostsQuery2.addValueEventListener(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                       if (dataSnapshot.getKey().equals("Seller")) {
//
//                           dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
//                           startActivity(new Intent(SplashScreen.this, AddView.class));
//                           finish();
//                       }
//                   }
//
//                   @Override
//                   public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                   }
//               });
            }
        } else {
            startActivity(Intent(this@SplashScreen, SignIn::class.java))
        }
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        finish()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }
}