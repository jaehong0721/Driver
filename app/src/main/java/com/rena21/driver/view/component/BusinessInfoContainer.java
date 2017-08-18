package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.util.TransformDataUtil;
import com.rena21.driver.view.layout.InputInfoLayout;
import com.rena21.driver.view.layout.TagLayout;
import com.rena21.driver.view.widget.AddMajorItemButton;
import com.rena21.driver.view.widget.DeliveryAreaView;
import com.rena21.driver.view.widget.MajorItemView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessInfoContainer extends FrameLayout {

    public interface AddMajorItemListener {
        void onAddMajorItem(ArrayList<String> majorItems);
    }

    public interface RemoveMajorItemListener {
        void onRemoveMajorItem(List<String> items);
    }

    private AddMajorItemListener addMajorItemListener;
    private RemoveMajorItemListener removeMajorItemListener;

    private TextView tvPartnerNum;
    private TextView tvOrderNum;
    private TextView tvAge;

    private TagLayout majorItemsLayout;
    private TagLayout deliveryAreasLayout;
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

    private AddMajorItemButton addMajorItemButton;

    private HashMap<View, View> transformableView;

    private boolean editMode;
    private boolean loadedData;

    private ArrayList<String> majorItems;

    public BusinessInfoContainer(@NonNull Context context) {
        super(context, null);
    }

    public BusinessInfoContainer(@NonNull final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.component_vendor_business_info, this);

        transformableView = new HashMap<>();

        tvPartnerNum = (TextView) findViewById(R.id.tvPartnerNum);
        inputPartnerNum = (InputInfoLayout) findViewById(R.id.inputPartnerNum);
        transformableView.put(tvPartnerNum, inputPartnerNum);

        tvOrderNum = (TextView) findViewById(R.id.tvOrderNum);
        inputOrderNum = (InputInfoLayout) findViewById(R.id.inputOrderNum);
        transformableView.put(tvOrderNum, inputOrderNum);

        tvAge = (TextView) findViewById(R.id.tvAge);
        inputAge = (InputInfoLayout) findViewById(R.id.inputAge);
        transformableView.put(tvAge, inputAge);

        tvClosedDay = (TextView) findViewById(R.id.tvClosedDay);
        inputClosedDay = (InputInfoLayout) findViewById(R.id.inputClosedDay);
        transformableView.put(tvClosedDay, inputClosedDay);

        tvDeliveryTime = (TextView) findViewById(R.id.tvDeliveryTime);
        inputDeliveryTime = (InputInfoLayout) findViewById(R.id.inputDeliveryTime);
        transformableView.put(tvDeliveryTime, inputDeliveryTime);

        tvBusinessLicenseNumber = (TextView) findViewById(R.id.tvBusinessLicenseNumber);
        inputBusinessNumber = (InputInfoLayout) findViewById(R.id.inputBusinessLicenseNumber);
        transformableView.put(tvBusinessLicenseNumber, inputBusinessNumber);

        deliveryAreasLayout = (TagLayout) findViewById(R.id.deliveryAreasLayout);
        inputDeliveryArea = (InputInfoLayout) findViewById(R.id.inputDeliveryArea);
        transformableView.put(deliveryAreasLayout, inputDeliveryArea);

        tvDeliveryArea = (TextView) findViewById(R.id.tvDeliveryArea);
        majorItemsLayout = (TagLayout) findViewById(R.id.majorItemsLayout);

        addMajorItemButton = new AddMajorItemButton(getContext());
        addMajorItemButton.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(addMajorItemListener == null) return;
                if(!loadedData) {
                    Toast.makeText(context, "품목 정보를 불러오는 중입니다.\n잠시만 기다려주세요..", Toast.LENGTH_SHORT).show();
                    return;
                }
                addMajorItemListener.onAddMajorItem(majorItems);
            }
        });
    }

    public void setViewOnEditMode() {
        editMode = true;
        for(Map.Entry<View, View> entry : transformableView.entrySet()) {
            int id = entry.getKey().getId();
            if(id == deliveryAreasLayout.getId()) {
                String deliveryAreas = TransformDataUtil.makeDeliveryAreasString(deliveryAreasLayout.getTagNames());
                ((InputInfoLayout)entry.getValue()).setText(deliveryAreas);
            } else {
                TextView tv = (TextView)entry.getKey();
                InputInfoLayout input = (InputInfoLayout) entry.getValue();

                String text = tv.getText().toString();
                if(id == tvPartnerNum.getId() || id == tvOrderNum.getId() || id == tvAge.getId()) {
                    if(text.length() != 0) {
                        text = text.substring(0,text.length()-1);
                    }
                }
                input.setText(text);
            }
            entry.getKey().setVisibility(View.GONE);
            entry.getValue().setVisibility(View.VISIBLE);
        }
    }

    public void setNormalMode() {
        editMode = false;
        for(Map.Entry<View, View> entry : transformableView.entrySet()) {
            entry.getValue().setVisibility(View.GONE);
            entry.getKey().setVisibility(View.VISIBLE);
        }
    }

    public void setRemoveMajorItemListener(RemoveMajorItemListener listener) {
        this.removeMajorItemListener = listener;
    }

    public void setAddMajorItemListener(AddMajorItemListener listener) {
        this.addMajorItemListener = listener;
    }

    public void setBusinessInfoData(final BusinessInfoData businessInfoData) {
        loadedData = true;

        majorItemsLayout.removeAllViews();
        majorItemsLayout.addView(addMajorItemButton);
        if(businessInfoData.majorItems != null) {
            this.majorItems = businessInfoData.majorItems;
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
                majorItemsLayout.addView(majorItem, majorItemView, 0);
            }
        }

        if ((editMode)) return;

        tvPartnerNum.setText(String.valueOf(businessInfoData.partnerNum)+"개");
        tvOrderNum.setText(String.valueOf(businessInfoData.orderNum)+"회");
        tvAge.setText(String.valueOf(businessInfoData.age)+"년");

        deliveryAreasLayout.removeAllViews();
        if(businessInfoData.deliveryAreas != null) {
            for (String deliveryArea : businessInfoData.deliveryAreas) {
                DeliveryAreaView deliveryAreaView = new DeliveryAreaView(getContext());
                deliveryAreaView.setArea(deliveryArea);
                deliveryAreasLayout.addView(deliveryArea, deliveryAreaView);
            }
        } else {
            deliveryAreasLayout.addView(tvDeliveryArea);
        }

        tvClosedDay.setText(businessInfoData.closedDay);
        tvDeliveryTime.setText(businessInfoData.deliveryTime);
        tvBusinessLicenseNumber.setText(businessInfoData.businessLicenseNumber);
    }

    public BusinessInfoData getNewBusinessInfoData() {
        BusinessInfoData businessInfoData = new BusinessInfoData();

        String partnerNum = inputPartnerNum.getText();
        String orderNum = inputOrderNum.getText();
        String age = inputAge.getText();

        businessInfoData.partnerNum = partnerNum.equals("") ? 0 : Integer.parseInt(partnerNum);
        businessInfoData.orderNum = orderNum.equals("") ? 0 : Integer.parseInt(orderNum);
        businessInfoData.age = age.equals("") ? 0 : Integer.parseInt(age);
        businessInfoData.deliveryAreas = TransformDataUtil.makeDeliveryAreasList(inputDeliveryArea.getText());
        businessInfoData.closedDay = inputClosedDay.getText();
        businessInfoData.deliveryTime = inputDeliveryTime.getText();
        businessInfoData.businessLicenseNumber = inputBusinessNumber.getText();
        businessInfoData.majorItems = majorItemsLayout.getTagNames();

        return businessInfoData;
    }
}
