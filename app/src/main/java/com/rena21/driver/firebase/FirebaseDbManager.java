package com.rena21.driver.firebase;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDbManager {

    private final FirebaseDatabase instance;

    public FirebaseDbManager(FirebaseDatabase instance) {
        this.instance = instance;
    }

    public void getRestaurantName(String restaurantPhoneNumber, ValueEventListener listener) {
        instance.getReference().child("restaurants")
                .child(restaurantPhoneNumber)
                .child("info")
                .child("restaurantName")
                .addListenerForSingleValueEvent(listener);
    }
}
