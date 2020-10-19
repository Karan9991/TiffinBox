package com.tiff.tiffinbox.Seller.addCustomers.map.helpers2;

import com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener;
import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import kotlin.jvm.internal.Intrinsics;

public final class FirebaseEventListenerHelper implements ChildEventListener {
    private final FirebaseDriverListener firebaseDriverListener;

    public void onCancelled( DatabaseError p0) {
    }

    public void onChildMoved(DataSnapshot p0, String p1) {
    }

    public void onChildChanged(DataSnapshot p0, String p1) {
        Intrinsics.checkParameterIsNotNull(p0, "p0");
        com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver driver = (com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver)p0.getValue(com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver.class);
        com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener var10000 = this.firebaseDriverListener;
        if (driver == null) {
            Intrinsics.throwNpe();
        }
        var10000.onDriverUpdated(driver);
    }

    public void onChildAdded(DataSnapshot p0, String p1) {
        Intrinsics.checkParameterIsNotNull(p0, "p0");
        com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver driver = (com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver)p0.getValue(com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver.class);
        com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener var10000 = this.firebaseDriverListener;
        if (driver == null) {
            Intrinsics.throwNpe();
        }

        var10000.onDriverAdded(driver);
    }

    public void onChildRemoved( DataSnapshot p0) {
        Intrinsics.checkParameterIsNotNull(p0, "p0");
        com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver driver = (com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver)p0.getValue(com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver.class);
        com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2.FirebaseDriverListener var10000 = this.firebaseDriverListener;
        if (driver == null) {
            Intrinsics.throwNpe();
        }

        var10000.onDriverRemoved(driver);
    }

    public FirebaseEventListenerHelper(FirebaseDriverListener firebaseDriverListener) {
        super();
        this.firebaseDriverListener = firebaseDriverListener;
    }
}
