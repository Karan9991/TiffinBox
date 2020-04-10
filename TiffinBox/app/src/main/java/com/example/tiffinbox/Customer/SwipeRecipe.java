package com.example.tiffinbox.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.os.Bundle;

import com.example.tiffinbox.R;

import java.util.ArrayList;
import java.util.List;

public class SwipeRecipe extends AppCompatActivity {
    ViewPager viewPager;
    Adapter adapter;
    List<ModelDemo> modelDemos;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_recipe);
        modelDemos = new ArrayList<>();
        modelDemos.add(new ModelDemo(R.drawable.brochure, "Brochure", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
        modelDemos.add(new ModelDemo(R.drawable.sticker, "Sticker", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
        modelDemos.add(new ModelDemo(R.drawable.poster, "Poster", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
        modelDemos.add(new ModelDemo(R.drawable.namecard, "Namecard", "Business cards are cards bearing business information about a company or individual."));

        adapter = new Adapter(modelDemos, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
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
}