package com.mercadopago.model;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by vaserber on 3/13/17.
 */

public class EncryptedCardToken {

    private String cardId;
    private String encryptedCvv;
    private Device device;

    public EncryptedCardToken(String cardId) {
        this.cardId = cardId;
    }

    public String getEncryptedCvv() {
        return encryptedCvv;
    }

    public Device getDevice() {
        return device;
    }

    public String getCardId() {
        return cardId;
    }

    public void setEncryptedCvv(String encryptedCvv) {
        this.encryptedCvv = encryptedCvv;
    }

    public void setDevice(Context context) {
        this.device = new Device(context);
    }

    public boolean validate() {
        return validateCardId() && validateEncryptedCvv();
    }

    public boolean validateCardId() {
        return !TextUtils.isEmpty(cardId) && TextUtils.isDigitsOnly(cardId);
    }

    public boolean validateEncryptedCvv() {
        return !TextUtils.isEmpty(encryptedCvv);
    }
}
