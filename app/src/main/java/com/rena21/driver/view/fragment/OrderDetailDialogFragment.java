package com.rena21.driver.view.fragment;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.rena21.driver.activities.MainActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderDetailAdapter;

public class OrderDetailDialogFragment extends DialogFragment {

    private OrderAcceptedListener orderAcceptedListener;
    private FirebaseDbManager dbManager;

    //출처 : https://developer.android.com/guide/components/fragments.html?hl=ko#CommunicatingWithActivity
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        try {
            orderAcceptedListener = (OrderAcceptedListener) getActivity();
            dbManager = ((MainActivity) getActivity()).getDbManager();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OrderAcceptedListener");
        }
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_order_detail, container);

        final TextView tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
        RecyclerView rvOrderDetail = (RecyclerView) view.findViewById(R.id.rvOrderDetail);
        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);

        final Order order = getArguments().getParcelable("order");
        final String fileName = getArguments().getString("fileName");

        if (order.accepted) {
            btnAccept.setVisibility(View.GONE);
        } else {
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    orderAcceptedListener.onOrderAccepted(fileName);
                    dismiss();
                }
            });
        }

        final String restaurantPhoneNumber = fileName.split("_")[0];

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

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(order.orderItems);

        rvOrderDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderDetail.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvOrderDetail.setAdapter(orderDetailAdapter);
        return view;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
