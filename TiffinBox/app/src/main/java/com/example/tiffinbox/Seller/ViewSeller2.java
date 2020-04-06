package com.example.tiffinbox.Seller;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.Message;
import com.example.tiffinbox.Seller.Model.ViewRecipe;
import com.example.tiffinbox.ToastListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewSeller2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSeller2 extends Fragment implements androidx.appcompat.view.ActionMode.Callback, MessagesAdapter.MessageAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
   // private SwipeRefreshLayout swipeRefreshLayout;
    ViewRecipe viewRecipe = new ViewRecipe();
    //  private ActionModeCallback actionModeCallback;
    public static androidx.appcompat.view.ActionMode actionMode;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//Firebase
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();

    DatabaseReference df = database.getReference();
    DatabaseReference df2 = database2.getReference();

    Query queryImgUrl, queryCountChildren, deleteRecipe, deleteRecipe2;
    long countChildren;
    public ViewSeller2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewSeller2.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewSeller2 newInstance(String param1, String param2) {
        ViewSeller2 fragment = new ViewSeller2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_view_seller2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
//        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MessagesAdapter(getContext(), messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
//For invisible divider
        RecyclerView.ItemDecoration decoration = new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
                Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.linedivider);
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View child = parent.getChildAt(i);
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                    int top = child.getBottom() + params.bottomMargin;
                    int bottom = top + dividerDrawable.getIntrinsicHeight();
                    dividerDrawable.setBounds(left, top, right, bottom);
                    dividerDrawable.draw(c);
                }
            }
        };
        recyclerView.addItemDecoration(decoration);

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
getFirebase();

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
    }

   public void getFirebase(){

            queryCountChildren = df.child("Seller").child(firebaseAuth.getCurrentUser().getUid());
//        if ((df.child("Seller").child("l7iPjBUiqjUmIDOpz5iu6LMeJlq1"))!=null) {
//        }
       queryCountChildren.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               countChildren = dataSnapshot.getChildrenCount();
               int intcountChildren = (int) countChildren;
               for (int z=0; z<intcountChildren; z++){
                   mAdapter.aList.add(z, null);
               }

               Log.i("loop",""+mAdapter.aList+"v"+countChildren);
               Log.i("vv ", ""+dataSnapshot.getKey()+dataSnapshot.getChildrenCount());
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       queryImgUrl = df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe");
       queryImgUrl.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               viewRecipe = dataSnapshot.getValue(ViewRecipe.class);

               // adapter.add(new MEs(dataSnapshot.getKey(),viewRecipe.imageURL));
               messages.add(new Message(dataSnapshot.getKey(),viewRecipe.imageURL));
               //Log.i("Recipetest",""+viewRecipe.recipe);
               mAdapter.notifyDataSetChanged();
                //ToastListener.shortToast(getContext(), "ssss"+dataSnapshot.getKey());
           }
           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }
           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }
           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }
           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

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
    public void deleteRecipe2(String desc){
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Seller");
       ref
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("Recipe")
                .child(desc).removeValue();
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
       // inflater.inflate(R.menu.cancel_ride, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Toast.makeText(getContext(), "Search...", Toast.LENGTH_SHORT).show();
            return true;
        }
//        if (id == R.id.cancel_ride) {
//            startActivity(new Intent(MainActivity.this, Sent.class));
//            //return true;
//        }


        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onRefresh() {
//      //  getFirebase();
//        // swipe refresh is performed, fetch the messages again
//     //   getInbox();
//    }
    @Override
    public void onIconClicked(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(ViewSeller2.this);
        }
      //  Log.i("vvvv", "shortclick3");
        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Message message = messages.get(position);
        message.setImportant(!message.isImportant());
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onMessageRowClicked(int position, View view) {
     //   Log.i("vvv",((TextView)view.findViewById(R.id.tvTitle)).getText().toString());
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
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

    @Override
    public void onRowLongClicked(int position, View view) {
        // long press is performed, enable action mode
        enableActionMode(position);
      //  Log.i("textview",((TextView) view.findViewById(R.id.tvTitle)).getText().toString());

        //    ToastListener.shortToast(getContext(), "long");
    }
    //((AppCompatActivity) getActivity()).
    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity)getContext()).startSupportActionMode(ViewSeller2.this);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
        // disable swipe refresh if action mode is enabled
      //  swipeRefreshLayout.setEnabled(false);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // delete all the selected messages
                deleteMessages();
                for (int y=0; y<mAdapter.aList.size(); y++){
                    if (mAdapter.aList.get(y)!=null) {
                        deleteRecipe2(mAdapter.aList.get(y));
                    }
                    //Log.i("aaaaaaaaaaaaaa",  "List" +mAdapter.aList);
                }
                mode.finish();
                return true;
//            case R.id.selectAll:
//             mAdapter.selectAll();
//                actionMode.setTitle(mAdapter.getSelectedItemCount()  + "  Selected");
//
//                // mode.finish();
//                return true;
//            case R.id.test:
//              //  for (int j=0;j<=mAdapter.getSelectedItems().size();j++) {
//                    //  if (a[0] == null) {
//                    //}
////                for (int y=0; y<mAdapter.aList.size(); y++){
////                deleteRecipe2(mAdapter.aList.get(y),"lk");
////                    Log.i("aaaaaaaaaaaaaa",  "List" +mAdapter.aList);
////                }
//               // deleteRecipe2("b","jh");
//               // Log.i("aaaaaaaaaaaaaa",  "List" +mAdapter.aList);
//                return  true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mAdapter.clearSelections();
      //  swipeRefreshLayout.setEnabled(true);
        actionMode = null;
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.resetAnimationIndex();
                // mAdapter.notifyDataSetChanged();
            }
        });
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
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
