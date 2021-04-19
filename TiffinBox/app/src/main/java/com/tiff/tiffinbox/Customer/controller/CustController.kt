package com.tiff.tiffinbox.Customer.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.tiff.tiffinbox.authentication.ui.SignIn

class CustController {
    private var builder2: AlertDialog.Builder? = null
    fun logout(context: Context) {
        builder2 = AlertDialog.Builder(context)
        builder2!!.setTitle("Logout")
        builder2!!.setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    FirebaseAuth.getInstance().signOut()
                    val i = Intent(context, SignIn::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(i)
                }
                .setNegativeButton("No") { dialog, id -> dialog.cancel() }
        val alert = builder2!!.create()
        alert.show()
    }
}