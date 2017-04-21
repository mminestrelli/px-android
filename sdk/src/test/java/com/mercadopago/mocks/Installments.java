package com.mercadopago.mocks;

import com.mercadopago.model.Installment;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

public class Installments {
    private Installments(){}

    public static Installment getInstallment() {
        String json = ResourcesUtil.getStringResource("installments.json");
        return JsonUtil.getInstance().fromJson(json, Installment.class);
    }
}
