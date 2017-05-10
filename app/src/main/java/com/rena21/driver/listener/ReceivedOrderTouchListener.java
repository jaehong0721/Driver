package com.rena21.driver.listener;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class ReceivedOrderTouchListener extends RecyclerView.SimpleOnItemTouchListener {

    private ItemClickListener itemClickListener;
    private GestureDetectorCompat gestureDetectorCompat;

    public interface ItemClickListener {
        void onItemClick(View view);
    }

    public ReceivedOrderTouchListener(Context context, ItemClickListener itemClickListener) {
        this.gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        this.itemClickListener = itemClickListener;
    }

    @Override public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && gestureDetectorCompat.onTouchEvent(e)) {
            itemClickListener.onItemClick(childView);
        }
        return false;
    }
}
