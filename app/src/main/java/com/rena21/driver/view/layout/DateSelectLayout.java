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
        void onDateChanged(DateTime prevDateTime, DateTime dateTime);
    }

    private DateTime displayDate;
    private DateTime prevDisplayDate;

    private DateChangedListener listener;

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
                prevDisplayDate = displayDate;
                displayDate = displayDate.minusDays(1);
                showDisplayDate();
                notifyCurrentDate();
            }
        });

        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (isTodayDisplayed()) {
                    Toast.makeText(context, "마지막 날짜입니다", Toast.LENGTH_SHORT).show();
                } else {
                    prevDisplayDate = displayDate;
                    displayDate = displayDate.plusDays(1);
                    showDisplayDate();
                    notifyCurrentDate();
                }
            }
        });

        displayDate = DateTime.now().withTimeAtStartOfDay();
    }

    public void setDateChangedListener(DateChangedListener listener) {
        this.listener = listener;
        showDisplayDate();
        notifyCurrentDate();
    }

    public void removeDateChangedListener(DateChangedListener listener) {
        this.listener = null;
    }

    public DateTime getDisplayDate() {
        return displayDate;
    }

    private void showDisplayDate() {
        String dateText = JodaTimeUtil.yyyyMMddSimple(displayDate);
        tvDate.setText(dateText);
    }

    private void notifyCurrentDate() {
        listener.onDateChanged(prevDisplayDate, displayDate);
    }

    private boolean isTodayDisplayed() {
        long nowInMillis = DateTime.now().withTimeAtStartOfDay().getMillis();
        long displayInMillis = displayDate.withTimeAtStartOfDay().getMillis();
        return nowInMillis == displayInMillis;
    }
}
