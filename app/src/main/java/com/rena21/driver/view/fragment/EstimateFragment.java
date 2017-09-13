package com.rena21.driver.view.fragment;

import android.content.Intent;
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
import com.rena21.driver.activities.InputPriceOfEstimateActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Estimate;
import com.rena21.driver.models.Reply;
import com.rena21.driver.util.DpToPxConverter;
import com.rena21.driver.view.adapter.EstimateViewPagerAdapter;

public class EstimateFragment extends Fragment {

    private ViewPager vpEstimate;
    private FirebaseDbManager dbManager;

    private ChildEventListener allEstimateListener;
    private ChildEventListener myReplyListener;

    private EstimateViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate, container, false);

        vpEstimate = (ViewPager) view.findViewById(R.id.vpEstimate);
        vpEstimate.setClipToPadding(false);

        int padding = DpToPxConverter.convertDpToPx(20,getResources().getDisplayMetrics());
        vpEstimate.setPadding(padding, 0, padding, 0);

        int margin = DpToPxConverter.convertDpToPx(10, getResources().getDisplayMetrics());
        vpEstimate.setPageMargin(margin);
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewPagerAdapter = new EstimateViewPagerAdapter(new EstimateViewPagerAdapter.InputPriceListener() {
            @Override public void onInputPrice(String estimateKey) {
                Intent intent = new Intent(getActivity(), InputPriceOfEstimateActivity.class);
                startActivity(intent);
            }
        });

        vpEstimate.setAdapter(viewPagerAdapter);

        allEstimateListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Estimate estimate = dataSnapshot.getValue(Estimate.class);

                viewPagerAdapter.addAllEstimate(estimateKey, estimate);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Estimate estimate = dataSnapshot.getValue(Estimate.class);

                viewPagerAdapter.changeEstimate(estimateKey, estimate);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        myReplyListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Reply reply = dataSnapshot.getValue(Reply.class);

                viewPagerAdapter.addMyReply(estimateKey, reply);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        dbManager = App.getApplication(getContext().getApplicationContext()).getDbManager();
        dbManager.subscribeAllEstimate(allEstimateListener);
        dbManager.subscribeMyReply(myReplyListener);
    }

    @Override public void onDestroy() {
        dbManager.removeAllEstimateListener(allEstimateListener);
        dbManager.removeMyReplyListener(myReplyListener);
        super.onDestroy();
    }
}
