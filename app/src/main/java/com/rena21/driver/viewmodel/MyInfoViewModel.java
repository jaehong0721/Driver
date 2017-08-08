package com.rena21.driver.viewmodel;


import android.content.Context;

import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.repository.VendorImageRepository;

public class MyInfoViewModel {

    private VendorImageRepository vendorImageRepository;

    private VendorImageData vendorImagesData;

    private String phoneNumber;

    public MyInfoViewModel(Context context, String phoneNumber) {
        this.phoneNumber = phoneNumber;

        this.vendorImageRepository = new VendorImageRepository(context, phoneNumber);
        this.vendorImagesData = vendorImageRepository.loadImage();
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
}
