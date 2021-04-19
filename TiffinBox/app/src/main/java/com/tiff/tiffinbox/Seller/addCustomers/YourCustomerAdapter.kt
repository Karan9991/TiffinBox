package com.tiff.tiffinbox.Seller.addCustomers

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tiff.tiffinbox.Customer.Model.CardModel
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.Model.YourCustomerModel
import java.util.*

class YourCustomerAdapter(context: Context, resourceId: Int, var cardModelList: MutableList<YourCustomerModel?>) : ArrayAdapter<YourCustomerModel?>(context, resourceId, cardModelList), Filterable {
    var myContext: Context
    var cardModelListfiltered: List<YourCustomerModel?>
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
            convertView = inflater.inflate(R.layout.yourcustomerhelper, parent, false)
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
        holder!!.tvID.text = cardModelListfiltered[position]!!.id
        holder!!.tvName.text = cardModelListfiltered[position]!!.name
        holder!!.tvEmail.text = cardModelListfiltered[position]!!.email
        holder!!.tvPhone.text = cardModelListfiltered[position]!!.mobile
        holder!!.tvAddress.text = cardModelListfiltered[position]!!.address
        return convertView!!
    }

    class ViewHolder(view: View?) {
        var tvID: TextView
        var tvName: TextView
        var tvEmail: TextView
        var tvPhone: TextView
        var tvAddress: TextView
        var relativeLayout: LinearLayout? = null

        init {
            tvID = view!!.findViewById<View>(R.id.yourCustID) as TextView
            tvName = view.findViewById<View>(R.id.yourcusttvName) as TextView
            tvEmail = view.findViewById<View>(R.id.yourcusttvEmail) as TextView
            tvPhone = view.findViewById<View>(R.id.yourcusttvPhone) as TextView
            tvAddress = view.findViewById<View>(R.id.yourcusttvAddress) as TextView
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
                    val resultsModel: MutableList<YourCustomerModel?> = ArrayList()
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
                cardModelListfiltered = results.values as List<YourCustomerModel?>
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return cardModelListfiltered.size
    }

    override fun getItem(position: Int): YourCustomerModel? {
        return cardModelListfiltered[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun remove(`object`: YourCustomerModel?) {
        cardModelList.remove(`object`)
        notifyDataSetChanged()
    }

    val myList: List<YourCustomerModel?>
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

    fun updateRecords(card: MutableList<YourCustomerModel?>) {
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