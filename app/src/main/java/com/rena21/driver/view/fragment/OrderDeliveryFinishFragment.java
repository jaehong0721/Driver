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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.activities.OrderDetailActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderDeliveryFinishedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderDetailWithPriceAdapter;


public class OrderDeliveryFinishFragment extends Fragment implements ValueEventListener{
    private static final String FILE_NAME = "fileName";

    private String fileName;

    private FirebaseDbManager dbManager;

    private OrderDeliveryFinishedListener listener;

    private TextView tvRestaurantName;
    private RecyclerView rvOrderDetail;
    private Button btnDeliveryFinish;

    public OrderDeliveryFinishFragment() {}

    public static OrderDeliveryFinishFragment newInstance(String param1) {
        OrderDeliveryFinishFragment fragment = new OrderDeliveryFinishFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrderDeliveryFinishedListener) {
            listener = (OrderDeliveryFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OrderDeliveryFinishedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(FILE_NAME);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_delivery_finish, container, false);

        btnDeliveryFinish = (Button) view.findViewById(R.id.btnDeliveryFinish);
        btnDeliveryFinish.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onOrderDeliveryFinished(fileName);
            }
        });

        tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
        rvOrderDetail = (RecyclerView) view.findViewById(R.id.rvOrderDetail);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = ((OrderDetailActivity)getActivity()).getDbManager();
        dbManager.addValueEventListenerToSpecificOrderRef(fileName, this);

        final String restaurantPhoneNumber = getPhoneNumber(fileName);
        dbManager.getRestaurantName(restaurantPhoneNumber, new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                String restaurantName = dataSnapshot.getValue(String.class);
                if (restaurantName == null) {
                    tvRestaurantName.setText(restaurantPhoneNumber);
                } else {
                    tvRestaurantName.setText(restaurantName);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        dbManager.removeValueEventListenerFromSpecificOrderRef(fileName, this);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        final Order order = dataSnapshot.getValue(Order.class);

        if(order == null) {
            return;
        }

        OrderDetailWithPriceAdapter orderDetailWithPriceAdapter = new OrderDetailWithPriceAdapter(order.orderItems);
        rvOrderDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderDetail.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvOrderDetail.setAdapter(orderDetailWithPriceAdapter);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.e("", "error: " + databaseError.toString());
    }

    private String getPhoneNumber(String fileName) {
        return fileName.split("_")[0];
    }
}
