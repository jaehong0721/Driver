package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.rena21.driver.R;

public class ImageViewPager extends FrameLayout {
    public ImageViewPager(@NonNull Context context) {
        super(context, null);
    }

    public ImageViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.widget_image_view_pager, this);
    }
}
