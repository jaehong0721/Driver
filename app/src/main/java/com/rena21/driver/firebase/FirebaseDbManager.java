package com.rena21.driver.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.models.BusinessInfoData;
import com.rena21.driver.models.ContactInfoData;

import java.util.HashMap;
import java.util.List;

public class FirebaseDbManager {

    private static final String VENDORS = "vendors";
    private static final String INFO = "info";
    private static final String RESTAURANTS = "restaurants";
    private static final String RESTAURANT_NAME = "restaurantName";
    private static final String ORDERS = "orders";
    private static final String ACCEPTED = "accepted";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String DELIVERED_TIME = "deliveredTime";
    private static final String BUSINESS_INFO = "businessInfo";
    private static final String MAJOR_ITEMS = "majorItems";
    private static final String RANKING_INFO = "rankingInfo";
    private static final String CATEGORY = "category";
    private static final String LARGE = "large";
    private static final String MIDDLE = "middle";

    private final FirebaseDatabase instance;
    private final String vendorPhoneNumber;


    public FirebaseDbManager(FirebaseDatabase instance, String vendorPhoneNumber) {
        this.instance = instance;
        this.vendorPhoneNumber = vendorPhoneNumber;
        instance.setPersistenceEnabled(true);
    }

    public void getVendorInfoDataSnapshot(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void getRestaurantName(String restaurantPhoneNumber, ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(RESTAURANTS)
                .child(restaurantPhoneNumber)
                .child(INFO)
                .child(RESTAURANT_NAME);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void getOrderAccepted(String fileName, ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(fileName)
                .child(ACCEPTED);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void getTotalPrice(String fileName, ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(fileName)
                .child(TOTAL_PRICE);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void multiPathUpdateValue(HashMap<String, Object> pathMap, OnCompleteListener<Void> listener) {
        DatabaseReference dr = getRootRef();
        dr.keepSynced(true);
        dr.updateChildren(pathMap).addOnCompleteListener(listener);
    }

    public void addChildEventListenerToOrdersRef(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);
        dr.keepSynced(true);
        dr.addChildEventListener(listener);
    }

    public void removeChildEventListenerFromOrderRef(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);
        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    public void addValueEventListenerToSpecificOrderRef(String orderKey, ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);
        dr.keepSynced(true);
        dr.child(orderKey)
                .addValueEventListener(listener);
    }

    public void removeValueEventListenerFromSpecificOrderRef(String orderKey, ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);

        dr.keepSynced(true);
        dr.child(orderKey)
                .removeEventListener(listener);
    }

    public void addChildEventListenerToOrderRefOnSpecificDate(long dateMillis, ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);

        dr.keepSynced(true);

        dr.orderByChild(DELIVERED_TIME)
                .equalTo(dateMillis)
                .addChildEventListener(listener);
    }

    public void removeChildEventListenerFromOrderRefOnSpecificDate(long dateMillis, ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber);

        dr.keepSynced(true);

        dr.orderByChild(DELIVERED_TIME)
                .equalTo(dateMillis)
                .removeEventListener(listener);
    }

    public void updateBusinessInfoData(BusinessInfoData businessInfoData) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO);
        dr.keepSynced(true);
        dr.setValue(businessInfoData);
    }

    public void subscribeBusinessInfo(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO);
        dr.keepSynced(true);
        dr.addValueEventListener(listener);
    }

    public void removeBusinessInfoListener(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO);
        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    public void updateMajorItems(List<String> items) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO)
                .child(MAJOR_ITEMS);
        dr.keepSynced(true);
        dr.setValue(items);
    }

    public void updateContactInfoData(ContactInfoData contactInfoData) {
        HashMap<String, Object> pathMap = new HashMap<>();
        pathMap.put("/address/", contactInfoData.address);
        pathMap.put("/phoneNumber/", contactInfoData.phoneNumber);
        pathMap.put("/vendorName/", contactInfoData.vendorName);

        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO);
        dr.keepSynced(true);
        dr.updateChildren(pathMap);
    }

    public void subscribeContactInfo(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO);
        dr.keepSynced(true);
        dr.addValueEventListener(listener);
    }

    public void removeContactInfoListener(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO);
        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    public void subscribeRankingInfo(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(RANKING_INFO);
        dr.keepSynced(true);
        dr.addValueEventListener(listener);
    }

    public void removeRankingInfoListener(ValueEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(RANKING_INFO);
        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    public void getLargeCategoryList(ValueEventListener listener) {
        DatabaseReference dr = getRootRef().child(CATEGORY).child(LARGE);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void getMiddleCategoryList(ToastErrorHandlingListener listener) {
        DatabaseReference dr = getRootRef().child(CATEGORY).child(MIDDLE);
        dr.keepSynced(true);
        dr.addListenerForSingleValueEvent(listener);
    }

    public void subscribeAllEstimate(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child("estimate")
                .child(RESTAURANTS);

        dr.keepSynced(true);
        dr.addChildEventListener(listener);
    }

    public void removeAllEstimateListener(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child("estimate")
                .child(RESTAURANTS);

        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    public void subscribeMyReply(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child("estimate")
                .child(VENDORS)
                .child(vendorPhoneNumber);

        dr.keepSynced(true);
        dr.addChildEventListener(listener);
    }

    public void removeMyReplyListener(ChildEventListener listener) {
        DatabaseReference dr = getRootRef()
                .child("estimate")
                .child(VENDORS)
                .child(vendorPhoneNumber);

        dr.keepSynced(true);
        dr.removeEventListener(listener);
    }

    private DatabaseReference getRootRef() {
        return instance.getReference();
    }
}
