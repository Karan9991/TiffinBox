package com.example.tiffinbox.Seller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.CardModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewSeller.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewSeller#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSeller extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
  //  Toolbar toolbar;

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
        ListView lvCards = (ListView) getView().findViewById(R.id.list_cards);
        CardsAdapter adapter = new CardsAdapter(getContext());

        lvCards.setAdapter(adapter);
        adapter.addAll(new CardModel(R.drawable.jupiter, R.string.mercury),
                new CardModel(R.drawable.jupiter, R.string.venus),
                new CardModel(R.drawable.earth, R.string.earth),
                new CardModel(R.drawable.jupiter, R.string.mars),
                new CardModel(R.drawable.jupiter, R.string.jupiter),
                new CardModel(R.drawable.earth, R.string.saturn),
                new CardModel(R.drawable.jupiter, R.string.uranus),
                new CardModel(R.drawable.earth, R.string.neptune),
                new CardModel(R.drawable.jupiter, R.string.pluto));
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
