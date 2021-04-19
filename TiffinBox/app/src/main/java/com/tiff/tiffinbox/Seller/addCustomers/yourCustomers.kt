package com.tiff.tiffinbox.Seller.addCustomers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.addCustomers.Model.YourCustomerModel
import com.tiff.tiffinbox.Seller.addCustomers.map.Map
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [yourCustomers.newInstance] factory method to
 * create an instance of this fragment.
 */
class yourCustomers : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var adapter: YourCustomerAdapter? = null
    var myList: List<YourCustomerModel?>? = null
    var builder2: AlertDialog.Builder? = null

    //Firebase
    var database = FirebaseDatabase.getInstance()
    var df = database.reference
    var firebaseAuth: FirebaseAuth? = null
    var queryInfo: Query? = null
    var queryImgUrl: Query? = null
    var yourCustomerModel: YourCustomerModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            mParam2 = arguments!!.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_your_customers, container, false)
        val textView = root.findViewById<TextView>(R.id.section_label)
        setHasOptionsMenu(true)
        builder2 = AlertDialog.Builder(context!!)
        firebaseAuth = FirebaseAuth.getInstance()


        //Firebase
        queryInfo = df.child("Seller").child(firebaseAuth!!.currentUser!!.uid).child("MyCustomers")
        queryImgUrl = df.child("Customer")

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");
        myList = ArrayList()
        val lvCards = root.findViewById<View>(R.id.yourCustlist_cards) as ListView
        adapter = YourCustomerAdapter(context!!, R.layout.yourcustomerhelper, myList as ArrayList<YourCustomerModel?>)
        lvCards.adapter = adapter
        gettingSellerList()
        lvCards.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val custid = (view.findViewById<View>(R.id.yourCustID) as TextView).text.toString()
            val name = (view.findViewById<View>(R.id.yourcusttvName) as TextView).text.toString()
            val address = (view.findViewById<View>(R.id.yourcusttvAddress) as TextView).text.toString()
            Log.i("cccccc", " $custid")
            Log.i("nnnnn", " $name")
            val intent = Intent(context, Map::class.java)
            intent.putExtra("name", name)
            intent.putExtra("address", address)
            intent.putExtra("custid", custid)
            startActivity(intent)
        }
        return root
    }

    fun gettingSellerList() {
        queryInfo!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                //getting Recipe's images
                for (child in dataSnapshot.children) {
                    Log.i("oooooo", "ok" + dataSnapshot.key.toString())
                    for (childd in child.children) {
                        yourCustomerModel = childd.getValue(YourCustomerModel::class.java)
                        //  imageur = addCustomerModel.imageURL;
                        //    adapter.add(new CardModel(cardModel.imageURL));
                        // Log.i("for","for"+cardModel.imageURL+cardModel.name+cardModel.address);
                        // adapter.add(new CardModel(cardModel.getImageURL()));

                        //This might work but it retrieves all the data
                    }
                }
                //getting name and address
                yourCustomerModel = dataSnapshot.getValue(YourCustomerModel::class.java)
                adapter!!.add(YourCustomerModel(yourCustomerModel!!.id, yourCustomerModel!!.name, yourCustomerModel!!.email, yourCustomerModel!!.mobile, yourCustomerModel!!.address))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
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

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment yourCustomers.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): yourCustomers {
            val fragment = yourCustomers()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}