package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.view.widget.CurrencyFormatEditText;

import java.util.List;

public class OrderDetailWithPriceAdapter extends RecyclerView.Adapter<OrderDetailWithPriceAdapter.OrderDetailViewHolder>
                                        implements CurrencyFormatEditText.AmountInputFinishListener {

    class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemName;
        TextView tvItemCount;
        CurrencyFormatEditText etItemPrice;

        public OrderDetailViewHolder(View itemView, CurrencyFormatEditText.AmountInputFinishListener listener) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
            etItemPrice = (CurrencyFormatEditText) itemView.findViewById(R.id.etItemPrice);
            etItemPrice.addTextChangedFinishListener(listener);
        }

        public void bind(String itemName, String itemCount, int itemPrice) {
            tvItemName.setText(itemName);
            tvItemCount.setText(itemCount);
            etItemPrice.setTag(getAdapterPosition());
            etItemPrice.setText(String.valueOf(itemPrice));
        }
    }

    private List<OrderItem> orderItems;

    public OrderDetailWithPriceAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_order_detail_with_price, parent, false);
        return new OrderDetailViewHolder(view, this);
    }

    @Override public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);

        holder.bind(orderItem.name, orderItem.count, orderItem.price);
    }

    @Override public int getItemCount() {
        return orderItems.size();
    }

    public List getOrderItems() {
        return orderItems;
    }

    private void setOrderItemPrice(int position, int itemPrice) {
        orderItems.get(position).price = itemPrice;
    }

    @Override public void onAmountInputFinish(Object tag, long amount) {
        orderItems.get((int) tag).price = (int) amount;
    }
}
