package com.tiff.tiffinbox.Seller.addCustomers.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.AddCustomerAdapter
import com.tiff.tiffinbox.Seller.addCustomers.Model.AddCustomerModel
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {
    private var pageViewModel: PageViewModel? = null
    var adapter: AddCustomerAdapter? = null
    var myList: MutableList<AddCustomerModel?>? = null
    var builder2: AlertDialog.Builder? = null
    var builder3: AlertDialog.Builder? = null

    //Firebase
    var database = FirebaseDatabase.getInstance()
    var df = database.reference
    private var firebaseAuth: FirebaseAuth? = null
    var queryInfo: Query? = null
    var queryImgUrl: Query? = null
    var addCustomerModel: AddCustomerModel? = null
    var addCustomerDatabase: AddCustomerModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)
        var index = 1
        if (arguments != null) {
            index = arguments!!.getInt(ARG_SECTION_NUMBER)
        }
        pageViewModel!!.setIndex(index)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_add_customer, container, false)
        val textView = root.findViewById<TextView>(R.id.section_label)
        setHasOptionsMenu(true)
        builder2 = AlertDialog.Builder(context!!)
        builder3 = AlertDialog.Builder(context!!)
        firebaseAuth = FirebaseAuth.getInstance()

        //Firebase
        queryInfo = df.child("Customer")
        queryImgUrl = df.child("Customer")

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");
        myList = ArrayList()
        val lvCards = root.findViewById<View>(R.id.addCustlist_cards) as ListView
        adapter = AddCustomerAdapter(context!!, R.layout.addcustomerhelper, myList!!)
        lvCards.adapter = adapter
        gettingCustomerList()
        lvCards.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val custid = (view.findViewById<View>(R.id.addcusttvId) as TextView).text.toString()
            val email = (view.findViewById<View>(R.id.addcusttvEmail) as TextView).text.toString()
            val name = (view.findViewById<View>(R.id.addcusttvName) as TextView).text.toString()
            val mobile = (view.findViewById<View>(R.id.addcusttvPhone) as TextView).text.toString()
            val address = (view.findViewById<View>(R.id.addcusttvAddress) as TextView).text.toString()
            Log.i("checking....", " $custid")
            builder2!!.setTitle(name)
            builder2!!.setMessage("You want add $name?")
                    .setCancelable(false)
                    .setPositiveButton("Add") { dialog, id ->
                        Log.i("test", "v $custid")
                        addCustomerDatabase = AddCustomerModel(custid, name, email, mobile, address)
                        addCustomer(custid)
                    }
                    .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
            val alert = builder2!!.create()
            alert.show()
        }
        pageViewModel!!.text.observe(this, {
            //                textView.setText(s);
        })
        return root
    }

    fun gettingCustomerList() {
        queryInfo!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                //getting Recipe's images
                for (child in dataSnapshot.children) {
                    Log.i("oooooo", "ok" + dataSnapshot.key.toString())
                    for (childd in child.children) {
                        addCustomerModel = childd.getValue(AddCustomerModel::class.java)
                    }
                }
                //getting name and address
                addCustomerModel = dataSnapshot.getValue(AddCustomerModel::class.java)
                adapter!!.add(AddCustomerModel(addCustomerModel!!.id, addCustomerModel!!.name, addCustomerModel!!.email, addCustomerModel!!.mobile, addCustomerModel!!.address))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Snackbar.make(view, "Add Customers for give delivery Notification", Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        activity!!.menuInflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.searchView)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Email, Name or Phone"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //Log.e("Main"," data search"+newText);
                adapter!!.filter.filter(newText)
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.searchView) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun addCustomer(id: String) {
        df.child("Seller").child(firebaseAuth!!.currentUser!!.uid).child("MyCustomers").child(id).setValue(addCustomerDatabase)
        builder3!!.setTitle("Customer Added")
        builder3!!.setMessage("Tap on Your customer. When you will reach near the customer location. Customer will get the notification as ''Tiffin Delivered''")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, id -> dialog.cancel() }
        val alert = builder3!!.create()
        alert.show()
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(index: Int): PlaceholderFragment {
            val fragment = PlaceholderFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }
}