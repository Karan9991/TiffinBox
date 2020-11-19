package com.tiff.tiffinbox.Seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tiff.tiffinbox.Chat.MainActivity;
import com.tiff.tiffinbox.Data;
import com.tiff.tiffinbox.FindUserType;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Profile.Profile;
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomer;
import com.tiff.tiffinbox.Seller.ui.main.SectionsPagerAdapter;
import com.tiff.tiffinbox.authentication.SignIn;

public class AddView extends AppCompatActivity implements AddSeller.OnFragmentInteractionListener, ViewSeller2.OnFragmentInteractionListener {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FindUserType findUserType;
    AlertDialog.Builder builder2;

    Data data = Data.getInstance();

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    TextView tvhamburgerName,tvhamburgerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_view);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        builder2 = new AlertDialog.Builder(this);
        //   FloatingActionButton fab = findViewById(R.id.fab);
//        FloatingActionButton fabChatSeller = findViewById(R.id.fabChatSeller);

        findUserType = new FindUserType();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        dl = (DrawerLayout)findViewById(R.id.activity_mainseller);
        t = new ActionBarDrawerToggle(this, dl,R.string.descSeller, R.string.addressSeller);

        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Post Your Tiffin Ad");

        nv = (NavigationView)findViewById(R.id.nvseller);
        nv.setItemIconTintList(null);
        View hView =  nv.getHeaderView(0);
        tvhamburgerName = (TextView)hView.findViewById(R.id.tvhamburgername);
        tvhamburgerEmail = (TextView)hView.findViewById(R.id.tvhamburgeremail);
//        df2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tvhamburgerName.setText(dataSnapshot.child("name").getValue().toString());
//                tvhamburgerEmail.setText(dataSnapshot.child("email").getValue().toString());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.sellerProfile:
                        startActivity(new Intent(AddView.this, Profile.class));
                        break;
                    case R.id.sellerChat:
                        startActivity(new Intent(AddView.this, MainActivity.class));
                        break;
                    case R.id.sellerLogout:
                        logout();
                        break;
                    case R.id.selleryourcustomers:
                        startActivity(new Intent(AddView.this, AddCustomer.class));
                        break;
                    default:
                        return true;
                }

                return true;

            }
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                if (data.isNetworkAvailable(getApplicationContext())){
//                    startActivity(new Intent(AddView.this, AddCustomer.class));
//                }else {
//                    Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                }
//            }
//        });
//        fabChatSeller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (data.isNetworkAvailable(getApplicationContext())){
//                    startActivity(new Intent(AddView.this, MainActivity.class));
//                }else {
//                    Snackbar.make(view, "No Internet", Snackbar.LENGTH_LONG).setAction("Action", null).show();                }
//            }
//        });
    }
        private void logout(){
        builder2.setTitle("Logout");
        builder2.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(AddView.this, SignIn.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder2.create();
        alert.show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}