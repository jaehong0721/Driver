package com.rena21.driver.view.fragment;


import android.app.Dialog;
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
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.model.ParcelableOrder;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderDetailAdapter;

public class OrderDetailDialogFragment extends DialogFragment {

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ParcelableOrder parcelableOrder = getArguments().getParcelable("order");
        Log.d("OrderDetail", "식당 이름 : " + parcelableOrder.restaurantName + "품목 : " + parcelableOrder.orderItems);

        View view = inflater.inflate(R.layout.dialog_fragment_order_detail, container);
        TextView tvRestaurantName = (TextView) view.findViewById(R.id.tvRestaurantName);
        RecyclerView rvOrderDetail = (RecyclerView) view.findViewById(R.id.rvOrderDetail);

        tvRestaurantName.setText(parcelableOrder.restaurantName);

        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(parcelableOrder.orderItems);

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
        if (dialog!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
