package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2;
import com.tiff.tiffinbox.Seller.addCustomers.map.model2.Driver;


public interface FirebaseDriverListener {
    void onDriverAdded(Driver var1);

    void onDriverRemoved(Driver var1);

    void onDriverUpdated(Driver var1);
}
