package com.mercadopago.mocks;

import com.mercadopago.model.Installment;
import com.mercadopago.model.PayerCost;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

/**
 * Created by mreverter on 4/20/17.
 */
public class Installments {
    private Installments(){}

    public static Installment getInstallments() {
        String json = ResourcesUtil.getStringResource("installments.json");
        return JsonUtil.getInstance().fromJson(json, Installment.class);
    }
}
