package com.rena21.driver.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.services.FileUploadService;
import com.rena21.driver.util.ImagePickUpUtil;
import com.rena21.driver.view.component.VendorImageContainer;
import com.rena21.driver.viewmodel.MyInfoViewModel;

import java.util.Observable;
import java.util.Observer;

import static android.app.Activity.RESULT_OK;

public class MyInfoFragment extends Fragment {

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final int PICK_IMAGE_REQUEST = 0;

    private String phoneNumber;

    private MyInfoViewModel myInfoViewModel;
    private BroadcastReceiver fileUploadSuccessReceiver;

    private VendorImageContainer vendorImageContainer;

    public MyInfoFragment() {}

    public static MyInfoFragment newInstance(String phoneNumber) {
        MyInfoFragment fragment = new MyInfoFragment();
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(PHONE_NUMBER);
        }

        fileUploadSuccessReceiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                String file = intent.getStringExtra("file");
                boolean success = intent.getBooleanExtra("success", false);
                if(success) myInfoViewModel.addVendorImage();
            }
        };
        myInfoViewModel = new MyInfoViewModel(getContext(), phoneNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_info, container, false);
        initVendorImageContainer(rootView);
        return rootView;
    }

    @Override public void onStart() {
        super.onStart();
        getContext().registerReceiver(fileUploadSuccessReceiver, new IntentFilter("com.rena21.driver.ACTION_UPLOAD"));
    }

    @Override public void onStop() {
        getContext().unregisterReceiver(fileUploadSuccessReceiver);
        super.onStop();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_REQUEST:
                if(resultCode == RESULT_OK) {
                    boolean result = ImagePickUpUtil.saveImageFileFromResult(getContext(), data);
                    if(result) {
                        Intent intent = new Intent(getContext(), FileUploadService.class);
                        getContext().startService(intent);
                        Toast.makeText(getContext(), "사진을 저장 중입니다", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "사진 저장에 실패하였습니다", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                Log.d("test", "사진 저장 실패");
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void initVendorImageContainer(View rootView) {
        vendorImageContainer = (VendorImageContainer) rootView.findViewById(R.id.vendorImageContainer);
        vendorImageContainer.setVendorImagesData(myInfoViewModel.getVendorImagesData());
        vendorImageContainer.setAddPhotoListener(new VendorImageContainer.addPhotoListener() {
            @Override public void onAddPhoto() {
                Intent chooseImageIntent = ImagePickUpUtil.getPickImageIntent(getContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            }
        });
        vendorImageContainer.setRemovePhotoListener(new VendorImageContainer.RemovePhotoListener() {
            @Override public void onRemovePhoto(String imageUrl) {
                myInfoViewModel.removeVendorImage(imageUrl);
            }
        });
        Observer vendorImagesObserver = new Observer() {
            @Override public void update(Observable o, Object arg) {
                switch ((VendorImageData.RESULT)arg) {
                    case ADDED:
                        vendorImageContainer.addedImage(o);
                        break;

                    case REMOVED:
                        vendorImageContainer.removedImage(o);
                        break;
                }
            }
        };
        myInfoViewModel.getVendorImagesData().addObserver(vendorImagesObserver);
    }
}
