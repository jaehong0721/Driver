package com.rena21.driver.view.layout;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rena21.driver.R;
import com.rena21.driver.util.DpToPxConverter;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class InputInfoLayout extends RelativeLayout{

    private EditText etInput;
    private ImageView ivClear;

    private TypedArray typedArray;

    public InputInfoLayout(Context context) {
        this(context, null);
    }

    public InputInfoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.InputInfoLayout);
        boolean clearable = typedArray.getBoolean(R.styleable.InputInfoLayout_clearable, false);

        if(clearable) {
            initClearButton(context, attrs);
            addView(ivClear);
        }
        initEditText(context, attrs);
        addView(etInput);
    }

    public void setText(String s) {
        etInput.setText(s);
    }

    private void initEditText(Context context, AttributeSet attrs) {
        etInput = new EditText(context, attrs);
        etInput.setTextSize(14);
        etInput.setTextColor(ContextCompat.getColor(context, R.color.business_info_text_color));
        etInput.setMaxLines(1);
        CalligraphyUtils.applyFontToTextView(context,etInput, getResources().getString(R.string.fonts_path));

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int rightMargin = DpToPxConverter.convertDpToPx(20,getResources().getDisplayMetrics());
        int leftMargin = DpToPxConverter.convertDpToPx(20,getResources().getDisplayMetrics());;
        lp.setMargins(leftMargin,0,rightMargin,0);
        lp.addRule(CENTER_VERTICAL);
        if(ivClear != null)
            lp.addRule(LEFT_OF,ivClear.getId());
        etInput.setLayoutParams(lp);

        String hint = typedArray.getString(R.styleable.InputInfoLayout_android_hint);
        etInput.setHint(hint);

        int inputType = typedArray.getInt(R.styleable.InputInfoLayout_android_inputType,-1);
        if(inputType != -1)
            etInput.setInputType(inputType);
    }

    private void initClearButton(Context context, AttributeSet attrs) {
        ivClear = new ImageView(context, attrs);
        ivClear.setId(R.id.ivClear);
        ivClear.setImageResource(R.drawable.c_delete_item);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int rightMargin = DpToPxConverter.convertDpToPx(16,getResources().getDisplayMetrics());;
        lp.setMargins(0,0,rightMargin,0);
        lp.addRule(ALIGN_PARENT_RIGHT);
        lp.addRule(CENTER_VERTICAL);
        ivClear.setLayoutParams(lp);

        ivClear.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                etInput.setText("");
            }
        });
    }

    @Override public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        etInput.setVisibility(visibility);
        if(ivClear != null)
            ivClear.setVisibility(visibility);
    }
}
