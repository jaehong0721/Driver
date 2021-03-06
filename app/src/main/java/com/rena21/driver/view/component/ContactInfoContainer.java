package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.models.ContactInfoData;
import com.rena21.driver.view.layout.InputInfoLayout;

import java.util.HashMap;
import java.util.Map;

public class ContactInfoContainer extends FrameLayout {

    private final String[] filter = {"010", "011", "017", "016", "018", "019"};
    private TextView tvName;
    private TextView tvAddress;
    private TextView tvPhoneNumber;

    private InputInfoLayout inputVendorName;
    private InputInfoLayout inputAddress;
    private InputInfoLayout inputPhoneNumber;

    private HashMap<TextView, InputInfoLayout> transformableView;

    public ContactInfoContainer(Context context) {
        this(context,null);
    }

    public ContactInfoContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.component_vendor_contact_info,this);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        transformableView = new HashMap<>();

        tvName = (TextView)findViewById(R.id.tvName);
        inputVendorName = (InputInfoLayout)findViewById(R.id.inputVendorName);
        transformableView.put(tvName, inputVendorName);

        tvAddress = (TextView)findViewById(R.id.tvAddress);
        inputAddress = (InputInfoLayout)findViewById(R.id.inputAddress);
        transformableView.put(tvAddress, inputAddress);

        tvPhoneNumber = (TextView)findViewById(R.id.tvPhoneNumber);
        inputPhoneNumber = (InputInfoLayout)findViewById(R.id.inputPhoneNumber);
        transformableView.put(tvPhoneNumber,inputPhoneNumber);
    }

    public void setContactInfoData(ContactInfoData contactInfoData) {
        tvName.setText(contactInfoData.vendorName);
        tvAddress.setText(contactInfoData.address);
        tvPhoneNumber.setText(contactInfoData.phoneNumber);
    }

    public void setEditMode() {
        for(Map.Entry<TextView,InputInfoLayout> entry : transformableView.entrySet()) {
            TextView tv = entry.getKey();
            InputInfoLayout input = entry.getValue();
            input.setText(tv.getText().toString());

            tv.setVisibility(View.GONE);
            input.setVisibility(View.VISIBLE);
        }
    }

    public void setNormalMode() {
        for(Map.Entry<TextView,InputInfoLayout> entry : transformableView.entrySet()) {
            entry.getValue().setVisibility(View.GONE);
            entry.getKey().setVisibility(View.VISIBLE);
        }
    }

    public ContactInfoData getNewContactInfoData() {
        String phoneNumber = inputPhoneNumber.getText();
        String vendorName = inputVendorName.getText();

        if(phoneNumber.equals("") || vendorName.equals("")) {
            Toast.makeText(getContext(),"이름과 핸드폰번호는 필수 입력사항입니다",Toast.LENGTH_SHORT).show();
            return null;
        }


        for(int i = 0; i<filter.length; i++) {
            if(phoneNumber.startsWith(filter[i])) {
                break;
            } else {
                if(i != filter.length-1) continue;
                Toast.makeText(getContext(), "전화번호는 핸드폰번호만 가능합니다", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        ContactInfoData contactInfoData = new ContactInfoData();
        contactInfoData.phoneNumber = phoneNumber;
        contactInfoData.vendorName = vendorName;
        contactInfoData.address = inputAddress.getText();
        return contactInfoData;
    }
}
