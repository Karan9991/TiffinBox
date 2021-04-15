package com.tiff.tiffinbox;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FindUserType implements ValueEventListener {
    public static String userType;
    Query querySeller, queryCustomer;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DatabaseReference mFirebasedataRefSell = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFirebasedataRefCust = FirebaseDatabase.getInstance().getReference();

    public String findUT(String email) {
       if (firebaseUser != null) {
           querySeller = mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(email);
           queryCustomer = mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(email);
           triggerQuey();
       }
       return userType;
   }

    public void triggerQuey() {
        if (queryCustomer != null && querySeller != null) {
            querySeller.addValueEventListener(this);
            queryCustomer.addValueEventListener(this);

        }
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.i("zzzzzzzzz","zzzzz");

        if (dataSnapshot.getValue() != null) {
            String key = dataSnapshot.getKey();
            if (key.equals("Customer")) {
                userType = "Customer";
            } else if (key.equals("Seller")){
                 userType = "Seller";
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
