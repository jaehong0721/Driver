package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.RepliedEstimateItem;
import com.rena21.driver.view.widget.CurrencyFormatEditText;

import java.util.ArrayList;

public class InputEstimatePriceAdapter extends RecyclerView.Adapter<InputEstimatePriceAdapter.InputEstimatePriceViewHolder>{

    class InputEstimatePriceViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItemInfo;
        private final CurrencyFormatEditText etInputPrice;

        public InputEstimatePriceViewHolder(View itemView) {
            super(itemView);

            tvItemInfo = (TextView) itemView.findViewById(R.id.tvItemInfo);
            etInputPrice = (CurrencyFormatEditText) itemView.findViewById(R.id.etInputPrice);
        }

        public void bind(RepliedEstimateItem item) {
            tvItemInfo.setText(item.itemName + ", " + item.itemNum);
            etInputPrice.setText(String.valueOf(item.price));
        }
    }

    ArrayList<RepliedEstimateItem> items = new ArrayList<>();

    public InputEstimatePriceAdapter(ArrayList<RepliedEstimateItem> items) {
        this.items = items;
    }

    @Override public InputEstimatePriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_input_estimate_item, parent, false);
        return new InputEstimatePriceViewHolder(view);
    }

    @Override public void onBindViewHolder(InputEstimatePriceViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }
}
