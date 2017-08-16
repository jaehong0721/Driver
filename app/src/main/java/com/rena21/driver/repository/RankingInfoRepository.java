package com.rena21.driver.repository;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.ContainerToObserve;
import com.rena21.driver.models.RankingInfoData;

public class RankingInfoRepository implements ValueEventListener{
    private ContainerToObserve<RankingInfoData> dataContainer;
    private FirebaseDbManager dbManager;

    public RankingInfoRepository(FirebaseDbManager dbManager) {
        this.dataContainer = new ContainerToObserve<>();
        this.dbManager = dbManager;
    }

    public ContainerToObserve<RankingInfoData> subscribeRankingInfo() {
        dbManager.subscribeRankingInfo(this);
        return dataContainer;
    }

    public void cancelSubscription() {
        dbManager.removeRankingInfoListener(this);
    }

    public void saveRankingInfoData(RankingInfoData rankingInfoData) {
        dbManager.updateRankingInfo(rankingInfoData);
    }

    @Override public void onDataChange(DataSnapshot dataSnapshot) {
        RankingInfoData rankingInfoData = new RankingInfoData();

        if(dataSnapshot.exists()) {
            rankingInfoData = dataSnapshot.getValue(RankingInfoData.class);
        }

        dataContainer.setObject(rankingInfoData);
    }

    @Override public void onCancelled(DatabaseError databaseError) {
        Log.d("test", databaseError.getMessage());
    }
}
