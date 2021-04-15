package com.tiff.tiffinbox.Customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.tiff.tiffinbox.Chat.MainActivity;
import com.tiff.tiffinbox.Customer.Model.CardModel;
import com.tiff.tiffinbox.Customer.adapter.CardsAdapter;
import com.tiff.tiffinbox.Customer.controller.CustController;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.ViewRecipe;
import com.tiff.tiffinbox.Seller.Profile.Profile;

import java.util.ArrayList;
import java.util.List;

public class Customer extends AppCompatActivity {
    CardsAdapter adapter;
    ViewRecipe viewRecipe;
    List<CardModel> myList;
    AlertDialog.Builder builder2;
    TextView tvhamburgerName, tvhamburgerEmail;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    DatabaseReference df2;
    FirebaseUser fuser;
    Query queryInfo, queryImgUrl;
    String imageur;
    CardModel cardModel;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    private CustController custController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        init();
      gettingSellerList();
    }

    public void gettingSellerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.i("oooooo","ok"+dataSnapshot.getKey().toString());
                    for (DataSnapshot childd : child.getChildren()) {
                        cardModel = childd.getValue(CardModel.class);
                        imageur = cardModel.imageURL;
                    }
                }
                cardModel = dataSnapshot.getValue(CardModel.class);
                adapter.add(new CardModel(cardModel.getName(),cardModel.getAddress(), imageur, cardModel.email, cardModel.mobile));
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
        searchView.setQueryHint("Address or Seller Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        if(t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void init(){
        getSupportActionBar().setTitle("Brampton Tiffins");
        builder2 = new AlertDialog.Builder(this);
        viewRecipe = new ViewRecipe();
        custController = new CustController();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        df2 = FirebaseDatabase.getInstance().getReference("Customer").child(fuser.getUid());
        dl = (DrawerLayout)findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl,R.string.descSeller, R.string.addressSeller);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView)findViewById(R.id.nv);
        nv.setItemIconTintList(null);
        View hView =  nv.getHeaderView(0);
        tvhamburgerName = (TextView)hView.findViewById(R.id.tvhamburgername);
        tvhamburgerEmail = (TextView)hView.findViewById(R.id.tvhamburgeremail);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch(id)
                {
                    case R.id.account:
                        startActivity(new Intent(Customer.this, Profile.class));
                        break;
                    case R.id.settings:
                        startActivity(new Intent(Customer.this, MainActivity.class));
                        break;
                    case R.id.mycart:
                        custController.logout(Customer.this);
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });

        queryInfo = df.child("Seller");
        queryImgUrl = df.child("Seller");
        myList = new ArrayList<CardModel>();

        ListView lvCards = (ListView) findViewById(R.id.list_cards);
        lvCards.setSaveEnabled(true);
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
    }
}
