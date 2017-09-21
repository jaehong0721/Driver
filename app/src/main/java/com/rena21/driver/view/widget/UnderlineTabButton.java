package com.rena21.driver.view.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rena21.driver.R;


public class UnderlineTabButton extends FrameLayout {

    private TextView textView;
    private View underLine;

    private Context context;

    public UnderlineTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        View view = inflate(context, R.layout.widget_underline_button, this);
        textView = (TextView) view.findViewById(R.id.textView);
        underLine = view.findViewById(R.id.underLine);
    }

    @Override public void setSelected(boolean selected) {
        if (selected) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.primaryGreen));
            underLine.setVisibility(View.VISIBLE);
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.textBlackSub));
            underLine.setVisibility(View.INVISIBLE);
        }
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
