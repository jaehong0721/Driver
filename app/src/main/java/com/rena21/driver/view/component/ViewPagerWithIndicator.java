package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.SettableIndicatorViewPager;
import com.rena21.driver.view.widget.ViewPagerIndicator;


public class ViewPagerWithIndicator extends FrameLayout {

    private SettableIndicatorViewPager settableIndicatorViewPager;
    private ViewPagerIndicator viewPagerIndicator;

    public ViewPagerWithIndicator(@NonNull Context context) {
        this(context, null);
    }

    public ViewPagerWithIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.component_view_pager_with_indicator, this);
        settableIndicatorViewPager = (SettableIndicatorViewPager) view.findViewById(R.id.settableIndicatorViewPager);
        viewPagerIndicator = (ViewPagerIndicator) view.findViewById(R.id.viewPagerIndicator);

        settableIndicatorViewPager.setIndicator(viewPagerIndicator);
    }

    public void setAdapter(PagerAdapter adapter) {
        settableIndicatorViewPager.setAdapter(adapter);
    }

    public int getCurrentItem() {
        return settableIndicatorViewPager.getCurrentItem();
    }
}
