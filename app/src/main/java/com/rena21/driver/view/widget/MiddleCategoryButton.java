package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rena21.driver.R;

public class MiddleCategoryButton extends FrameLayout {

    private RelativeLayout container;
    private TextView tvMiddleCategoryName;
    private ImageView ivCheckMark;

    public MiddleCategoryButton(@NonNull Context context) {
        this(context, null);
    }

    public MiddleCategoryButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = inflate(context, R.layout.widget_middle_category, this);

        container = (RelativeLayout) view.findViewById(R.id.container);
        tvMiddleCategoryName = (TextView) view.findViewById(R.id.tvMiddleCategoryName);
        ivCheckMark = (ImageView) view.findViewById(R.id.ivCheckMark);
    }

    @Override public void setSelected(boolean selected) {
        if(selected) {
            container.setBackgroundColor(ContextCompat.getColor(container.getContext(),R.color.primaryGreen));
            tvMiddleCategoryName.setTextColor(ContextCompat.getColor(tvMiddleCategoryName.getContext(), R.color.white));
            ivCheckMark.setImageResource(R.drawable.unselected_checkmark);
        } else {
            container.setBackgroundColor(ContextCompat.getColor(container.getContext(),R.color.white));
            tvMiddleCategoryName.setTextColor(ContextCompat.getColor(tvMiddleCategoryName.getContext(), R.color.business_info_text_color));
            ivCheckMark.setImageResource(R.drawable.selected_checkmark);
        }
    }

    public void setMiddleCategoryName(String middleCategoryName) {
        tvMiddleCategoryName.setText(middleCategoryName);
    }
}
