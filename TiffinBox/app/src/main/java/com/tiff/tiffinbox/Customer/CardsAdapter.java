package com.tiff.tiffinbox.Customer;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Customer.Model.CardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardsAdapter extends ArrayAdapter<CardModel> implements Filterable {

    Context myContext;
List<CardModel> cardModelList;
    List<CardModel> cardModelListfiltered;

    LayoutInflater inflater;
CardModel cardModel = new CardModel();
    private static int currentSelectedIndex = -1;

    private  SparseBooleanArray mSelectedItemsIds;
    private int mSelectedP = -1;

    ViewHolder holder;

    public CardsAdapter(Context context, int resourceId,  List<CardModel> cardModelList) {
        super(context,resourceId, cardModelList);
        this.cardModelList = cardModelList;
        this.cardModelListfiltered = cardModelList;
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
    //    Glide.with(getContext()).load(model.getImageURL()).into(holder.imageView);
        Picasso.with(getContext()).
                load(model.getImageURL()).into(holder.imageView);
        //  holder.tvTitle.setText(model.getTitle());
        holder.tvName.setText(cardModelListfiltered.get(position).getName());
        holder.tvAddress.setText(cardModelListfiltered.get(position).getAddress());
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



//Filter for search
    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = cardModelList.size();
                    filterResults.values = cardModelList;

                }else{
                    List<CardModel> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(CardModel itemsModel:cardModelList){
                        if(itemsModel.getAddress().toLowerCase().contains(searchStr)||itemsModel.getName().toLowerCase().contains(searchStr)){
                            resultsModel.add(itemsModel);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                cardModelListfiltered = (List<CardModel>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;    }
    @Override
    public int getCount() {
        return cardModelListfiltered.size();
    }

    @Nullable
    @Override
    public CardModel getItem(int position) {
        return cardModelListfiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

