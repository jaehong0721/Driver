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

    public void saveBusinessInfoData(BusinessInfoData businessInfoData) {
        dbManager.updateBusinessInfoData(businessInfoData);
    }

    public ContainerToObserve<BusinessInfoData> subscribeBusinessInfo() {
        dbManager.subscribeBusinessInfo(this);
        return dataContainer;
    }

    public void cancelSubscription() {
        dbManager.removeBusinessInfoListener(this);
    }

    public void addMajorItem(List<String> items) {
        dbManager.updateMajorItems(items);
    }

    public void removeMajorItem(List<String> items) {
        dbManager.updateMajorItems(items);
    }
}
