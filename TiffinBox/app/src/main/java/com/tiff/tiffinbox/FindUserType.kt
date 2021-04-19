package com.tiff.tiffinbox

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FindUserType : ValueEventListener {
    var querySeller: Query? = null
    var queryCustomer: Query? = null
    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseUser = firebaseAuth.currentUser
    var mFirebasedataRefSell = FirebaseDatabase.getInstance().reference
    var mFirebasedataRefCust = FirebaseDatabase.getInstance().reference
    fun findUT(email: String?): String? {
        if (firebaseUser != null) {
            querySeller = mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(email)
            queryCustomer = mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(email)
            triggerQuey()
        }
        return userType
    }

    fun triggerQuey() {
        if (queryCustomer != null && querySeller != null) {
            querySeller!!.addValueEventListener(this)
            queryCustomer!!.addValueEventListener(this)
        }
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        Log.i("zzzzzzzzz", "zzzzz")
        if (dataSnapshot.value != null) {
            val key = dataSnapshot.key
            if (key == "Customer") {
                userType = "Customer"
            } else if (key == "Seller") {
                userType = "Seller"
            }
        }
    }

    override fun onCancelled(databaseError: DatabaseError) {}

    companion object {
        var userType: String? = null
    }
}