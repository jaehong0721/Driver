package com.rena21.driver.repository;


import android.content.Context;
import android.os.Handler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.network.FileTransferUtil;


public class VendorImageRepository {

    private final Context context;
    private final String phoneNumber;
    private VendorImageData vendorImagesData;

    private final String S3_ADDRESS = "https://s3.ap-northeast-2.amazonaws.com/thegreatmerchant/";
    private AmazonS3 s3;

    public VendorImageRepository(Context context, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.vendorImagesData = new VendorImageData();

        s3 = FileTransferUtil.getS3Client(context);
    }

    public void saveImage() {
        //원래 이곳에서 s3에 저장을 해줘야함
        //지금은 s3service를 통해 저장이 되고있음
        //지금 상태에선 단순히 reload
        loadImage();
    }

    public VendorImageData loadImage() {
        final Handler handler = new Handler(context.getMainLooper());
        new Thread(new Runnable() {
            @Override public void run() {
                for(final S3ObjectSummary file : s3.listObjects("thegreatmerchant", "image/" + phoneNumber + "/").getObjectSummaries()) {
                    if(file.getKey().equals("image/")) continue;
                    handler.post(new Runnable() {
                        @Override public void run() {
                            if(vendorImagesData.getImageUrls().contains(S3_ADDRESS + file.getKey())) return;
                            vendorImagesData.addImageUrl(S3_ADDRESS + file.getKey());
                        }
                    });
                }
            }
        }).start();

        return vendorImagesData;
    }

    public void removeImage(final String data) {
        new Thread(new Runnable() {
            @Override public void run() {
                s3.deleteObject(new DeleteObjectRequest("thegreatmerchant", data.substring(data.indexOf("image/" + phoneNumber + "/"))));
            }
        }).start();

        vendorImagesData.removeImageUrl(data);
    }
}
