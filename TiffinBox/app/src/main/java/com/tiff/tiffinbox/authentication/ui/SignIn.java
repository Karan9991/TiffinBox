package com.tiff.tiffinbox.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiff.tiffinbox.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tiff.tiffinbox.Validate;
import com.tiff.tiffinbox.Validator;
import com.tiff.tiffinbox.authentication.model.User;
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel;
import com.tiff.tiffinbox.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity implements Validate {
    EditText etEmailLogin, etPasswordLogin;
    Button btnLogin;
    TextView tvSignUp, tvForgotPassword;
    private ProgressBar progressBar;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    Boolean isValid;
    Validator validator;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference mFirebasedataRefSell;
    DatabaseReference mFirebasedataRefCust;
    DatabaseReference dfOnline;
    DatabaseReference dfOnline2;

    private ActivitySignInBinding activitySignInBinding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        activitySignInBinding = DataBindingUtil.setContentView(SignIn.this, R.layout.activity_sign_in);
        activitySignInBinding.setLifecycleOwner(this);
        activitySignInBinding.setAuthViewModel(authViewModel);

        authViewModel.getUser(this).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.e("onChanged()  "," "+user.getEmail());
            }
        });

        init();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()){
                    authViewModel.login(progressBar, SignIn.this, etEmailLogin.getText().toString(), etPasswordLogin.getText().toString());
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
    public boolean validations() {
        if (Validator.isEmpty(etEmailLogin.getText().toString())){
            etEmailLogin.setError("E-Mail is required!");
            isValid = false;
        }
        else if(!Validator.isValidEmail(etEmailLogin.getText().toString())){
            etEmailLogin.setError("Please enter a valid email address");
            isValid = false;
        }
        else if (Validator.isEmpty(etPasswordLogin.getText().toString())){
            etPasswordLogin.setError("Password is required!");
            isValid = false;
        }
        else {
            etEmailLogin.setError(null);
            isValid = true;
        }
        return isValid;
    }

    private void init(){
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
        validator = new Validator();
        etEmailLogin.addTextChangedListener(validator);

//Firebase Objects intialized
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mFirebasedataRefSell  = FirebaseDatabase.getInstance().getReference();
        mFirebasedataRefCust  = FirebaseDatabase.getInstance().getReference();

//If User already signed in
        authViewModel.authCheck();
    }
}
