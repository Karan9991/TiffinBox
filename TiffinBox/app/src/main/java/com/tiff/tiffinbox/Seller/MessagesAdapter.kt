package com.tiff.tiffinbox.Seller

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.SparseBooleanArray
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.MessagesAdapter.MyViewHolder
import com.tiff.tiffinbox.Seller.Model.Message
import com.tiff.tiffinbox.Seller.helper.FlipAnimator.flipView
import java.util.*

//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.RecyclerView;
//import com.abhi.inbox.R;
//import com.abhi.inbox.helper.CircleTransform;
//import com.abhi.inbox.helper.FlipAnimator;
//import com.abhi.inbox.model.Message;
class MessagesAdapter(private val mContext: Context, private val messages: MutableList<Message>, private val listener: MessageAdapterListener) : RecyclerView.Adapter<MyViewHolder>() {
    private val selectedItems: SparseBooleanArray

    // array used to perform multiple animation at once
    private val animationItemsIndex: SparseBooleanArray
    private var reverseAllAnimations = false
    var a = arrayOfNulls<String>(10)
    var j = 0
    @JvmField
    var aList = ArrayList<String?>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), OnLongClickListener {
        var from: TextView? = null
        var subject: TextView? = null
        var message: TextView? = null
        var iconText: TextView? = null
        var timestamp: TextView? = null
        var title: TextView
        var iconImp: ImageView? = null
        var imgProfile: ImageView? = null
        var imgleft: ImageView
        var imgright: ImageView
        var imageView: ImageView
        var messageContainer: LinearLayout
        var iconContainer: RelativeLayout
        var iconBack: RelativeLayout
        var iconFront: RelativeLayout
        override fun onLongClick(view: View): Boolean {
            listener.onRowLongClicked(adapterPosition, view)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            return true
        }

        init {
            //            from = (TextView) view.findViewById(R.id.from);
//            subject = (TextView) view.findViewById(R.id.txt_primary);
//            message = (TextView) view.findViewById(R.id.txt_secondary);
//            iconText = (TextView) view.findViewById(R.id.icon_text);
            //timestamp = (TextView) view.findViewById(R.id.timestamp);
            title = view.findViewById<View>(R.id.tvTitle) as TextView
            imageView = view.findViewById<View>(R.id.imgView) as ImageView
            iconBack = view.findViewById<View>(R.id.icon_back) as RelativeLayout
            iconFront = view.findViewById<View>(R.id.icon_front) as RelativeLayout
            imgleft = view.findViewById<View>(R.id.imgView) as ImageView
            imgright = view.findViewById<View>(R.id.rightarrowViewimg) as ImageView
            messageContainer = view.findViewById<View>(R.id.message_container) as LinearLayout
            iconContainer = view.findViewById<View>(R.id.icon_container) as RelativeLayout
            view.setOnLongClickListener(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragmentaddview_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]

        // displaying text view data
//        holder.from.setText(message.getFrom());
//        holder.subject.setText(message.getSubject());
//        holder.message.setText(message.getMessage());
//        holder.timestamp.setText(message.getTimestamp());
        holder.title.text = message.imageTitle
        Glide.with(mContext).load(message.imageURL).into(holder.imageView)

        //  Log.i("sssssssss","v"+message.getImageTitle());
        // displaying the first letter of From in icon text
//        holder.iconText.setText(message.getFrom().substring(0, 1));

        //  change the row state to activated
        holder.itemView.isActivated = selectedItems[position, false]

        // change the font style depending on message read status
        //     applyReadStatus(holder, message);

        // handle message star
        //   applyImportant(holder, message);

        // handle icon animation
        applyIconAnimation(holder, position)

        // display profile image
        // applyProfilePicture(holder, message);

//         apply click events
        applyClickEvents(holder, position)
    }

    private fun applyClickEvents(holder: MyViewHolder, position: Int) {
        holder.iconContainer.setOnLongClickListener { view ->
            listener.onRowLongClicked(position, view)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            true
        }

//        holder.iconImp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listener.onIconImportantClicked(position);
//            }
//        });
        holder.iconContainer.setOnClickListener { view ->
            listener.onMessageRowClicked(position, view)
            if (ViewSeller2.actionMode == null) {
                val title = (view.findViewById<View>(R.id.tvTitle) as TextView).text.toString()
                val intent = Intent(mContext.applicationContext, Recipe::class.java)
                intent.putExtra("etTitle", title)
                mContext.startActivity(intent)
                Log.i("vvvv", "shortclick")
            }
        }
        holder.messageContainer.setOnLongClickListener { view ->
            listener.onRowLongClicked(position, view)
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            //  aList.add(position, ((TextView)view.findViewById(R.id.tvTitle)).getText().toString());
            true
        }
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
    private fun applyIconAnimation(holder: MyViewHolder, position: Int) {
        if (selectedItems[position, false]) {
            holder.iconFront.visibility = View.GONE
            resetIconYAxis(holder.iconBack)
            holder.iconBack.visibility = View.VISIBLE
            holder.iconBack.alpha = 1f
            Log.i("iftest1", "ttttt$position")
            if (currentSelectedIndex == position) {
                Log.i("iftest2", "ttttt$position")
                //Log.i("iftest2","ttttt"+position);
                //  if (a[0] == null) {

//                    a[j] = holder.title.getText().toString();
                try {
                    aList[position] = holder.title.text.toString()
                    //j++;
                } catch (e: ArrayIndexOutOfBoundsException) {
                }
                //}
                //    selectedViews.put("selected",holder.title.getText().toString());
                flipView(mContext, holder.iconBack, holder.iconFront, true)
                resetCurrentIndex()
            }
        } else {
            Log.i("iftest3", "ttttt$position")
            holder.iconBack.visibility = View.GONE
            resetIconYAxis(holder.iconFront)
            holder.iconFront.visibility = View.VISIBLE
            holder.iconFront.alpha = 1f
            //Log.i("test1","ttttt"+position);
            if (reverseAllAnimations && animationItemsIndex[position, false] || currentSelectedIndex == position) {
                //  Log.i("test2","ttttt"+position);
                //a[position] = null;
                //  remove(a, 1);
                try {
//                    aList.get(position);
                    aList[position] = null
                    // aList.remove(position);
                    //j--;
                } catch (e: ArrayIndexOutOfBoundsException) {
                }
                //  removeTheElement(a,position);
                //selectedViews.put("unselected",holder.title.getText().toString());
                Log.i("iftest4", "ttttt$position")
                flipView(mContext, holder.iconBack, holder.iconFront, false)
                resetCurrentIndex()
            }
        }
        //        for (int i=0; i<=animationItemsIndex.size();i++){
//        Log.i("anumated", "a "+animationItemsIndex.get(i, true));
//    }
        //   Log.i("anumated", "a "+ testD(holder,position));
    }

    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private fun resetIconYAxis(view: View) {
        if (view.rotationY != 0f) {
            view.rotationY = 0f
        }
    }

    fun resetAnimationIndex() {
        reverseAllAnimations = false
        animationItemsIndex.clear()
    }

    override fun getItemId(position: Int): Long {
        return messages[position].id.toLong()
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
    override fun getItemCount(): Int {
        return messages.size
    }

    fun toggleSelection(pos: Int) {
        currentSelectedIndex = pos
        if (selectedItems[pos, false]) {
            selectedItems.delete(pos)
            animationItemsIndex.delete(pos)
        } else {
            selectedItems.put(pos, true)
            animationItemsIndex.put(pos, true)
        }
        notifyItemChanged(pos)
    }

    fun selectAll() {
        val checkedCount = messages.size
        for (i in 0 until checkedCount) {
            selectedItems.put(i, true)
            if (selectedItems[i, true]) {
                animationItemsIndex.put(i, true)
                notifyItemChanged(i)
            }
        }
    }

    fun clearSelections() {
        reverseAllAnimations = true
        selectedItems.clear()
        notifyDataSetChanged()
    }

    val selectedItemCount: Int
        get() = selectedItems.size()

    fun getSelectedItems(): List<Int> {
        val items: MutableList<Int> = ArrayList(selectedItems.size())
        for (i in 0 until selectedItems.size()) {
            items.add(selectedItems.keyAt(i))
        }
        return items
    }

    fun removeData(position: Int) {
        messages.removeAt(position)
        resetCurrentIndex()
    }

    private fun resetCurrentIndex() {
        currentSelectedIndex = -1
    }

    interface MessageAdapterListener {
        fun onIconClicked(position: Int)
        fun onIconImportantClicked(position: Int)
        fun onMessageRowClicked(position: Int, view: View?)
        fun onRowLongClicked(position: Int, view: View?)
    }

    companion object {
        // index is used to animate only the selected row
        // dirty fix, find a better solution
        private var currentSelectedIndex = -1
    }

    init {
        selectedItems = SparseBooleanArray()
        animationItemsIndex = SparseBooleanArray()
    }
}