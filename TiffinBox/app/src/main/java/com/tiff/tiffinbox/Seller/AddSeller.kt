package com.tiff.tiffinbox.Seller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.Model.AddRecipe
import com.tiff.tiffinbox.Validate
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AddSeller.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AddSeller.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSeller : Fragment(), Validate {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    // Folder path for Firebase Storage.
    var Storage_Path = "Recipe/"

    // Root Database Name for Firebase Database.
    //String Database_Path = "All_Image_Uploads_Database";
    var FilePathUri: Uri? = null

    // Image request code for onActivityResult() .
    var Image_Request_Code = 7
    var isValid: Boolean? = null
    private var mListener: OnFragmentInteractionListener? = null
    var ImageUploadId: String? = null
    var imageUploadInfo: AddRecipe? = null
    var downlduri: Uri? = null
    var countChildren: Long = 0
    var intcountChildren = 0
    var builder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    //UI
    var btnAdd: Button? = null
    var cvSellerTitle: CardView? = null
    var cvSellerPrice: CardView? = null
    var cvSellerDesc: CardView? = null
    var etSellerTitle: EditText? = null
    var etSellerPrice: EditText? = null
    var etSellerDesc: EditText? = null
    var ivUploadimg: ImageView? = null
    var tvHeader: TextView? = null
    var progressDialog: ProgressDialog? = null

    //Firebase
    var storageReference: StorageReference? = null
    var databaseReference: DatabaseReference? = null
    var firebaseAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //UI
        cvSellerTitle = getView()!!.findViewById(R.id.cvSellerTitle)
        cvSellerPrice = getView()!!.findViewById(R.id.cvSellerPrice)
        cvSellerDesc = getView()!!.findViewById(R.id.cvSellerDesc)
        etSellerTitle = getView()!!.findViewById(R.id.etSellerTitle)
        etSellerPrice = getView()!!.findViewById(R.id.etSellerPrice)
        etSellerDesc = getView()!!.findViewById(R.id.etSellerDescription)
        ivUploadimg = getView()!!.findViewById(R.id.ivUploadimg)
        tvHeader = getView()!!.findViewById(R.id.tvHeader)
        ivUploadimg = getView()!!.findViewById(R.id.ivUploadimg)
        btnAdd = getView()!!.findViewById(R.id.btnAdd)
        builder = AlertDialog.Builder(activity!!, R.style.Theme_AppCompat_Dialog_Alert)
        alertDialog = AlertDialog.Builder(activity!!, R.style.Theme_AppCompat_Dialog_Alert).create()
        isValid = false
        progressDialog = ProgressDialog(context)
        //Firebase
        storageReference = FirebaseStorage.getInstance().reference
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        val animation1 = TranslateAnimation(400.0f, 0.0f, 0.0f, 0.0f) // new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        animation1.duration = 1000 // animation duration
        //        animation1.setRepeatCount(1); // animation repeat count if u want to repeat
        animation1.fillAfter = true
        // btn.startAnimation(animation1);//your_view for mine is imageView
        cvSellerTitle!!.startAnimation(animation1)
        cvSellerPrice!!.startAnimation(animation1)
        cvSellerDesc!!.startAnimation(animation1)
        //Count Recipe
        countRecipe()

//Upload image
        ivUploadimg!!.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            // Setting intent type as image to select image from phone storage.
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code)
        })
        //Add recipe
        btnAdd!!.setOnClickListener(View.OnClickListener {
            if (validations()) {
                if (countRecipe() < 3) {
                    //  Toast.makeText(getContext(), "Recipes", Toast.LENGTH_LONG).show();
                    UploadImageFileToFirebaseStorage()
                } else {
                    Toast.makeText(context, "You can't post more than 3 Ads", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data
            try {
                // Getting selected image into Bitmap.
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.applicationContext.contentResolver, FilePathUri)
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
        val contentResolver = activity!!.applicationContext.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    fun UploadImageFileToFirebaseStorage() {

        // Checking whether FilePathUri Is empty or not.
        if (FilePathUri != null) {
            // Setting progressDialog Title.
            progressDialog!!.setTitle("Tiffin Ad Posting...")
            // Showing progressDialog.
            progressDialog!!.show()
            // Creating second StorageReference.
            val storageReference2nd = storageReference!!.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri))

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri!!)
                    .addOnSuccessListener {
                        storageReference2nd.downloadUrl.addOnSuccessListener { uri ->
                            downlduri = uri
                            imageUploadInfo = AddRecipe(downlduri.toString(), etSellerPrice!!.text.toString(), etSellerDesc!!.text.toString())
                            databaseReference!!.child("Seller").child(firebaseAuth!!.currentUser!!.uid).child("Recipe").child(etSellerTitle!!.text.toString()).setValue(imageUploadInfo)
                            //                                    databaseReference.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child("Title").setValue(etSellerTitle.getText().toString());
                            progressDialog!!.dismiss()

                            // @SuppressWarnings("VisibleForTests")
                            addPostedAlert()
                            //Toast.makeText(getContext(), "Your Ad Published, Customers will Contact You Soon",Toast.LENGTH_LONG).show();
                        }
                    } // If something goes wrong .
                    .addOnFailureListener { exception -> // Hiding the progressDialog.
                        progressDialog!!.dismiss()
                        // Showing exception erro message.
                        Toast.makeText(context, exception.message, Toast.LENGTH_LONG).show()
                    } // On progress change upload time.
                    .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                        override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
                            // Setting progressDialog Title.
                            progressDialog!!.setTitle("Tiffin Ad Posting...")
                        }
                    })
        } else {
            Toast.makeText(context, "Please Select Image", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        //        btn = (Button) getView().findViewById(R.id.button3);
//        btn.setText("VVVVv");
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_seller, container, false)
        return inflater.inflate(R.layout.fragment_add_seller, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun validations(): Boolean {
        if (TextUtils.isEmpty(etSellerTitle!!.text)) {
            etSellerTitle!!.error = "Title is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etSellerPrice!!.text)) {
            etSellerPrice!!.error = "Price is required!"
            isValid = false
        } else if (TextUtils.isEmpty(etSellerDesc!!.text)) {
            etSellerDesc!!.error = "Description is required!"
            isValid = false
        } else {
            isValid = true
        }
        return isValid!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    fun countRecipe(): Int {
        val databaseCountRecipe = FirebaseDatabase.getInstance()
        val dfCountRecipe = databaseCountRecipe.reference
        val countRecipe: Query
        countRecipe = dfCountRecipe.child("Seller").child(firebaseAuth!!.currentUser!!.uid).child("Recipe")
        countRecipe.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                countChildren = dataSnapshot.childrenCount
                intcountChildren = countChildren.toInt()
                Log.i("Count Children ", "" + intcountChildren + " Values" + dataSnapshot.value)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return intcountChildren
    }

    private fun addPostedAlert() {
        builder!!.setTitle("Your Ad Published, Customers will Contact You Soon")
        builder!!.setCancelable(false)
                .setPositiveButton("Ok") { dialog, id -> dialog.cancel() }
        val alert = builder!!.create()
        alert.show()
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSeller.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): AddSeller {
            val fragment = AddSeller()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}