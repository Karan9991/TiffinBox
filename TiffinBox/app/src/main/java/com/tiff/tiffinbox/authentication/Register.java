package com.tiff.tiffinbox.authentication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Validate;
import com.tiff.tiffinbox.authentication.Model.User;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Register extends AppCompatActivity implements Validate {
TextView tvsignin;
EditText etName, etMobile, etEmail, etPassword, etAddress;
Button btnRegister;
Spinner spinner;
private ProgressBar progressBar;
AuthenticationPresenterLayer registerPresenterLayer;
 Register register;
//Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
//Address field
    private static final int AUTOCOMPLETE_REQUEST_CODE = 22;
    private static final String TAG = com.tiff.tiffinbox.authentication.Register.class.getSimpleName();

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

        registerPresenterLayer = new AuthenticationPresenterLayer(this);
        register = this;

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

//Address field
        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearchCalled();
            }
        });
        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    onSearchCalled();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //      String userid =  mDatabase.getDatabase().getReference().push().getKey();
//        startActivity(new Intent(Register.this, AddView.class));
                //  testNewUser(etName.getText().toString(), etMobile.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etAddress.getText().toString(), spinner.getSelectedItem().toString(), "default", "offline", userid, etEmail.getText().toString(), etEmail.getText().toString().toLowerCase());
                if (validations() && setSpinnerError(spinner, "Please select user type")) {
                    registerPresenterLayer.progressbarShow(progressBar);
                    firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            registerPresenterLayer.progressbarHide(progressBar);
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            String userid = firebaseUser.getUid();
                                            writeNewUser(etName.getText().toString(), etMobile.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etAddress.getText().toString(), spinner.getSelectedItem().toString(), "default", "offline", userid, etEmail.getText().toString(), etEmail.getText().toString().toLowerCase());
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            // updateUI(user);
                                            registerPresenterLayer.toast(getApplicationContext(), "Registered Successfully Please check your E-Mail for verification");
                                            etName.setText("");
                                            etAddress.setText("");
                                            etMobile.setText("");
                                            etEmail.setText("");
                                            etPassword.setText("");
                                            spinner.setSelection(0);
                                            // startActivity(new Intent(SignUp.this, SignIn.class));
                                            //finish();
                                        } else {
                                            registerPresenterLayer.toast(getApplicationContext(), task.getException().getMessage());
                                        }
                                    }
                                });

                            } else {
                                registerPresenterLayer.toast(getApplicationContext(), "SignUp Unsuccessful " + task.getException().getMessage());
                            }
                        }
                    });
                }

            }

});
        tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPresenterLayer.navigateToAhead(SignIn.class);
            }
        });
    }

    private void writeNewUser(String name, String mobile, String email, String password, String address, String userType, String imageURL, String status, String id, String username, String search) {

        User user = new User(name, mobile, email, password, address, userType, imageURL, status, id, username, search);
      //  String key = mDatabase.getDatabase().getReference().push().getKey();
       String uid = firebaseAuth.getCurrentUser().getUid();
        mDatabase.child(userType).child(uid).setValue(user);
    }
    private void testNewUser(String name, String mobile, String email, String password, String address, String userType, String imageURL, String status, String id, String username, String search) {

        User user = new User(name, mobile, email, password, address, userType, imageURL, status, id, username, search);
          String key = mDatabase.getDatabase().getReference().push().getKey();
        String uid = firebaseAuth.getUid();
        mDatabase.child(userType).child(key).setValue(user);
        mDatabase.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("desc").setValue("Tiffin and catering in Mississauga and Brampton. We make variety of dishes to cover full nutrition profile for a family keeping meals seasonal, traditional and healthy.\n" +
                "1. Standard tiffin\n" +
                "8oz dal\n" +
                "8oz sabji\n" +
                "8oz rice+ 4 roti\n" +
                "Or\n" +
                "8 roti + no rice\n" +
                "Monthly rates\n" +
                "Mon-Fri 190\n" +
                "Mon-Sat 216\n" +
                "2. Big tiffin\n" +
                "12oz dal\n" +
                "12oz sabji\n" +
                "12oz rice + 6roti\n");
        mDatabase.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("imageURL").setValue("Adsfd");
        mDatabase.child(userType).child(key).child("Recipe").child("Apni Rasoi: Pure Veg tiffin and catering service.").child("price").setValue("On Call");

    }
    @Override
    public boolean validations() {
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
            etPassword.setError( "Password must between 8 and 20 characters; must contain at least one lowercase letter, one uppercase letter, one numeric digit, and one special character, but cannot contain whitespace" );
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

//Address field
    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA") //CANADA
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
                etAddress.setText(place.getAddress().toString());
                etAddress.setError(null);
             //   Toast.makeText(Register.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
                String address = place.getAddress();
                // do query with address

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Toast.makeText(Register.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
