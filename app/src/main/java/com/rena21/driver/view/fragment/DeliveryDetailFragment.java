package com.rena21.driver.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.activities.DeliveryDetailActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.DeliveryDetailAdapter;


public class DeliveryDetailFragment extends Fragment implements ValueEventListener{

    private static final String FILE_NAME = "fileName";

    private String fileName;

    private FirebaseDbManager dbManager;

    private RecyclerView rvDeliveryDetail;

    public DeliveryDetailFragment() {}

    public static DeliveryDetailFragment newInstance(String param1) {
        DeliveryDetailFragment fragment = new DeliveryDetailFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_delivery_detail, container, false);

        rvDeliveryDetail = (RecyclerView) view.findViewById(R.id.rvDeliveryDetail);

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
        dbManager.removeValueEventListenerFromSpecificOrderRef(fileName, this);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Order order = dataSnapshot.getValue(Order.class);

        if(order == null) {
            return;
        }

        DeliveryDetailAdapter adapter = new DeliveryDetailAdapter(order.orderItems);
        rvDeliveryDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDeliveryDetail.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvDeliveryDetail.setAdapter(adapter);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.e("", "error: " + databaseError.toString());
    }
}
