package com.tiff.tiffinbox.Seller.addCustomers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.ViewRecipe;
import com.tiff.tiffinbox.Seller.addCustomers.Model.AddCustomerModel;
import com.tiff.tiffinbox.Seller.addCustomers.Model.YourCustomerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link yourCustomers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class yourCustomers extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    YourCustomerAdapter adapter;
    List<YourCustomerModel> myList;
    AlertDialog.Builder builder2;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    FirebaseAuth firebaseAuth;

    Query queryInfo, queryImgUrl;

    AddCustomerModel addCustomerModel;

    public yourCustomers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment yourCustomers.
     */
    // TODO: Rename and change types and number of parameters

    public static yourCustomers newInstance(String param1, String param2) {
        yourCustomers fragment = new yourCustomers();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_your_customers, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        setHasOptionsMenu(true);

        builder2 = new AlertDialog.Builder(getContext());
        firebaseAuth = FirebaseAuth.getInstance();


        //Firebase
        queryInfo = df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("MyCustomers");
        queryImgUrl = df.child("Customer");

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");

        myList = new ArrayList<YourCustomerModel>();

        ListView lvCards = (ListView) root.findViewById(R.id.yourCustlist_cards);
        adapter = new YourCustomerAdapter(getContext(),R.layout.yourcustomerhelper, myList);
        lvCards.setAdapter(adapter);

        gettingSellerList();

        return root;
    }

    public void gettingSellerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //getting Recipe's images
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("oooooo","ok"+dataSnapshot.getKey().toString());
                    for (DataSnapshot childd : child.getChildren()) {
                        addCustomerModel = childd.getValue(AddCustomerModel.class);
                        //  imageur = addCustomerModel.imageURL;
                        //    adapter.add(new CardModel(cardModel.imageURL));
                        // Log.i("for","for"+cardModel.imageURL+cardModel.name+cardModel.address);
                        // adapter.add(new CardModel(cardModel.getImageURL()));

                        //This might work but it retrieves all the data
                    }
                }
//getting name and address
                addCustomerModel = dataSnapshot.getValue(AddCustomerModel.class);
                adapter.add(new YourCustomerModel(addCustomerModel.getName(), addCustomerModel.email, addCustomerModel.mobile));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Email, Name or Phone");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("Main"," data search"+newText);
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.searchView){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}