package com.mercadopago.mocks;

import com.mercadopago.model.Payment;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

public class Payments {
    public static Payment getApprovedPayment() {
        String json = ResourcesUtil.getStringResource("approved_payment.json");
        return JsonUtil.getInstance().fromJson(json, Payment.class);
    }
}
