package com.rena21.driver.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseDbManager {

    private final FirebaseDatabase instance;
    private final String vendorPhoneNumber;


    public FirebaseDbManager(FirebaseDatabase instance, String vendorPhoneNumber) {
        this.instance = instance;
        this.vendorPhoneNumber = vendorPhoneNumber;
    }

    private DatabaseReference getSynchronizedAllOrdersRef() {
        DatabaseReference orderRef = instance.getReference("orders")
                                            .child("vendors")
                                            .child(vendorPhoneNumber);
        orderRef.keepSynced(true);
        return orderRef;
    }

    private DatabaseReference getSpecificOrderRef(String fileName) {
        return instance.getReference("orders")
                .child("vendors")
                .child(vendorPhoneNumber)
                .child(fileName);
    }

    public void getRestaurantName(String restaurantPhoneNumber, ValueEventListener listener) {
        instance.getReference().child("restaurants")
                .child(restaurantPhoneNumber)
                .child("info")
                .child("restaurantName")
                .addListenerForSingleValueEvent(listener);
    }

    public void getOrderAccepted(String fileName, ValueEventListener listener) {
        instance.getReference("orders")
                .child("vendors")
                .child(vendorPhoneNumber)
                .child(fileName)
                .child("accepted")
                .addListenerForSingleValueEvent(listener);
    }

    public void multiPathUpdateValue(HashMap<String, Object> pathMap, OnCompleteListener<Void> listener) {
        instance.getReference()
                .updateChildren(pathMap)
                .addOnCompleteListener(listener);
    }

    public void addChildEventListenerToOrdersRef(ChildEventListener listener) {
        getSynchronizedAllOrdersRef().addChildEventListener(listener);
    }

    public void removeChildEventListenerFromOrderRef(ChildEventListener listener) {
        getSynchronizedAllOrdersRef().removeEventListener(listener);
    }

    public void addValueEventListenerToSpecificOrderRef(String orderKey, ValueEventListener listener) {
        getSpecificOrderRef(orderKey).addValueEventListener(listener);
    }

    public void removeValueEventListenerFromSpecificOrderRef(String orderKey, ValueEventListener listener) {
        getSpecificOrderRef(orderKey).removeEventListener(listener);
    }
}
