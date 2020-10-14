package com.tiff.tiffinbox.Seller.addCustomers.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tiff.tiffinbox.Customer.Customer;
import com.tiff.tiffinbox.Customer.SwipeRecipe;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.ViewRecipe;
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomerAdapter;
import com.tiff.tiffinbox.Seller.addCustomers.Model.AddCustomerModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    AddCustomerAdapter adapter;
    List<AddCustomerModel> myList;
    AlertDialog.Builder builder2;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    private FirebaseAuth firebaseAuth;

    Query queryInfo, queryImgUrl;

    AddCustomerModel addCustomerModel, addCustomerDatabase;
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_customer, container, false);
        final TextView textView = root.findViewById(R.id.section_label);

        setHasOptionsMenu(true);

        builder2 = new AlertDialog.Builder(getContext());
        firebaseAuth = FirebaseAuth.getInstance();

        //Firebase
        queryInfo = df.child("Customer");
        queryImgUrl = df.child("Customer");

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");

        myList = new ArrayList<AddCustomerModel>();

        ListView lvCards = (ListView) root.findViewById(R.id.addCustlist_cards);
        adapter = new AddCustomerAdapter(getContext(),R.layout.addcustomerhelper, myList);
        lvCards.setAdapter(adapter);

        gettingCustomerList();

        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String custid = ((TextView) view.findViewById(R.id.addcusttvId)).getText().toString();
                String email = ((TextView) view.findViewById(R.id.addcusttvEmail)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.addcusttvName)).getText().toString();
                String mobile = ((TextView) view.findViewById(R.id.addcusttvPhone)).getText().toString();
                builder2.setTitle(name);
                builder2.setMessage("You want add "+name+"?")
                        .setCancelable(false)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("test","v "+custid);
                                addCustomerDatabase = new AddCustomerModel(custid, name, email, mobile);
                                addCustomer(custid);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder2.create();
                alert.show();

            }
        });

        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
    public void gettingCustomerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //getting Recipe's images
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("oooooo","ok"+dataSnapshot.getKey().toString());
                    for (DataSnapshot childd : child.getChildren()) {
                        addCustomerModel = childd.getValue(AddCustomerModel.class);

                    }
                }
//getting name and address
                addCustomerModel = dataSnapshot.getValue(AddCustomerModel.class);
                adapter.add(new AddCustomerModel(addCustomerModel.getId(), addCustomerModel.getName(), addCustomerModel.email, addCustomerModel.mobile));
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
    private void addCustomer(String id){
        df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("MyCustomers").child(id).setValue(addCustomerDatabase);
    }
}