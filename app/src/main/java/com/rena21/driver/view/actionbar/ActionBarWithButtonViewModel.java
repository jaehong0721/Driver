package com.rena21.driver.view.actionbar;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rena21.driver.R;


public class ActionBarWithButtonViewModel {

    public static ActionBarWithButtonViewModel createWithActionBar(ActionBar supportActionBar) {
        ActionBarWithButtonViewModel instance = new ActionBarWithButtonViewModel(supportActionBar);
        instance.setup();
        return instance;
    }

    private ActionBarWithButtonViewModel(ActionBar actionBar) { this.actionBar = actionBar; }

    private ActionBar actionBar;
    private TextView tvActionBarTitle;
    private ImageButton btnClose;

    private void setup() {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_with_button);
        View view = actionBar.getCustomView();

        tvActionBarTitle = (TextView) view.findViewById(R.id.tvActionBarTitle);
        btnClose = (ImageButton) view.findViewById(R.id.btnClose);
    }

    public void setCloseButtonListener(View.OnClickListener listener) {
        btnClose.setOnClickListener(listener);
    }

    public ActionBarWithButtonViewModel setTitle(String title) {
        tvActionBarTitle.setText(title);
        return this;
    }
}
