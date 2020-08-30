package com.tiff.tiffinbox.Seller.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.tiff.tiffinbox.ConnectionReceiver;
import com.tiff.tiffinbox.Validate;
import com.tiff.tiffinbox.authentication.SignIn;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.AddView;
import com.tiff.tiffinbox.Seller.Model.SellerProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements Validate {
EditText etName,etMobile,etAddress;
Button btnEdit, btnUpdate, btnDeleteAccount;
ImageView imgProfileleftArrow, imgLogout;

private boolean isValid;
SellerProfile sellerProfile;
AlertDialog.Builder builder, builder2;

        BoundService boundService;
        boolean serviceBound = false;

    ConnectionReceiver receiver;
    IntentFilter intentFilter;
    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

//Firebase
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etEditProfileName);
        etMobile = findViewById(R.id.etEditProfileMobile);
        etAddress = findViewById(R.id.etEditProfileAddress);
        btnEdit = findViewById(R.id.btnEditProfile);
        btnUpdate = findViewById(R.id.btnUpdateProfile);
        btnDeleteAccount = findViewById(R.id.btnDeleteProfile);
        imgProfileleftArrow = findViewById(R.id.imgProfileLeftArrow);
        imgLogout = findViewById(R.id.imgProfileLogout);

        isValid = false;
        sellerProfile = new SellerProfile();
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);

        gettingFirebaseData();

        receiver = new ConnectionReceiver();
        intentFilter = new IntentFilter("com.tiff.tiffinbox.SOME_ACTION");
        intentFilter.addAction(CONNECTIVITY_ACTION);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpdate.setVisibility(View.VISIBLE);
                btnDeleteAccount.setVisibility(View.VISIBLE);
                etName.setEnabled(true);
                etMobile.setEnabled(true);
                etAddress.setEnabled(true);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   if (validations() && serviceBound){
                       boundService.updateProfile(etName.getText().toString(),etMobile.getText().toString(),etAddress.getText().toString());
                   }
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    alertDialogdelete();
            }
        });

        imgProfileleftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, AddView.class));
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }


    private void gettingFirebaseData(){
         df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 sellerProfile = dataSnapshot.getValue(SellerProfile.class);
                 etName.setText(sellerProfile.getName());
                 etMobile.setText(sellerProfile.getMobile());
                 etAddress.setText(sellerProfile.getAddress());
                 etName.setEnabled(false);
                 etMobile.setEnabled(false);
                 etAddress.setEnabled(false);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
    }

    private void deleteAccount(){
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Your Account Deleted Permanently", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Profile.this, SignIn.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    }
                });
    }

    private void alertDialogdelete(){
        builder.setTitle("Are You Sure?");
        builder.setMessage("Your All Data Will be Deleted Permanently")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                       }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout(){
        builder2.setTitle("Logout");
        builder2.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(Profile.this, SignIn.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder2.create();
        alert.show();
    }

    @Override
    public boolean validations() {
        if (TextUtils.isEmpty(etName.getText())) {
            etName.setError("Name is required!");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etMobile.getText())) {
            etMobile.setError("Mobile is required!");
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Serviceeeeeee","onstart");
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (serviceBound){

            unbindService(serviceConnection);
            serviceBound = false;
            Intent intent = new Intent(Profile.this, BoundService.class);
            stopService(intent);
            Log.i("Serviceeeeeee","onstop");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) iBinder;
            boundService = myBinder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
        }
    };

}
