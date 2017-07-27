package com.rena21.driver.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rena21.driver.R;


public class MainTabButton extends FrameLayout{

    private ImageView ivImage;
    private TextView tvText;

    private String text;
    private int selectedImage;
    private int unselectedImage;
    private int selectedColor;
    private int unselectedColor;

    public MainTabButton(Context context) {
        super(context, null);
    }

    public MainTabButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.widget_main_tab_button, this);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvText = (TextView) findViewById(R.id.tvText);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MainTabButton);

        text = typedArray.getString(R.styleable.MainTabButton_text);
        tvText.setText(text);

        selectedColor = typedArray.getColor(R.styleable.MainTabButton_selectedColor, -1);
        unselectedColor = typedArray.getColor(R.styleable.MainTabButton_unselectedColor, -1);
        selectedImage = typedArray.getResourceId(R.styleable.MainTabButton_selectedImage, -1);
        unselectedImage = typedArray.getResourceId(R.styleable.MainTabButton_unselectedImage, -1);

        typedArray.recycle();
    }

    @Override public void setSelected(boolean selected) {
        if(selected) {
            tvText.setTextColor(selectedColor);
            ivImage.setImageResource(selectedImage);
        } else {
            tvText.setTextColor(unselectedColor);
            ivImage.setImageResource(unselectedImage);
        }
    }
}
