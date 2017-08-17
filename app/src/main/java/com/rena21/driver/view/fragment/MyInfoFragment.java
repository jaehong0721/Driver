package com.rena21.driver.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.activities.MainActivity;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.models.ContactInfoData;
import com.rena21.driver.models.ContainerToObserve;
import com.rena21.driver.models.RankingInfoData;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.services.FileUploadService;
import com.rena21.driver.util.ImagePickUpUtil;
import com.rena21.driver.view.component.BusinessInfoContainer;
import com.rena21.driver.view.component.ContactInfoContainer;
import com.rena21.driver.view.component.RankingInfoContainer;
import com.rena21.driver.view.component.VendorImageContainer;
import com.rena21.driver.viewmodel.MyInfoViewModel;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.app.Activity.RESULT_OK;

public class MyInfoFragment extends Fragment {

    private static final String PHONE_NUMBER = "phoneNumber";
    private static final int PICK_IMAGE_REQUEST = 0;

    private String phoneNumber;

    private MyInfoViewModel myInfoViewModel;
    private BroadcastReceiver fileUploadSuccessReceiver;

    private RankingInfoContainer rankingInfoContainer;
    private ContactInfoContainer contactInfoContainer;
    private VendorImageContainer vendorImageContainer;
    private BusinessInfoContainer businessInfoContainer;
    private RelativeLayout titleBar;
    private ImageView ivClose;
    private ImageView ivEdit;
    private Button btnSaveInfo;

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
        FirebaseDbManager dbManager = ((MainActivity)getActivity()).getDbManager();
        myInfoViewModel = new MyInfoViewModel(getContext(), phoneNumber, dbManager);
        myInfoViewModel.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_info, container, false);

        initRankingInfoContainer(rootView);
        initContactInfoContainer(rootView);
        initVendorImageContainer(rootView);
        initBusinessInfoContainer(rootView);

        titleBar = (RelativeLayout) rootView.findViewById(R.id.titleBar);

        ivClose = (ImageView) rootView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setNormalMode();
                contactInfoContainer.setNormalMode();
                businessInfoContainer.setNormalMode();
            }
        });

        ivEdit = (ImageView) rootView.findViewById(R.id.ivEditMode);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setEditMode();
                contactInfoContainer.setEditMode();
                businessInfoContainer.setViewOnEditMode();
            }
        });

        btnSaveInfo = (Button) rootView.findViewById(R.id.btnSaveInfo);
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContactInfoData newContactInfoData = contactInfoContainer.getNewContactInfoData();
                if(newContactInfoData == null) return;
                myInfoViewModel.saveContactInfoData(newContactInfoData);
                BusinessInfoData newBusinessInfoData = businessInfoContainer.getNewBusinessInfoData();
                myInfoViewModel.saveBusinessInfoData(newBusinessInfoData);

                Toast.makeText(getContext(), "저장중입니다", Toast.LENGTH_SHORT).show();
                setNormalMode();
                contactInfoContainer.setNormalMode();
                businessInfoContainer.setNormalMode();
            }
        });
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

    @Override public void onDestroy() {
        myInfoViewModel.onDestroy();
        super.onDestroy();
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

    private void initContactInfoContainer(View rootView) {
        contactInfoContainer = (ContactInfoContainer) rootView.findViewById(R.id.contactInfoContainer);
        Observer contactInfoObserver = new Observer() {
            @Override public void update(Observable o, Object arg) {
                contactInfoContainer.setContactInfoData(((ContainerToObserve<ContactInfoData>)o).getObject());
            }
        };
        myInfoViewModel.getContactInfoDataContainer().addObserver(contactInfoObserver);
    }

    private void initBusinessInfoContainer(View rootView) {
        businessInfoContainer = (BusinessInfoContainer) rootView.findViewById(R.id.businessInfoContainer);
        businessInfoContainer.setAddMajorItemListener(new BusinessInfoContainer.AddMajorItemListener() {
            @Override public void onAddMajorItem() {
                Toast.makeText(getContext(), "품목 추가!!", Toast.LENGTH_SHORT).show();
            }
        });
        businessInfoContainer.setRemoveMajorItemListener(new BusinessInfoContainer.RemoveMajorItemListener() {
            @Override public void onRemoveMajorItem(List<String> items) {
                myInfoViewModel.removeMajorItem(items);
            }
        });
        Observer businessInfoObserver = new Observer() {
            @Override public void update(Observable o, Object arg) {
                businessInfoContainer.setBusinessInfoData(((ContainerToObserve<BusinessInfoData>)o).getObject());
            }
        };
        myInfoViewModel.getBusinessInfoDataContainer().addObserver(businessInfoObserver);
    }

    private void initRankingInfoContainer(View rootView) {
        rankingInfoContainer = (RankingInfoContainer) rootView.findViewById(R.id.rankingInfoContainer);
        Observer rankingInfoObserver = new Observer() {
            @Override public void update(Observable o, Object arg) {
                rankingInfoContainer.setRankingInfoData(((ContainerToObserve<RankingInfoData>) o).getObject());
            }
        };
        myInfoViewModel.getRankingInfoDataContainer().addObserver(rankingInfoObserver);
    }

    private void setEditMode() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
        titleBar.setVisibility(View.VISIBLE);
        rankingInfoContainer.setVisibility(View.GONE);
        ivEdit.setVisibility(View.GONE);
        btnSaveInfo.setVisibility(View.VISIBLE);
    }

    private void setNormalMode() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        titleBar.setVisibility(View.GONE);
        rankingInfoContainer.setVisibility(View.VISIBLE);
        ivEdit.setVisibility(View.VISIBLE);
        btnSaveInfo.setVisibility(View.GONE);
    }
}
