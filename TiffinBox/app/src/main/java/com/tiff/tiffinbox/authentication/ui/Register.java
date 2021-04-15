package com.tiff.tiffinbox.authentication.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.MultiDex;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Validate;
import com.tiff.tiffinbox.Validator;
import com.tiff.tiffinbox.authentication.model.User;
import com.tiff.tiffinbox.authentication.viewModel.AuthViewModel;
import com.tiff.tiffinbox.databinding.ActivityRegisterBinding;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Register extends AppCompatActivity implements Validate {
    TextView tvsignin;
    EditText etName, etMobile, etEmail, etPassword, etAddress;
    Button btnRegister;
    Spinner spinner;
    private ProgressBar progressBar;
    Register register;
//Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
//Address field
    private static final int AUTOCOMPLETE_REQUEST_CODE = 22;
    private static final String TAG = Register.class.getSimpleName();

    String[] ITEMS = {"Customer", "Seller"};
    Boolean isValid, isSpinnerValid;
    private ActivityRegisterBinding activityRegisterBinding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        activityRegisterBinding = DataBindingUtil.setContentView(Register.this, R.layout.activity_register);
        activityRegisterBinding.setLifecycleOwner(this);
        activityRegisterBinding.setAuthViewModel(authViewModel);

        authViewModel.getUser(this).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                etName.setText("");
                etAddress.setText("");
                etMobile.setText("");
                etEmail.setText("");
                etPassword.setText("");
                spinner.setSelection(0);
                Log.e("onChanged()  "," "+user.getEmail());
            }
        });

        init();

//Address field
//        String apiKey = getString(R.string.api_key);
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), apiKey);
//        }
//        etAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // onSearchCalled();
//            }
//        });
//        etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                 //   onSearchCalled();
//                }
//            }
//        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  if (validations() && setSpinnerError(spinner, "Please select user type")) {
                    progressBar.setVisibility(View.VISIBLE);
                    authViewModel.register(progressBar,Register.this, etName.getText().toString(), etMobile.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), etAddress.getText().toString(), spinner.getSelectedItem().toString(), "default", "offline", etEmail.getText().toString(), etEmail.getText().toString().toLowerCase());
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
        if (Validator.isEmpty(etName.getText().toString())) {
            etName.setError("Name is required!");
            isValid = false;
        }
        else if (Validator.isEmpty(etMobile.getText().toString())) {
            etMobile.setError("Mobile Number is required!");
            isValid = false;
        }
        else if (Validator.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("E-Mail is required!");
            isValid = false;
        }
        else if(!Validator.isValidEmail(etEmail.getText().toString())){
            etEmail.setError("Please enter a valid email address");
            isValid = false;
        }
        else if (Validator.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Password is required!");
            isValid = false;
        }
        else if(!Validator.isValidPassword(etPassword.getText().toString())){
            etPassword.setError( "Password must between 8 and 20 characters; must contain at least one lowercase letter, one uppercase letter, one numeric digit, and one special character, but cannot contain whitespace" );
            isValid = false;
        }
        else if (Validator.isEmpty(etAddress.getText().toString())) {
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
        View selectedView = spinner.getSelectedView();
        if (selectedItemOfMySpinner == 0) {
            if (selectedView != null && selectedView instanceof TextView) {
                TextView selectedTextView = (TextView) selectedView;
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
//    public void onSearchCalled() {
//        // Set the fields to specify which types of place data to return.
//        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
//        // Start the autocomplete intent.
//        Intent intent = new Autocomplete.IntentBuilder(
//                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("CA") //CANADA
//                .build(this);
//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
//                etAddress.setText(place.getAddress().toString());
//                etAddress.setError(null);
//             //   Toast.makeText(Register.this, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
//                String address = place.getAddress();
//                // do query with address
//
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Toast.makeText(Register.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
//                Log.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }

    private void init() {
        //UI
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etAddress = findViewById(R.id.etAddress);
        tvsignin = findViewById(R.id.tvsignin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar_cyclic);
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
    }
}
