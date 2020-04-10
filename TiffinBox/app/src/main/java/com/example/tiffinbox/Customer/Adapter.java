package com.example.tiffinbox.Customer;

import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.tiffinbox.R;

import java.util.List;

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
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);

        imageView.setImageResource(modelDemos.get(position).getImage());
        title.setText(modelDemos.get(position).getTitle());
        desc.setText(modelDemos.get(position).getDesc());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("param", modelDemos.get(position).getTitle());
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
