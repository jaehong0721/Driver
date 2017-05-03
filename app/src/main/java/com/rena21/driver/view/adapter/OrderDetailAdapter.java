package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.pojo.OrderItem;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>{

    class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName;
        TextView tvItemCount;

        public OrderDetailViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvItemCount);
        }

        public void bind(String itemName, String itemCount) {
            tvItemName.setText(itemName);
            tvItemCount.setText(itemCount);
        }
    }

    private List<OrderItem> orderItems;

    public OrderDetailAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @Override public OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_order_detail, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override public void onBindViewHolder(OrderDetailViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);

        holder.bind(orderItem.name, orderItem.count);
    }

    @Override public int getItemCount() {
        return orderItems.size();
    }



}
