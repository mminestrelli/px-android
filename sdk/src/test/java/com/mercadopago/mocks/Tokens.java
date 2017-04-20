package com.mercadopago.mocks;

import com.mercadopago.model.Token;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.utils.ResourcesUtil;

/**
 * Created by mreverter on 4/20/17.
 */
public class Tokens {
    private Tokens() {}

    public static Token getVisaToken() {
        String json = ResourcesUtil.getStringResource("token_visa.json");
        return JsonUtil.getInstance().fromJson(json, Token.class);
    }
}
