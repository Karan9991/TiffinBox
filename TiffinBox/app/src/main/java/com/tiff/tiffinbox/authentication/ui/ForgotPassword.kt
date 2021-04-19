package com.tiff.tiffinbox.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Validator
import com.tiff.tiffinbox.authentication.model.User
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel
import com.tiff.tiffinbox.databinding.ActivityForgotPasswordBinding

class ForgotPassword : AppCompatActivity() {
    private var etForgotPassword: EditText? = null
    private var tvForgotPassword: TextView? = null
    private var btnForgotPassword: Button? = null
    private var isValid = false
    private var authViewModel: AuthViewModel? = null
    private var binding: ActivityForgotPasswordBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding = DataBindingUtil.setContentView(this@ForgotPassword, R.layout.activity_forgot_password)
        binding!!.setLifecycleOwner(this)
        binding!!.setAuthViewModel(authViewModel)
        val liveData: LiveData<User> = authViewModel!!.getUser(this)
        etForgotPassword = findViewById(R.id.etForgotPassword)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
        btnForgotPassword = findViewById(R.id.btnForgotPassword)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("Forgot Password")
        authViewModel!!.getUser(this).observe(this, { user ->
            binding!!.tvForgotPassword.text = liveData.value!!.email
          //  Log.e("onChanged()  ", " " + user.getEmail())
        })
        btnForgotPassword!!.setOnClickListener(View.OnClickListener {
            if (validations()) {
                authViewModel!!.submitEmail(etForgotPassword!!.getText().toString())
            }
        })
    }

    private fun validations(): Boolean {
        if (Validator.isEmpty(etForgotPassword!!.text.toString())) {
            etForgotPassword!!.error = "Enter an E-Mail Address"
            isValid = false
        } else if (!Validator.isValidEmail(etForgotPassword!!.text.toString())) {
            etForgotPassword!!.error = "Enter a Valid E-mail Address"
            isValid = false
        } else {
            isValid = true
        }
        return isValid
    }

    override fun onSupportNavigateUp(): Boolean {
        startActivity(Intent(this@ForgotPassword, SignIn::class.java))
        return super.onSupportNavigateUp()
    }
}