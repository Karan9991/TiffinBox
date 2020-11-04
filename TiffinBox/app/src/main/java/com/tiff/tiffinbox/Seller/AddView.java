package com.tiff.tiffinbox.Seller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tiff.tiffinbox.Data;
import com.tiff.tiffinbox.FindUserType;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomer;
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.MainActivity;
import com.tiff.tiffinbox.Seller.ui.main.SectionsPagerAdapter;

public class AddView extends AppCompatActivity implements AddSeller.OnFragmentInteractionListener, ViewSeller2.OnFragmentInteractionListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FindUserType findUserType;

    Data data = Data.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        FloatingActionButton fabChatSeller = findViewById(R.id.fabChatSeller);

        findUserType = new FindUserType();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if (data.isNetworkAvailable(getApplicationContext())){
                    startActivity(new Intent(AddView.this, AddCustomer.class));
                }else {
                    Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        fabChatSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (data.isNetworkAvailable(getApplicationContext())){
                    startActivity(new Intent(AddView.this, MainActivity.class));
                }else {
                    Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}