package com.example.tiffinbox.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiffinbox.Customer;
import com.example.tiffinbox.MainActivity;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.AddView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SignIn extends AppCompatActivity implements ValueEventListener {
EditText etEmailLogin, etPasswordLogin;
Button btnLogin;

     Boolean isValid;
     String data, data2;
    Map<String, Object> map,map2;
//Firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
//    FirebaseDatabase firebaseDatabaseCust = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReferenceCust = firebaseDatabaseCust.getReference();
//    DatabaseReference dataCust = databaseReferenceCust.child("Customer");
//    FirebaseDatabase firebaseDatabaseSell = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReferenceSell = firebaseDatabaseSell.getReference();
//    DatabaseReference dataSell = databaseReferenceSell.child("Seller");

    DatabaseReference mFirebasedataRefSell = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mFirebasedataRefCust = FirebaseDatabase.getInstance().getReference();
    Query querySeller, queryCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//UI
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
//Variables
        isValid = false;
//Firebase Objects
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //        if (firebaseUser!=null&&firebaseAuth.getCurrentUser().isEmailVerified()){
//            startActivity(new Intent(SignIn.this, MainActivity.class));
//            finish();
//        }
                btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()){
                    firebaseAuth.signInWithEmailAndPassword(etEmailLogin.getText().toString(), etPasswordLogin.getText().toString()).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()){

                                    querySeller = mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(etEmailLogin.getText().toString());
                                    queryCustomer = mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(etEmailLogin.getText().toString());
                                triggerQuey();
                                }else {
                                    Toast.makeText(SignIn.this,"Please verify your E-Mail address", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(SignIn.this, "SignIn Failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String key = dataSnapshot.getKey();
            if (key.equals("Customer")) {
                data = dataSnapshot.getValue().toString();
                Toast.makeText(getApplicationContext(), "value " + data, Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignIn.this, Customer.class));

            } else if (key.equals("Seller")){
                data2 = dataSnapshot.getValue().toString();
                Toast.makeText(getApplicationContext(),"firebase "+data2,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignIn.this, AddView.class));

            }
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(getApplicationContext(),"failed "+data2,Toast.LENGTH_SHORT).show();

    }
public void triggerQuey(){
    if (queryCustomer!=null && querySeller!=null) {
        querySeller.addValueEventListener(this);
        queryCustomer.addValueEventListener(this);
    }
}
    private boolean validations(){
        if (TextUtils.isEmpty(etEmailLogin.getText())) {
            etEmailLogin.setError("E-Mail is required!");
            isValid = false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(etEmailLogin.getText().toString().trim()).matches()) {
            etEmailLogin.setError("Please enter a valid email address");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etPasswordLogin.getText())) {
            etPasswordLogin.setError("Password is required!");
            isValid = false;
        }
        else {
            isValid = true;
        }
        return isValid;
    }
}
