package com.rena21.driver.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.pojo.Order;
import com.rena21.driver.pojo.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.MyViewHolder>{

    class MyViewHolder extends ViewHolder{
        TextView tvTimeStamp;
        TextView tvRestaurantName;
        TextView tvItems;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvRestaurantName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
            tvItems = (TextView) itemView.findViewById(R.id.tvItems);
        }

        public void bind(String timeStamp, String restaurantName, String orderItems) {
            tvTimeStamp.setText(timeStamp);
            tvRestaurantName.setText(restaurantName);
            tvItems.setText(orderItems);
        }
    }

    private HashMap<String, Order> orderMap;
    private ArrayList<String> fileNameList;

    public ReceivedOrdersAdapter() {
        this.orderMap = new HashMap<>();
        this.fileNameList = new ArrayList<>();
    }

    @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override public void onBindViewHolder(MyViewHolder holder, int position) {
        String fileName = fileNameList.get(position);
        Order order = orderMap.get(fileName);

        String timeStamp = getDisplayTimeFromfileName(fileName);
        String restaurantName = order.restaurantName;
        String orderItems = makeOrderItemsString(order.orderItems);

        holder.bind(timeStamp, restaurantName, orderItems);
    }

    @Override public int getItemCount() {
        return fileNameList.size();
    }

    public void addedItem(String fileName, Order order) {
        orderMap.put(fileName, order);
        fileNameList.add(0, fileName);

        notifyItemInserted(0);
    }

    public void changedItem(String fileName, Order order) {
        int position = fileNameList.indexOf(fileName);

        orderMap.remove(fileName);
        orderMap.put(fileName, order);

        notifyItemChanged(position);
    }

    public void removedItem(String fileName) {
        int position = fileNameList.indexOf(fileName);

        orderMap.remove(fileName);
        fileNameList.remove(fileName);

        notifyItemRemoved(position);
    }

    private String getDisplayTimeFromfileName(String fileName) {
        StringBuffer sb = new StringBuffer();
        String timeStamp = fileName.substring(16, 26);
        for (int i = 0; i < timeStamp.length(); i++) {
            if (i == 2) {
                sb.append(".");
            }
            if (i == 4) {
                sb.append("  ");
            }
            if (i == 6) {
                sb.append(":");
            }
            if (i == 8) {
                sb.append(":");
            }
            sb.append(timeStamp.charAt(i));
        }
        return sb.toString();
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

    public Order getItem(int position) {
        return orderMap.get(fileNameList.get(position));
    }
}
