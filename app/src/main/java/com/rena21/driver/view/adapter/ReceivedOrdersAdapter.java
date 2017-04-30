package com.rena21.driver.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.model.ReceivedOrder;
import com.rena21.driver.pojo.Order;
import com.rena21.driver.pojo.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.MyViewHolder>{

    class MyViewHolder extends ViewHolder{
        TextView tvRestaurantName;
        TextView tvItems;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
            tvItems = (TextView) itemView.findViewById(R.id.tvItems);
        }

        public void bind(String restaurantName, String orderItems) {
            tvRestaurantName.setText(restaurantName);
            tvItems.setText(orderItems);
        }
    }

    private HashMap<String, ReceivedOrder> receivedOrderItemMap;
    private ArrayList<String> fileNameList;

    public ReceivedOrdersAdapter() {
        this.receivedOrderItemMap = new HashMap<>();
        this.fileNameList = new ArrayList<>();
    }

    @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override public void onBindViewHolder(MyViewHolder holder, int position) {
        String fileName = fileNameList.get(position);
        ReceivedOrder receivedOrder = receivedOrderItemMap.get(fileName);

        holder.bind(receivedOrder.restaurantName, receivedOrder.orderItems);
    }

    @Override public int getItemCount() {
        return fileNameList.size();
    }

    public void addedItem(String fileName, Order order) {
        String restaurantName = order.restaurantName;
        String orderItems = makeOrderItemsString(order.orderItems);

        ReceivedOrder receivedOrder = new ReceivedOrder(restaurantName, orderItems);

        receivedOrderItemMap.put(fileName, receivedOrder);
        fileNameList.add(0, fileName);

        notifyItemInserted(0);
    }

    public void changedItem(String fileName, Order order) {
        int position = fileNameList.indexOf(fileName);

        String restaurantName = order.restaurantName;
        String orderItems = makeOrderItemsString(order.orderItems);

        ReceivedOrder receivedOrder = receivedOrderItemMap.get(fileName);
        receivedOrder.restaurantName = restaurantName;
        receivedOrder.orderItems = orderItems;

        notifyItemChanged(position);
    }

    public void removedItem(String fileName) {
        int position = fileNameList.indexOf(fileName);

        receivedOrderItemMap.remove(fileName);
        fileNameList.remove(fileName);

        notifyItemRemoved(position);
    }

    private String makeOrderItemsString(List<OrderItem> orderItems) {
        StringBuffer sb = new StringBuffer();

        for (OrderItem item : orderItems) {
            sb.append(item.name)
                    .append(" ")
                    .append(item.count)
                    .append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
