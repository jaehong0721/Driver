package com.rena21.driver.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Estimate;
import com.rena21.driver.view.adapter.EstimateViewPagerAdapter;

public class EstimateFragment extends Fragment {

    private ViewPager vpEstimate;
    private FirebaseDbManager dbManager;

    private ChildEventListener allEstimateListener;

    private EstimateViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate, container, false);

        vpEstimate = (ViewPager) view.findViewById(R.id.vpEstimate);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPagerAdapter = new EstimateViewPagerAdapter();
        vpEstimate.setAdapter(viewPagerAdapter);

        allEstimateListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Estimate estimate = dataSnapshot.getValue(Estimate.class);

                viewPagerAdapter.addAllEstimate(estimateKey, estimate);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };


        dbManager = App.getApplication(getContext().getApplicationContext()).getDbManager();
        dbManager.subscribeAllEstimate(allEstimateListener);
    }

    @Override public void onDestroy() {
        dbManager.removeAllEstimateListener(allEstimateListener);
        super.onDestroy();
    }
}
