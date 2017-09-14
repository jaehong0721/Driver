package com.rena21.driver.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

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
    private RadioGroup radioGroup;
    private AppCompatRadioButton rbAll;
    private LinearLayout emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_estimate, container, false);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);

        vpEstimate = (ViewPager) view.findViewById(R.id.vpEstimate);
        vpEstimate.setClipToPadding(false);

        int padding = DpToPxConverter.convertDpToPx(20,getResources().getDisplayMetrics());
        vpEstimate.setPadding(padding, 0, padding, 0);

        int margin = DpToPxConverter.convertDpToPx(10, getResources().getDisplayMetrics());
        vpEstimate.setPageMargin(margin);

        viewPagerAdapter = new EstimateViewPagerAdapter(
                new EstimateViewPagerAdapter.InputPriceListener() {
                    @Override public void onInputPrice(String estimateKey) {
                        startInputPriceActivity(estimateKey, "input");
                    }
                },
                new EstimateViewPagerAdapter.ModifyPriceListener() {
                    @Override public void onModifyPrice(String estimateKey) {
                        startInputPriceActivity(estimateKey, "modify");
                    }
                });

        radioGroup = (RadioGroup) view.findViewById(R.id.rdGroupToFiltering);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.rbAll) {
                    viewPagerAdapter.setDisplayedEstimate("all");
                    vpEstimate.setCurrentItem(0);
                }
                else if(checkedId == R.id.rbMyReply) {
                    viewPagerAdapter.setDisplayedEstimate("myReply");
                    vpEstimate.setCurrentItem(0);
                }
            }
        });

        rbAll = (AppCompatRadioButton) view.findViewById(R.id.rbAll);
        if(rbAll.isChecked())
            viewPagerAdapter.setDisplayedEstimate("all");

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vpEstimate.setAdapter(viewPagerAdapter);

        allEstimateListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Estimate estimate = dataSnapshot.getValue(Estimate.class);

                int position = viewPagerAdapter.addAllEstimate(estimateKey, estimate);

                if(emptyView.getVisibility() == View.VISIBLE)
                    emptyView.setVisibility(View.GONE);
                if(vpEstimate.getVisibility() == View.GONE)
                    vpEstimate.setVisibility(View.VISIBLE);

                vpEstimate.setCurrentItem(position);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Estimate estimate = dataSnapshot.getValue(Estimate.class);

                int position = viewPagerAdapter.changeEstimate(estimateKey, estimate);
                vpEstimate.setCurrentItem(position);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        myReplyListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Reply reply = dataSnapshot.getValue(Reply.class);

                int position = viewPagerAdapter.addMyReply(estimateKey, reply);
                vpEstimate.setCurrentItem(position);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String estimateKey = dataSnapshot.getKey();
                Reply reply = dataSnapshot.getValue(Reply.class);

                int position = viewPagerAdapter.changeMyReply(estimateKey, reply);
                vpEstimate.setCurrentItem(position);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {}
        };

        dbManager = App.getApplication(getContext().getApplicationContext()).getDbManager();
        dbManager.subscribeAllEstimate(allEstimateListener);
        dbManager.subscribeMyReply(myReplyListener);
    }

    @Override public void onDestroyView() {
        radioGroup.clearCheck();
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        dbManager.removeAllEstimateListener(allEstimateListener);
        dbManager.removeMyReplyListener(myReplyListener);
        radioGroup.clearCheck();
        super.onDestroy();
    }

    private void startInputPriceActivity(String estimateKey, String what) {
        Intent intent = new Intent(getActivity(), InputPriceOfEstimateActivity.class);
        intent.putExtra("estimateKey", estimateKey);
        intent.putExtra("what", what);
        startActivity(intent);
    }
}
