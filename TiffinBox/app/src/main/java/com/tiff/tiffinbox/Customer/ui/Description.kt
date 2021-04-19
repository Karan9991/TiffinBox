package com.tiff.tiffinbox.Customer.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tiff.tiffinbox.R

class Description : AppCompatActivity() {
    var tvdesc: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        tvdesc = findViewById(R.id.tvDescription)
        description()
    }

    private fun description() {
        val desc = intent
        val description: String
        if (desc != null) {
            description = desc.getStringExtra("desc")
            tvdesc!!.text = description
        }
    }
}