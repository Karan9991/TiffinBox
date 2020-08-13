package com.tiff.tiffinbox.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.tiff.tiffinbox.Authentication.SignIn;
import com.tiff.tiffinbox.Chat.MainActivity;
import com.tiff.tiffinbox.Chat.MessageActivity;
import com.tiff.tiffinbox.Customer.Model.CardModel;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.ViewRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Customer extends AppCompatActivity {
    CardsAdapter adapter;
    ViewRecipe viewRecipe;
    List<CardModel> myList;
    AlertDialog.Builder builder2;
    ImageView imgCustomerLogout;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    DatabaseReference df2;
    DatabaseReference df3;
    FirebaseUser fuser;
    Query queryInfo, queryImgUrl;
    String imageur;

    CardModel cardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        getSupportActionBar().setTitle("Tiffin Recipes");
        builder2 = new AlertDialog.Builder(this);
        imgCustomerLogout = findViewById(R.id.imgCustomerLogout);

        viewRecipe = new ViewRecipe();

        FloatingActionButton fab = findViewById(R.id.fabChatCustomer);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        df2 = FirebaseDatabase.getInstance().getReference("Customer").child(fuser.getUid());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action"+userid, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
//                intent.putExtra("userid", userid);
//                startActivity(intent);
                startActivity(new Intent(Customer.this, MainActivity.class));
            }
        });

        //Firebase
        queryInfo = df.child("Seller");
        queryImgUrl = df.child("Seller");

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");

        myList = new ArrayList<CardModel>();

        ListView lvCards = (ListView) findViewById(R.id.list_cards);

        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String email = ((TextView) view.findViewById(R.id.tvTest)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.tvName)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.tvAddress)).getText().toString();
                String mobile = ((TextView) view.findViewById(R.id.tvPhone)).getText().toString();

                Intent intent = new Intent(Customer.this, SwipeRecipe.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("mobile", mobile);
                startActivity(intent);

            }
        });

        adapter = new CardsAdapter(getApplicationContext(),R.layout.customerhelper, myList);
        lvCards.setAdapter(adapter);

        gettingSellerList();

        imgCustomerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    public void gettingSellerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
 //getting Recipe's images
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("oooooo","ok"+dataSnapshot.getKey().toString());
                    for (DataSnapshot childd : child.getChildren()) {
                        cardModel = childd.getValue(CardModel.class);
                        imageur = cardModel.imageURL;
                        //    adapter.add(new CardModel(cardModel.imageURL));
                       // Log.i("for","for"+cardModel.imageURL+cardModel.name+cardModel.address);
                        // adapter.add(new CardModel(cardModel.getImageURL()));

                        //This might work but it retrieves all the data
                    }
                }
//getting name and address
                cardModel = dataSnapshot.getValue(CardModel.class);
                //viewRecipe = dataSnapshot.getValue(ViewRecipe.class);
                //  adapter.add(new CardModel(dataSnapshot.getKey(),viewRecipe.imageURL));
                adapter.add(new CardModel(cardModel.getName(),cardModel.getAddress(), imageur, cardModel.email, cardModel.mobile));
              //  Log.i("for222","for222"+cardModel.getImageURL());
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem menuItem = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Location or Seller Name");

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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.searchView){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        builder2.setTitle("Logout");
        builder2.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(Customer.this, SignIn.class);
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
}
