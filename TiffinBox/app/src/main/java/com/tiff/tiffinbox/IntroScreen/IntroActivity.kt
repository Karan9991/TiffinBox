package com.tiff.tiffinbox.IntroScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.SplashScreen
import java.util.*

//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
class IntroActivity : AppCompatActivity() {
    private var screenPager: ViewPager? = null
    var introViewPagerAdapter: IntroViewPagerAdapter? = null
    var tabIndicator: TabLayout? = null
    var btnNext: Button? = null
    var position = 0
    var btnGetStarted: Button? = null
    var btnAnim: Animation? = null
    var tvSkip: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // when this activity is about to be launch we need to check if its openened before or not
        if (restorePrefData()) {
            val mainActivity = Intent(applicationContext, SplashScreen::class.java)
            startActivity(mainActivity)
            finish()
        }
        setContentView(R.layout.activity_intro)

        // hide the action bar
        supportActionBar!!.hide()

        // ini views
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.intro_button_animation)
        //    tvSkip = findViewById(R.id.tv_skip);

        // fill list screen
        val mList: MutableList<ScreenItem> = ArrayList()
        mList.add(ScreenItem("Tiffin Box", "See Advertisements for Buy Tiffin as Monthly Meal, Sign Up as Seller for Post kind of Recipes", R.drawable.intro1))
        mList.add(ScreenItem("Free Fast Delivery", "Free and Fast Delivery Everywhere Everyday in Month", R.drawable.intro2))
        mList.add(ScreenItem("Easy Payment", "Just Directly Contact to Sellers for Pay", R.drawable.intro3))

        // setup viewpager
        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager!!.setAdapter(introViewPagerAdapter)

        // setup tablayout with viewpager
        tabIndicator!!.setupWithViewPager(screenPager)

        // next button click Listner
        btnNext!!.setOnClickListener(View.OnClickListener {
            position = screenPager!!.getCurrentItem()
            if (position < mList.size) {
                position++
                screenPager!!.setCurrentItem(position)
            }
            if (position == mList.size - 1) { // when we rech to the last screen

                // TODO : show the GETSTARTED Button and hide the indicator and the next button
                loaddLastScreen()
            }
        })

        // tablayout add change listener
        tabIndicator!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0!!.position == mList.size - 1) {
                    loaddLastScreen()
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })


        // Get Started button click listener
        btnGetStarted!!.setOnClickListener(View.OnClickListener { //open main activity
            val mainActivity = Intent(applicationContext, SplashScreen::class.java)
            startActivity(mainActivity)
            // also we need to save a boolean value to storage so next time when the user run the app
            // we could know that he is already checked the intro screen activity
            // i'm going to use shared preferences to that process
            savePrefsData()
            finish()
        })

        // skip button click listener
//
//        tvSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                screenPager.setCurrentItem(mList.size());
//            }
//        });
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpnend", false)
    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpnend", true)
        editor.commit()
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private fun loaddLastScreen() {
        btnNext!!.visibility = View.INVISIBLE
        btnGetStarted!!.visibility = View.VISIBLE
        //    tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator!!.visibility = View.INVISIBLE
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted!!.animation = btnAnim
    }
}