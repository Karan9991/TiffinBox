package com.tiff.tiffinbox.Seller.addCustomers.map.helpers2

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener
import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver
import kotlin.jvm.internal.Intrinsics

class FirebaseEventListenerHelper(private val firebaseDriverListener: FirebaseDriverListener) : ChildEventListener {
    override fun onCancelled(p0: DatabaseError) {}
    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        Intrinsics.checkParameterIsNotNull(p0, "p0")
        val driver = p0.getValue(Driver::class.java)
        val var10000 = firebaseDriverListener
        if (driver == null) {
            Intrinsics.throwNpe()
        }
        var10000.onDriverUpdated(driver)
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        Intrinsics.checkParameterIsNotNull(p0, "p0")
        val driver = p0.getValue(Driver::class.java)
        val var10000 = firebaseDriverListener
        if (driver == null) {
            Intrinsics.throwNpe()
        }
        var10000.onDriverAdded(driver)
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        Intrinsics.checkParameterIsNotNull(p0, "p0")
        val driver = p0.getValue(Driver::class.java)
        val var10000 = firebaseDriverListener
        if (driver == null) {
            Intrinsics.throwNpe()
        }
        var10000.onDriverRemoved(driver)
    }
}