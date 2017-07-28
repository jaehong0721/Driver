package com.rena21.driver.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rena21.driver.R;
import com.rena21.driver.util.DpToPxConverter;

import java.util.ArrayList;

public class ViewPagerIndicator extends RelativeLayout {

    private Context context;
    private ArrayList<View> dots;

    private final int width = DpToPxConverter.convertDpToPx(6,getResources().getDisplayMetrics());
    private final int height = DpToPxConverter.convertDpToPx(6,getResources().getDisplayMetrics());

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.dots = new ArrayList<>();
    }

    public void changeDot(int count) {

        if(count < dots.size()) {
            removeView(findViewById(dots.size()));
            dots.remove(dots.size()-1);
            return;
        }

        for (int i = dots.size(); i < count; i++) {

            View dot = new View(context);

            LayoutParams layoutParams = new LayoutParams(width, height);
            if (dots.size() == 0) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else {
                layoutParams.leftMargin = 20;
                layoutParams.addRule(RelativeLayout.RIGHT_OF, dots.get(i - 1).getId());
            }
            dot.setLayoutParams(layoutParams);
            dot.setBackgroundResource(R.drawable.unselected_indicator_shape);

            dot.setId(i + 1);
            dots.add(dot);
            addView(dot);
        }
    }

    public void selectDot(int position) {
        for (View dot : dots) {
            if (dots.get(position) == dot) {
                dot.setBackgroundResource(R.drawable.selected_indicator_shape);
            } else {
                dot.setBackgroundResource(R.drawable.unselected_indicator_shape);
            }
        }
    }

}
