package com.rena21.driver.view.layout;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.util.JodaTimeUtil;

import org.joda.time.DateTime;

public class DateSelectLayout extends RelativeLayout {

    public interface DateChangedListener {
        void onDateChanged(DateTime dateTime);
    }

    DateTime displayDate;

    private TextView tvDate;
    private ImageButton btnBefore;
    private ImageButton btnNext;

    public DateSelectLayout(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_date_select, this);
        tvDate = (TextView) view.findViewById(R.id.tvDate);

        btnBefore = (ImageButton) view.findViewById(R.id.btnBefore);
        btnBefore.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                displayDate = displayDate.minusDays(1);
                showDisplayDate();
            }
        });

        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (isTodayDisplayed()) {
                    Toast.makeText(context, "마지막 날짜입니다", Toast.LENGTH_SHORT).show();
                } else {
                    displayDate = displayDate.plusDays(1);
                    showDisplayDate();
                }
            }
        });

        displayDate = DateTime.now().withTimeAtStartOfDay();

        showDisplayDate();
    }

    private void showDisplayDate() {
        String dateText = JodaTimeUtil.yyyyMMddSimple(displayDate);
        tvDate.setText(dateText);
    }

    private boolean isTodayDisplayed() {
        long nowInMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        long displayInMillis = displayDate.withTimeAtStartOfDay().getMillis();
        return nowInMillis == displayInMillis;
    }
}
