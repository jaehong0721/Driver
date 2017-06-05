package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.view.widget.CurrencyFormatTextView;

import java.util.List;

public class DeliveryDetailAdapter extends RecyclerView.Adapter<DeliveryDetailAdapter.OrderDetailViewHolder>{

    class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemCount;
        CurrencyFormatTextView tvItemPrice;

        public OrderDetailViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
            tvItemPrice = (CurrencyFormatTextView) itemView.findViewById(R.id.tvItemPrice);
        }

        public void bind(String itemName, String itemCount, int itemPrice) {
            tvItemName.setText(itemName);
            tvItemCount.setText(itemCount);
            tvItemPrice.setWon(itemPrice);
        }
    }

    private List<OrderItem> orderItems;

    public DeliveryDetailAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_delivery_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);

        holder.bind(orderItem.name, orderItem.count, orderItem.price);
    }

    @Override public int getItemCount() {
        return orderItems.size();
    }
}
