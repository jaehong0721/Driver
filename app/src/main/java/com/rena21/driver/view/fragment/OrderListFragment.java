package com.rena21.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rena21.driver.R;
import com.rena21.driver.activities.MainActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderClickedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;


public class OrderListFragment extends Fragment implements ChildEventListener {

    private OrderClickedListener orderClickedListener;

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

    private FirebaseDbManager dbManager;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try {
            orderClickedListener = (OrderClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OrderClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        dbManager = ((MainActivity)getActivity()).getDbManager();

        dbManager.addChildEventListenerToOrdersRef(this);

        rvReceivedOrders = (RecyclerView) view.findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter(dbManager);

        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvReceivedOrders.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);

        receivedOrdersAdapter.addOnItemClickListener(new ReceivedOrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(String fileName) {
                orderClickedListener.onOrderClicked("detail", fileName);
            }
        });
        return view;
    }

    @Override public void onDetach() {
        super.onDetach();
        dbManager.removeChildEventListenerFromOrderRef(this);
    }

    @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String fileName = dataSnapshot.getKey();
        Order order = dataSnapshot.getValue(Order.class);

        int position = receivedOrdersAdapter.addedItem(fileName, order);
        rvReceivedOrders.smoothScrollToPosition(position);
    }

    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String fileName = dataSnapshot.getKey();
        Order order = dataSnapshot.getValue(Order.class);

        int position = receivedOrdersAdapter.changedItem(fileName, order);
        rvReceivedOrders.smoothScrollToPosition(position);
    }

    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
        String fileName = dataSnapshot.getKey();

        receivedOrdersAdapter.removedItem(fileName);
    }

    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

    @Override public void onCancelled(DatabaseError databaseError) {}
}
