package com.tiff.tiffinbox.Customer.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.tiff.tiffinbox.Customer.Model.ModelDemo
import com.tiff.tiffinbox.Customer.ui.Description
import com.tiff.tiffinbox.R

//import android.support.annotation.NonNull;
//import android.support.v4.view.PagerAdapter;
class Adapter(private val modelDemos: List<ModelDemo>, private val context: Context) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        return modelDemos.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater!!.inflate(R.layout.item, container, false)
        val imageView: ImageView
        val title: TextView
        val desc: TextView
        val price: TextView
        imageView = view.findViewById(R.id.image)
        title = view.findViewById(R.id.title)
        desc = view.findViewById(R.id.desc)
        price = view.findViewById(R.id.price)


        // imageView.setImageResource(modelDemos.get(position).getImage());
        title.text = modelDemos[position].title
        price.text = " Price : " + modelDemos[position].price
        desc.text = modelDemos[position].desc
        Glide.with(context).load(modelDemos[position].imageURL).into(imageView)
        view.setOnClickListener {
            Log.i("changaaaaaaaaaaaa", "ok" + modelDemos[position].title)
            val intent = Intent(context, Description::class.java)
            intent.putExtra("desc", modelDemos[position].desc)
            context.startActivity(intent)
            // finish();
        }
        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}