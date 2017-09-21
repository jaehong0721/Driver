package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.RepliedEstimateItem;
import com.rena21.driver.models.RequestedEstimateItem;
import com.rena21.driver.view.widget.CurrencyFormatTextView;

import java.util.ArrayList;

public class EstimateItemAdapter extends RecyclerView.Adapter<EstimateItemAdapter.EstimateItemViewHolder> {

    ArrayList<? extends RequestedEstimateItem> estimateItems;
    boolean isReplied;

    class EstimateItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemInfo;
        CurrencyFormatTextView tvPrice;

        public EstimateItemViewHolder(View itemView) {
            super(itemView);
            tvItemInfo = (TextView) itemView.findViewById(R.id.tvItemInfo);
            tvPrice = (CurrencyFormatTextView) itemView.findViewById(R.id.tvPrice);
        }

        public void setChildVisibility() {
            if(isReplied)
                tvPrice.setVisibility(View.VISIBLE);
            else
                tvPrice.setVisibility(View.GONE);
        }

        public void bind(RequestedEstimateItem requestedEstimateItem) {
            String itemName = requestedEstimateItem.itemName;
            String itemNum = requestedEstimateItem.itemNum;
            tvItemInfo.setText(itemName + ", " + itemNum);

            if(isReplied)
                tvPrice.setWon(((RepliedEstimateItem) requestedEstimateItem).price);
        }
    }

    public EstimateItemAdapter(ArrayList<? extends RequestedEstimateItem> estimateItems, boolean isReplied) {
        this.estimateItems = estimateItems;
        this.isReplied = isReplied;
    }

    @Override public EstimateItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_estimate_item, parent, false);
        return new EstimateItemViewHolder(view);
    }

    @Override public void onBindViewHolder(EstimateItemViewHolder holder, int position) {
        holder.setChildVisibility();
        holder.bind(estimateItems.get(position));
    }

    @Override public int getItemCount() {
        return estimateItems.size();
    }
}
