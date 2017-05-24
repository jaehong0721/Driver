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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.activities.DeliveryDetailActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.ModifyFinishedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.util.AmountCalculateUtil;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderDetailWithPriceAdapter;

import java.util.List;


public class DeliveryModifyFragment extends Fragment implements ValueEventListener{

    private static final String FILE_NAME = "fileName";

    private String fileName;

    private Order order;

    private FirebaseDbManager dbManager;

    private OrderDetailWithPriceAdapter orderDetailWithPriceAdapter;

    private ModifyFinishedListener finishedListener;

    private RecyclerView rvDeliveryDetail;
    private Button btnSave;

    public DeliveryModifyFragment() {}

    public static DeliveryModifyFragment newInstance(String param1) {
        DeliveryModifyFragment fragment = new DeliveryModifyFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ModifyFinishedListener) {
            finishedListener = (ModifyFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnModifyFinishedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(FILE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_delivery_modify, container, false);

        rvDeliveryDetail = (RecyclerView) view.findViewById(R.id.rvOrderDetail);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                List<OrderItem> orderItems = orderDetailWithPriceAdapter.getOrderItems();
                order.orderItems = orderItems;
                order.totalPrice = AmountCalculateUtil.sumOfEachOrderItem(orderItems);

                finishedListener.onModifyFinished(order, fileName);
            }
        });

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbManager = ((DeliveryDetailActivity)getActivity()).getDbManager();
        dbManager.addValueEventListenerToSpecificOrderRef(fileName, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        finishedListener = null;
        dbManager.removeValueEventListenerFromSpecificOrderRef(fileName, this);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        order = dataSnapshot.getValue(Order.class);

        if(order == null) {
            return;
        }

        orderDetailWithPriceAdapter = new OrderDetailWithPriceAdapter(order.orderItems);
        rvDeliveryDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDeliveryDetail.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvDeliveryDetail.setAdapter(orderDetailWithPriceAdapter);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.e("", "error: " + databaseError.toString());
    }
}
