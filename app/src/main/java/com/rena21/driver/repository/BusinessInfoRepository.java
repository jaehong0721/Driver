package com.rena21.driver.repository;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.models.ContainerToObserve;

import java.util.List;

public class BusinessInfoRepository implements ValueEventListener {

    private ContainerToObserve<BusinessInfoData> dataContainer;
    private FirebaseDbManager dbManager;

    public BusinessInfoRepository(FirebaseDbManager dbManager) {
        this.dataContainer = new ContainerToObserve<>();
        this.dbManager = dbManager;
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        BusinessInfoData businessInfoData = new BusinessInfoData();

        if(dataSnapshot.exists()) {
            businessInfoData = dataSnapshot.getValue(BusinessInfoData.class);
        }

        dataContainer.setObject(businessInfoData);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.d("test", databaseError.getMessage());
    }

    public ContainerToObserve<BusinessInfoData> subscribeBusinessInfo(String phoneNumber) {
        dbManager.subscribeBusinessInfo(phoneNumber, this);
        return dataContainer;
    }

    public void cancelSubscription(String phoneNumber) {
        dbManager.removeBusinessInfoListener(phoneNumber, this);
    }

    public void removeMajorItem(String phoneNumber, List<String> items) {
        dbManager.updateMajorItems(phoneNumber, items);
    }
}
