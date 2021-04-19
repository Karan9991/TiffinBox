package com.tiff.tiffinbox.Customer.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.tiff.tiffinbox.Customer.Model.CardModel
import com.tiff.tiffinbox.R
import java.util.*

//import android.support.annotation.NonNull;
/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */
class CardsAdapter(context: Context, resourceId: Int, var cardModelList: MutableList<CardModel?>) : ArrayAdapter<CardModel?>(context, resourceId, cardModelList), Filterable {
    var myContext: Context
    var cardModelListfiltered: List<CardModel?>
    var inflater: LayoutInflater
    var cardModel = CardModel()
    var selectedIds: SparseBooleanArray
        private set
    private var mSelectedP = -1
    var holder: ViewHolder? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.customerhelper, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val model = getItem(position)
        // holder.imageView.setImageResource(model.getImageURL());
        //    Glide.with(getContext()).load(model.getImageURL()).into(holder.imageView);
        Picasso.with(context).load(model!!.imageURL).into(holder!!.imageView)
        //  holder.tvTitle.setText(model.getTitle());
        holder!!.tvName.text = cardModelListfiltered[position]!!.name
        holder!!.tvAddress.text = cardModelListfiltered[position]!!.address
        holder!!.tvTest.text = model.email
        holder!!.tvPhone.text = model.mobile
        return convertView!!
    }

    class ViewHolder(view: View?) {
        var imageView: ImageView
        var tvName: TextView
        var tvAddress: TextView
        var tvTest: TextView
        var tvPhone: TextView
        var relativeLayout: LinearLayout? = null
        var tvSubtitle: TextView? = null

        init {
            imageView = view!!.findViewById<View>(R.id.imgRecipe) as ImageView
            tvName = view.findViewById<View>(R.id.tvName) as TextView
            tvAddress = view.findViewById<View>(R.id.tvAddress) as TextView
            tvTest = view.findViewById<View>(R.id.tvTest) as TextView
            tvPhone = view.findViewById<View>(R.id.tvPhone) as TextView
        }
    }

    //Filter for search
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.length == 0) {
                    filterResults.count = cardModelList.size
                    filterResults.values = cardModelList
                } else {
                    val resultsModel: MutableList<CardModel?> = ArrayList()
                    val searchStr = constraint.toString().toLowerCase()
                    for (itemsModel in cardModelList) {
                        if (itemsModel!!.address!!.toLowerCase().contains(searchStr) || itemsModel.name!!.toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                cardModelListfiltered = results.values as List<CardModel?>
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return cardModelListfiltered.size
    }

    override fun getItem(position: Int): CardModel? {
        return cardModelListfiltered[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun remove(`object`: CardModel?) {
        cardModelList.remove(`object`)
        notifyDataSetChanged()
    }

    val myList: List<CardModel?>
        get() = cardModelList

    fun pos(position: Int) {
        holder!!.tvName.setBackgroundResource(R.color.colorAccent)
        holder!!.relativeLayout!!.setBackgroundResource(R.color.colorAccent)
    }

    fun toggleSelection(position: Int) {
        selectView(position, !selectedIds[position])
    }

    // Remove selection after unchecked
    fun removeSelection() {
        selectedIds = SparseBooleanArray()
        notifyDataSetChanged()
    }

    fun selectView(position: Int, value: Boolean) {
        if (value) {
            currentSelectedIndex = position
            selectedIds.put(position, value)
        } else {
            selectedIds.delete(position)
        }
        notifyDataSetChanged()
    }

    fun setSelectedItem(itemPosition: Int) {
        mSelectedP = itemPosition
        notifyDataSetChanged()
    }

    // Get number of selected item
    val selectedCount: Int
        get() = selectedIds.size()

    fun updateRecords(card: MutableList<CardModel?>) {
        cardModelList = card
        notifyDataSetChanged()
    }

    private fun resetCurrentIndex() {
        currentSelectedIndex = -1
    }

    companion object {
        private var currentSelectedIndex = -1
    }

    init {
        cardModelListfiltered = cardModelList
        selectedIds = SparseBooleanArray()
        myContext = context
        inflater = LayoutInflater.from(context)
    }
}