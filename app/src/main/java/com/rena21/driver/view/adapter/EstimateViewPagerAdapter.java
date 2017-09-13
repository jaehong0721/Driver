package com.rena21.driver.view.adapter;


import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.Estimate;
import com.rena21.driver.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;


public class EstimateViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> allEstimateKeyList = new ArrayList<>();
    private ArrayList<String> myEstimateKeyList = new ArrayList<>();

    private HashMap<String, Estimate> allEstimateMap = new HashMap<>();

    @Override public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager_estimate,container,false);
        TextView tvRestaurantName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
        TextView tvRestaurantAddress = (TextView) itemView.findViewById(R.id.tvRestaurantAddress);

        String estimateKey = allEstimateKeyList.get(position);
        Estimate estimate = allEstimateMap.get(estimateKey);

        tvRestaurantName.setText(estimate.restaurantName);
        tvRestaurantAddress.setText(estimate.restaurantAddress);

        RecyclerView rvEstimateItem = (RecyclerView) itemView.findViewById(R.id.rvEstimateItem);
        rvEstimateItem.setLayoutManager(new LinearLayoutManager(container.getContext()));
        rvEstimateItem.addItemDecoration(new DividerItemDecoration(container.getContext(), R.drawable.shape_divider_for_received_orders));
        EstimateItemAdapter adapter = new EstimateItemAdapter(estimate.items, false);
        rvEstimateItem.setAdapter(adapter);

        container.addView(itemView);
        return itemView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override public int getCount() {
        return allEstimateKeyList.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    public void addAllEstimate(String estimateKey, Estimate estimate) {
        allEstimateKeyList.add(estimateKey);
        allEstimateMap.put(estimateKey, estimate);

        notifyDataSetChanged();
    }

    public void changeEstimate(String estimateKey, Estimate estimate) {
        allEstimateMap.put(estimateKey, estimate);

        notifyDataSetChanged();
    }
}