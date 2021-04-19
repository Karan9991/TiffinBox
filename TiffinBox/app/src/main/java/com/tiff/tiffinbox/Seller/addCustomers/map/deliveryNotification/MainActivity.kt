package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tiff.tiffinbox.R

class MainActivity : AppCompatActivity() {
    var EmailTB: EditText? = null
    var PassTB: EditText? = null
    var LoginB: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EmailTB = findViewById(R.id.EmailTB)
        PassTB = findViewById(R.id.PassTB)
        LoginB = findViewById(R.id.Login)
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, SendNotif::class.java))
        } else {
            LoginB!!.setOnClickListener(View.OnClickListener { FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailTB!!.getText().toString().trim { it <= ' ' }, PassTB!!.getText().toString().trim { it <= ' ' }).addOnSuccessListener { startActivity(Intent(this@MainActivity, SendNotif::class.java)) } })
        }
    }
}