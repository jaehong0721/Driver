package com.rena21.driver.repository;


import android.content.Context;
import android.os.Handler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.rena21.driver.R;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.network.FileTransferUtil;


public class VendorImageRepository {

    private final Context context;
    private final String phoneNumber;
    private VendorImageData vendorImagesData;

    private final String S3_ADDRESS;
    private final String S3_BUCKET_NAME;
    private AmazonS3 s3;

    public VendorImageRepository(Context context, String phoneNumber) {
        this.context = context;
        this.phoneNumber = phoneNumber;
        this.vendorImagesData = new VendorImageData();

        s3 = FileTransferUtil.getS3Client(context);
        S3_BUCKET_NAME = context.getResources().getString(R.string.s3_bucket_name);
        S3_ADDRESS = context.getResources().getString(R.string.s3_base_address) + "/" + S3_BUCKET_NAME;
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
                for(final S3ObjectSummary file : s3.listObjects(S3_BUCKET_NAME, "image/" + phoneNumber + "/").getObjectSummaries()) {
                    if(file.getKey().equals("image/" + phoneNumber)) continue;
                    handler.post(new Runnable() {
                        @Override public void run() {
                            if(vendorImagesData.getImageUrls().contains(S3_ADDRESS + "/" + file.getKey())) return;
                            vendorImagesData.addImageUrl(S3_ADDRESS + "/" + file.getKey());
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
                s3.deleteObject(new DeleteObjectRequest(S3_BUCKET_NAME, data.substring(data.indexOf("image/" + phoneNumber + "/"))));
            }
        }).start();

        vendorImagesData.removeImageUrl(data);
    }
}
