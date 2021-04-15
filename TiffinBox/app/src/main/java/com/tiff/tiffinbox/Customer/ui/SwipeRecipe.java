package com.tiff.tiffinbox.Customer.ui;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiff.tiffinbox.Customer.Model.ModelDemo;
import com.tiff.tiffinbox.Customer.adapter.Adapter;
import com.tiff.tiffinbox.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeRecipe extends AppCompatActivity {
    ViewPager viewPager;
    Adapter adapter;
    List<ModelDemo> modelDemos;
    Integer[] colors = null;
    String userid;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    TextView tvSellerName, tvSellerEmail, tvSellerPhone, tvSellerAddress;
    String tvEmail, tvName, tvAddress, tvMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recipe);

        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvSellerPhone = findViewById(R.id.tvSellerPhone);
        tvSellerAddress = findViewById(R.id.tvSellerAddress);

        gettingIntent();
        getRecipes();

        tvSellerName.setText("Name: "+tvName);
        tvSellerEmail.setText("E-Mail: "+tvEmail);
        tvSellerAddress.setText("Address: "+tvAddress);
        tvSellerPhone.setText("Phone: "+tvMobile);
    }

    public void all(){
        adapter = new Adapter(modelDemos, this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4),
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };
        colors = colors_temp;
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(
                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }
                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

public  void getRecipes(){
    modelDemos = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    Query querySellerDetail;
    querySellerDetail = df.child("Seller").orderByChild("email").equalTo(tvEmail);
    querySellerDetail.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                userid = child.getKey();
                for (DataSnapshot childd : child.getChildren()) {
                    for (DataSnapshot childdd : childd.getChildren()) {
                        ModelDemo modelDemo = new ModelDemo();
                        modelDemo = childdd.getValue(ModelDemo.class);
                        modelDemos.add(new ModelDemo(modelDemo.imageURL, childdd.getKey(), modelDemo.getDesc(), modelDemo.getPrice()));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    all();
}
    private void gettingIntent(){
        tvEmail = getIntent().getStringExtra("email");
        tvName = getIntent().getStringExtra("name");
        tvAddress = getIntent().getStringExtra("address");
        tvMobile = getIntent().getStringExtra("mobile");
    }

}
