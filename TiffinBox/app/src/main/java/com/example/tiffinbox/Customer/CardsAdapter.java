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

  //  List<CardModel> dataList;
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
//try {
//    if (position == mSelectedP) {
//        holder.tvNa.setBackgroundColor(Color.GREEN);
//
//    }
//}catch (ArrayIndexOutOfBoundsException e){
//
//}

//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentSelectedIndex = position;
//                ToastListener.shortToast(getContext(), "RL");
//                notifyDataSetChanged();
//
//            }
//        });
   //     pos(position);

        //listview working
        //holder.tvTitle.setText(getItem(position).toString());

//if (convertView.isSelected()){
//    convertView.setBackgroundResource(R.color.colorPrimary);
//}

        // Capture position and set to the  ImageView

      //  holder.imageView.setImageResource(R.drawable.ic_launcher_background);
//        if (model.isSelected()){
//            holder.tvTitle.setBackgroundResource(R.color.colorAccent);
//            holder.relativeLayout.setBackgroundResource(R.color.colorAccent);
//            ToastListener.longToast(getContext(), "pos"+position+"current"+currentSelectedIndex);
//        }
//        else {
//           // holder.relativeLayout.setBackgroundResource(R.color.colorAccent);
//
//        }
     //   Log.i("View", "View"+model.getEmail());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView tvName, tvAddress, tvTest;
        LinearLayout relativeLayout;
        TextView tvSubtitle;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.imgRecipe);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            tvTest = (TextView) view.findViewById(R.id.tvTest);

            //  relativeLayout = (LinearLayout) view.findViewById(R.id.fragmntaddViewRL);
        }
    }

    @Override
    public void remove(@Nullable CardModel object) {
        cardModelList.remove(object);
        notifyDataSetChanged();
    }

    // get List after update or delete

    public List<CardModel> getMyList() {
        return cardModelList;
    }

public void pos(int position){
   // if (currentSelectedIndex == position){
        //convertView.setBackgroundColor(Color.BLUE);
        holder.tvName.setBackgroundResource(R.color.colorAccent);
        holder.relativeLayout.setBackgroundResource(R.color.colorAccent);
        ToastListener.longToast(getContext(), "pos"+position+"current"+currentSelectedIndex);
       // resetCurrentIndex();
   // }

}

    public void  toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
        //cardModel.setSelected(true);
//        if (cardModel.isSelected()){
//            holder.tvTitle.setBackgroundResource(R.color.colorAccent);
//            holder.relativeLayout.setBackgroundResource(R.color.colorAccent);
//            ToastListener.longToast(getContext(), "pos"+position+"current"+currentSelectedIndex+cardModel.isSelected());
//        //cardModel.setSelected(false);
//        }
        //ToastListener.shortToast(getContext(),"selection"+cardModel.isSelected());
    //    pos(position);

    }

    // Remove selection after unchecked

    public void  removeSelection() {

        mSelectedItemsIds = new  SparseBooleanArray();

        notifyDataSetChanged();

    }

    // Item checked on selection

    public void selectView(int position, boolean value) {

        if (value) {
            currentSelectedIndex = position;

            mSelectedItemsIds.put(position, value);
          //  ToastListener.longToast(getContext(), "OKOKOK");
           //pos(position);
         //  resetCurrentIndex();
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

