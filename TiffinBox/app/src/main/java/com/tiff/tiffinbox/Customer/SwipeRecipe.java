package com.tiff.tiffinbox.Customer;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tiff.tiffinbox.Customer.Model.ModelDemo;
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
  //  ImageView imgLeftArrowSwipeR;

    String tvEmail, tvName, tvAddress, tvMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recipe);

        tvSellerName = findViewById(R.id.tvSellerName);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);
        tvSellerPhone = findViewById(R.id.tvSellerPhone);
        tvSellerAddress = findViewById(R.id.tvSellerAddress);
       // imgLeftArrowSwipeR = findViewById(R.id.imgLeftArrowSwipeR);

//        FloatingActionButton fab = findViewById(R.id.fabChat);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action"+userid, Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
//                intent.putExtra("userid", userid);
//                startActivity(intent);
//                //startActivity(new Intent(SwipeRecipe.this, MainActivity.class));
//            }
//        });

        gettingIntent();
        getRecipes();

        tvSellerName.setText("Name: "+tvName);
        tvSellerEmail.setText("E-Mail: "+tvEmail);
        tvSellerAddress.setText("Address: "+tvAddress);
        tvSellerPhone.setText("Phone: "+tvMobile);

//        imgLeftArrowSwipeR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SwipeRecipe.this, Customer.class));
//            }
//        });
    }

    public void all(){
      //  modelDemos.add(new ModelDemo(R.drawable.brochure, "Brochure", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
//        modelDemos.add(new ModelDemo(R.drawable.sticker, "Sticker", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
//        modelDemos.add(new ModelDemo(R.drawable.poster, "Poster", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
//        modelDemos.add(new ModelDemo(R.drawable.namecard, "Namecard", "Business cards are cards bearing business information about a company or individual."));
        Log.i("testing", "testing");
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
                Log.i("kkkkkkkkkkkkkkk","key"+child.getKey());
                userid = child.getKey();
                for (DataSnapshot childd : child.getChildren()) {

                    for (DataSnapshot childdd : childd.getChildren()) {

                        ModelDemo modelDemo = new ModelDemo();
                        modelDemo = childdd.getValue(ModelDemo.class);
                        Log.i("Email", "email" + modelDemo.imageURL);
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
