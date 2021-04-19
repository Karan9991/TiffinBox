package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        if (firebaseUser != null) {
            updateToken(refreshToken)
        }
    }

    private fun updateToken(refreshToken: String?) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val token1 = Token(refreshToken)
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(token1)
    }
}