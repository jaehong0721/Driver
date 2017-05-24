package com.rena21.driver.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.activities.DeliveryDetailActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.PaymentFinishedListener;


public class PaymentFragment extends Fragment {

    private static final String FILE_NAME = "fileName";

    private String fileName;

    private int totalPrice;

    private FirebaseDbManager dbManager;

    private PaymentFinishedListener finishedListener;

    private TextView tvTotalPrice;
    private EditText etDeposit;
    private Button btnSave;

    public PaymentFragment() {}

    public static PaymentFragment newInstance(String param1) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(FILE_NAME, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PaymentFinishedListener) {
            finishedListener = (PaymentFinishedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PaymentFinishedListener");
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

        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);
        etDeposit = (EditText) view.findViewById(R.id.etDeposit);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int totalDeposit = Integer.parseInt(etDeposit.getText().toString());
                if(totalDeposit > totalPrice) {  //비동기에 따른 문제 발생 가능성
                    Toast.makeText(getContext(), "총 납품금액보다 큰 금액을 수금할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                finishedListener.onPaymentFinished(fileName,totalDeposit);
            }
        });

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbManager = ((DeliveryDetailActivity)getActivity()).getDbManager();

        dbManager.getTotalPrice(fileName, new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                totalPrice = dataSnapshot.getValue(Integer.class);
                tvTotalPrice.setText(totalPrice + "원");
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                Log.e("", "error: " + databaseError.toString());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        finishedListener = null;
    }
}
