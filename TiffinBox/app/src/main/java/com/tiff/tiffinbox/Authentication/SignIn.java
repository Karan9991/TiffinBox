package com.tiff.tiffinbox.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tiff.tiffinbox.Chat.MainActivity;
import com.tiff.tiffinbox.Customer.Customer;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.AddView;
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
TextView tvSignUp, tvForgotPassword;
private ProgressBar progressBar;
public static String UT;

SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


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

    DatabaseReference mFirebasedataRefSell;
    DatabaseReference mFirebasedataRefCust;
    DatabaseReference dfOnline;
    DatabaseReference dfOnline2;
    Query querySeller, queryCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//UI
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar = findViewById(R.id.progressBar_cyclicSignin);
        sharedPref = getSharedPreferences("UserType", Context.MODE_PRIVATE);
        editor =  sharedPref.edit();

        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);

//Variables
        isValid = false;
//Firebase Objects intialized
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mFirebasedataRefSell  = FirebaseDatabase.getInstance().getReference();
        mFirebasedataRefCust  = FirebaseDatabase.getInstance().getReference();


//If User already signed in
                if (firebaseUser!=null&&firebaseAuth.getCurrentUser().isEmailVerified()){
                    dfOnline = FirebaseDatabase.getInstance().getReference();
                    dfOnline2 = FirebaseDatabase.getInstance().getReference();

                    Query myTopPostsQuery = dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail());
                    Query myTopPostsQuery2 = dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail());
                    if (sharedPref.getString("UT",null).equals("Customer")){
                        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.i("qqqqqqqqqqqqqqqqqqqqqqqqq","q"+dataSnapshot.getKey());
                                if (dataSnapshot.getKey().equals("Customer")){
                                    dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
                                    startActivity(new Intent(SignIn.this, Customer.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else if (sharedPref.getString("UT",null).equals("Seller")) {
                        myTopPostsQuery2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getKey().equals("Seller")) {

                                    dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
                                    startActivity(new Intent(SignIn.this, AddView.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    //ok
//                    querySeller = mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail());
//                    queryCustomer = mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail());
//                    triggerQuey();
                 // finish();
                 //   startActivity(new Intent(SignIn.this, AddView.class));
                    }

                btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()){
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(etEmailLogin.getText().toString(), etPasswordLogin.getText().toString()).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                if (firebaseAuth.getCurrentUser().isEmailVerified()){
//startActivity(new Intent(SignIn.this, Customer.class));
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

                tvSignUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SignIn.this, Register.class));
                    }
                });

           tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, ForgotPassword.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
     //   dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
     //   mFirebasedataRefSell.removeEventListener(this);
       // mFirebasedataRefCust.removeEventListener(this);
        Log.i("RRRRRRRRRRRRRRRRRRRRRRR","RRRRRRRRRRRRRRRRRRRRRRRRR");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
//        mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
//        mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Log.i("qqqq","qqqq");

        if (dataSnapshot.getValue() != null) {
            String key = dataSnapshot.getKey();
            if (key.equals("Customer")) {
                data = dataSnapshot.getValue().toString();
              //  Toast.makeText(getApplicationContext(), "value " + data, Toast.LENGTH_LONG).show()
         //       mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
       // mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
                editor.putString("UT", "Customer");
                editor.commit();
               mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
                startActivity(new Intent(SignIn.this, Customer.class));
                //finish();
 UT = "Customer";
                Log.i("cccccccccccccccccccccccc","cccccccccccccccccccccc");
//                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            } else if (key.equals("Seller")){
                Log.i("sssssssssssssssssssssssss","ssssssssssssssssssssssssssssss");

                UT = "Seller";
                editor.putString("UT", "Seller");
                editor.commit();

                data2 = dataSnapshot.getValue().toString();
              //  Toast.makeText(getApplicationContext(),"firebase "+data2,Toast.LENGTH_SHORT).show();
               // mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
     //mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);
              mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(etEmailLogin.getText().toString()).removeEventListener(this);

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
