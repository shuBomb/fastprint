package com.app.fastprint.Hyper.task;


public interface PaymentStatusRequestListener {

    void onErrorOccurred();
    void onPaymentStatusReceived(Boolean paymentStatus);
}
