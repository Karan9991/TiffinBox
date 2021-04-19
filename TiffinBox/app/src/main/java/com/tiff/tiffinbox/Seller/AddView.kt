package com.tiff.tiffinbox.Seller

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tiff.tiffinbox.Chat.MainActivity
import com.tiff.tiffinbox.Data
import com.tiff.tiffinbox.FindUserType
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.Profile.Profile
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomer
import com.tiff.tiffinbox.Seller.ui.main.SectionsPagerAdapter
import com.tiff.tiffinbox.authentication.ui.SignIn

class AddView : AppCompatActivity(), AddSeller.OnFragmentInteractionListener, ViewSeller2.OnFragmentInteractionListener {
    var fragmentManager: FragmentManager? = null
    var fragmentTransaction: FragmentTransaction? = null
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var findUserType: FindUserType? = null
    var builder2: AlertDialog.Builder? = null
    var data = Data.instance
    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private var nv: NavigationView? = null
    var tvhamburgerName: TextView? = null
    var tvhamburgerEmail: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_view)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        builder2 = AlertDialog.Builder(this)
        //   FloatingActionButton fab = findViewById(R.id.fab);
//        FloatingActionButton fabChatSeller = findViewById(R.id.fabChatSeller);
        findUserType = FindUserType()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser
        dl = findViewById<View>(R.id.activity_mainseller) as DrawerLayout
        t = ActionBarDrawerToggle(this, dl, R.string.descSeller, R.string.addressSeller)
        dl!!.addDrawerListener(t!!)
        t!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Post Your Tiffin Ad"
        nv = findViewById<View>(R.id.nvseller) as NavigationView
        nv!!.itemIconTintList = null
        val hView = nv!!.getHeaderView(0)
        tvhamburgerName = hView.findViewById<View>(R.id.tvhamburgername) as TextView?
        tvhamburgerEmail = hView.findViewById<View>(R.id.tvhamburgeremail) as TextView?
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
        nv!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.sellerProfile -> startActivity(Intent(this@AddView, Profile::class.java))
                R.id.sellerChat -> startActivity(Intent(this@AddView, MainActivity::class.java))
                R.id.sellerLogout -> logout()
                R.id.selleryourcustomers -> startActivity(Intent(this@AddView, AddCustomer::class.java))
                else -> return@OnNavigationItemSelectedListener true
            }
            true
        })
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

    private fun logout() {
        builder2!!.setTitle("Logout")
        builder2!!.setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    FirebaseAuth.getInstance().signOut()
                    val i = Intent(this@AddView, SignIn::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder2!!.create()
        alert.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (t!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri?) {}
}