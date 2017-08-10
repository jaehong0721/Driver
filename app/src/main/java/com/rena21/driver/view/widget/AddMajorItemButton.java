package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.rena21.driver.R;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class AddMajorItemButton extends android.support.v7.widget.AppCompatButton {

    public AddMajorItemButton(Context context) {
        this(context, null);
    }

    public AddMajorItemButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setFlexGrow(1.0f);
        setLayoutParams(lp);

        setBackground(ContextCompat.getDrawable(context, R.drawable.add_major_item_button_shape));
        setTextColor(ContextCompat.getColor(context, R.color.white));
        setTextSize(14);
        CalligraphyUtils.applyFontToTextView(context,this, getResources().getString(R.string.fonts_path));
        setText("품목추가");
    }
}
