package com.tiff.tiffinbox.Customer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.tiff.tiffinbox.Customer.Model.ModelDemo;
import com.tiff.tiffinbox.R;

import java.util.List;

//import android.support.annotation.NonNull;
//import android.support.v4.view.PagerAdapter;

public class Adapter extends PagerAdapter {

    private List<ModelDemo> modelDemos;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<ModelDemo> modelDemos, Context context) {
        this.modelDemos = modelDemos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return modelDemos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView;
        TextView title, desc, price;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        price = view.findViewById(R.id.price);


        // imageView.setImageResource(modelDemos.get(position).getImage());
        title.setText(modelDemos.get(position).getTitle());
        price.setText(" Price : "+modelDemos.get(position).getPrice());
        desc.setText(modelDemos.get(position).getDesc());

      Glide.with(context).load(modelDemos.get(position).getImageURL()).into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("changaaaaaaaaaaaa","ok"+modelDemos.get(position).getTitle());
                Intent intent = new Intent(context, Description.class);
                intent.putExtra("desc", modelDemos.get(position).getDesc());
                context.startActivity(intent);
                // finish();
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
