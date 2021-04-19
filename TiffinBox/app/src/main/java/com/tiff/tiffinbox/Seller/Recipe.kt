package com.tiff.tiffinbox.Seller

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.Model.EditRecipe
import com.tiff.tiffinbox.Validate
import java.io.IOException
import java.util.*

class Recipe : AppCompatActivity(), ValueEventListener, Validate {
    //UI
    var imgRecipe: ImageView? = null
    var imgView: ImageView? = null
    var imgLeftArrow: ImageView? = null
    var etTitle: EditText? = null
    var etPrice: EditText? = null
    var etDesc: EditText? = null
    var editRecipe: EditRecipe? = null
    var btnEdit: Button? = null
    var btnUpdate: Button? = null
    var title: String? = null
    var key: String? = null
    var Storage_Path = "Recipe/"
    var FilePathUri: Uri? = null
    var Image_Request_Code = 7
    var downlduri: Uri? = null
    var progressDialog: ProgressDialog? = null
    private var isValid = false

    //Firebase
    var database = FirebaseDatabase.getInstance()
    var firebaseAuth = FirebaseAuth.getInstance()
    var df: DatabaseReference? = null
    var dfUpdate: DatabaseReference? = null
    var dfImgUpdate: DatabaseReference? = null
    var storageReference: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

//UI
        imgRecipe = findViewById(R.id.imgRecipe)
        etTitle = findViewById(R.id.etTitle)
        etPrice = findViewById(R.id.etPrice)
        etDesc = findViewById(R.id.etDesc)
        btnEdit = findViewById(R.id.btnEdit)
        btnUpdate = findViewById(R.id.btnUpdate)
        imgView = findViewById(R.id.imgView)
        imgLeftArrow = findViewById(R.id.imgLeftArrow)
        editRecipe = EditRecipe()
        key = firebaseAuth.currentUser!!.uid
        progressDialog = ProgressDialog(this@Recipe)
        isValid = false
        dfUpdate = database.reference.child("Seller").child(firebaseAuth.currentUser!!.uid).child("Recipe")
        storageReference = FirebaseStorage.getInstance().reference
        etTitle!!.setEnabled(false)
        gettingIntent()
        imgLeftArrow!!.setOnClickListener(View.OnClickListener { startActivity(Intent(this@Recipe, AddView::class.java)) })
        imgView!!.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            // Setting intent type as image to select image from phone storage.
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code)
        })
        btnEdit!!.setOnClickListener(View.OnClickListener {
            btnUpdate!!.setVisibility(View.VISIBLE)
            imgView!!.setVisibility(View.VISIBLE)
        })
        btnUpdate!!.setOnClickListener(View.OnClickListener {
            if (validations()) {
                UploadImageFileToFirebaseStorage(etDesc!!.getText().toString(), etPrice!!.getText().toString())
            }
            // writeNewPost(etDesc.getText().toString(), editRecipe.getImageURL(), etPrice.getText().toString());
        })
    }

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        if (dataSnapshot != null) {
            editRecipe = dataSnapshot.getValue(EditRecipe::class.java)
            etTitle!!.setText(title)
            etPrice!!.setText(editRecipe!!.price)
            etDesc!!.setText(editRecipe!!.desc)
            Glide.with(this@Recipe).load(editRecipe!!.imageURL).into(imgRecipe!!)
        }
    }

    private fun writeNewPost(desc: String, imageURL: String?, price: String) {
        // Create new post at /user-posts/$userid/$postid and at /posts/$postid simultaneously
        editRecipe = EditRecipe(desc, imageURL, price)
        val postValues = editRecipe!!.toMap()
        val childUpdates: MutableMap<String?, Any> = HashMap()
        childUpdates[title] = postValues
        dfUpdate!!.updateChildren(childUpdates)
        Toast.makeText(this@Recipe, "Ad Updated", Toast.LENGTH_LONG).show()
    }

    private fun gettingIntent() {
        title = intent.getStringExtra("etTitle")
        if (title != null) {
            df = database.reference.child("Seller").child(firebaseAuth.currentUser!!.uid).child("Recipe").child(title!!)
            df!!.addValueEventListener(this)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data
            try {
                // Getting selected image into Bitmap.
                val bitmap = MediaStore.Images.Media.getBitmap(application.applicationContext.contentResolver, FilePathUri)
                // Setting up bitmap selected image into ImageView.
                //    ivUploadimg.setImageBitmap(bitmap);
                // After selecting image change choose button above text.
                //  ChooseButton.setText("Image Selected");
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = application.applicationContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    fun UploadImageFileToFirebaseStorage(desc: String?, price: String?) {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            // Setting progressDialog Title.
            progressDialog!!.setTitle("Image is Uploading...")
            // Showing progressDialog.
            progressDialog!!.show()
            // Creating second StorageReference.
            val storageReference2nd = storageReference!!.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri))

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri!!)
                    .addOnSuccessListener {
                        storageReference2nd.downloadUrl.addOnSuccessListener { uri ->
                            downlduri = uri
                            //     imageUploadInfo = new AddRecipe(downlduri.toString(),etSellerPrice.getText().toString(), etSellerDesc.getText().toString());
                            editRecipe = EditRecipe(desc, downlduri.toString(), price)
                            val postValues = editRecipe!!.toMap()
                            val childUpdates: MutableMap<String?, Any> = HashMap()
                            childUpdates[title] = postValues
                            dfUpdate!!.updateChildren(childUpdates)
                            //   dfImgUpdate.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(etSellerTitle.getText().toString()).setValue(imageUploadInfo);
                            progressDialog!!.dismiss()

                            //  @SuppressWarnings("VisibleForTests")
                            Toast.makeText(this@Recipe, "Ad Updated", Toast.LENGTH_LONG).show()
                        }
                    } // If something goes wrong .
                    .addOnFailureListener { exception -> // Hiding the progressDialog.
                        progressDialog!!.dismiss()
                        // Showing exception erro message.
                        Toast.makeText(this@Recipe, exception.message, Toast.LENGTH_LONG).show()
                    } // On progress change upload time.
                    .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                        override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                            // Setting progressDialog Title.
                            progressDialog!!.setTitle("Ad Updating...")
                        }
                    })
        } else {
            writeNewPost(etDesc!!.text.toString(), editRecipe!!.imageURL, etPrice!!.text.toString())
            //   Toast.makeText(Recipe.this, "Please Select Image", Toast.LENGTH_LONG).show();
        }
    }

    override fun onCancelled(databaseError: DatabaseError) {
        Toast.makeText(this@Recipe, databaseError.toException().toString(), Toast.LENGTH_LONG).show()
    }

    override fun validations(): Boolean {
        if (TextUtils.isEmpty(etTitle!!.text)) {
            etTitle!!.error = "Title is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etPrice!!.text)) {
            etPrice!!.error = "Price is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etDesc!!.text)) {
            etDesc!!.error = "Description is required!"
            isValid = false
        } else {
            isValid = true
        }
        return isValid
    }
}