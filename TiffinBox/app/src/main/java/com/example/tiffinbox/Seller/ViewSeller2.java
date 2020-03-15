package com.example.tiffinbox.Seller;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import android.widget.Toast;

import com.example.tiffinbox.R;
import com.example.tiffinbox.Seller.Model.CardModel;
import com.example.tiffinbox.Seller.Model.Message;
import com.example.tiffinbox.Seller.network.ApiClient;
import com.example.tiffinbox.Seller.network.ApiInterface;
import com.example.tiffinbox.ToastListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewSeller2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewSeller2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener,androidx.appcompat.view.ActionMode.Callback, MessagesAdapter.MessageAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Message> messages = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionModeCallback actionModeCallback;
    private androidx.appcompat.view.ActionMode actionMode;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new MessagesAdapter(getContext(), messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);


     //   actionModeCallback = new ActionModeCallback();
      //  messages.add(new Message("vvv"));
     //   messages.add(new Message("bb"));
//        messages.add(new Message("cc"));
//        messages.add(new Message("dd"));
//        messages.add(new Message("ee"));
//        messages.add(new Message("ff"));
//        messages.add(new Message("gg"));
//        messages.add(new Message("hh"));
//        messages.add(new Message("ii"));
//        messages.add(new Message("jj"));

     //   getInbox();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getInbox();
                    }
                }
        );
    }
    /**
     * Fetches mail messages by making HTTP request
     * url: https://api.androidhive.info/json/inbox.json
     */
    private void getInbox() {
        messages.add(new Message("aaaaaaaaa"));

        messages.add(new Message("bbbbbb"));
        Message message = new Message();

message.setColor(getRandomMaterialColor("400"));
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Message>> call = apiService.getInbox();
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                // clear the inbox
                messages.clear();

                // add all the messages
                // messages.addAll(response.body());

                // TODO - avoid looping
                // the loop was performed to add colors to each message
                for (Message message : response.body()) {
                    // generate a random color
                    message.setColor(getRandomMaterialColor("400"));
                    messages.add(message);
                }

                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
public void setC(Message message){
    message.setColor(Color.GREEN);

}
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

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        getInbox();
    }
    @Override
    public void onIconClicked(int position) {

        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(ViewSeller2.this);
        }
        Log.i("vvvv", "shortclick3");

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
    public void onMessageRowClicked(int position) {
        Log.i("vvv","shortclick2");
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
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
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
        swipeRefreshLayout.setEnabled(false);
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
                mode.finish();
                return true;
            case R.id.selectAll:
             mAdapter.selectAll();
                actionMode.setTitle(mAdapter.getSelectedItemCount()  + "  Selected");
               // mode.finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mAdapter.clearSelections();
        swipeRefreshLayout.setEnabled(true);
        actionMode = null;
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.resetAnimationIndex();
                // mAdapter.notifyDataSetChanged();
            }
        });
    }

    private class ActionModeCallback implements androidx.appcompat.view.ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(androidx.appcompat.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(androidx.appcompat.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(androidx.appcompat.view.ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }
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
