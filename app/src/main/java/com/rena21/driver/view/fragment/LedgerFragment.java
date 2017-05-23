package com.rena21.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rena21.driver.R;
import com.rena21.driver.activities.MainActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.layout.AmountSummaryLayout;
import com.rena21.driver.view.layout.DateSelectLayout;

import org.joda.time.DateTime;


public class LedgerFragment extends Fragment implements DateSelectLayout.DateChangedListener {

    private DateSelectLayout dateSelectLayout;
    private AmountSummaryLayout amountSummaryLayout;
    private RecyclerView rvOrdersWithPrice;

    private FirebaseDbManager dbManager;
    private ChildEventListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ledger, container, false);
        dateSelectLayout = (DateSelectLayout) view.findViewById(R.id.dateSelectLayout);
        amountSummaryLayout = (AmountSummaryLayout) view.findViewById(R.id.amountSummaryLayout);
        rvOrdersWithPrice = (RecyclerView) view.findViewById(R.id.rvOrdersWithPrice);

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = ((MainActivity)getActivity()).getDbManager();
        listener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                Log.d("ledgerfragmnet", order.deliveredTime + "");
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        dateSelectLayout.setDateChangedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dbManager.removeChildEventListenerFromOrderRefOnSpecificDate(dateSelectLayout.getDisplayDate().getMillis(), listener);
        dateSelectLayout.removeDateChangedListener(this);
    }

    @Override public void onDateChanged(DateTime prevDisplayTime, DateTime displayTime) {
        if(prevDisplayTime != null) {
            dbManager.removeChildEventListenerFromOrderRefOnSpecificDate(prevDisplayTime.getMillis(), listener);
        }
        dbManager.addChildEventListenerToOrderRefOnSpecificDate(displayTime.getMillis(), listener);
    }
}
