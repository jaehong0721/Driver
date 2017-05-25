package com.rena21.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.rena21.driver.util.AmountCalculateUtil;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.adapter.OrderSummaryOnLedgerAdapter;
import com.rena21.driver.view.layout.AmountSummaryLayout;
import com.rena21.driver.view.layout.DateSelectLayout;

import org.joda.time.DateTime;

import java.util.HashMap;


public class LedgerFragment extends Fragment implements DateSelectLayout.DateChangedListener {

    private DateSelectLayout dateSelectLayout;
    private AmountSummaryLayout amountSummaryLayout;
    private RecyclerView rvOrdersWithPrice;

    private FirebaseDbManager dbManager;

    private OrderClickedListener orderClickedListener;
    private ChildEventListener eventListener;

    private OrderSummaryOnLedgerAdapter orderSummaryOnLedgerAdapter;

    HashMap<String, Integer> totalPriceMap;
    HashMap<String, Integer> totalDepositMap;

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
        View view = inflater.inflate(R.layout.fragment_ledger, container, false);
        dateSelectLayout = (DateSelectLayout) view.findViewById(R.id.dateSelectLayout);
        amountSummaryLayout = (AmountSummaryLayout) view.findViewById(R.id.amountSummaryLayout);
        rvOrdersWithPrice = (RecyclerView) view.findViewById(R.id.rvOrdersWithPrice);

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = ((MainActivity)getActivity()).getDbManager();

        orderSummaryOnLedgerAdapter = new OrderSummaryOnLedgerAdapter(dbManager);

        rvOrdersWithPrice.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrdersWithPrice.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.shape_divider_for_received_orders));
        rvOrdersWithPrice.setAdapter(orderSummaryOnLedgerAdapter);

        orderSummaryOnLedgerAdapter.addOnItemClickListener(new OrderSummaryOnLedgerAdapter.OnItemClickListener() {
            @Override public void onItemClick(String fileName) {
                orderClickedListener.onOrderClicked("ledger", fileName);
            }
        });

        totalPriceMap = new HashMap<>();
        totalDepositMap = new HashMap<>();

        eventListener = new ChildEventListener() {

            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String fileName = dataSnapshot.getKey();
                Order order = dataSnapshot.getValue(Order.class);

                orderSummaryOnLedgerAdapter.addedItem(fileName, order);

                totalPriceMap.put(fileName,order.totalPrice);
                totalDepositMap.put(fileName,order.totalDeposit);
                setAmountSummary(totalPriceMap, totalDepositMap);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String fileName = dataSnapshot.getKey();
                Order order = dataSnapshot.getValue(Order.class);

                orderSummaryOnLedgerAdapter.changedItem(fileName, order);

                totalPriceMap.remove(fileName);
                totalPriceMap.put(fileName, order.totalPrice);

                totalDepositMap.remove(fileName);
                totalDepositMap.put(fileName, order.totalDeposit);
                setAmountSummary(totalPriceMap, totalDepositMap);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        dateSelectLayout.setDateChangedListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dbManager.removeChildEventListenerFromOrderRefOnSpecificDate(dateSelectLayout.getDisplayDate().getMillis(), eventListener);
        dateSelectLayout.removeDateChangedListener(this);
        orderClickedListener = null;
    }

    @Override public void onDateChanged(DateTime prevDisplayTime, DateTime displayTime) {
        if(prevDisplayTime != null) {
            dbManager.removeChildEventListenerFromOrderRefOnSpecificDate(prevDisplayTime.getMillis(), eventListener);
            orderSummaryOnLedgerAdapter.clearData();
            amountSummaryLayout.clearData();
            totalPriceMap.clear();
            totalDepositMap.clear();
        }
        dbManager.addChildEventListenerToOrderRefOnSpecificDate(displayTime.getMillis(), eventListener);
    }

    private void setAmountSummary(HashMap totalPriceMap, HashMap totalDepositMap) {
        int sumOfTotalPrices = AmountCalculateUtil.sumOfTotal(totalPriceMap.values());
        int sumOfTotalDeposit = AmountCalculateUtil.sumOfTotal(totalDepositMap.values());

        amountSummaryLayout.setTotalSumOfOrders(sumOfTotalPrices);
        amountSummaryLayout.setTotalSumOfIncomes(sumOfTotalDeposit);
        amountSummaryLayout.setTotalSumOfUnpaid(sumOfTotalPrices - sumOfTotalDeposit);
    }
}
