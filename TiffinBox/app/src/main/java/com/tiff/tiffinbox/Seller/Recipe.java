package com.tiff.tiffinbox.Seller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.EditRecipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tiff.tiffinbox.Validate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Recipe extends AppCompatActivity implements ValueEventListener, Validate {

//UI
    ImageView imgRecipe,imgView,imgLeftArrow;
    EditText etTitle, etPrice, etDesc;
    EditRecipe editRecipe;
    Button btnEdit,btnUpdate;

    String title,key;
    String Storage_Path = "Recipe/";
    Uri FilePathUri;
    int Image_Request_Code = 7;
    Uri downlduri;
    ProgressDialog progressDialog ;
    private boolean isValid;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference df,dfUpdate,dfImgUpdate;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

//UI
        imgRecipe = findViewById(R.id.imgRecipe);
        etTitle = findViewById(R.id.etTitle);
        etPrice = findViewById(R.id.etPrice);
        etDesc = findViewById(R.id.etDesc);
        btnEdit = findViewById(R.id.btnEdit);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgView = findViewById(R.id.imgView);
        imgLeftArrow = findViewById(R.id.imgLeftArrow);

        editRecipe = new EditRecipe();
         key = firebaseAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(Recipe.this);
        isValid = false;

        dfUpdate = database.getReference().child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe");
        storageReference = FirebaseStorage.getInstance().getReference();

         etTitle.setEnabled(false);
         gettingIntent();

         imgLeftArrow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Recipe.this, AddView.class));
             }
         });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUpdate.setVisibility(View.VISIBLE);
                imgView.setVisibility(View.VISIBLE);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validations()) {
                    UploadImageFileToFirebaseStorage(etDesc.getText().toString(), etPrice.getText().toString());
                }
               // writeNewPost(etDesc.getText().toString(), editRecipe.getImageURL(), etPrice.getText().toString());
            }
        });

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if (dataSnapshot!=null){
               editRecipe = dataSnapshot.getValue(EditRecipe.class);
               etTitle.setText(title);
               etPrice.setText(editRecipe.getPrice());
               etDesc.setText(editRecipe.getDesc());
               Glide.with(Recipe.this).load(editRecipe.getImageURL()).into(imgRecipe);
           }
    }
    private void writeNewPost(String desc, String imageURL, String price) {
        // Create new post at /user-posts/$userid/$postid
        // and at /posts/$postid simultaneously
         editRecipe = new EditRecipe(desc, imageURL, price);
        Map<String, Object> postValues = editRecipe.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(title , postValues);
        dfUpdate.updateChildren(childUpdates);
        Toast.makeText(Recipe.this, "Recipe Updated", Toast.LENGTH_LONG).show();

    }
    private void gettingIntent(){
        title = getIntent().getStringExtra("etTitle");
        if (title!=null) {
            df = database.getReference().child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(title);
            df.addValueEventListener(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplication().getApplicationContext().getContentResolver(), FilePathUri);
                // Setting up bitmap selected image into ImageView.
                //    ivUploadimg.setImageBitmap(bitmap);
                // After selecting image change choose button above text.
                //  ChooseButton.setText("Image Selected");

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplication().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage(String desc, String price) {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            // Setting progressDialog Title.
            progressDialog.setTitle("Image is Uploading...");
            // Showing progressDialog.
            progressDialog.show();
            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downlduri = uri;
                               //     imageUploadInfo = new AddRecipe(downlduri.toString(),etSellerPrice.getText().toString(), etSellerDesc.getText().toString());
                                    editRecipe = new EditRecipe(desc, downlduri.toString(), price);
                                    Map<String, Object> postValues = editRecipe.toMap();

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put(title , postValues);
                                    dfUpdate.updateChildren(childUpdates);
                                 //   dfImgUpdate.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(etSellerTitle.getText().toString()).setValue(imageUploadInfo);
                                    progressDialog.dismiss();

                                    //  @SuppressWarnings("VisibleForTests")
                                    Toast.makeText(Recipe.this, "Recipe Updated", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Hiding the progressDialog.
                            progressDialog.dismiss();
                            // Showing exception erro message.
                            Toast.makeText(Recipe.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Setting progressDialog Title.
                            progressDialog.setTitle("Updating...");
                        }
                    });
        }
        else {
writeNewPost(etDesc.getText().toString(), editRecipe.getImageURL(), etPrice.getText().toString());
         //   Toast.makeText(Recipe.this, "Please Select Image", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(Recipe.this, databaseError.toException().toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean validations() {
        if (TextUtils.isEmpty(etTitle.getText())) {
            etTitle.setError("Title is required!");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etPrice.getText())) {
            etPrice.setError("Price is required!");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etDesc.getText())) {
            etDesc.setError("Description is required!");
            isValid = false;
        }
        else {
            isValid = true;
        }
        return isValid;
    }
//    private boolean validations(){
//        if (TextUtils.isEmpty(etTitle.getText())) {
//            etTitle.setError("Title is required!");
//            isValid = false;
//        }
//        else if (TextUtils.isEmpty(etPrice.getText())) {
//            etPrice.setError("Price is required!");
//            isValid = false;
//        }
//        else if (TextUtils.isEmpty(etDesc.getText())) {
//            etDesc.setError("Description is required!");
//            isValid = false;
//        }
//        else {
//            isValid = true;
//        }
//        return isValid;
//    }
}
