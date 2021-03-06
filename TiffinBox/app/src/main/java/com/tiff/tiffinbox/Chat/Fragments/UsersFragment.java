package com.tiff.tiffinbox.Chat.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiff.tiffinbox.authentication.Model.User;
import com.tiff.tiffinbox.Chat.Adapter.UserAdapter;
import com.tiff.tiffinbox.R;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<com.tiff.tiffinbox.authentication.Model.User> mUsers;
    SharedPreferences sharedPref;

    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPref = getActivity().getSharedPreferences("UserType", Context.MODE_PRIVATE);

        mUsers = new ArrayList<>();

        if (sharedPref.getString("UT",null).equals("Customer")){
            readSellers();
        }else if (sharedPref.getString("UT",null).equals("Seller")){
            readCustomers();
        }
        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (sharedPref.getString("UT",null).equals("Customer")){
                    searchSellers(charSequence.toString().toLowerCase());
                }else if (sharedPref.getString("UT",null).equals("Seller")){
                    searchCustomers(charSequence.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchCustomers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseDatabase.getInstance().getReference("Customer").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    com.tiff.tiffinbox.authentication.Model.User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUsers.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void searchSellers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        Query query = FirebaseDatabase.getInstance().getReference("Seller").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    com.tiff.tiffinbox.authentication.Model.User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUsers.add(user);
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void readUsers() {
//        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Seller");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (search_users.getText().toString().equals("")) {
//                    mUsers.clear();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        com.tiff.tiffinbox.Authentication.Model.User user = snapshot.getValue(com.tiff.tiffinbox.Authentication.Model.User.class);
//                        //Log.i("TTTTTTTT", "vv"+user.getId());
////                        if (!user.getId().equals(firebaseUser.getUid())) {
////                            mUsers.add(user);
////                        }
//                            mUsers.add(user);
//
//                    }
//
//                    userAdapter = new UserAdapter(getContext(), mUsers, false);
//                    recyclerView.setAdapter(userAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
private void readSellers() {
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Seller");

    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (search_users.getText().toString().equals("")) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    com.tiff.tiffinbox.authentication.Model.User user = snapshot.getValue(com.tiff.tiffinbox.authentication.Model.User.class);
                    //Log.i("TTTTTTTT", "vv"+user.getId());
//                        if (!user.getId().equals(firebaseUser.getUid())) {
//                            mUsers.add(user);
//                        }
                    mUsers.add(user);

                }

                userAdapter = new UserAdapter(getContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
    private void readCustomers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Customer");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        com.tiff.tiffinbox.authentication.Model.User user = snapshot.getValue(com.tiff.tiffinbox.authentication.Model.User.class);
                        //Log.i("TTTTTTTT", "vv"+user.getId());
//                        if (!user.getId().equals(firebaseUser.getUid())) {
//                            mUsers.add(user);
//                        }
                        mUsers.add(user);

                    }

                    userAdapter = new UserAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
