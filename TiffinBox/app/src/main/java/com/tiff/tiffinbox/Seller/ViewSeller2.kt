package com.tiff.tiffinbox.Seller

import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tiff.tiffinbox.R
import com.tiff.tiffinbox.Seller.MessagesAdapter.MessageAdapterListener
import com.tiff.tiffinbox.Seller.Model.Message
import com.tiff.tiffinbox.Seller.Model.ViewRecipe
import com.tiff.tiffinbox.Seller.ViewSeller2
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ViewSeller2.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewSeller2 : Fragment(), ActionMode.Callback, MessageAdapterListener {
    private val messages: MutableList<Message> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: MessagesAdapter? = null

    // private SwipeRefreshLayout swipeRefreshLayout;
    var viewRecipe: ViewRecipe? = ViewRecipe()

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    //Firebase
    var firebaseAuth = FirebaseAuth.getInstance()
    var database = FirebaseDatabase.getInstance()
    var database2 = FirebaseDatabase.getInstance()
    var df = database.reference
    var df2 = database2.reference
    var queryImgUrl: Query? = null
    var queryCountChildren: Query? = null
    var deleteRecipe: Query? = null
    var deleteRecipe2: Query? = null
    var countChildren: Long = 0
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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_view_seller2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = getView()!!.findViewById<View>(R.id.recycler_view) as RecyclerView
        //        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);
        mAdapter = MessagesAdapter(context!!, messages, this)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        recyclerView!!.adapter = mAdapter
        //For invisible divider
        val decoration: ItemDecoration = object : ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                val dividerDrawable = ContextCompat.getDrawable(context!!, R.drawable.linedivider)
                val left = parent.paddingLeft
                val right = parent.width - parent.paddingRight
                val childCount = parent.childCount
                for (i in 0 until childCount) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val top = child.bottom + params.bottomMargin
                    val bottom = top + dividerDrawable!!.intrinsicHeight
                    dividerDrawable.setBounds(left, top, right, bottom)
                    dividerDrawable.draw(c)
                }
            }
        }
        recyclerView!!.addItemDecoration(decoration)

//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//                return true;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                ToastListener.shortToast(getContext(),""+((TextView) rv.findViewById(R.id.tvTitle)).getText().toString());
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
//fetching firebase record
        firebase

        //    show loader and fetch messages
//        swipeRefreshLayout.post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                     getFirebase();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
//        );
    }// adapter.add(new MEs(dataSnapshot.getKey(),viewRecipe.imageURL));

    //Log.i("Recipetest",""+viewRecipe.recipe);
    //ToastListener.shortToast(getContext(), "ssss"+dataSnapshot.getKey());
// Log.i("loop",""+mAdapter.aList+"v"+countChildren);
    //Log.i("vv ", ""+dataSnapshot.getKey()+dataSnapshot.getChildrenCount());
    //        if ((df.child("Seller").child("l7iPjBUiqjUmIDOpz5iu6LMeJlq1"))!=null) {
//        }
    val firebase: Unit
        get() {
            queryCountChildren = df.child("Seller").child(firebaseAuth.currentUser!!.uid)
            //        if ((df.child("Seller").child("l7iPjBUiqjUmIDOpz5iu6LMeJlq1"))!=null) {
//        }
            queryCountChildren!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    countChildren = dataSnapshot.childrenCount
                    val intcountChildren = countChildren.toInt()
                    for (z in 0 until intcountChildren) {
                        mAdapter!!.aList.add(z, null)
                    }

                    // Log.i("loop",""+mAdapter.aList+"v"+countChildren);
                    //Log.i("vv ", ""+dataSnapshot.getKey()+dataSnapshot.getChildrenCount());
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
            queryImgUrl = df.child("Seller").child(firebaseAuth.currentUser!!.uid).child("Recipe")
            queryImgUrl!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    viewRecipe = dataSnapshot.getValue(ViewRecipe::class.java)

                    // adapter.add(new MEs(dataSnapshot.getKey(),viewRecipe.imageURL));
                    messages.add(Message(dataSnapshot.key, viewRecipe!!.imageURL))
                    //Log.i("Recipetest",""+viewRecipe.recipe);
                    mAdapter!!.notifyDataSetChanged()
                    //ToastListener.shortToast(getContext(), "ssss"+dataSnapshot.getKey());
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    //    public void deleteRecipe(String desc){
    //          deleteRecipe = df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").orderByKey();
    //          deleteRecipe.addListenerForSingleValueEvent(new ValueEventListener() {
    //              @Override
    //              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //                  Log.i("ONDATA1", "ONDATA1");
    //                  Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
    //                  // Object myKey = map.keySet().toArray()[];
    //                  for (int i = 0; i < map.size(); i++) {
    //                      //int i = 0;
    //                      Object myKey = map.keySet().toArray()[i];
    //                      Log.d("Hashmapdemo", "Key is: " + myKey);
    //                      df2.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").orderByChild("desc").equalTo("a").addListenerForSingleValueEvent(new ValueEventListener() {
    //                          @Override
    //                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //
    //                          }
    //
    //                          @Override
    //                          public void onCancelled(@NonNull DatabaseError databaseError) {
    //
    //                          }
    //                      });
    ////Log.i("NewHAshmap", "new "+map);
    //                     // deleteRecipe2(desc, myKey.toString());
    ////                      Log.i("Hashmap", "V " + desc + i + " " + myKey.toString());
    //                      //i++;
    //                      // }
    //                      // Log.i("message", dataSnapshot.getKey()+" v "+dataSnapshot.getValue());
    //                      //  deleteRecipe.getRef().removeValue();
    //                      // dataSnapshot.getRef().removeValue();
    //                  }
    //              }
    //              @Override
    //              public void onCancelled(@NonNull DatabaseError databaseError) {
    //
    //              }
    //          });
    //    }
    fun deleteRecipe2(desc: String?) {
        // Log.i("ONDATA2 "+desc,"ONDATA2 "+desc2);
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Seller");
//        Query dummyQuery = ref
//                .child(firebaseAuth.getCurrentUser().getUid())
//                .child("Recipe")
//                .orderByChild("desc")
//                .equalTo(desc);
//        dummyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                for (DataSnapshot dummySnapshot: dataSnapshot.getChildren()) {
////                    // dummySnapshot.getRef().removeValue();
////                }
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//              //  Log.i("NEW", "key "+dummyQuery.orderByChild(desc2).equalTo(desc));
////                dataSnapshot.child("Seller").child(firebaseAuth.getCurrentUser().getUid())
////                        .child("Recipe").child(dataSnapshot.getKey()).getRef().removeValue();
//           }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("tag", "onCancelled", databaseError.toException());
//            }
//        });
        val ref = FirebaseDatabase.getInstance().getReference("Seller")
        ref
                .child(firebaseAuth.currentUser!!.uid)
                .child("Recipe")
                .child(desc!!).removeValue()
        Toast.makeText(context, "Ad Deleted", Toast.LENGTH_LONG).show()
        //real
//        deleteRecipe2 = df2.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(desc2).orderByValue().equalTo(desc);
//       deleteRecipe2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Log.i("ONDATA2","ONDATA2 "+dataSnapshot.getKey());
//              //  df2.child("Seller").child("l7iPjBUiqjUmIDOpz5iu6LMeJlq1").child("Recipe").child(dataSnapshot.getKey()).setValue(null);
////                for (DataSnapshot appleSnapshot: dataSnapshot.getKey()) {
////                    appleSnapshot.getRef().removeValue();
////                }
//                // Object myKey = map.keySet().toArray()[];
//             //   Log.i("deleteRecipe2"+desc2+desc, dataSnapshot.getKey()+" v "+dataSnapshot.getValue());
//                //  dataSnapshot.child("Seller").child("l7iPjBUiqjUmIDOpz5iu6LMeJlq1").child("Recipe").child(desc2).getRef().removeValue();
//
//                df2.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe").child(desc2).orderByValue().equalTo(desc).getRef().removeValue();
//                //  df.removeValue();
//                // dataSnapshot.getRef().removeValue();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    //    private void getInbox() {
    //        messages.add(new Message("aaaaaaaaa"));
    //
    //        messages.add(new Message("bbbbbb"));
    //        Message message = new Message();
    //
    ////message.setColor(getRandomMaterialColor("400"));
    //        swipeRefreshLayout.setRefreshing(true);
    //
    //        ApiInterface apiService =
    //                ApiClient.getClient().create(ApiInterface.class);
    //
    //        Call<List<Message>> call = apiService.getInbox();
    //        call.enqueue(new Callback<List<Message>>() {
    //            @Override
    //            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
    //                // clear the inbox
    //                messages.clear();
    //
    //                // add all the messages
    //                // messages.addAll(response.body());
    //
    //                // TODO - avoid looping
    //                // the loop was performed to add colors to each message
    //                for (Message message : response.body()) {
    //                    // generate a random color
    //                 //   message.setColor(getRandomMaterialColor("400"));
    //                    messages.add(message);
    //                }
    //
    //                mAdapter.notifyDataSetChanged();
    //                swipeRefreshLayout.setRefreshing(false);
    //            }
    //
    //            @Override
    //            public void onFailure(Call<List<Message>> call, Throwable t) {
    //                Toast.makeText(getContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
    //                swipeRefreshLayout.setRefreshing(false);
    //            }
    //        });
    //    }
    //    private int getRandomMaterialColor(String typeColor) {
    //        int returnColor = Color.GRAY;
    //        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());
    //
    //        if (arrayId != 0) {
    //            TypedArray colors = getResources().obtainTypedArray(arrayId);
    //            int index = (int) (Math.random() * colors.length());
    //            returnColor = colors.getColor(index, Color.GRAY);
    //            colors.recycle();
    //        }
    //        return returnColor;
    //    }
    //public void setC(Message message){
    //    message.setColor(Color.GREEN);
    //
    //}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // inflater.inflate(R.menu.main, menu);
        // inflater.inflate(R.menu.cancel_ride, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

//        if (id == R.id.action_search) {
//            Toast.makeText(getContext(), "Search...", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        if (id == R.id.cancel_ride) {
//            startActivity(new Intent(MainActivity.this, Sent.class));
//            //return true;
//        }
        return super.onOptionsItemSelected(item)
    }

    //    @Override
    //    public void onRefresh() {
    //      //  getFirebase();
    //        // swipe refresh is performed, fetch the messages again
    //     //   getInbox();
    //    }
    override fun onIconClicked(position: Int) {
        if (actionMode == null) {
            actionMode = (activity as AppCompatActivity?)!!.startSupportActionMode(this@ViewSeller2)
        }
        //  Log.i("vvvv", "shortclick3");
        toggleSelection(position)
    }

    override fun onIconImportantClicked(position: Int) {
        // Star icon is clicked,
        // mark the message as important
        val message = messages[position]
        message.isImportant = !message.isImportant
        messages[position] = message
        mAdapter!!.notifyDataSetChanged()
    }

    override fun onMessageRowClicked(position: Int, view: View?) {
        //   Log.i("vvv",((TextView)view.findViewById(R.id.tvTitle)).getText().toString());
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter!!.selectedItemCount > 0) {
            enableActionMode(position)
        }
        //else {
//            // read the message which removes bold from the row
//            Message message = messages.get(position);
//            message.setRead(true);
//            messages.set(position, message);
//            mAdapter.notifyDataSetChanged();
//
//           // Toast.makeText(getContext(), "Read: " + message.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    override fun onRowLongClicked(position: Int, view: View?) {
        // long press is performed, enable action mode
        enableActionMode(position)
        //  Log.i("textview",((TextView) view.findViewById(R.id.tvTitle)).getText().toString());

        //    ToastListener.shortToast(getContext(), "long");
    }

    //((AppCompatActivity) getActivity()).
    private fun enableActionMode(position: Int) {
        if (actionMode == null) {
            actionMode = (context as AppCompatActivity?)!!.startSupportActionMode(this@ViewSeller2)
        }
        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        mAdapter!!.toggleSelection(position)
        val count = mAdapter!!.selectedItemCount
        if (count == 0) {
            actionMode!!.finish()
        } else {
            actionMode!!.title = count.toString()
            actionMode!!.invalidate()
        }
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        mode.menuInflater.inflate(R.menu.menu_action_mode, menu)
        // disable swipe refresh if action mode is enabled
        //  swipeRefreshLayout.setEnabled(false);
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                // delete all the selected messages
                deleteMessages()
                var y = 0
                while (y < mAdapter!!.aList.size) {
                    if (mAdapter!!.aList[y] != null) {
                        deleteRecipe2(mAdapter!!.aList[y])
                    }
                    y++
                }
                mode.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        mAdapter!!.clearSelections()
        //  swipeRefreshLayout.setEnabled(true);
        actionMode = null
        recyclerView!!.post {
            mAdapter!!.resetAnimationIndex()
            // mAdapter.notifyDataSetChanged();
        }
        //   mAdapter.aList.clear();
    }

    //    private class ActionModeCallback implements androidx.appcompat.view.ActionMode.Callback {
    //        @Override
    //        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
    //            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
    //
    //            // disable swipe refresh if action mode is enabled
    //            swipeRefreshLayout.setEnabled(false);
    //            return true;
    //        }
    //
    //        @Override
    //        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
    //            return false;
    //        }
    //
    //        @Override
    //        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
    //            switch (item.getItemId()) {
    //                case R.id.action_delete:
    //                    // delete all the selected messages
    //                    deleteMessages();
    //                    mode.finish();
    //                    return true;
    //                case R.id.selectAll:
    //                    mAdapter.selectAll();
    //                    actionMode.setTitle(mAdapter.getSelectedItemCount()  + "  Selected");
    //                    // mode.finish();
    //                    return true;
    //                default:
    //                    return false;
    //            }
    //        }
    //
    //        @Override
    //        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
    //            mAdapter.clearSelections();
    //            swipeRefreshLayout.setEnabled(true);
    //            actionMode = null;
    //            recyclerView.post(new Runnable() {
    //                @Override
    //                public void run() {
    //                    mAdapter.resetAnimationIndex();
    //                    // mAdapter.notifyDataSetChanged();
    //                }
    //            });
    //        }
    //    }
    private fun deleteMessages() {
        mAdapter!!.resetAnimationIndex()
        val selectedItemPositions = mAdapter!!.getSelectedItems()
        for (i in selectedItemPositions.indices.reversed()) {
            mAdapter!!.removeData(selectedItemPositions[i])
        }
        mAdapter!!.notifyDataSetChanged()
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        //  private ActionModeCallback actionModeCallback;
        var actionMode: ActionMode? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewSeller2.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): ViewSeller2 {
            val fragment = ViewSeller2()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}