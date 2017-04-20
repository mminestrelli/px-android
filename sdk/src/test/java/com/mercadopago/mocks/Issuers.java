package com.mercadopago.mocks;

import com.google.gson.reflect.TypeToken;

import com.mercadopago.model.Issuer;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

import java.lang.reflect.Type;
import java.util.List;

public class Issuers {
    private Issuers() {}

    public static List<Issuer> getIssuers() {
        String json = ResourcesUtil.getStringResource("issuers.json");
        Type listType = new TypeToken<List<Issuer>>() {
        }.getType();
        return JsonUtil.getInstance().getGson().fromJson(json, listType);
    }
}
