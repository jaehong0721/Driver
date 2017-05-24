package com.rena21.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rena21.driver.R;
import com.rena21.driver.listener.ModifyFinishedListener;


public class DeliveryModifyFragment extends Fragment {

    private static final String FILE_NAME = "fileName";

    private String fileName;

    private ModifyFinishedListener finishedListener;

    public DeliveryModifyFragment() {}

    public static DeliveryModifyFragment newInstance(String param1) {
        DeliveryModifyFragment fragment = new DeliveryModifyFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ModifyFinishedListener) {
            finishedListener = (ModifyFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnModifyFinishedListener");
        }
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

        return inflater.inflate(R.layout.fragment_delivery_modify, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        finishedListener = null;
    }
}
