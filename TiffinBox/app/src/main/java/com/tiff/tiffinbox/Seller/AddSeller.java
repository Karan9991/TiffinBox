package com.tiff.tiffinbox.Seller;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.AddRecipe;
import com.tiff.tiffinbox.Validate;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSeller.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSeller#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSeller extends Fragment implements Validate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
// Folder path for Firebase Storage.
    String Storage_Path = "Recipe/";
// Root Database Name for Firebase Database.
//String Database_Path = "All_Image_Uploads_Database";
    Uri FilePathUri;
// Image request code for onActivityResult() .
    int Image_Request_Code = 7;
    Boolean isValid;
    private OnFragmentInteractionListener mListener;
    String ImageUploadId;
    AddRecipe imageUploadInfo;
     Uri downlduri;
     long countChildren;
    int intcountChildren;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    //UI
    Button btnAdd;
    CardView cvSellerTitle, cvSellerPrice, cvSellerDesc;
    EditText etSellerTitle, etSellerPrice, etSellerDesc;
    ImageView ivUploadimg;
    TextView tvHeader;
    ProgressDialog progressDialog ;
//Firebase
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public AddSeller() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSeller.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSeller newInstance(String param1, String param2) {
        AddSeller fragment = new AddSeller();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//UI
        cvSellerTitle = getView().findViewById(R.id.cvSellerTitle);
        cvSellerPrice = getView().findViewById(R.id.cvSellerPrice);
        cvSellerDesc = getView().findViewById(R.id.cvSellerDesc);
        etSellerTitle = getView().findViewById(R.id.etSellerTitle);
        etSellerPrice = getView().findViewById(R.id.etSellerPrice);
        etSellerDesc = getView().findViewById(R.id.etSellerDescription);
        ivUploadimg = getView().findViewById(R.id.ivUploadimg);
        tvHeader = getView().findViewById(R.id.tvHeader);
        ivUploadimg = getView().findViewById(R.id.ivUploadimg);
        btnAdd = getView().findViewById(R.id.btnAdd);

        builder = new AlertDialog.Builder(getActivity(),  R.style.Theme_AppCompat_Dialog_Alert);
         alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert).create();

        isValid = false;
        progressDialog = new ProgressDialog(getContext());
//Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

                TranslateAnimation animation1 = new TranslateAnimation(400.0f, 0.0f, 0.0f, 0.0f); // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation1.setDuration(1000); // animation duration
//        animation1.setRepeatCount(1); // animation repeat count if u want to repeat
        animation1.setFillAfter(true);
       // btn.startAnimation(animation1);//your_view for mine is imageView
        cvSellerTitle.startAnimation(animation1);
        cvSellerPrice.startAnimation(animation1);
        cvSellerDesc.startAnimation(animation1);
//Count Recipe
countRecipe();

//Upload image
        ivUploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
            }
        });
//Add recipe
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validations()) {
                    if (countRecipe() < 3) {
                      //  Toast.makeText(getContext(), "Recipes", Toast.LENGTH_LONG).show();
                        UploadImageFileToFirebaseStorage();
                    }else {
                        Toast.makeText(getContext(), "You can't post more than 3 Ads", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
            }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {
                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), FilePathUri);
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

        ContentResolver contentResolver = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    public void UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            // Setting progressDialog Title.
            progressDialog.setTitle("Tiffin Ad Posting...");
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
                                    imageUploadInfo = new AddRecipe(downlduri.toString(),etSellerPrice.getText().toString(), etSellerDesc.getText().toString());

                                    databaseReference.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(etSellerTitle.getText().toString()).setValue(imageUploadInfo);
//                                    databaseReference.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child("Title").setValue(etSellerTitle.getText().toString());

                                    progressDialog.dismiss();

                                     // @SuppressWarnings("VisibleForTests")
                                    addPostedAlert();
                                    //Toast.makeText(getContext(), "Your Ad Published, Customers will Contact You Soon",Toast.LENGTH_LONG).show();


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
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            // Setting progressDialog Title.
                            progressDialog.setTitle("Tiffin Ad Posting...");
                        }
                    });
        }
        else {

            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        btn = (Button) getView().findViewById(R.id.button3);
//        btn.setText("VVVVv");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_seller, container, false);

        return inflater.inflate(R.layout.fragment_add_seller, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public boolean validations() {
        if (TextUtils.isEmpty(etSellerTitle.getText())) {
            etSellerTitle.setError("Title is required!");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etSellerPrice.getText())) {
            etSellerPrice.setError("Price is required!");
            isValid = false;
        }
        else if (TextUtils.isEmpty(etSellerDesc.getText())) {
            etSellerDesc.setError("Description is required!");
            isValid = false;
        }
        else {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public int countRecipe(){
        FirebaseDatabase databaseCountRecipe = FirebaseDatabase.getInstance();
        DatabaseReference dfCountRecipe = databaseCountRecipe.getReference();
        Query countRecipe;
        countRecipe = dfCountRecipe.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe");
        countRecipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countChildren = dataSnapshot.getChildrenCount();
                 intcountChildren = (int) countChildren;

                Log.i("Count Children ", ""+intcountChildren+" Values"+dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return intcountChildren;
    }

        private void addPostedAlert(){
        builder.setTitle("Your Ad Published, Customers will Contact You Soon");
        builder.setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

