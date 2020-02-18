package com.example.tiffinbox.Seller;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.CardModel;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardsAdapter extends ArrayAdapter<CardModel> {
    public CardsAdapter(Context context) {
        super(context, R.layout.fragmentaddview_list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.fragmentaddview_list, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CardModel model = getItem(position);

       // holder.imageView.setImageResource(model.getImageURL());
        Glide.with(getContext()).load(model.getImageURL()).into(holder.imageView);

        //  holder.tvTitle.setText(model.getTitle());
        holder.tvTitle.setText(model.getImageTitle());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvTitle;
        TextView tvSubtitle;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imgView);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        }
    }
}
