package com.rena21.driver.viewmodel;


import android.content.Context;

import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.models.ContactInfoData;
import com.rena21.driver.models.ContainerToObserve;
import com.rena21.driver.models.RankingInfoData;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.repository.BusinessInfoRepository;
import com.rena21.driver.repository.ContactInfoRepository;
import com.rena21.driver.repository.RankingInfoRepository;
import com.rena21.driver.repository.VendorImageRepository;

import java.util.List;

public class MyInfoViewModel {

    private final Context context;

    private RankingInfoRepository rankingInfoRepository;
    private VendorImageRepository vendorImageRepository;
    private ContactInfoRepository contactInfoRepository;
    private BusinessInfoRepository businessInfoRepository;

    private VendorImageData vendorImagesData;
    private ContainerToObserve<RankingInfoData> rankingInfoDataContainer;
    private ContainerToObserve<ContactInfoData> contactInfoDataContainer;
    private ContainerToObserve<BusinessInfoData> businessInfoDataContainer;

    private final FirebaseDbManager dbManager;

    private String phoneNumber;

    public MyInfoViewModel(Context context, String phoneNumber, FirebaseDbManager dbManager) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.dbManager = dbManager;
    }

    public void onCreate() {
        this.vendorImageRepository = new VendorImageRepository(context, phoneNumber);
        this.vendorImagesData = vendorImageRepository.loadImage();

        this.rankingInfoRepository = new RankingInfoRepository(dbManager);
        this.rankingInfoDataContainer = rankingInfoRepository.subscribeRankingInfo();

        this.contactInfoRepository = new ContactInfoRepository(dbManager);
        this.contactInfoDataContainer = contactInfoRepository.subscribeContactInfo();

        this.businessInfoRepository = new BusinessInfoRepository(dbManager);
        this.businessInfoDataContainer = businessInfoRepository.subscribeBusinessInfo();
    }

    public void onDestroy() {
        contactInfoRepository.cancelSubscription();
        businessInfoRepository.cancelSubscription();
        rankingInfoRepository.cancelSubscription();
    }

    public VendorImageData getVendorImagesData() {
        return this.vendorImagesData;
    }
    public void addVendorImage() {
        vendorImageRepository.saveImage();
    }
    public void removeVendorImage(String imageUrl) {
        vendorImageRepository.removeImage(imageUrl);
    }

    public void saveContactInfoData(ContactInfoData contactInfoData) {
        contactInfoRepository.saveContactInfoData(contactInfoData);
    }
    public ContainerToObserve getContactInfoDataContainer() {
        return contactInfoDataContainer;
    }

    public void saveBusinessInfoData(BusinessInfoData businessInfoData) {
        businessInfoRepository.saveBusinessInfoData(businessInfoData);
    }
    public ContainerToObserve getBusinessInfoDataContainer() {
        return businessInfoDataContainer;
    }
    public void addMajorItem(List<String> items) {
        businessInfoRepository.addMajorItem(items);
    }
    public void removeMajorItem(List<String> items) {
        businessInfoRepository.removeMajorItem(items);
    }

    public ContainerToObserve getRankingInfoDataContainer() {
        return rankingInfoDataContainer;
    }
}
