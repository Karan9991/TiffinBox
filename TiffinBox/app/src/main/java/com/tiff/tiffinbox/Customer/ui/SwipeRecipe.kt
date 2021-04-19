package com.tiff.tiffinbox.Customer.ui

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.firebase.database.*
import com.tiff.tiffinbox.Customer.Model.ModelDemo
import com.tiff.tiffinbox.Customer.adapter.Adapter
import com.tiff.tiffinbox.R
import java.util.*

class SwipeRecipe : AppCompatActivity() {
    var viewPager: ViewPager? = null
    var adapter: Adapter? = null
    var modelDemos: MutableList<ModelDemo>? = null
    var colors: Array<Int>? = null
    var userid: String? = null
    var argbEvaluator = ArgbEvaluator()
    var tvSellerName: TextView? = null
    var tvSellerEmail: TextView? = null
    var tvSellerPhone: TextView? = null
    var tvSellerAddress: TextView? = null
    var tvEmail: String? = null
    var tvName: String? = null
    var tvAddress: String? = null
    var tvMobile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_recipe)
        tvSellerName = findViewById(R.id.tvSellerName)
        tvSellerEmail = findViewById(R.id.tvSellerEmail)
        tvSellerPhone = findViewById(R.id.tvSellerPhone)
        tvSellerAddress = findViewById(R.id.tvSellerAddress)
        gettingIntent()
        recipes
        tvSellerName!!.setText("Name: $tvName")
        tvSellerEmail!!.setText("E-Mail: $tvEmail")
        tvSellerAddress!!.setText("Address: $tvAddress")
        tvSellerPhone!!.setText("Phone: $tvMobile")
    }

    fun all() {
        adapter = Adapter(modelDemos!!, this)
        viewPager = findViewById(R.id.viewPager)
        viewPager!!.setAdapter(adapter)
        viewPager!!.setPadding(130, 0, 130, 0)
        val colors_temp = arrayOf(
                resources.getColor(R.color.color1),
                resources.getColor(R.color.color2),
                resources.getColor(R.color.color3),
                resources.getColor(R.color.color4),
                resources.getColor(R.color.color1),
                resources.getColor(R.color.color2),
                resources.getColor(R.color.color3),
                resources.getColor(R.color.color4)
        )
        colors = colors_temp
        viewPager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position < adapter!!.count - 1 && position < colors!!.size - 1) {
                    viewPager!!.setBackgroundColor(
                            (argbEvaluator.evaluate(
                                    positionOffset,
                                    colors!![position],
                                    colors!![position + 1]
                            ) as Int)
                    )
                } else {
                    viewPager!!.setBackgroundColor(colors!![colors!!.size - 1])
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    val recipes: Unit
        get() {
            modelDemos = ArrayList()
            val database = FirebaseDatabase.getInstance()
            val df = database.reference
            val querySellerDetail: Query
            querySellerDetail = df.child("Seller").orderByChild("email").equalTo(tvEmail)
            querySellerDetail.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (child in dataSnapshot.children) {
                        userid = child.key
                        for (childd in child.children) {
                            for (childdd in childd.children) {
                                var modelDemo: ModelDemo? = ModelDemo()
                                modelDemo = childdd.getValue(ModelDemo::class.java)
                                modelDemos!!.add(ModelDemo(modelDemo!!.imageURL, childdd.key, modelDemo.desc, modelDemo.price))
                                adapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            all()
        }

    private fun gettingIntent() {
        tvEmail = intent.getStringExtra("email")
        tvName = intent.getStringExtra("name")
        tvAddress = intent.getStringExtra("address")
        tvMobile = intent.getStringExtra("mobile")
    }
}