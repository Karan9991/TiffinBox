package com.tiff.tiffinbox.authentication.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Validate
import com.tiff.tiffinbox.Validator
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel
import com.tiff.tiffinbox.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity(), Validate {
    var etEmailLogin: EditText? = null
    var etPasswordLogin: EditText? = null
    var btnLogin: Button? = null
    var tvSignUp: TextView? = null
    var tvForgotPassword: TextView? = null
    private var progressBar: ProgressBar? = null
    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var isValid: Boolean? = null
    var validator: Validator? = null
    var firebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var mFirebasedataRefSell: DatabaseReference? = null
    var mFirebasedataRefCust: DatabaseReference? = null
    var dfOnline: DatabaseReference? = null
    var dfOnline2: DatabaseReference? = null
    private var activitySignInBinding: ActivitySignInBinding? = null
    private var authViewModel: AuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        activitySignInBinding = DataBindingUtil.setContentView(this@SignIn, R.layout.activity_sign_in)
        activitySignInBinding!!.setLifecycleOwner(this)
        activitySignInBinding!!.setAuthViewModel(authViewModel)
        authViewModel!!.getUser(this).observe(this, {
            user -> Log.e("onChanged()  ", " ")
        })
        init()
        btnLogin!!.setOnClickListener {
            if (validations()) {
                authViewModel!!.login(progressBar!!, this@SignIn, etEmailLogin!!.text.toString(), etPasswordLogin!!.text.toString())
            }
        }
        tvSignUp!!.setOnClickListener { startActivity(Intent(this@SignIn, Register::class.java)) }
        tvForgotPassword!!.setOnClickListener { startActivity(Intent(this@SignIn, ForgotPassword::class.java)) }
    }

    override fun validations(): Boolean {
        if (Validator.isEmpty(etEmailLogin!!.text.toString())) {
            etEmailLogin!!.error = "E-Mail is required!"
            isValid = false
        } else if (!Validator.isValidEmail(etEmailLogin!!.text.toString())) {
            etEmailLogin!!.error = "Please enter a valid email address"
            isValid = false
        } else if (Validator.isEmpty(etPasswordLogin!!.text.toString())) {
            etPasswordLogin!!.error = "Password is required!"
            isValid = false
        } else {
            etEmailLogin!!.error = null
            isValid = true
        }
        return isValid!!
    }

    private fun init() {
        //UI
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        tvSignUp = findViewById(R.id.tvSignUp)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        progressBar = findViewById(R.id.progressBar_cyclicSignin)
        sharedPref = getSharedPreferences("UserType", MODE_PRIVATE)
        editor = sharedPref!!.edit()
        progressBar!!.setVisibility(View.GONE)
        progressBar!!.getIndeterminateDrawable().setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY)

//Variables
        isValid = false
        validator = Validator()
        etEmailLogin!!.addTextChangedListener(validator)

//Firebase Objects intialized
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth!!.currentUser
        mFirebasedataRefSell = FirebaseDatabase.getInstance().reference
        mFirebasedataRefCust = FirebaseDatabase.getInstance().reference

//If User already signed in
        authViewModel!!.authCheck()
    }
}