package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.ContactInfoData;

public class ContactInfoContainer extends FrameLayout {

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvPhoneNumber;

    public ContactInfoContainer(Context context) {
        super(context,null);
    }

    public ContactInfoContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.layout_vendor_contact_info,this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        tvName = (TextView)findViewById(R.id.tvName);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);
    }


    public void setContactInfoData(ContactInfoData contactInfoData) {
        tvName.setText(contactInfoData.vendorName);
        tvAddress.setText(contactInfoData.address);
        tvPhoneNumber.setText(contactInfoData.phoneNumber);
    }
}
