package com.example.tiffinbox.Customer;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Customer.Model.CardModel;
import com.example.tiffinbox.ToastListener;

import java.util.List;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardsAdapter extends ArrayAdapter<CardModel> {

    Context myContext;
List<CardModel> cardModelList;
    LayoutInflater inflater;
CardModel cardModel = new CardModel();
    private static int currentSelectedIndex = -1;

    private  SparseBooleanArray mSelectedItemsIds;
    private int mSelectedP = -1;

    ViewHolder holder;

    public CardsAdapter(Context context, int resourceId,  List<CardModel> cardModelList) {
        super(context,resourceId, cardModelList);
        this.cardModelList = cardModelList;
        mSelectedItemsIds = new SparseBooleanArray();

        this.myContext = context;

        inflater =  LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.customerhelper, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CardModel model = getItem(position);
       // holder.imageView.setImageResource(model.getImageURL());
        Glide.with(getContext()).load(model.getImageURL()).into(holder.imageView);

        //  holder.tvTitle.setText(model.getTitle());
        holder.tvName.setText(model.getName());
        holder.tvAddress.setText(model.getAddress());
        holder.tvTest.setText(model.getEmail());
        holder.tvPhone.setText(model.getMobile());

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvName, tvAddress, tvTest, tvPhone;
        LinearLayout relativeLayout;
        TextView tvSubtitle;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imgRecipe);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvTest = (TextView) view.findViewById(R.id.tvTest);
            tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        }
    }

    @Override
    public void remove(@Nullable CardModel object) {
        cardModelList.remove(object);
        notifyDataSetChanged();
    }

    public List<CardModel> getMyList() {
        return cardModelList;
    }

public void pos(int position){
        holder.tvName.setBackgroundResource(R.color.colorAccent);
        holder.relativeLayout.setBackgroundResource(R.color.colorAccent);

}
    public void  toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }

    // Remove selection after unchecked

    public void  removeSelection() {

        mSelectedItemsIds = new  SparseBooleanArray();

        notifyDataSetChanged();

    }

    public void selectView(int position, boolean value) {

        if (value) {
            currentSelectedIndex = position;
            mSelectedItemsIds.put(position, value);
        }else {

            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();

    }

    public void setSelectedItem(int itemPosition) {
        mSelectedP = itemPosition;
        notifyDataSetChanged();
    }
    // Get number of selected item

    public int  getSelectedCount() {

        return mSelectedItemsIds.size();

    }
    public void updateRecords(List<CardModel> card){
        this.cardModelList = card;

        notifyDataSetChanged();
    }

    public  SparseBooleanArray getSelectedIds() {

        return mSelectedItemsIds;

    }
    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
}

