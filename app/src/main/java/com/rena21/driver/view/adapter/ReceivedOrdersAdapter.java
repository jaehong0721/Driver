package com.rena21.driver.view.adapter;

import android.support.v4.content.ContextCompat;
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
import com.rena21.driver.etc.ComparatorTimeSort;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Order;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.util.FileNameUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.grantland.widget.AutofitTextView;


public class ReceivedOrdersAdapter extends RecyclerView.Adapter<ReceivedOrdersAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String fileName);
    }

    static class MyViewHolder extends ViewHolder {

        private AutofitTextView tvRestaurantName;
        private AutofitTextView tvItems;
        private TextView tvTimeStamp;
        private TextView tvDeliveryFinish;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvRestaurantName = (AutofitTextView) itemView.findViewById(R.id.tvRestaurantName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            tvItems = (AutofitTextView) itemView.findViewById(R.id.tvItems);
            tvDeliveryFinish = (TextView) itemView.findViewById(R.id.tvDeliveryFinish);
        }

        public void bind(String restaurantName, String orderItems, String timeStamp, String state) {
            int dimColor = ContextCompat.getColor(itemView.getContext(), R.color.textDim);

            tvRestaurantName.setText(restaurantName);
            tvRestaurantName.setTextColor(dimColor);

            tvItems.setText(orderItems);
            tvItems.setTextColor(dimColor);

            tvTimeStamp.setText(timeStamp);
            tvTimeStamp.setTextColor(dimColor);

            tvDeliveryFinish.setText(state);
            tvDeliveryFinish.setTextColor(dimColor);
        }

        public void bind(String restaurantName, String orderItems, String timeStamp, String state, View.OnClickListener onClickListener) {
            tvRestaurantName.setText(restaurantName);
            tvRestaurantName.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textBlack));

            tvItems.setText(orderItems);
            tvItems.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textBlackSub));

            tvTimeStamp.setText(timeStamp);
            tvTimeStamp.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.primaryOrange));

            tvDeliveryFinish.setText(state);
            tvDeliveryFinish.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.textBlack));

            itemView.setOnClickListener(onClickListener);
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
        final String timeStamp = FileNameUtil.getDisplayTimeFromfileName(fileName);
        final Order order = orderMap.get(fileName);
        final String restaurantPhoneNumber = getPhoneNumber(fileName);

        String orderItems = makeOrderItemsString(order.orderItems);
        String restaurantName = restaurantNameMapCache.containsKey(restaurantPhoneNumber) ?
                restaurantNameMapCache.get(restaurantPhoneNumber) : restaurantPhoneNumber;

        if(order.delivered) {
            holder.bind(restaurantName, orderItems, timeStamp, "납품완료");
        } else if(order.accepted) {
            holder.bind(restaurantName, orderItems, timeStamp, "주문확인", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onItemClickListener.onItemClick(fileName);
                }
            });
        } else {
            holder.bind(restaurantName, orderItems, timeStamp, "주문접수", new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onItemClickListener.onItemClick(fileName);
                }
            });
        }
    }

    @Override public int getItemCount() {
        return fileNameList.size();
    }

    public void addedItem(final String fileName, Order order) {
        orderMap.put(fileName, order);
        fileNameList.add(fileName);

        ComparatorTimeSort orderByTime = new ComparatorTimeSort();
        Collections.sort(fileNameList, orderByTime);

        final String restaurantPhoneNumber = getPhoneNumber(fileName);

        //시간순으로 다시 정렬되면서 데이터셋이 전체적으로 바뀌기 때문에 notifyDataSetChanged()호출
        notifyDataSetChanged();
        // 식당 이름이 저장되지 않은 경우
        if (!restaurantNameMapCache.containsKey(restaurantPhoneNumber)) {
            dbManager.getRestaurantName(restaurantPhoneNumber, new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot dataSnapshot) {
                    String restaurantName = dataSnapshot.getValue(String.class);
                    restaurantNameMapCache.put(restaurantPhoneNumber, (restaurantName == null) ? restaurantPhoneNumber : restaurantName);
                    //todo notifyItemChanged()로
                    notifyDataSetChanged();
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
