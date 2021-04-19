package com.tiff.tiffinbox.authentication.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Validate
import com.tiff.tiffinbox.Validator
import com.tiff.tiffinbox.authentication.model.User
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel
import com.tiff.tiffinbox.databinding.ActivityRegisterBinding
import fr.ganfra.materialspinner.MaterialSpinner

class Register : AppCompatActivity(), Validate {
    var tvsignin: TextView? = null
    var etName: EditText? = null
    var etMobile: EditText? = null
    var etEmail: EditText? = null
    var etPassword: EditText? = null
    var etAddress: EditText? = null
    var btnRegister: Button? = null
    var spinner: Spinner? = null
    private var progressBar: ProgressBar? = null
    var register: Register? = null

    //Firebase
    private var mDatabase: DatabaseReference? = null
    private var firebaseAuth: FirebaseAuth? = null
    var ITEMS = arrayOf("Customer", "Seller")
    var isValid: Boolean? = null
    var isSpinnerValid: Boolean? = null
    private var activityRegisterBinding: ActivityRegisterBinding? = null
    private var authViewModel: AuthViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        activityRegisterBinding = DataBindingUtil.setContentView(this@Register, R.layout.activity_register)
        activityRegisterBinding!!.setLifecycleOwner(this)
        activityRegisterBinding!!.setAuthViewModel(authViewModel)
        authViewModel!!.getUser(this).observe(this, { user ->
            etName!!.setText("")
            etAddress!!.setText("")
            etMobile!!.setText("")
            etEmail!!.setText("")
            etPassword!!.setText("")
            spinner!!.setSelection(0)
           // Log.e("onChanged()  ", " " + user.getEmail())
        })
        init()

//Address field
//        String apiKey = getString(R.string.api_key);
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//        }
//        etAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // onSearchCalled();
//            }
//        });
//        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                 //   onSearchCalled();
//                }
//            }
//        });
        btnRegister!!.setOnClickListener {
            if (validations() && setSpinnerError(spinner, "Please select user type")) {
                progressBar!!.visibility = View.VISIBLE
                authViewModel!!.register(progressBar!!, this@Register, etName!!.text.toString(), etMobile!!.text.toString(), etEmail!!.text.toString(), etPassword!!.text.toString(), etAddress!!.text.toString(), spinner!!.selectedItem.toString(), "default", "offline", etEmail!!.text.toString(), etEmail!!.text.toString().toLowerCase())
            }
        }
        tvsignin!!.setOnClickListener { startActivity(Intent(this@Register, SignIn::class.java)) }
    }

    private fun testNewUser(name: String, mobile: String, email: String, password: String, address: String, userType: String, imageURL: String, status: String, id: String, username: String, search: String) {
        val user = User(name, mobile, email, password, address, userType, imageURL, status, id, username, search)
        val key = mDatabase!!.database.reference.push().key
        val uid = firebaseAuth!!.uid
        mDatabase!!.child(userType).child(key!!).setValue(user)
        mDatabase!!.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("desc").setValue("""
    Tiffin and catering in Mississauga and Brampton. We make variety of dishes to cover full nutrition profile for a family keeping meals seasonal, traditional and healthy.
    1. Standard tiffin
    8oz dal
    8oz sabji
    8oz rice+ 4 roti
    Or
    8 roti + no rice
    Monthly rates
    Mon-Fri 190
    Mon-Sat 216
    2. Big tiffin
    12oz dal
    12oz sabji
    12oz rice + 6roti
    
    """.trimIndent())
        mDatabase!!.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("imageURL").setValue("Adsfd")
        mDatabase!!.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("price").setValue("On Call")
    }

    override fun validations(): Boolean {
        if (Validator.isEmpty(etName!!.text.toString())) {
            etName!!.error = "Name is required!"
            isValid = false
        } else if (Validator.isEmpty(etMobile!!.text.toString())) {
            etMobile!!.error = "Mobile Number is required!"
            isValid = false
        } else if (Validator.isEmpty(etEmail!!.text.toString())) {
            etEmail!!.error = "E-Mail is required!"
            isValid = false
        } else if (!Validator.isValidEmail(etEmail!!.text.toString())) {
            etEmail!!.error = "Please enter a valid email address"
            isValid = false
        } else if (Validator.isEmpty(etPassword!!.text.toString())) {
            etPassword!!.error = "Password is required!"
            isValid = false
        } else if (!Validator.isValidPassword(etPassword!!.text.toString())) {
            etPassword!!.error = "Password must between 8 and 20 characters; must contain at least one lowercase letter, one uppercase letter, one numeric digit, and one special character, but cannot contain whitespace"
            isValid = false
        } else if (Validator.isEmpty(etAddress!!.text.toString())) {
            etAddress!!.error = "Address is required!"
            isValid = false
        } else {
            isValid = true
        }
        return isValid!!
    }

    fun setSpinnerError(spinner: Spinner?, error: String?): Boolean {
        val selectedItemOfMySpinner = spinner!!.selectedItemPosition
        val selectedView = spinner.selectedView
        if (selectedItemOfMySpinner == 0) {
            if (selectedView != null && selectedView is TextView) {
                val selectedTextView = selectedView
                selectedTextView.text = error // actual error message
                selectedTextView.setTextColor(Color.RED)
            }
            isSpinnerValid = false
        } else {
            isSpinnerValid = true
        }
        return isSpinnerValid!!
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    //Address field
    //    public void onSearchCalled() {
    //        // Set the fields to specify which types of place data to return.
    //        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
    //        // Start the autocomplete intent.
    //        Intent intent = new Autocomplete.IntentBuilder(
    //                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA") //CANADA
    //                .build(this);
    //        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    //    }
    //    @Override
    //    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    //        super.onActivityResult(requestCode, resultCode, data);
    //        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
    //            if (resultCode == RESULT_OK) {
    //                Place place = Autocomplete.getPlaceFromIntent(data);
    //                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
    //                etAddress.setText(place.getAddress().toString());
    //                etAddress.setError(null);
    //             //   Toast.makeText(Register.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
    //                String address = place.getAddress();
    //                // do query with address
    //
    //            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
    //                // TODO: Handle the error.
    //                Status status = Autocomplete.getStatusFromIntent(data);
    //                Toast.makeText(Register.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
    //                Log.i(TAG, status.getStatusMessage());
    //            } else if (resultCode == RESULT_CANCELED) {
    //                // The user canceled the operation.
    //            }
    //        }
    //    }
    private fun init() {
        //UI
        etName = findViewById(R.id.etName)
        etMobile = findViewById(R.id.etMobile)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etAddress = findViewById(R.id.etAddress)
        tvsignin = findViewById(R.id.tvsignin)
        btnRegister = findViewById(R.id.btnRegister)
        progressBar = findViewById(R.id.progressBar_cyclic)
        register = this

//Variables
        isValid = false
        isSpinnerValid = false
        //Firebase Objects
        mDatabase = FirebaseDatabase.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ITEMS)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner = findViewById<View>(R.id.spinner) as MaterialSpinner
        spinner!!.adapter = adapter
        progressBar!!.setVisibility(View.GONE)
        progressBar!!.getIndeterminateDrawable().setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
    }

    companion object {
        //Address field
        private const val AUTOCOMPLETE_REQUEST_CODE = 22
        private val TAG = Register::class.java.simpleName
    }
}