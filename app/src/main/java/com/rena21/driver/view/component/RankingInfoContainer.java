package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rena21.driver.R;
import com.rena21.driver.models.RankingInfoData;

public class RankingInfoContainer extends FrameLayout {

    private final TextView tvRank;
    private final TextView tvVisitCount;

    public RankingInfoContainer(@NonNull Context context) {
        this(context, null);
    }

    public RankingInfoContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.component_vendor_ranking_info, this);

        tvRank = (TextView) findViewById(R.id.tvRank);
        tvVisitCount = (TextView) findViewById(R.id.tvVisitCount);
    }

    public void setRankingInfoData(RankingInfoData rankingInfoData) {
        String rank = String.valueOf(rankingInfoData.rank);
        String visitCount = String.valueOf(rankingInfoData.visitCount);

        rank = rank.equals("0") ? "???" : rank+"위";
        visitCount = visitCount.equals("0") ? "???" : visitCount+"명";

        tvRank.setText(rank);
        tvVisitCount.setText(visitCount);
    }
}
