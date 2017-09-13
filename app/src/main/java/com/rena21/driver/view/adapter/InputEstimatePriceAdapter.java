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
import java.util.HashSet;

public class InputEstimatePriceAdapter extends RecyclerView.Adapter<InputEstimatePriceAdapter.InputEstimatePriceViewHolder>
                                        implements CurrencyFormatEditText.AmountInputFinishListener{

    static class InputEstimatePriceViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItemInfo;
        private final CurrencyFormatEditText etInputPrice;

        public InputEstimatePriceViewHolder(View itemView, CurrencyFormatEditText.AmountInputFinishListener listener) {
            super(itemView);

            tvItemInfo = (TextView) itemView.findViewById(R.id.tvItemInfo);
            etInputPrice = (CurrencyFormatEditText) itemView.findViewById(R.id.etInputPrice);
            etInputPrice.addTextChangedFinishListener(listener);
        }

        public void bind(RepliedEstimateItem item) {
            tvItemInfo.setText(item.itemName + ", " + item.itemNum);
            etInputPrice.setTag(getAdapterPosition());
            etInputPrice.setText(String.valueOf(item.price));
        }
    }

    public interface ChangeReplyCountListener {
        void onChangeReplyCount(int count);
    }

    ArrayList<RepliedEstimateItem> items = new ArrayList<>();
    HashSet<String> repliedItems = new HashSet<>();

    ChangeReplyCountListener listener;

    public InputEstimatePriceAdapter(ArrayList<RepliedEstimateItem> items, ChangeReplyCountListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override public InputEstimatePriceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_input_estimate_item, parent, false);
        return new InputEstimatePriceViewHolder(view, this);
    }

    @Override public void onBindViewHolder(InputEstimatePriceViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override public void onAmountInputFinish(Object tag, long amount) {
        items.get((int) tag).price = (int) amount;

        if(amount != 0)
            repliedItems.add(items.get((int) tag).itemName);
        else
            repliedItems.remove(items.get((int) tag).itemName);

        listener.onChangeReplyCount(repliedItems.size());
    }

    public ArrayList<RepliedEstimateItem> getItems() {
        return items;
    }
}
