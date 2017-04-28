package view;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.rena21.driver.R;

public class ActionBarViewModel {
    public static ActionBarViewModel createWithActionBar(ActionBar supportActionBar) {
        ActionBarViewModel instance = new ActionBarViewModel(supportActionBar);
        instance.setup();
        return instance;
    }

    private ActionBarViewModel(ActionBar actionBar) { this.actionBar = actionBar; }

    private ActionBar actionBar;
    private TextView tvActionBarTitle;

    private void setup() {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar);
        View view = actionBar.getCustomView();
        tvActionBarTitle = (TextView) view.findViewById(R.id.tvActionBarTitle);
    }

    public ActionBarViewModel setTitle(String title) {
        tvActionBarTitle.setText(title);
        return this;
    }
}
