package com.rena21.driver.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rena21.driver.R;


public class DeliverySummaryFragment extends Fragment {

    private static final String FILE_NAME = "fileName";

    private String fileName;

    public DeliverySummaryFragment() {}

    public static DeliverySummaryFragment newInstance(String param1) {
        DeliverySummaryFragment fragment = new DeliverySummaryFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(FILE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_delivery_summary, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
