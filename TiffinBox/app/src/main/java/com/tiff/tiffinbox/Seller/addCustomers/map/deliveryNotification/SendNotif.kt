package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.Client.getClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendNotif : AppCompatActivity() {
    var UserTB: EditText? = null
    var Title: EditText? = null
    var Message: EditText? = null
    var send: Button? = null
    private var apiService: APIService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_notif)
        UserTB = findViewById(R.id.UserID)
        UserTB!!.setText("kpqwHFjCd6NjwMvhaTXhp1Ejk5J3")
        Title = findViewById(R.id.Title)
        Message = findViewById(R.id.Message)
        send = findViewById(R.id.button)
        apiService = getClient("https://fcm.googleapis.com/")!!.create(APIService::class.java)
        send!!.setOnClickListener(View.OnClickListener {
            FirebaseDatabase.getInstance().reference.child("Tokens").child(UserTB!!.getText().toString().trim { it <= ' ' }).child("token").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val usertoken = dataSnapshot.getValue(String::class.java)
                    sendNotifications(usertoken, Title!!.getText().toString().trim { it <= ' ' }, Message!!.getText().toString().trim { it <= ' ' })
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        })
        UpdateToken()
    }

    private fun UpdateToken() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val refreshToken = FirebaseInstanceId.getInstance().token
        val token = Token(refreshToken)
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(token)
    }

    fun sendNotifications(usertoken: String?, title: String?, message: String?) {
        val data = Data(title, message)
        val sender = NotificationSender(data, usertoken)
        apiService!!.sendNotifcation(sender)!!.enqueue(object : Callback<MyResponse?> {
            //           //change callback<myresposnse>
            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.code() == 200) {
                    if (response.body()!!.success != 1) {
                        Toast.makeText(this@SendNotif, "Failed ", Toast.LENGTH_LONG)
                    }
                }            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

//    object : Callback<MyResponse> {
//        override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse>) {
//            if (response.code() == 200) {
//                if (response.body()!!.success != 1) {
//                    Toast.makeText(this@SendNotif, "Failed ", Toast.LENGTH_LONG)
//                }
//            }
//        }
//
//        //            kpqwHFjCd6NjwMvhaTXhp1Ejk5J3
//        override fun onFailure(call: Call<MyResponse>, t: Throwable) {}
//    }
}