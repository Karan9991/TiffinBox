package com.tiff.tiffinbox.Authentication;

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

import com.tiff.tiffinbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
EditText etForgotPassword;
Button btnForgotPassword;

private FirebaseAuth firebaseAuth;

private boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etForgotPassword = findViewById(R.id.etForgotPassword);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        isValid = false;
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()){
                    firebaseAuth.sendPasswordResetEmail(etForgotPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                etForgotPassword.setError(null);
                                Toast.makeText(ForgotPassword.this, "Password Link sent to your E-Mail, Please check your E-Mail", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }

            }
        });
    }
    private boolean validations(){
        if (TextUtils.isEmpty(etForgotPassword.getText())) {
            etForgotPassword.setError("E-Mail is required!");
            isValid = false;
        }
       else if (!Patterns.EMAIL_ADDRESS.matcher(etForgotPassword.getText().toString().trim()).matches()){
            etForgotPassword.setError( "Please enter a valid email address");
        }

        else {
            isValid = true;
        }
        return isValid;
    }
    
    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(ForgotPassword.this,SignIn.class));

        return super.onSupportNavigateUp();
    }
}
