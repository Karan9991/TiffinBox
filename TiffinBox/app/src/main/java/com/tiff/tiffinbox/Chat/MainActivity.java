package com.tiff.tiffinbox.Chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//import android.widget.TabLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//import android.support.v4.view.ViewPager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tiff.tiffinbox.Authentication.Model.User;
import com.tiff.tiffinbox.Authentication.SignIn;
import com.tiff.tiffinbox.Chat.Fragments.ChatsFragment;
import com.tiff.tiffinbox.Chat.Fragments.ProfileFragment;
import com.tiff.tiffinbox.Chat.Fragments.UsersFragment;
import com.tiff.tiffinbox.Chat.Model.Chat;
import com.tiff.tiffinbox.FindUserType;
import com.tiff.tiffinbox.R;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference, refUserType;
    FindUserType findUserType;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        findUserType = new FindUserType();
        sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (sharedPref.getString("UT",null).equals("Customer")){
            Customerr();
        }else if (sharedPref.getString("UT",null).equals("Seller")){
            Sellerr();
        }

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);


        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                int unread = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()){
                        unread++;
                    }
                }

                if (unread == 0){
                    viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
                } else {
                    viewPagerAdapter.addFragment(new ChatsFragment(), "("+unread+") Chats");
                }

                if (sharedPref.getString("UT",null).equals("Seller")){
                    viewPagerAdapter.addFragment(new UsersFragment(), "Customers");
                }else if (sharedPref.getString("UT",null).equals("Customer")){
                    viewPagerAdapter.addFragment(new UsersFragment(), "Sellers");
                }
             //   viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
public void Sellerr()
{
    reference = FirebaseDatabase.getInstance().getReference("Seller").child(firebaseUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            com.tiff.tiffinbox.Authentication.Model.User user = dataSnapshot.getValue(User.class);
            username.setText(user.getUsername());
            if (user.getImageURL().equals("default")){
                profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                //change this
             //   Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                Picasso.with(getApplicationContext()).
                        load(user.getImageURL()).into(profile_image);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
public void Customerr(){
    reference = FirebaseDatabase.getInstance().getReference("Customer").child(firebaseUser.getUid());
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            com.tiff.tiffinbox.Authentication.Model.User user = dataSnapshot.getValue(User.class);
            username.setText(user.getUsername());
            if (user.getImageURL().equals("default")){
                profile_image.setImageResource(R.mipmap.ic_launcher);
            } else {
                //change this
            //    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                Picasso.with(getApplicationContext()).
                        load(user.getImageURL()).into(profile_image);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//
//            case  R.id.logout:
//                FirebaseAuth.getInstance().signOut();
//                // change this code beacuse your app will crash
//                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                return true;
//        }
//
//        return false;
//    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status){
        if (sharedPref.getString("UT",null).equals("Customer")){
            reference = FirebaseDatabase.getInstance().getReference("Customer").child(firebaseUser.getUid());
             HashMap<String, Object> map = new HashMap<>();
            map.put("status", status);
            reference.updateChildren(map);
            Log.i("dddddddddddddddddddcustomer","di"+sharedPref.getString("UT",null));
        }else if (sharedPref.getString("UT",null).equals("Seller")){
            reference = FirebaseDatabase.getInstance().getReference("Seller").child(firebaseUser.getUid());
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", status);
            reference.updateChildren(map);
            Log.i("dddddddddddddddddddseller","di"+sharedPref.getString("UT",null));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
       Log.i("OnResume","onresume");
         status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        Log.i("Onpause","onpause");

    }
}
