package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.rena21.driver.R;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.view.layout.InputInfoLayout;
import com.rena21.driver.view.widget.AddMajorItemButton;
import com.rena21.driver.view.widget.DeliveryAreaView;
import com.rena21.driver.view.widget.MajorItemView;

import java.util.ArrayList;
import java.util.List;

public class BusinessInfoContainer extends FrameLayout {

    public interface AddMajorItemListener {
        void onAddMajorItem();
    }

    public interface RemoveMajorItemListener {
        void onRemoveMajorItem(List<String> items);
    }

    private AddMajorItemListener addMajorItemListener;
    private RemoveMajorItemListener removeMajorItemListener;

    private TextView tvPartnerNum;
    private TextView tvOrderNum;
    private TextView tvAge;

    private FlexboxLayout majorItemsLayout;
    private FlexboxLayout deliveryAreasLayout;
    private TextView tvDeliveryArea;
    private TextView tvClosedDay;
    private TextView tvDeliveryTime;
    private TextView tvBusinessLicenseNumber;

    private InputInfoLayout inputPartnerNum;
    private InputInfoLayout inputOrderNum;
    private InputInfoLayout inputAge;
    private InputInfoLayout inputDeliveryArea;
    private InputInfoLayout inputClosedDay;
    private InputInfoLayout inputDeliveryTime;
    private InputInfoLayout inputBusinessNumber;

    private ArrayList<View> viewsOnEditMode;
    private ArrayList<View> viewsOnNormalMode;

    public BusinessInfoContainer(@NonNull Context context) {
        super(context, null);
    }

    public BusinessInfoContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.component_vendor_business_info, this);

        viewsOnNormalMode = new ArrayList<>();

        tvPartnerNum = (TextView) findViewById(R.id.tvPartnerNum);
        viewsOnNormalMode.add(tvPartnerNum);

        tvOrderNum = (TextView) findViewById(R.id.tvOrderNum);
        viewsOnNormalMode.add(tvOrderNum);

        tvAge = (TextView) findViewById(R.id.tvAge);
        viewsOnNormalMode.add(tvAge);

        tvClosedDay = (TextView) findViewById(R.id.tvClosedDay);
        viewsOnNormalMode.add(tvClosedDay);

        tvDeliveryTime = (TextView) findViewById(R.id.tvDeliveryTime);
        viewsOnNormalMode.add(tvDeliveryTime);

        tvBusinessLicenseNumber = (TextView) findViewById(R.id.tvBusinessLicenseNumber);
        viewsOnNormalMode.add(tvBusinessLicenseNumber);

        majorItemsLayout = (FlexboxLayout) findViewById(R.id.majorItemsLayout);
        deliveryAreasLayout = (FlexboxLayout) findViewById(R.id.deliveryAreasLayout);
        tvDeliveryArea = (TextView) findViewById(R.id.tvDeliveryArea);

        viewsOnEditMode = new ArrayList<>();

        inputPartnerNum = (InputInfoLayout) findViewById(R.id.inputPartnerNum);
        viewsOnEditMode.add(inputPartnerNum);

        inputOrderNum = (InputInfoLayout) findViewById(R.id.inputOrderNum);
        viewsOnEditMode.add(inputOrderNum);

        inputAge = (InputInfoLayout) findViewById(R.id.inputAge);
        viewsOnEditMode.add(inputAge);

        inputDeliveryArea = (InputInfoLayout) findViewById(R.id.inputDeliveryArea);
        viewsOnEditMode.add(inputDeliveryArea);

        inputClosedDay = (InputInfoLayout) findViewById(R.id.inputClosedDay);
        viewsOnEditMode.add(inputClosedDay);

        inputDeliveryTime = (InputInfoLayout) findViewById(R.id.inputDeliveryTime);
        viewsOnEditMode.add(inputDeliveryTime);

        inputBusinessNumber = (InputInfoLayout) findViewById(R.id.inputBusinessLicenseNumber);
        viewsOnEditMode.add(inputBusinessNumber);
    }

    public void setViewOnEditMode() {
        for(View view : viewsOnNormalMode) {
            view.setVisibility(View.GONE);
        }

        inputPartnerNum.setVisibility(View.VISIBLE);
        for(View view : viewsOnEditMode) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void setNormalMode() {
        for(View view : viewsOnEditMode) {
            view.setVisibility(View.GONE);
        }

        for(View view : viewsOnNormalMode) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void setRemoveMajorItemListener(RemoveMajorItemListener listener) {
        this.removeMajorItemListener = listener;
    }

    public void setAddMajorItemListener(AddMajorItemListener listener) {
        this.addMajorItemListener = listener;
    }

    public void setBusinessInfoData(final BusinessInfoData businessInfoData) {
        tvPartnerNum.setText(String.valueOf(businessInfoData.partnerNum)+"개");
        tvOrderNum.setText(String.valueOf(businessInfoData.orderNum)+"회");
        tvAge.setText(String.valueOf(businessInfoData.age)+"년");

        majorItemsLayout.removeAllViews();
        AddMajorItemButton addMajorItemButton = new AddMajorItemButton(getContext());
        addMajorItemButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(addMajorItemListener == null) return;
                addMajorItemListener.onAddMajorItem();
            }
        });
        majorItemsLayout.addView(addMajorItemButton);

        if(businessInfoData.majorItems != null) {
            for (final String majorItem: businessInfoData.majorItems) {
                final MajorItemView majorItemView = new MajorItemView(getContext());
                majorItemView.setItemName(majorItem);
                majorItemView.setIvDeleteClickListener(new OnClickListener() {
                    @Override public void onClick(View v) {
                        if(removeMajorItemListener == null) return;
                        businessInfoData.majorItems.remove(majorItem);
                        removeMajorItemListener.onRemoveMajorItem(businessInfoData.majorItems);
                    }
                });
                majorItemsLayout.addView(majorItemView, 0);
            }
        }

        deliveryAreasLayout.removeAllViews();
        if(businessInfoData.deliveryAreas != null) {
            tvDeliveryArea.setVisibility(View.GONE);
            deliveryAreasLayout.setVisibility(View.VISIBLE);
            for (String deliveryArea : businessInfoData.deliveryAreas) {
                DeliveryAreaView deliveryAreaView = new DeliveryAreaView(getContext());
                deliveryAreaView.setArea(deliveryArea);
                deliveryAreasLayout.addView(deliveryAreaView);
            }
        } else {
            deliveryAreasLayout.setVisibility(View.GONE);
            tvDeliveryArea.setVisibility(View.VISIBLE);
        }

        tvClosedDay.setText(businessInfoData.closedDay);
        tvDeliveryTime.setText(businessInfoData.deliveryTime);
        tvBusinessLicenseNumber.setText(businessInfoData.businessLicenseNumber);
    }
}