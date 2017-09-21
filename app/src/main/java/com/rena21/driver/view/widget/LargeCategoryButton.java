package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rena21.driver.R;

public class LargeCategoryButton extends FrameLayout {

    private TextView tvLargeCategoryName;
    private ImageView ivExtension;
    private View rootView;

    public LargeCategoryButton(@NonNull Context context) {
        this(context, null);
    }

    public LargeCategoryButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        rootView = inflate(context, R.layout.widget_large_category, this);
        tvLargeCategoryName = (TextView) rootView.findViewById(R.id.tvLargeCategoryName);
        ivExtension = (ImageView) rootView.findViewById(R.id.ivExtension);
    }

    @Override public void setSelected(boolean selected) {
        if (selected) {
            rootView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.white));
            ivExtension.setVisibility(View.VISIBLE);
        } else {
            rootView.setBackgroundColor(ContextCompat.getColor(rootView.getContext(), R.color.white_two));
            ivExtension.setVisibility(View.INVISIBLE);
        }
    }

    public void setLargeCategoryName(String categoryName) {
        tvLargeCategoryName.setText(categoryName);
    }
}
