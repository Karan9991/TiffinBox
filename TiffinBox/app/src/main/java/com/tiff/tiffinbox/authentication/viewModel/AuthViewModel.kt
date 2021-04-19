package com.tiff.tiffinbox.authentication.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tiff.tiffinbox.Customer.ui.Customer
import com.tiff.tiffinbox.Seller.AddView
import com.tiff.tiffinbox.authentication.model.User
import es.dmoral.toasty.Toasty

class AuthViewModel : ViewModel(), ValueEventListener {
    @JvmField
    var user = MutableLiveData<User>()
    var userMutableLiveData: MutableLiveData<User>? = null
    private var context: Context? = null
    private var data: String? = null
    private var data2: String? = null
    private var email: String? = null
    var editor: SharedPreferences.Editor? = null
    var sharedPref: SharedPreferences? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null
    private var mDatabase: DatabaseReference? = null
    private var querySeller: Query? = null
    private var queryCustomer: Query? = null
    private var mFirebasedataRefSell: DatabaseReference? = null
    private var mFirebasedataRefCust: DatabaseReference? = null
    private var dfOnline: DatabaseReference? = null
    private var dfOnline2: DatabaseReference? = null
    fun getUser(context: Context): MutableLiveData<User> {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData()
            sharedPref = context.getSharedPreferences("UserType", Context.MODE_PRIVATE)
            editor = sharedPref!!.edit()
            firebaseAuth = FirebaseAuth.getInstance()
            mDatabase = FirebaseDatabase.getInstance().reference
            mFirebasedataRefSell = FirebaseDatabase.getInstance().reference
            mFirebasedataRefCust = FirebaseDatabase.getInstance().reference
            this.context = context
        }
        return userMutableLiveData!!
    }

    fun submitEmail(email: String?) {
        firebaseAuth!!.sendPasswordResetEmail(email!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val loginUser = User(email)
                userMutableLiveData!!.postValue(loginUser)
                Toasty.success(context!!, "Password Link sent to your E-Mail, Please check your E-Mail", Toast.LENGTH_LONG).show()
            } else {
                val loginUser = User(email)
                userMutableLiveData!!.postValue(loginUser)
                Toasty.error(context!!, task.exception!!.message!!, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun register(progressBar: ProgressBar, activity: Activity?, name: String?, mobile: String?, email: String?, password: String?, address: String?, userType: String?, imageURL: String?,
                 status: String?, username: String?, search: String?) {
        firebaseAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(activity!!) { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                firebaseAuth!!.currentUser!!.sendEmailVerification().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth!!.currentUser
                        val userUid = firebaseUser!!.uid
                        val registerUser = User(name, mobile, email, password, address, userType, imageURL, status, userUid, username, search)
                        userMutableLiveData!!.postValue(registerUser)
                        val uid = firebaseAuth!!.currentUser!!.uid
                        mDatabase!!.child(userType!!).child(uid).setValue(registerUser)
                        Toasty.success(context!!, "Registered Successfully Please check your E-Mail for verification", Toast.LENGTH_LONG).show()
                    } else {
                        Toasty.error(context!!, task.exception!!.message!!, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toasty.error(context!!, "SignUp Unsuccessful " + task.exception!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(progressBar: ProgressBar, activity: Activity?, email: String?, password: String?) {
        this.email = email
        progressBar.visibility = View.VISIBLE
        firebaseAuth!!.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener(activity!!) { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                if (firebaseAuth!!.currentUser!!.isEmailVerified) {
                    querySeller = mFirebasedataRefSell!!.child("Seller").orderByChild("email").equalTo(email)
                    queryCustomer = mFirebasedataRefCust!!.child("Customer").orderByChild("email").equalTo(email)
                    triggerQuey()
                    Toasty.success(context!!, "Login Success", Toast.LENGTH_SHORT).show()
                } else {
                    Toasty.error(context!!, "Please verify your E-Mail address", Toast.LENGTH_LONG).show()
                }
            } else {
                Toasty.error(context!!, "SignIn Failed " + task.exception!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun authCheck() {
        firebaseUser = firebaseAuth!!.currentUser
        if (firebaseUser != null && firebaseAuth!!.currentUser!!.isEmailVerified) {
            dfOnline = FirebaseDatabase.getInstance().reference
            dfOnline2 = FirebaseDatabase.getInstance().reference
            val myTopPostsQuery = dfOnline!!.child("Customer").orderByChild("email").equalTo(firebaseUser!!.email)
            val myTopPostsQuery2 = dfOnline2!!.child("Seller").orderByChild("email").equalTo(firebaseUser!!.email)
            if (sharedPref!!.getString("UT", null) == "Customer") {
                myTopPostsQuery.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.key == "Customer") {
                            dfOnline!!.child("Customer").orderByChild("email").equalTo(firebaseUser!!.email).removeEventListener(this)
                            context!!.startActivity(Intent(context, Customer::class.java))
                            (context as Activity?)!!.finish()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            } else if (sharedPref!!.getString("UT", null) == "Seller") {
                myTopPostsQuery2.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.key == "Seller") {
                            dfOnline2!!.child("Seller").orderByChild("email").equalTo(firebaseUser!!.email).removeEventListener(this)
                            context!!.startActivity(Intent(context, AddView::class.java))
                            (context as Activity?)!!.finish()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }
        }
    }

    private fun triggerQuey() {
        if (queryCustomer != null && querySeller != null) {
            querySeller!!.addValueEventListener(this)
            queryCustomer!!.addValueEventListener(this)
        }
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot.value != null) {
            val key = dataSnapshot.key
            if (key == "Customer") {
                data = dataSnapshot.value.toString()
                editor!!.putString("UT", "Customer")
                editor!!.commit()
                mFirebasedataRefCust!!.child("Customer").orderByChild("email").equalTo(email).removeEventListener(this)
                context!!.startActivity(Intent(context, Customer::class.java))
                UT = "Customer"
                (context as Activity?)!!.finish()
            } else if (key == "Seller") {
                UT = "Seller"
                editor!!.putString("UT", "Seller")
                editor!!.commit()
                data2 = dataSnapshot.value.toString()
                mFirebasedataRefSell!!.child("Seller").orderByChild("email").equalTo(email).removeEventListener(this)
                context!!.startActivity(Intent(context, AddView::class.java))
                (context as Activity?)!!.finish()
            }
        }
    }

    override fun onCancelled(databaseError: DatabaseError) {
        Toast.makeText(context, "failed $data2", Toast.LENGTH_SHORT).show()
    }

    companion object {
        var UT: String? = null
    }
}