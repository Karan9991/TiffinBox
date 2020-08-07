package com.tiff.tiffinbox.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tiff.tiffinbox.Authentication.Model.User;
import com.tiff.tiffinbox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Register extends AppCompatActivity {
TextView tvsignin;
EditText etName, etMobile, etEmail, etPassword, etAddress;
Button btnRegister;
Spinner spinner;
private ProgressBar progressBar;

//Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    String[] ITEMS = {"Customer", "Seller"};
    Boolean isValid, isSpinnerValid;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//UI
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        tvsignin = findViewById(R.id.tvsignin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar_cyclic);

//Variables
        isValid = false;
        isSpinnerValid = false;
//Firebase Objects
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.MULTIPLY);


        btnRegister.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (validations() && setSpinnerError(spinner,"Please select user type")){
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String userid = firebaseUser.getUid();
                                    writeNewUser(etName.getText().toString(), etMobile.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etAddress.getText().toString(), spinner.getSelectedItem().toString(), "default", "offline", userid, etEmail.getText().toString(), etEmail.getText().toString().toLowerCase());
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                   // updateUI(user);
                                    Toast.makeText(getApplicationContext(), "Registered Successfully Please check your E-Mail for verification", Toast.LENGTH_LONG).show();
                                    etName.setText("");
                                    etAddress.setText("");
                                    etMobile.setText("");
                                    etEmail.setText("");
                                    etPassword.setText("");
                                    spinner.setSelection(0);
                                    // startActivity(new Intent(SignUp.this, SignIn.class));
                                    //finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "SignUp Unsuccessful " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
});
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, SignIn.class));
            }
        });
    }

    private void writeNewUser(String name, String mobile, String email, String password, String address, String userType, String imageURL, String status, String id, String username, String search) {

        User user = new User(name, mobile, email, password, address, userType, imageURL, status, id, username, search);
      //  String key = mDatabase.getDatabase().getReference().push().getKey();
       String uid = firebaseAuth.getCurrentUser().getUid();
        mDatabase.child(userType).child(uid).setValue(user);
    }

private boolean validations(){
       if (TextUtils.isEmpty(etName.getText())) {
          etName.setError("Name is required!");
           isValid = false;
       }
      else if (TextUtils.isEmpty(etMobile.getText())) {
          etMobile.setError("Mobile Number is required!");
           isValid = false;
       }
      else if (TextUtils.isEmpty(etEmail.getText())) {
          etEmail.setError("E-Mail is required!");
           isValid = false;
       }
      else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
          etEmail.setError("Please enter a valid email address");
           isValid = false;
       }
      else if (TextUtils.isEmpty(etPassword.getText())) {
        etPassword.setError("Password is required!");
           isValid = false;
       }
       else if(!PASSWORD_PATTERN.matcher(etPassword.getText().toString().trim()).matches()){
           etPassword.setError( "Password between 8 and 20 characters; must contain at least one lowercase letter, one uppercase letter, one numeric digit, and one special character, but cannot contain whitespace" );
           isValid = false;
       }
       else if (TextUtils.isEmpty(etAddress.getText())) {
           etAddress.setError("Address is required!");
           isValid = false;
       }
      else {
          isValid = true;
       }
          return isValid;
      }

    public boolean setSpinnerError(Spinner spinner, String error){
        int selectedItemOfMySpinner = spinner.getSelectedItemPosition();
//        String actualPositionOfMySpinner = (String) spinner.getItemAtPosition(selectedItemOfMySpinner);
        View selectedView = spinner.getSelectedView();
        if (selectedItemOfMySpinner == 0) {
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError(error);
                selectedTextView.setText(error); // actual error message
                selectedTextView.setTextColor(Color.RED);
            }
            isSpinnerValid = false;
        }
        else {
            isSpinnerValid = true;
        }
        return isSpinnerValid;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);

    }

}
