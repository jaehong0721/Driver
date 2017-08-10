package com.rena21.driver.repository;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.ContactInfoData;
import com.rena21.driver.models.ContainerToObserve;

public class ContactInfoRepository implements ValueEventListener{
    private ContainerToObserve<ContactInfoData> dataContainer;
    private FirebaseDbManager dbManager;

    public ContactInfoRepository(FirebaseDbManager dbManager) {
        this.dataContainer = new ContainerToObserve<>();
        this.dbManager = dbManager;

    }

    public ContainerToObserve<ContactInfoData> subscribeContactInfo() {
        dbManager.subscribeContactInfo(this);
        return dataContainer;
    }

    public void cancelSubscription() {
        dbManager.removeContactInfoListener(this);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        ContactInfoData contactInfoData = new ContactInfoData();

        if(dataSnapshot.exists()) {
            contactInfoData = dataSnapshot.getValue(ContactInfoData.class);
        }

        dataContainer.setObject(contactInfoData);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.d("test", databaseError.getMessage());
    }
}
