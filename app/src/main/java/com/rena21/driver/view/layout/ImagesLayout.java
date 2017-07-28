package com.rena21.driver.view.layout;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.rena21.driver.R;

public class ImagesLayout extends FrameLayout
{
    public ImagesLayout(@NonNull Context context) {
        super(context, null);
    }

    public ImagesLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.layout_vendor_images,this);
    }
}
