package com.rena21.driver.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.MiddleCategoryButton;

import java.util.List;

public class MiddleCategoryAdapter extends RecyclerView.Adapter<MiddleCategoryAdapter.MiddleCategoryViewHolder>{

    class MiddleCategoryViewHolder extends RecyclerView.ViewHolder {
        MiddleCategoryButton middleCategory;

        MiddleCategoryViewHolder(View itemView) {
            super(itemView);
            middleCategory = (MiddleCategoryButton) itemView.findViewById(R.id.middleCategory);
        }

        void bind(final String categoryName, final int position) {
            if(selectedCategoryList.contains(categoryName)) {
                middleCategory.setSelected(true);
            } else {
                middleCategory.setSelected(false);
            }

            middleCategory.setMiddleCategoryName(categoryName);
            middleCategory.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(middleCategory.isSelected()) {
                        middleCategory.setSelected(false);
                        selectedCategoryList.remove(categoryName);
                    } else {
                        middleCategory.setSelected(true);
                        selectedCategoryList.add(categoryName);
                    }
                }
            });
        }
    }

    private List<String> middleCategoryList;
    private List<String> selectedCategoryList;

    public MiddleCategoryAdapter(List<String> middleCategoryList, List<String> selectedCategoryList) {
        this.middleCategoryList = middleCategoryList;
        this.selectedCategoryList = selectedCategoryList;
    }

    @Override public MiddleCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_middle_category, parent, false);
        return new MiddleCategoryViewHolder(view);
    }

    @Override public void onBindViewHolder(MiddleCategoryViewHolder holder, int position) {
        String category = middleCategoryList.get(position);
        holder.bind(category, position);
    }

    @Override public int getItemCount() {
        return middleCategoryList.size();
    }
}
