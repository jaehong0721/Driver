package com.rena21.driver.listener;


public interface PaymentFinishedListener {
    void onPaymentFinished(String fileName, int totalDeposit);
}
