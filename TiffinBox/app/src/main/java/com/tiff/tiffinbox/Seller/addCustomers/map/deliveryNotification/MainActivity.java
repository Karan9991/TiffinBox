package com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.tiff.tiffinbox.R;

public class MainActivity extends AppCompatActivity {

    EditText EmailTB, PassTB;
    Button LoginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EmailTB = findViewById(R.id.EmailTB);
        PassTB = findViewById(R.id.PassTB);
        LoginB = findViewById(R.id.Login);
        if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
            startActivity(new Intent(this, com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.SendNotif.class));
        } else {
            LoginB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailTB.getText().toString().trim(), PassTB.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            startActivity(new Intent(com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.MainActivity.this, com.tiff.tiffinbox.Seller.addCustomers.map.deliveryNotification.SendNotif.class));
                        }
                    });
                }
            });
        }
    }
}
