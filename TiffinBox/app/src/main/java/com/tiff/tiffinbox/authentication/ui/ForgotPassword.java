package com.tiff.tiffinbox.authentication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tiff.tiffinbox.Validator;
import com.tiff.tiffinbox.authentication.model.User;
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.databinding.ActivityForgotPasswordBinding;

public class ForgotPassword extends AppCompatActivity {

    private EditText etForgotPassword;
    private TextView tvForgotPassword;
    private Button btnForgotPassword;
    private boolean isValid;
    private AuthViewModel authViewModel;
    private ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        binding = DataBindingUtil.setContentView(ForgotPassword.this, R.layout.activity_forgot_password);
        binding.setLifecycleOwner(this);
        binding.setAuthViewModel(authViewModel);
        LiveData<User> liveData = authViewModel.getUser(this);

        etForgotPassword = findViewById(R.id.etForgotPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        authViewModel.getUser( this).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                    binding.tvForgotPassword.setText(liveData.getValue().email);
                    Log.e("onChanged()  "," "+user.getEmail());
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()) {
                    authViewModel.submitEmail(etForgotPassword.getText().toString());
                }
            }
        });
    }

    private boolean validations(){
        if (Validator.isEmpty(etForgotPassword.getText().toString())) {
            etForgotPassword.setError("Enter an E-Mail Address");
            isValid = false;
        }
       else if (!Validator.isValidEmail(etForgotPassword.getText().toString())){
            etForgotPassword.setError( "Enter a Valid E-mail Address");
            isValid = false;
        }
        else {
            isValid = true;
        }
        return isValid;
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(ForgotPassword.this, SignIn.class));
        return super.onSupportNavigateUp();
    }
}
