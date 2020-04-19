package com.example.tiffinbox.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.tiffinbox.Customer.Model.CardModel;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.ViewRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Customer extends AppCompatActivity {
    CardsAdapter adapter;
    ViewRecipe viewRecipe;
    List<CardModel> myList;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    Query queryInfo, queryImgUrl;
    String imageur;

    CardModel cardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        getSupportActionBar().setTitle("Recipes");

        viewRecipe = new ViewRecipe();

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

    }

    public void gettingSellerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
 //getting Recipe's images
                for (DataSnapshot child : dataSnapshot.getChildren()) {
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

}
