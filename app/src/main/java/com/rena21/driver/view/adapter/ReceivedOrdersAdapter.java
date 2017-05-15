package com.rena21.driver.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Order;
import com.rena21.driver.models.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String fileName, Order order);
    }

    static class MyViewHolder extends ViewHolder {

        private TextView tvTimeStamp;
        private TextView tvRestaurantName;
        private TextView tvItems;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvRestaurantName = (TextView) itemView.findViewById(R.id.tvRestaurantName);
            tvItems = (TextView) itemView.findViewById(R.id.tvItems);
        }

        public void bind(String timeStamp, String restaurantName, String orderItems, View.OnClickListener onClickListener) {
            tvTimeStamp.setText(timeStamp);
            tvItems.setText(orderItems);
            itemView.setOnClickListener(onClickListener);
            tvRestaurantName.setText(restaurantName);
        }


    }

    private final FirebaseDbManager dbManager;
    private HashMap<String, Order> orderMap;
    private ArrayList<String> fileNameList;
    private HashMap<String, String> restaurantNameMapCache;
    private OnItemClickListener onItemClickListener;

    public ReceivedOrdersAdapter(FirebaseDbManager dbManager) {
        this.orderMap = new HashMap<>();
        this.fileNameList = new ArrayList<>();
        this.restaurantNameMapCache = new HashMap<>();
        this.dbManager = dbManager;
    }

    @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override public void onBindViewHolder(MyViewHolder holder, int position) {
        final String fileName = fileNameList.get(position);
        final Order order = orderMap.get(fileName);
        final String restaurantPhoneNumber = getPhoneNumber(fileName);

        String timeStamp = getDisplayTimeFromfileName(fileName);
        String orderItems = makeOrderItemsString(order.orderItems);

        String restaurantName = restaurantNameMapCache.containsKey(restaurantPhoneNumber) ?
                restaurantNameMapCache.get(restaurantPhoneNumber) : restaurantPhoneNumber;


        holder.bind(timeStamp, restaurantName, orderItems, new View.OnClickListener() {
            @Override public void onClick(View v) {
                onItemClickListener.onItemClick(fileName, order);
            }
        });

    }

    @Override public int getItemCount() {
        return fileNameList.size();
    }

    public void addedItem(final String fileName, Order order) {
        orderMap.put(fileName, order);
        fileNameList.add(0, fileName);
        final String restaurantPhoneNumber = getPhoneNumber(fileName);
        notifyItemInserted(0);
        // 식당 이름이 저장되지 않은 경우
        if (!restaurantNameMapCache.containsKey(restaurantPhoneNumber)) {
            dbManager.getRestaurantName(restaurantPhoneNumber, new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {
                    String restaurantName = dataSnapshot.getValue(String.class);
                    restaurantNameMapCache.put(restaurantPhoneNumber, (restaurantName == null) ? restaurantPhoneNumber : restaurantName);
                    notifyItemChanged(0);
                }

                @Override public void onCancelled(DatabaseError databaseError) { }
            });
        }
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

    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private String getPhoneNumber(String fileName) {
        return fileName.split("_")[0];
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

    public Order getOrder(String orderKey) {
        return orderMap.get(orderKey);
    }

}
