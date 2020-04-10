package com.example.tiffinbox.Customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tiffinbox.Customer.Model.CardModel;
import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.ViewRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class Customer extends AppCompatActivity {
    CardsAdapter adapter;
    ViewRecipe viewRecipe;
    List<CardModel> myList;

    //Firebase
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    Query queryInfo, queryImgUrl;
    String imageur;

    CardModel cardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        viewRecipe = new ViewRecipe();

        //Firebase
        queryInfo = df.child("Seller");
        queryImgUrl = df.child("Seller");

        // Todo--   for get all images in Recipe
//        queryImgUrl = df.child("Seller").orderByChild("imageURL");

        myList = new ArrayList<CardModel>();

        ListView lvCards = (ListView) findViewById(R.id.list_cards);

        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String email = ((TextView) view.findViewById(R.id.tvTest)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.tvName)).getText().toString();
                String address = ((TextView) view.findViewById(R.id.tvAddress)).getText().toString();
                String mobile = ((TextView) view.findViewById(R.id.tvPhone)).getText().toString();

                Intent intent = new Intent(Customer.this, SwipeRecipe.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("address", address);
                intent.putExtra("mobile", mobile);

                startActivity(intent);

            }
        });

        adapter = new CardsAdapter(getApplicationContext(),R.layout.customerhelper, myList);
        lvCards.setAdapter(adapter);

//        lvCards.setFocusable(false);
//        lvCards.setFocusableInTouchMode(false);
       // lvCards.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

//        adapter.add(new CardModel(false,"aaaa"));
//        adapter.add(new CardModel(false,"bbbb"));
//        adapter.add(new CardModel(false,"cccc"));
//        adapter.add(new CardModel(false,"dddd"));
//        adapter.add(new CardModel(false,"eeee"));
//        adapter.add(new CardModel(false,"ffff"));
//        adapter.add(new CardModel(false,"gggg"));
//        adapter.add(new CardModel(false,"hhhh"));
//        adapter.add(new CardModel(false,"iiii"));

//        lvCards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//               // view.setSelected(true);
//               // adapter.pos(i);
//             //   CardModel model = myList.get(i);
////CardModel model = new CardModel();
////                if (model.isSelected())
////                    model.setSelected(false);
////
////                else
//
////model.setSelected(true);
////                if (model.isSelected())
////                    model.setSelected(false);
////
////                else
////                    model.setSelected(true);
////
////                myList.set(i, model);
////
////                model.setSelected(true);
//
//                      // myList.set(i, model);
//
//                return true;
//            }
//        });

//lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//     //  adapter.toggleSelected(i, view);
//        CardModel model = myList.get(i);
//
//        if (model.isSelected())
//            model.setSelected(false);
//
//        else
//            model.setSelected(true);
//
//        //myList.set(i, model);
//    }
//});

//        lvCards.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
//            int j=9;
//
//            @Override
//            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
//                // TODO  Auto-generated method stub
//
//                final int checkedCount  = lvCards.getCheckedItemCount();
//
//                // Set the  CAB title according to total checked items
//
//                actionMode.setTitle(checkedCount  + "  Selected");
//                //view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                // Calls  toggleSelection method from ListViewAdapter Class
//
////CardModel model = new CardModel();
////model.setSelected(true);
////                CardModel model = myList.get(i);
////
////                if (model.isSelected()) {
////                    model.setSelected(false);
////                }
////                else {
////                    model.setSelected(true);
////                }
////
////                myList.set(i, model);
////                adapter.updateRecords(myList);
//
////                ToastListener.shortToast(getContext(),""+ model.isSelected()+i);
//                adapter.toggleSelection(i);
//                //working
//
//                adapter.setSelectedItem(i);
//                checked = lvCards.getCheckedItemPositions();
//                //  SparseBooleanArray check;
//                //for (int v = 0; v < len; v++) {
//
//                for (h = (checked.size() - 1); h >= 0; h--) {
//                    if (checked.valueAt(h)) {
//                        key = checked.keyAt(h);
//                        check = checked;
//                        boolean value = checked.get(key);
//                        //  if (value) {
//                        // String item =adapter. .get(v);
//                        /* do whatever you want with the checked item */
//                        //     Log.i("SparseBoolaeanArray", "Element " + checked.keyAt(h) + ":status::" + checked.get(checked.keyAt(h)));
//
//                    }
////                    lvCards.getChildAt(checked.keyAt(key)).setBackgroundColor(Color.GREEN);
////ToastListener.shortToast(getContext(), "key"+key);
//                }
////try {
////    if (checked.valueAt(h)) {
////        ToastListener.shortToast(getContext(), "ss" + check.keyAt(key) + check.get(check.keyAt(key)));
////    }
////}catch (ArrayIndexOutOfBoundsException e){
////    Log.i("arr", e.getMessage());
////}
//
//                //  }
//                SparseBooleanArray selected = adapter
//                        .getSelectedIds();
//
////
//
////                CardModel selecteditem;
////                for (int j = (selected.size() - 1); j >= 0; j--) {
////                    if (selected.valueAt(j)) {
////                         selecteditem = adapter
////                                .getItem(selected.keyAt(j));
////                        // Remove selected items following the ids
////
////                    }
////                }
//                //  int j = i;
////                if (adapter.get) {
////                    lvCards.getChildAt(j).setBackgroundColor(Color.GREEN);
////                    ToastListener.shortToast(getContext(), ""+j+" "+ i);
////                }
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
//                actionMode.getMenuInflater().inflate(R.menu.main, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
//
//
//                //  key=0;
//                return true;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
//                // TODO  Auto-generated method stub
//
//
//                switch (menuItem.getItemId()) {
//                    case R.id.selectAll:
//
//                        //
//
//                        final int checkedCount  = myList.size();
//
//                        // If item  is already selected or checked then remove or
//
//                        // unchecked  and again select all
//
//                        adapter.removeSelection();
//
//                        for (int i = 0; i <  checkedCount; i++) {
//
//                            lvCards.setItemChecked(i,   true);
//
////lvCards.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//                            //  listviewadapter.toggleSelection(i);
//
//                        }
//
//                        // Set the  CAB title according to total checked items
//
//
//
//                        // Calls  toggleSelection method from ListViewAdapter Class
//
//
//
//                        // Count no.  of selected item and print it
//
//                        actionMode.setTitle(checkedCount  + "  Selected");
//
//                        return true;
//                    case R.id.delete:
//                        // Calls getSelectedIds method from ListViewAdapter Class
//                        SparseBooleanArray selected = adapter
//                                .getSelectedIds();
//                        CardModel selecteditem;
//                        // Captures all selected ids with a loop
//                        for (int i = (selected.size() - 1); i >= 0; i--) {
//                            if (selected.valueAt(i)) {
//                                selecteditem = adapter
//                                        .getItem(selected.keyAt(i));
//                                // Remove selected items following the ids
//                                adapter.remove(selecteditem);
//
//
//                            }
//                        }
//                        // Close CAB
//                        actionMode.finish();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode actionMode) {
//                adapter.removeSelection();
//            }
//        });
//getd();
//        queryImgUrl.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
//
//                Log.i("TAG", "ok"+dataSnapshot.child("/imageURL").getValue());
//
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    for (DataSnapshot childd : child.getChildren()) {
//                        cardModel = childd.getValue(CardModel.class);
//                        imageur = cardModel.imageURL;
//
//                    //    adapter.add(new CardModel(cardModel.imageURL));
//                        Log.i("for","for"+cardModel.imageURL+cardModel.name+cardModel.address);
//                       // adapter.add(new CardModel(cardModel.getImageURL()));
//
//                        //This might work but it retrieves all the data
//                    }
//                }
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        gettingSellerList();


    }

    public void gettingSellerList(){
        queryInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
 //getting Recipe's images
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    for (DataSnapshot childd : child.getChildren()) {
                        cardModel = childd.getValue(CardModel.class);
                        imageur = cardModel.imageURL;
                        //    adapter.add(new CardModel(cardModel.imageURL));
                       // Log.i("for","for"+cardModel.imageURL+cardModel.name+cardModel.address);
                        // adapter.add(new CardModel(cardModel.getImageURL()));

                        //This might work but it retrieves all the data
                    }
                }

//getting name and address
                cardModel = dataSnapshot.getValue(CardModel.class);
                //viewRecipe = dataSnapshot.getValue(ViewRecipe.class);
                //  adapter.add(new CardModel(dataSnapshot.getKey(),viewRecipe.imageURL));
                adapter.add(new CardModel(cardModel.getName(),cardModel.getAddress(), imageur, cardModel.email, cardModel.mobile));
              //  Log.i("for222","for222"+cardModel.getImageURL());

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
}
