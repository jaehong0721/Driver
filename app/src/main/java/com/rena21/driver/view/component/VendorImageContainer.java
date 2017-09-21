package com.rena21.driver.view.component;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.models.VendorImageData;
import com.rena21.driver.view.adapter.VendorImageAdapter;

import java.util.Observable;


public class VendorImageContainer extends FrameLayout {

    public interface addPhotoListener {
        void onAddPhoto();
    }

    public interface RemovePhotoListener {
        void onRemovePhoto(String imageUrl);
    }

    public static final int MAX_IMAGE_NUM = 3;

    private LinearLayout emptyView;
    private ViewPagerWithIndicator viewPagerWithIndicator;
    private ImageView ivAddPhoto;
    private ImageView ivRemovePhoto;

    private VendorImageAdapter vendorImageAdapter;

    public VendorImageContainer(Context context) {
        this(context, null);
    }

    public VendorImageContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.component_vendor_images, this);

        emptyView = (LinearLayout) view.findViewById(R.id.emptyView);
        viewPagerWithIndicator = (ViewPagerWithIndicator) view.findViewById(R.id.imageViewPager);
        ivAddPhoto = (ImageView) view.findViewById(R.id.ivAddPhoto);
        ivRemovePhoto = (ImageView) view.findViewById(R.id.ivRemovePhoto);

        vendorImageAdapter = new VendorImageAdapter((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public void setAddPhotoListener(final addPhotoListener addPhotoListener) {
        ivAddPhoto.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if(vendorImageAdapter.getCount() == MAX_IMAGE_NUM) {
                    Toast.makeText(ivAddPhoto.getContext(), "사진은 최대 3장까지 저장 가능합니다", Toast.LENGTH_SHORT).show();
                } else {
                    addPhotoListener.onAddPhoto();
                }
            }
        });
    }

    public void setRemovePhotoListener(final RemovePhotoListener removePhotoListener) {
        ivRemovePhoto.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                try {
                    String imageUrlToRemove = vendorImageAdapter.getCurrentImageUrl(viewPagerWithIndicator.getCurrentItem());
                    removePhotoListener.onRemovePhoto(imageUrlToRemove);
                } catch (IndexOutOfBoundsException e) {}
            }
        });
    }

    public void setVendorImagesData(VendorImageData vendorImagesData) {
        vendorImageAdapter.setVendorImages(vendorImagesData.getImageUrls());
        viewPagerWithIndicator.setAdapter(vendorImageAdapter);
    }

    public void addedImage(Observable o) {
        if(((VendorImageData)o).getImageUrls().size() == 1) showImageView();
        vendorImageAdapter.updateVendorImages();
    }

    public void removedImage(Observable o) {
        if(((VendorImageData)o).getImageUrls().size() == 0) showEmptyView();
        vendorImageAdapter.updateVendorImages();
    }

    private void showEmptyView() {
        emptyView.setVisibility(VISIBLE);
        ivRemovePhoto.setVisibility(View.GONE);
        viewPagerWithIndicator.setVisibility(GONE);
    }

    private void showImageView() {
        viewPagerWithIndicator.setVisibility(VISIBLE);
        ivRemovePhoto.setVisibility(View.VISIBLE);
        emptyView.setVisibility(GONE);
    }
}
