package com.example.tiffinbox.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tiffinbox.Customer.Model.CardModel;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.ViewRecipe;
import com.example.tiffinbox.ToastListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SellerDetail extends AppCompatActivity {
    TextView tvSellerName, tvSellerEmail, tvSellerPhone, tvSellerAddress;


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    Query querySellerDetail, querywr;

    String a[] = new String[5];
    String tvEmail;
    int i=0;
    ViewRecipe viewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);

        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvSellerPhone = findViewById(R.id.tvSellerPhone);
        tvSellerAddress = findViewById(R.id.tvSellerAddress);

        viewRecipe = new ViewRecipe();

gettingIntent();
        ToastListener.shortToast(getApplicationContext(),"OKkkk"+tvEmail);

        querySellerDetail = df.child("Seller").orderByChild("email").equalTo(tvEmail);

        querySellerDetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot childd : child.getChildren()) {
                        for (DataSnapshot childdd : childd.getChildren()) {

                            // cardModel = childd.getValue(CardModel.class);
                            viewRecipe = childdd.getValue(ViewRecipe.class);
                            Log.i("Email", "email" + viewRecipe.desc);
                        }
                    }
//                String ab = a[0];
//                querywr = df.child("Seller").child(ab);

                    //  tvSellerName.setText();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//demo();
    }

    public void demo(){
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        DatabaseReference df2 = database2.getReference();
//        Query queryse = df.child("Seller").child(a[1]).child("name");

        querywr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i("KEy","k"+a[1]+dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void gettingIntent(){
        tvEmail = getIntent().getStringExtra("name");
//        if (tvEmail!=null) {
//            df = database.getReference().child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(title);
//            df.addValueEventListener(this);
//        }
    }
}
