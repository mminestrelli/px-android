package com.mercadopago.providers;

import android.content.Context;

import com.mercadopago.R;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.core.MercadoPagoServices;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Issuer;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;

import java.util.List;

/**
 * Created by mromar on 4/26/17.
 */

public class IssuersProviderImpl implements IssuersProvider {

    private final Context context;
    private final MercadoPagoServices mercadoPago;

    public IssuersProviderImpl(Context context, String publicKey, String privateKey) {
        this.context = context;

        this.mercadoPago = new MercadoPagoServices.Builder()
                .setContext(context)
                .setPublicKey(publicKey)
                .setPrivateKey(privateKey)
                .build();
    }

    @Override
    public void getIssuers(String paymentMethodId, String bin, final OnResourcesRetrievedCallback<List<Issuer>> onResourcesRetrievedCallback) {
        mercadoPago.getIssuers(paymentMethodId, bin, new Callback<List<Issuer>>() {
            @Override
            public void success(List<Issuer> issuers) {
                onResourcesRetrievedCallback.onSuccess(issuers);
            }

            @Override
            public void failure(ApiException apiException) {
                onResourcesRetrievedCallback.onFailure(new MercadoPagoError(apiException));
            }
        });
    }

    @Override
    public MercadoPagoError getEmptyIssuersError() {
        String message = context.getString(R.string.mpsdk_standard_error_message);
        String detail = context.getString(R.string.mpsdk_error_message_detail_issuers);

        return new MercadoPagoError(message, detail, false);
    }

    @Override
    public String getCardIssuersTitle() {
        return context.getString(R.string.mpsdk_card_issuers_title);
    }
}
