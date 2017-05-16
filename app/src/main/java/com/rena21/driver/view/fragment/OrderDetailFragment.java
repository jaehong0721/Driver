package com.rena21.driver.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.activities.OrderDetailActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderDetailAdapter;

import java.util.List;

public class OrderDetailFragment extends Fragment implements ValueEventListener{

    private FirebaseDbManager dbManager;
    private OrderAcceptedListener orderAcceptedListener;
    private String fileName;

    private TextView tvRestaurantName;
    private RecyclerView rvOrderDetail;
    private Button btnAccept;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try {
            orderAcceptedListener = (OrderAcceptedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OrderAcceptedListener");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        initView(view);

        initVariable();

        subscribeToSpecificOrderRef();

        return view;
    }

    @Override public void onDetach() {
        super.onDetach();
        cancelSubscriptionToSpecificOrderRef();
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {

        Log.d("fileName", "dataSnapshot: " + dataSnapshot);

        Order order = dataSnapshot.getValue(Order.class);

        if(order == null) {
            return;
        }

        setAcceptable(order.accepted);
        setRestaurantName();
        setOrderItems(order.orderItems);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.e("", "error: " + databaseError.toString());
    }

    private void subscribeToSpecificOrderRef() {
        dbManager.addValueEventListenerToSpecificOrderRef(fileName, this);
    }

    private void cancelSubscriptionToSpecificOrderRef() {
        dbManager.removeValueEventListenerFromSpecificOrderRef(fileName, this);
    }

    private void initView(View view) {
        btnAccept = (Button) view.findViewById(R.id.btnAccept);
        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
        rvOrderDetail = (RecyclerView) view.findViewById(R.id.rvOrderDetail);
    }

    private void initVariable() {
        dbManager = ((OrderDetailActivity) getActivity()).getDbManager();
        fileName = getArguments().getString("fileName");
    }

    private void setAcceptable(boolean accepted) {
        if (accepted) {
            btnAccept.setVisibility(View.GONE);
        } else {
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    orderAcceptedListener.onOrderAccepted(fileName);
                }
            });
        }
    }

    private void setRestaurantName() {
        final String restaurantPhoneNumber = getPhoneNumber(fileName);

        dbManager.getRestaurantName(restaurantPhoneNumber, new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                String restaurantName = dataSnapshot.getValue(String.class);
                if (restaurantName == null) {   // 식당 연락처가 없는 경우 전화번호를 보여줌
                    tvRestaurantName.setText(restaurantPhoneNumber);
                } else {
                    tvRestaurantName.setText(restaurantName);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void setOrderItems(List<OrderItem> orderItems) {
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(orderItems);
        rvOrderDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderDetail.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvOrderDetail.setAdapter(orderDetailAdapter);
    }

    private String getPhoneNumber(String fileName) {
        return fileName.split("_")[0];
    }
}
