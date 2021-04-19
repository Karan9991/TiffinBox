package com.tiff.tiffinbox.Customer.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tiff.tiffinbox.Chat.MainActivity
import com.tiff.tiffinbox.Customer.Model.CardModel
import com.tiff.tiffinbox.Customer.adapter.CardsAdapter
import com.tiff.tiffinbox.Customer.controller.CustController
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.Model.ViewRecipe
import com.tiff.tiffinbox.Seller.Profile.Profile
import java.util.*

class Customer : AppCompatActivity() {
    var adapter: CardsAdapter? = null
    var viewRecipe: ViewRecipe? = null
    var myList: List<CardModel?>? = null
    var builder2: AlertDialog.Builder? = null
    var tvhamburgerName: TextView? = null
    var tvhamburgerEmail: TextView? = null
    var database = FirebaseDatabase.getInstance()
    var df = database.reference
    var df2: DatabaseReference? = null
    var fuser: FirebaseUser? = null
    var queryInfo: Query? = null
    var queryImgUrl: Query? = null
    var imageur: String? = null
    var cardModel: CardModel? = null
    private var dl: DrawerLayout? = null
    private var t: ActionBarDrawerToggle? = null
    private var nv: NavigationView? = null
    private var custController: CustController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer)
        init()
        gettingSellerList()
    }

    fun gettingSellerList() {
        queryInfo!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                for (child in dataSnapshot.children) {
                    Log.i("oooooo", "ok" + dataSnapshot.key.toString())
                    for (childd in child.children) {
                        cardModel = childd.getValue(CardModel::class.java)
                        imageur = cardModel!!.imageURL
                    }
                }
                cardModel = dataSnapshot.getValue(CardModel::class.java)
                adapter!!.add(CardModel(cardModel!!.name, cardModel!!.address, imageur, cardModel!!.email, cardModel!!.mobile))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val menuItem = menu.findItem(R.id.searchView)
        val searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Address or Seller Name"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.searchView) {
            return true
        }
        return if (t!!.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    private fun init() {
        supportActionBar!!.title = "Brampton Tiffins"
        builder2 = AlertDialog.Builder(this)
        viewRecipe = ViewRecipe()
        custController = CustController()
        fuser = FirebaseAuth.getInstance().currentUser
        df2 = FirebaseDatabase.getInstance().getReference("Customer").child(fuser!!.uid)
        dl = findViewById<View>(R.id.activity_main) as DrawerLayout
        t = ActionBarDrawerToggle(this, dl, R.string.descSeller, R.string.addressSeller)
        dl!!.addDrawerListener(t!!)
        t!!.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        nv = findViewById<View>(R.id.nv) as NavigationView
        nv!!.itemIconTintList = null
        val hView = nv!!.getHeaderView(0)
        tvhamburgerName = hView.findViewById<View>(R.id.tvhamburgername) as TextView
        tvhamburgerEmail = hView.findViewById<View>(R.id.tvhamburgeremail) as TextView
        nv!!.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.account -> startActivity(Intent(this@Customer, Profile::class.java))
                R.id.settings -> startActivity(Intent(this@Customer, MainActivity::class.java))
                R.id.mycart -> custController!!.logout(this@Customer)
                else -> return@OnNavigationItemSelectedListener true
            }
            true
        })
        queryInfo = df.child("Seller")
        queryImgUrl = df.child("Seller")
        myList = ArrayList()
        val lvCards = findViewById<View>(R.id.list_cards) as ListView
        lvCards.isSaveEnabled = true
        lvCards.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val email = (view.findViewById<View>(R.id.tvTest) as TextView).text.toString()
            val name = (view.findViewById<View>(R.id.tvName) as TextView).text.toString()
            val address = (view.findViewById<View>(R.id.tvAddress) as TextView).text.toString()
            val mobile = (view.findViewById<View>(R.id.tvPhone) as TextView).text.toString()
            val intent = Intent(this@Customer, SwipeRecipe::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("address", address)
            intent.putExtra("mobile", mobile)
            startActivity(intent)
        }
        adapter = CardsAdapter(applicationContext, R.layout.customerhelper, myList!! as MutableList<CardModel?>)
        lvCards.adapter = adapter
    }
}