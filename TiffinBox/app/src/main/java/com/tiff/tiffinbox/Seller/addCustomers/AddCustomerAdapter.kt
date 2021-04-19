package com.tiff.tiffinbox.Seller.addCustomers

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tiff.tiffinbox.Customer.Model.CardModel
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.Model.AddCustomerModel
import java.util.*

//import android.support.annotation.NonNull;
/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */
class AddCustomerAdapter(context: Context, resourceId: Int, var cardModelList: MutableList<AddCustomerModel?>) : ArrayAdapter<AddCustomerModel?>(context, resourceId, cardModelList), Filterable {
    var myContext: Context
    var cardModelListfiltered: List<AddCustomerModel?>
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
            convertView = inflater.inflate(R.layout.addcustomerhelper, parent, false)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val model = getItem(position)
        // holder.imageView.setImageResource(model.getImageURL());
        //    Glide.with(getContext()).load(model.getImageURL()).into(holder.imageView);
//        Picasso.with(getContext()).
//                load(model.getImageURL()).into(holder.imageView);
        //  holder.tvTitle.setText(model.getTitle());
        holder!!.tvId.text = cardModelListfiltered[position]!!.id
        holder!!.tvName.text = cardModelListfiltered[position]!!.name
        holder!!.tvEmail.setText(cardModelListfiltered[position]!!.email)
        holder!!.tvPhone.setText(cardModelListfiltered[position]!!.mobile)
        holder!!.tvAddress.setText(cardModelListfiltered[position]!!.address)
        return convertView!!
    }

    class ViewHolder(view: View?) {
        var tvId: TextView
        var tvName: TextView
        var tvEmail: TextView
        var tvPhone: TextView
        var tvAddress: TextView
        var relativeLayout: LinearLayout? = null

        init {
            tvId = view!!.findViewById<View>(R.id.addcusttvId) as TextView
            tvName = view.findViewById<View>(R.id.addcusttvName) as TextView
            tvEmail = view.findViewById<View>(R.id.addcusttvEmail) as TextView
            tvPhone = view.findViewById<View>(R.id.addcusttvPhone) as TextView
            tvAddress = view.findViewById<View>(R.id.addcusttvAddress) as TextView
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
                    val resultsModel: MutableList<AddCustomerModel?> = ArrayList()
                    val searchStr = constraint.toString().toLowerCase()
                    for (itemsModel in cardModelList) {
                        if (itemsModel!!.name!!.toLowerCase().contains(searchStr) || itemsModel.email!!.toLowerCase().contains(searchStr) || itemsModel.mobile!!.toLowerCase().contains(searchStr)) {
                            resultsModel.add(itemsModel)
                        }
                        filterResults.count = resultsModel.size
                        filterResults.values = resultsModel
                    }
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                cardModelListfiltered = results.values as List<AddCustomerModel?>
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return cardModelListfiltered.size
    }

    override fun getItem(position: Int): AddCustomerModel? {
        return cardModelListfiltered[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun remove(`object`: AddCustomerModel?) {
        cardModelList.remove(`object`)
        notifyDataSetChanged()
    }

    val myList: List<AddCustomerModel?>
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

    fun updateRecords(card: MutableList<AddCustomerModel?>) {
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