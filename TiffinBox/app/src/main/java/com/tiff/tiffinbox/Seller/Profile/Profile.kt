package com.tiff.tiffinbox.Seller.Profile

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tiff.tiffinbox.ConnectionReceiver
import com.tiff.tiffinbox.Customer.ui.Customer
import com.tiff.tiffinbox.Data
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.AddView
import com.tiff.tiffinbox.Seller.Model.SellerProfile
import com.tiff.tiffinbox.Seller.Profile.BoundService
import com.tiff.tiffinbox.Seller.Profile.BoundService.MyBinder
import com.tiff.tiffinbox.Validate
import com.tiff.tiffinbox.authentication.ui.SignIn

class Profile : AppCompatActivity(), Validate {
    var etName: EditText? = null
    var etMobile: EditText? = null
    var etAddress: EditText? = null
    var btnEdit: Button? = null
    @JvmField
    var btnUpdate: Button? = null
    var btnDeleteAccount: Button? = null
    var imgProfileleftArrow: ImageView? = null
    private var isValid = false
    var sellerProfile: SellerProfile? = null
    var builder: AlertDialog.Builder? = null
    var builder2: AlertDialog.Builder? = null
    var boundService: BoundService? = null
    var serviceBound = false
    var receiver: ConnectionReceiver? = null
    var intentFilter: IntentFilter? = null

    //Firebase
    var firebaseAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    var df = database.reference
    var data = Data.instance
    var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        FirebaseApp.initializeApp(this)
        etName = findViewById(R.id.etEditProfileName)
        etMobile = findViewById(R.id.etEditProfileMobile)
        etAddress = findViewById(R.id.etEditProfileAddress)
        btnEdit = findViewById(R.id.btnEditProfile)
        btnUpdate = findViewById(R.id.btnUpdateProfile)
        btnDeleteAccount = findViewById(R.id.btnDeleteProfile)
        imgProfileleftArrow = findViewById(R.id.imgProfileLeftArrow)
        //  imgLogout = findViewById(R.id.imgProfileLogout);
        isValid = false
        sellerProfile = SellerProfile()
        builder = AlertDialog.Builder(this)
        builder2 = AlertDialog.Builder(this)
        sharedPreferences = getSharedPreferences("UserType", MODE_PRIVATE)
        gettingFirebaseData()
        receiver = ConnectionReceiver()
        intentFilter = IntentFilter("com.tiff.tiffinbox.SOME_ACTION")
        intentFilter!!.addAction(CONNECTIVITY_ACTION)
        btnEdit!!.setOnClickListener(View.OnClickListener {
            btnUpdate!!.setVisibility(View.VISIBLE)
            btnDeleteAccount!!.setVisibility(View.VISIBLE)
            etName!!.setEnabled(true)
            etMobile!!.setEnabled(true)
            etAddress!!.setEnabled(true)
        })
        btnUpdate!!.setOnClickListener(View.OnClickListener { view ->
            if (validations() && serviceBound && data!!.isNetworkAvailable(this@Profile)) {
                boundService!!.updateProfile(etName!!.getText().toString(), etMobile!!.getText().toString(), etAddress!!.getText().toString())
            } else {
                Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        })
        btnDeleteAccount!!.setOnClickListener(View.OnClickListener { alertDialogdelete() })
        imgProfileleftArrow!!.setOnClickListener(View.OnClickListener {
            if (sharedPreferences!!.getString("UT", null) == "Seller") {
                startActivity(Intent(this@Profile, AddView::class.java))
            } else if (sharedPreferences!!.getString("UT", null) == "Customer") {
                startActivity(Intent(this@Profile, Customer::class.java))
            }
        })

//        imgLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (data.isNetworkAvailable(Profile.this)){
//                    logout();
//                }else {
//                    Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    private fun gettingFirebaseData() {
        if (sharedPreferences!!.getString("UT", null) == "Seller") {
            df.child("Seller").child(firebaseAuth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    sellerProfile = dataSnapshot.getValue(SellerProfile::class.java)
                    etName!!.setText(sellerProfile!!.name)
                    etMobile!!.setText(sellerProfile!!.mobile)
                    etAddress!!.setText(sellerProfile!!.address)
                    etName!!.isEnabled = false
                    etMobile!!.isEnabled = false
                    etAddress!!.isEnabled = false
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        } else if (sharedPreferences!!.getString("UT", null) == "Customer") {
            df.child("Customer").child(firebaseAuth.currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    sellerProfile = dataSnapshot.getValue(SellerProfile::class.java)
                    etName!!.setText(sellerProfile!!.name)
                    etMobile!!.setText(sellerProfile!!.mobile)
                    etAddress!!.setText(sellerProfile!!.address)
                    etName!!.isEnabled = false
                    etMobile!!.isEnabled = false
                    etAddress!!.isEnabled = false
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun deleteAccount() {
        if (sharedPreferences!!.getString("UT", null) == "Seller") {
            df.child("Seller").child(firebaseAuth.currentUser!!.uid).removeValue()
            val user = FirebaseAuth.getInstance().currentUser
            user!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Your Account Deleted Permanently", Toast.LENGTH_LONG).show()
                            val i = Intent(this@Profile, SignIn::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        }
                    }
        } else if (sharedPreferences!!.getString("UT", null) == "Customer") {
            df.child("Customer").child(firebaseAuth.currentUser!!.uid).removeValue()
            val user = FirebaseAuth.getInstance().currentUser
            user!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Your Account Deleted Permanently", Toast.LENGTH_LONG).show()
                            val i = Intent(this@Profile, SignIn::class.java)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(i)
                        }
                    }
        }
    }

    private fun alertDialogdelete() {
        builder!!.setTitle("Are You Sure?")
        builder!!.setMessage("Your All Data Will be Deleted Permanently")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id -> deleteAccount() }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder!!.create()
        alert.show()
    }

    //    private void logout(){
    //        builder2.setTitle("Logout");
    //        builder2.setCancelable(false)
    //                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    //                    public void onClick(DialogInterface dialog, int id) {
    //                        FirebaseAuth.getInstance().signOut();
    //                        Intent i = new Intent(Profile.this, SignIn.class);
    //                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //                        startActivity(i);
    //                    }
    //                })
    //                .setNegativeButton("No", new DialogInterface.OnClickListener() {
    //                    public void onClick(DialogInterface dialog, int id) {
    //                        dialog.cancel();
    //                    }
    //                });
    //        AlertDialog alert = builder2.create();
    //        alert.show();
    //    }
    override fun validations(): Boolean {
        if (TextUtils.isEmpty(etName!!.text)) {
            etName!!.error = "Name is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etMobile!!.text)) {
            etMobile!!.error = "Mobile is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etAddress!!.text)) {
            etAddress!!.error = "Address is required!"
            isValid = false
        } else {
            isValid = true
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        Log.i("Serviceeeeeee", "onstart")
        val intent = Intent(this, BoundService::class.java)
        startService(intent)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter)
        val intent = Intent("com.tiff.tiffinbox.SOME_ACTION")
        sendBroadcast(intent)
    }

    override fun onStop() {
        super.onStop()
        data = null
        if (serviceBound) {
            unbindService(serviceConnection)
            serviceBound = false
            val intent = Intent(this@Profile, BoundService::class.java)
            stopService(intent)
            Log.i("Serviceeeeeee", "onstop")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val myBinder = iBinder as MyBinder
            boundService = myBinder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            serviceBound = false
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST = 100
        const val CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
    }
}