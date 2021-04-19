package com.tiff.tiffinbox.Seller.addCustomers.map.interfaces2

fun interface IPositiveNegativeListener {
    fun onPositive()
    fun onNegative() {}

    object DefaultImpl {
        @JvmStatic
        fun onNegative(`$this`: IPositiveNegativeListener?) {}
    }
}