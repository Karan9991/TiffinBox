package com.tiff.tiffinbox.Seller;

import android.content.Context;
import android.content.Intent;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.abhi.inbox.R;
//import com.abhi.inbox.helper.CircleTransform;
//import com.abhi.inbox.helper.FlipAnimator;
//import com.abhi.inbox.model.Message;
import com.bumptech.glide.Glide;
import com.tiff.tiffinbox.R;
import com.tiff.tiffinbox.Seller.Model.Message;
import com.tiff.tiffinbox.Seller.helper.FlipAnimator;

import java.util.ArrayList;
import java.util.List;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {
    private Context mContext;
    private List<Message> messages;
    private MessageAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    String[] a = new String[10];
    int j=0;
ArrayList<String> aList = new ArrayList<String>();


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView from, subject, message, iconText, timestamp, title;
        public ImageView iconImp, imgProfile,imgleft,imgright,imageView;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;

        public MyViewHolder(View view) {
            super(view);
//            from = (TextView) view.findViewById(R.id.from);
//            subject = (TextView) view.findViewById(R.id.txt_primary);
//            message = (TextView) view.findViewById(R.id.txt_secondary);
//            iconText = (TextView) view.findViewById(R.id.icon_text);
            //timestamp = (TextView) view.findViewById(R.id.timestamp);
                        title = (TextView) view.findViewById(R.id.tvTitle);
            imageView = (ImageView) view.findViewById(R.id.imgView);
            iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            imgleft = (ImageView) view.findViewById(R.id.imgView);
            imgright = (ImageView) view.findViewById(R.id.rightarrowViewimg);

            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition(),view);
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public MessagesAdapter(Context mContext, List<Message> messages, MessageAdapterListener listener) {
        this.mContext = mContext;
        this.messages = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragmentaddview_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Message message = messages.get(position);

        // displaying text view data
//        holder.from.setText(message.getFrom());
//        holder.subject.setText(message.getSubject());
//        holder.message.setText(message.getMessage());
//        holder.timestamp.setText(message.getTimestamp());


        holder.title.setText(message.getImageTitle());
        Glide.with(this.mContext).load(message.getImageURL()).into(holder.imageView);

      //  Log.i("sssssssss","v"+message.getImageTitle());
        // displaying the first letter of From in icon text
//        holder.iconText.setText(message.getFrom().substring(0, 1));

       //  change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        // change the font style depending on message read status
   //     applyReadStatus(holder, message);

        // handle message star
     //   applyImportant(holder, message);

        // handle icon animation
        applyIconAnimation(holder, position);

        // display profile image
       // applyProfilePicture(holder, message);

//         apply click events
        applyClickEvents(holder, position);
    }

    private void applyClickEvents(MyViewHolder holder, final int position) {
        holder.iconContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position,view);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

//        holder.iconImp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onIconImportantClicked(position);
//            }
//        });

        holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position, view);
                if (ViewSeller2.actionMode == null) {
                    String title = ((TextView) view.findViewById(R.id.tvTitle)).getText().toString();
                    Intent intent = new Intent(mContext.getApplicationContext(), Recipe.class);
                    intent.putExtra("etTitle", title);
                    mContext.startActivity(intent);
                    Log.i("vvvv", "shortclick");
                }
            }
        });

        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position,view);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
              //  aList.add(position, ((TextView)view.findViewById(R.id.tvTitle)).getText().toString());
                return true;
            }
        });
    }
//    private void applyProfilePicture(MyViewHolder holder, Message message) {
//        if (!TextUtils.isEmpty(message.getPicture())) {
////            Glide.with(mContext).load(message.getPicture())
////                    .thumbnail(0.5f)
////                   /* .crossFade()*/
////                    .transform(new CircleTransform())
////                    .diskCacheStrategy(DiskCacheStrategy.ALL)
////                    .into(holder.imgProfile);
//            holder.imgProfile.setColorFilter(null);
////            holder.iconText.setVisibility(View.GONE);
//        } else {
////            holder.imgProfile.setImageResource(R.drawable.bg_circle);
////            holder.imgProfile.setColorFilter(message.getColor());
////            holder.iconText.setVisibility(View.VISIBLE);
//        }
//    }
    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            Log.i("iftest1","ttttt"+position);
            if (currentSelectedIndex == position) {
                Log.i("iftest2","ttttt"+position);
                //Log.i("iftest2","ttttt"+position);
                    //  if (a[0] == null) {

//                    a[j] = holder.title.getText().toString();
                   try {
                       aList.set(position, holder.title.getText().toString());
                       //j++;
                   } catch (ArrayIndexOutOfBoundsException e) {

                   }
                    //}
            //    selectedViews.put("selected",holder.title.getText().toString());
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            Log.i("iftest3","ttttt"+position);
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            //Log.i("test1","ttttt"+position);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
             //  Log.i("test2","ttttt"+position);
               //a[position] = null;
             //  remove(a, 1);
                try {
//                    aList.get(position);
                  aList.set(position, null);
                 // aList.remove(position);
                    //j--;
                }catch (ArrayIndexOutOfBoundsException e){

                }
             //  removeTheElement(a,position);
                //selectedViews.put("unselected",holder.title.getText().toString());
                Log.i("iftest4","ttttt"+position);
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
//        for (int i=0; i<=animationItemsIndex.size();i++){
//        Log.i("anumated", "a "+animationItemsIndex.get(i, true));
//    }
             //   Log.i("anumated", "a "+ testD(holder,position));
    }
    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

//    private void applyImportant(MyViewHolder holder, Message message) {
////        if (message.isImportant()) {
////           // holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
////            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
////        } else {
////           // holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
//////            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
////        }
//    }

//    private void applyReadStatus(MyViewHolder holder, Message message) {
//        if (message.isRead()) {
////            holder.from.setTypeface(null, Typeface.NORMAL);
////            holder.subject.setTypeface(null, Typeface.NORMAL);
////            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
////            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
//        } else {
////            holder.from.setTypeface(null, Typeface.BOLD);
////            holder.subject.setTypeface(null, Typeface.BOLD);
////            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
////            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
//        }
//    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }
public void selectAll(){
    final int checkedCount  = messages.size();

    for (int i = 0; i <  checkedCount; i++) {
        selectedItems.put(i, true);
        if (selectedItems.get(i, true)) {
            animationItemsIndex.put(i, true);
            notifyItemChanged(i);
        }
    }

}
    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        messages.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface MessageAdapterListener {
        void onIconClicked(int position);

        void onIconImportantClicked(int position);

        void onMessageRowClicked(int position, View view);

        void onRowLongClicked(int position, View view);
    }
}