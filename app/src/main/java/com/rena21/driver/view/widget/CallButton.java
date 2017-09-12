package com.rena21.driver.view.widget;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.rena21.driver.R;


public class CallButton extends android.support.v7.widget.AppCompatImageView implements View.OnClickListener {

    private String phoneNumber;

    public CallButton(Context context) {
        this(context, null);
    }

    public CallButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        setImageResource(R.drawable.phone);
        setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        if (phoneNumber == null) {
            throw new RuntimeException("you must set both field - phoneNumber");
        }

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getContext(), "전화를 하려면 전화 권한을 허용해야 합니다", Toast.LENGTH_SHORT).show();
            return;
        }

        getContext().startActivity(intent);
    }

    public void setCalleeInfo(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
