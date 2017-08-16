package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.rena21.driver.R;
import com.rena21.driver.util.DpToPxConverter;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class MajorItemView extends RelativeLayout {

    private TextView tvItemName;
    private ImageView ivDelete;
    private OnClickListener listener;

    public MajorItemView(Context context) {
        this(context,null);
    }

    public MajorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int paddingLeft = DpToPxConverter.convertDpToPx(18, getResources().getDisplayMetrics());
        int paddingTop = DpToPxConverter.convertDpToPx(11, getResources().getDisplayMetrics());
        int paddingRight = DpToPxConverter.convertDpToPx(8, getResources().getDisplayMetrics());
        int paddingBottom = DpToPxConverter.convertDpToPx(11, getResources().getDisplayMetrics());

        int marginRight = DpToPxConverter.convertDpToPx(8, getResources().getDisplayMetrics());
        int marginBottom = DpToPxConverter.convertDpToPx(8, getResources().getDisplayMetrics());

        FlexboxLayout.LayoutParams rootLayoutParams = new FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        rootLayoutParams.setMargins(0,0,marginRight,marginBottom);
        rootLayoutParams.setFlexGrow(1.0f);
        setLayoutParams(rootLayoutParams);

        setGravity(Gravity.CENTER);
        setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        setBackground(ContextCompat.getDrawable(context, R.drawable.major_item_view_shape));

        initTextView(context, attrs);
        initImageView(context, attrs);

        addView(tvItemName);
        addView(ivDelete);
    }

    public void setIvDeleteClickListener(OnClickListener listener) {
        ivDelete.setOnClickListener(listener);
    }

    public void setItemName(String itemName) {
        if(tvItemName != null)
            tvItemName.setText(itemName);
    }

    private void initTextView(Context context, AttributeSet attrs) {
        tvItemName = new TextView(context, attrs);
        tvItemName.setId(R.id.tvItemName);

        LayoutParams tvLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        tvLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvItemName.setLayoutParams(tvLayoutParams);

        tvItemName.setTextColor(ContextCompat.getColor(context, R.color.business_info_text_color));
        tvItemName.setTextSize(14);
        CalligraphyUtils.applyFontToTextView(context,tvItemName, getResources().getString(R.string.fonts_path));
    }

    private void initImageView(Context context, AttributeSet attrs) {
        ivDelete = new ImageView(context, attrs);
        ivDelete.setId(R.id.ivDelete);

        LayoutParams ivLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        ivLayoutParams.leftMargin = DpToPxConverter.convertDpToPx(8, getResources().getDisplayMetrics());
        ivLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivLayoutParams.addRule(RelativeLayout.RIGHT_OF, tvItemName.getId());
        ivDelete.setLayoutParams(ivLayoutParams);

        ivDelete.setImageResource(R.drawable.c_delete_item);
    }
}
