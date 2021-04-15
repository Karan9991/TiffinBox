package com.tiff.tiffinbox.authentication.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
import com.tiff.tiffinbox.Customer.ui.Customer;
import com.tiff.tiffinbox.Seller.AddView;
import com.tiff.tiffinbox.authentication.model.User;

import es.dmoral.toasty.Toasty;

public class AuthViewModel extends ViewModel implements ValueEventListener {

    public MutableLiveData<User> user = new MutableLiveData<>();
    public MutableLiveData<User> userMutableLiveData;
    private Context context;
    private String data, data2, email;
    public static String UT;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    private Query querySeller, queryCustomer;
    private DatabaseReference mFirebasedataRefSell;
    private DatabaseReference mFirebasedataRefCust;
    private DatabaseReference dfOnline;
    private DatabaseReference dfOnline2;

    public MutableLiveData<User> getUser(Context context) {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
            sharedPref = context.getSharedPreferences("UserType", Context.MODE_PRIVATE);
            editor =  sharedPref.edit();
            firebaseAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mFirebasedataRefSell  = FirebaseDatabase.getInstance().getReference();
            mFirebasedataRefCust  = FirebaseDatabase.getInstance().getReference();
            this.context = context;
        }
        return userMutableLiveData;
    }

    public void submitEmail(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    User loginUser = new User(email);
                    userMutableLiveData.postValue(loginUser);
                    Toasty.success(context, "Password Link sent to your E-Mail, Please check your E-Mail", Toast.LENGTH_LONG).show();
                } else {
                    User loginUser = new User(email);
                    userMutableLiveData.postValue(loginUser);
                    Toasty.error(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void register(ProgressBar progressBar, Activity activity, String name, String mobile, String email, String password, String address, String userType, String imageURL,
                         String status, String username, String search){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String userUid = firebaseUser.getUid();
                                User registerUser = new User(name, mobile, email, password, address, userType, imageURL, status, userUid, username, search);
                                userMutableLiveData.postValue(registerUser);
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                mDatabase.child(userType).child(uid).setValue(registerUser);
                                Toasty.success(context, "Registered Successfully Please check your E-Mail for verification", Toast.LENGTH_LONG).show();
                            } else {
                                Toasty.error(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toasty.error(context, "SignUp Unsuccessful " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void login(ProgressBar progressBar, Activity activity, String email, String password){
        this.email = email;
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        querySeller = mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(email);
                        queryCustomer = mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(email);
                        triggerQuey();
                        Toasty.success(context, "Login Success", Toast.LENGTH_SHORT).show();
                    }else {
                        Toasty.error(context, "Please verify your E-Mail address", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toasty.error(context, "SignIn Failed " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void authCheck(){
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null&&firebaseAuth.getCurrentUser().isEmailVerified()){
            dfOnline = FirebaseDatabase.getInstance().getReference();
            dfOnline2 = FirebaseDatabase.getInstance().getReference();

            Query myTopPostsQuery = dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail());
            Query myTopPostsQuery2 = dfOnline2.child("Seller").orderByChild("email").equalTo(firebaseUser.getEmail());

            if (sharedPref.getString("UT",null).equals("Customer")){
                myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().equals("Customer")){
                            dfOnline.child("Customer").orderByChild("email").equalTo(firebaseUser.getEmail()).removeEventListener(this);
                            context.startActivity(new Intent(context, Customer.class));
                            ((Activity)context).finish();
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
                            context.startActivity(new Intent(context, AddView.class));
                            ((Activity)context).finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void triggerQuey() {
        if (queryCustomer != null && querySeller != null) {
            querySeller.addValueEventListener(this);
            queryCustomer.addValueEventListener(this);
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue() != null) {
            String key = dataSnapshot.getKey();
            if (key.equals("Customer")) {
                data = dataSnapshot.getValue().toString();
                editor.putString("UT", "Customer");
                editor.commit();
                mFirebasedataRefCust.child("Customer").orderByChild("email").equalTo(email).removeEventListener(this);
                context.startActivity(new Intent(context, Customer.class));
                UT = "Customer";
                ((Activity)context).finish();
            } else if (key.equals("Seller")){
                UT = "Seller";
                editor.putString("UT", "Seller");
                editor.commit();
                data2 = dataSnapshot.getValue().toString();
                mFirebasedataRefSell.child("Seller").orderByChild("email").equalTo(email).removeEventListener(this);
                context.startActivity(new Intent(context, AddView.class));
                ((Activity)context).finish();
            }
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(context,"failed "+data2,Toast.LENGTH_SHORT).show();
    }
}
