package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.LargeCategoryButton;

import java.util.List;

public class LargeCategoryAdapter extends RecyclerView.Adapter<LargeCategoryAdapter.LargeCategoryViewHolder>{

    public interface ClickLargeCategoryListener {
        void onClickLargeCategory(String largeCategoryName);
    }

    class LargeCategoryViewHolder extends RecyclerView.ViewHolder {
        LargeCategoryButton largeCategory;

        public LargeCategoryViewHolder(View itemView) {
            super(itemView);
            largeCategory = (LargeCategoryButton) itemView.findViewById(R.id.largeCategory);
        }

        public void bind(final String categoryName, final int position) {
            if(position == selectedPosition) {
                largeCategory.setSelected(true);
            } else {
                largeCategory.setSelected(false);
            }

            largeCategory.setLargeCategoryName(categoryName);
            largeCategory.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(selectedViewHolder == LargeCategoryViewHolder.this) return;

                    if(selectedViewHolder != null) selectedViewHolder.largeCategory.setSelected(false);
                    selectedViewHolder = LargeCategoryViewHolder.this;

                    selectedPosition = position;
                    largeCategory.setSelected(true);

                    listener.onClickLargeCategory(categoryName);
                }
            });
        }
    }

    private List<String> largeCategoryList;
    private LargeCategoryViewHolder selectedViewHolder;
    private int selectedPosition = -1;

    private final ClickLargeCategoryListener listener;

    public LargeCategoryAdapter(List<String> largeCategoryList, ClickLargeCategoryListener listener) {
        this.largeCategoryList = largeCategoryList;
        this.listener = listener;
    }

    @Override public LargeCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_large_category, parent, false);
        return new LargeCategoryViewHolder(view);
    }

    @Override public void onBindViewHolder(LargeCategoryViewHolder holder, int position) {
        String category = largeCategoryList.get(position);
        holder.bind(category, position);
    }

    @Override public int getItemCount() {
        return largeCategoryList.size();
    }


}
