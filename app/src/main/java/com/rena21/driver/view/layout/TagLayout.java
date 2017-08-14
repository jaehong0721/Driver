package com.rena21.driver.view.layout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

public class TagLayout extends FlexboxLayout {
    
    private List<String> tagNames;
    
    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        tagNames = new ArrayList();
    }
    
    @Override public void removeAllViews() {
        tagNames.clear();
        super.removeAllViews();
    }

    public void addView(String tagName, View child) {
        tagNames.add(tagName);
        super.addView(child);
    }

    public void addView(String tagName, View child, int index) {
        tagNames.add(index, tagName);
        super.addView(child,index);
    }
    
    public List<String> getTagNames() {
        return tagNames;
    }
}
