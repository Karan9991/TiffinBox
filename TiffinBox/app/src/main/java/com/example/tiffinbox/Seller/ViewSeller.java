package com.example.tiffinbox.Seller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.CardModel;
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
 * Activities that contain this fragment must implement the
 * {@link ViewSeller.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewSeller#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSeller extends Fragment implements ToastListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardsAdapter adapter;
    ViewRecipe viewRecipe;
    List<CardModel> myList;


    //  Toolbar toolbar;
//Firebase
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference df = database.getReference();
    Query queryImgUrl;
    int key,h;
    SparseBooleanArray checked,check;

    private OnFragmentInteractionListener mListener;

    public ViewSeller() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewSeller.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewSeller newInstance(String param1, String param2) {
        ViewSeller fragment = new ViewSeller();
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
        return inflater.inflate(R.layout.fragment_view_seller, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//Firebase
        queryImgUrl = df.child("Seller").child(firebaseAuth.getCurrentUser().getUid()).child("Recipe");

        myList = new  ArrayList<CardModel>();



        ListView lvCards = (ListView) getView().findViewById(R.id.list_cards);
//        lvCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(getContext(), ((TextView) view.findViewById(R.id.tvTitle)).getText().toString(),
////                        Toast.LENGTH_SHORT).show();
//                ((TextView) view.findViewById(R.id.tvTitle)).setBackgroundResource(R.color.colorAccent);
////                String title = ((TextView) view.findViewById(R.id.tvTitle)).getText().toString();
////                Intent intent = new Intent(getContext(), Recipe.class);
////                intent.putExtra("etTitle", title);
////                startActivity(intent);
//
//            }
//        });

        adapter = new CardsAdapter(getContext(),R.layout.fragmentaddview_list, myList);
        lvCards.setAdapter(adapter);

        lvCards.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        adapter.add(new CardModel(false,"aaaa"));
        adapter.add(new CardModel(false,"bbbb"));
        adapter.add(new CardModel(false,"cccc"));
        adapter.add(new CardModel(false,"dddd"));
        adapter.add(new CardModel(false,"eeee"));
        adapter.add(new CardModel(false,"ffff"));
        adapter.add(new CardModel(false,"gggg"));
        adapter.add(new CardModel(false,"hhhh"));
        adapter.add(new CardModel(false,"iiii"));

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

        lvCards.setMultiChoiceModeListener(new MultiChoiceModeListener() {
            int j=9;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                // TODO  Auto-generated method stub

                final int checkedCount  = lvCards.getCheckedItemCount();

                // Set the  CAB title according to total checked items

                actionMode.setTitle(checkedCount  + "  Selected");
                //view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                // Calls  toggleSelection method from ListViewAdapter Class

//CardModel model = new CardModel();
//model.setSelected(true);
//                CardModel model = myList.get(i);
//
//                if (model.isSelected()) {
//                    model.setSelected(false);
//                }
//                else {
//                    model.setSelected(true);
//                }
//
//                myList.set(i, model);
//                adapter.updateRecords(myList);

//                ToastListener.shortToast(getContext(),""+ model.isSelected()+i);
                adapter.toggleSelection(i);
                //working

                adapter.setSelectedItem(i);
                 checked = lvCards.getCheckedItemPositions();
              //  SparseBooleanArray check;
                 //for (int v = 0; v < len; v++) {

                for (h = (checked.size() - 1); h >= 0; h--) {
                    if (checked.valueAt(h)) {
                        key = checked.keyAt(h);
                        check = checked;
                        boolean value = checked.get(key);
                        //  if (value) {
                        // String item =adapter. .get(v);
                        /* do whatever you want with the checked item */
                   //     Log.i("SparseBoolaeanArray", "Element " + checked.keyAt(h) + ":status::" + checked.get(checked.keyAt(h)));

                    }
//                    lvCards.getChildAt(checked.keyAt(key)).setBackgroundColor(Color.GREEN);
//ToastListener.shortToast(getContext(), "key"+key);
                }
//try {
//    if (checked.valueAt(h)) {
//        ToastListener.shortToast(getContext(), "ss" + check.keyAt(key) + check.get(check.keyAt(key)));
//    }
//}catch (ArrayIndexOutOfBoundsException e){
//    Log.i("arr", e.getMessage());
//}

                //  }
                SparseBooleanArray selected = adapter
                        .getSelectedIds();

//

//                CardModel selecteditem;
//                for (int j = (selected.size() - 1); j >= 0; j--) {
//                    if (selected.valueAt(j)) {
//                         selecteditem = adapter
//                                .getItem(selected.keyAt(j));
//                        // Remove selected items following the ids
//
//                    }
//                }
              //  int j = i;
//                if (adapter.get) {
//                    lvCards.getChildAt(j).setBackgroundColor(Color.GREEN);
//                    ToastListener.shortToast(getContext(), ""+j+" "+ i);
//                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                actionMode.getMenuInflater().inflate(R.menu.main, menu);


                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {


              //  key=0;
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                // TODO  Auto-generated method stub


                switch (menuItem.getItemId()) {
                    case R.id.selectAll:

                        //

                        final int checkedCount  = myList.size();

                        // If item  is already selected or checked then remove or

                        // unchecked  and again select all

                        adapter.removeSelection();

                        for (int i = 0; i <  checkedCount; i++) {

                            lvCards.setItemChecked(i,   true);

//lvCards.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            //  listviewadapter.toggleSelection(i);

                        }

                        // Set the  CAB title according to total checked items



                        // Calls  toggleSelection method from ListViewAdapter Class



                        // Count no.  of selected item and print it

                        actionMode.setTitle(checkedCount  + "  Selected");

                        return true;
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        CardModel selecteditem;
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                 selecteditem = adapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapter.remove(selecteditem);


                            }
                        }
                        // Close CAB
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
adapter.removeSelection();
            }
        });

        queryImgUrl.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                 viewRecipe = dataSnapshot.getValue(ViewRecipe.class);
              //  adapter.add(new CardModel(dataSnapshot.getKey(),viewRecipe.imageURL));
//adapter.add(new CardModel(false,"SDafsdf"));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */




    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
