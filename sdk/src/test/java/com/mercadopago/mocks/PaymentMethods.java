package com.mercadopago.mocks;

import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

/**
 * Created by mreverter on 4/20/17.
 */

public class PaymentMethods {
    private PaymentMethods() {}

    public static PaymentMethod getPaymentMethodOn() {
        String json = ResourcesUtil.getStringResource("payment_method_visa.json");
        return JsonUtil.getInstance().fromJson(json, PaymentMethod.class);
    }

    public static PaymentMethod getPaymentMethodOff() {
        String json = ResourcesUtil.getStringResource("payment_method_pagofacil.json");
        return JsonUtil.getInstance().fromJson(json, PaymentMethod.class);
    }
}
