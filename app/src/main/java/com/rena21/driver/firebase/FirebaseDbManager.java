package com.rena21.driver.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private final FirebaseDatabase instance;
    private final String vendorPhoneNumber;


    public FirebaseDbManager(FirebaseDatabase instance, String vendorPhoneNumber) {
        this.instance = instance;
        this.vendorPhoneNumber = vendorPhoneNumber;
        instance.setPersistenceEnabled(true);
        getRootRef().keepSynced(true);
    }

    public void getVendorInfoDataSnapshot(ValueEventListener listener) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO)
                .addListenerForSingleValueEvent(listener);
    }

    public void getRestaurantName(String restaurantPhoneNumber, ValueEventListener listener) {
        getRootRef()
                .child(RESTAURANTS)
                .child(restaurantPhoneNumber)
                .child(INFO)
                .child(RESTAURANT_NAME)
                .addListenerForSingleValueEvent(listener);
    }

    public void getOrderAccepted(String fileName, ValueEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(fileName)
                .child(ACCEPTED)
                .addListenerForSingleValueEvent(listener);
    }

    public void getTotalPrice(String fileName, ValueEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(fileName)
                .child(TOTAL_PRICE)
                .addListenerForSingleValueEvent(listener);
    }

    public void multiPathUpdateValue(HashMap<String, Object> pathMap, OnCompleteListener<Void> listener) {
       getRootRef()
                .updateChildren(pathMap)
                .addOnCompleteListener(listener);
    }

    public void addChildEventListenerToOrdersRef(ChildEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .addChildEventListener(listener);
    }

    public void removeChildEventListenerFromOrderRef(ChildEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .removeEventListener(listener);
    }

    public void addValueEventListenerToSpecificOrderRef(String orderKey, ValueEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(orderKey)
                .addValueEventListener(listener);
    }

    public void removeValueEventListenerFromSpecificOrderRef(String orderKey, ValueEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(orderKey)
                .removeEventListener(listener);
    }

    public void addChildEventListenerToOrderRefOnSpecificDate(long dateMillis, ChildEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .orderByChild(DELIVERED_TIME)
                .equalTo(dateMillis)
                .addChildEventListener(listener);
    }

    public void removeChildEventListenerFromOrderRefOnSpecificDate(long dateMillis, ChildEventListener listener) {
        getRootRef()
                .child(ORDERS)
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .orderByChild(DELIVERED_TIME)
                .equalTo(dateMillis)
                .removeEventListener(listener);
    }

    public void updateBusinessInfoData(BusinessInfoData businessInfoData) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO)
                .setValue(businessInfoData);
    }

    public void subscribeBusinessInfo(ValueEventListener listener) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO)
                .addValueEventListener(listener);
    }

    public void removeBusinessInfoListener(ValueEventListener listener) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO)
                .removeEventListener(listener);
    }

    public void updateMajorItems(List<String> items) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(BUSINESS_INFO)
                .child(MAJOR_ITEMS)
                .setValue(items);
    }

    public void updateContactInfoData(ContactInfoData contactInfoData) {
        HashMap<String,Object> pathMap = new HashMap<>();
        pathMap.put("/address/", contactInfoData.address);
        pathMap.put("/phoneNumber/", contactInfoData.phoneNumber);
        pathMap.put("/vendorName/", contactInfoData.vendorName);

        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO)
                .updateChildren(pathMap);
    }

    public void subscribeContactInfo(ValueEventListener listener) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO)
                .addValueEventListener(listener);
    }

    public void removeContactInfoListener(ValueEventListener listener) {
        getRootRef()
                .child(VENDORS)
                .child(vendorPhoneNumber)
                .child(INFO)
                .removeEventListener(listener);
    }

    private DatabaseReference getRootRef() {
        return instance.getReference();
    }
}
