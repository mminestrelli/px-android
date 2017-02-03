package com.mercadopago.examples.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.examples.services.step1.CardActivity;
import com.mercadopago.examples.services.step2.SimpleVaultActivity;
import com.mercadopago.examples.services.step3.AdvancedVaultActivity;
import com.mercadopago.examples.services.step4.FinalVaultActivity;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class ExamplesUtils {

    public static final int SIMPLE_VAULT_REQUEST_CODE = 10;
    public static final int ADVANCED_VAULT_REQUEST_CODE = 11;
    public static final int FINAL_VAULT_REQUEST_CODE = 12;
    public static final int CARD_REQUEST_CODE = 13;

    // * Merchant public key
    public static final String DUMMY_MERCHANT_PUBLIC_KEY = "APP_USR-0aa4e281-002e-4666-a1cd-95df12595014";

    public static final String DUMMY_MERCHANT_PUBLIC_KEY_EXAMPLES_SERVICE = "APP_USR-0aa4e281-002e-4666-a1cd-95df12595014";
    // DUMMY_MERCHANT_PUBLIC_KEY_AR = "444a9ef5-8a6b-429f-abdf-587639155d88";
    // DUMMY_MERCHANT_PUBLIC_KEY_BR = "APP_USR-f163b2d7-7462-4e7b-9bd5-9eae4a7f99c3";
    // DUMMY_MERCHANT_PUBLIC_KEY_MX = "6c0d81bc-99c1-4de8-9976-c8d1d62cd4f2";
    // DUMMY_MERCHANT_PUBLIC_KEY_VZ = "2b66598b-8b0f-4588-bd2f-c80ca21c6d18";
    // DUMMY_MERCHANT_PUBLIC_KEY_CO = "aa371283-ad00-4d5d-af5d-ed9f58e139f1";

    // SANDBOX_PUBLIC_KEY_MPE = "TEST-540bb6d1-44d5-4607-92bf-3075023f53a0";
    // PRODUCTION_PUBLIC_KEY_MPE = "APP_USR-9fc1aacb-3558-421e-a7cc-b9ddc9a8d48a";
    //CHECKOUT_PREFERENCES_ID_MPE = "241958103-0ef22ce4-94ba-4ad0-a457-64569917360d";
    // SANDBOX_PUBLIC_KEY_MLU = "TEST-e7b54e3e-f1a7-4518-868d-4a3e6d8b0b30";
    // PRODUCTION_PUBLIC_KEY_MLU = "APP_USR-83c6e8f7-3b0e-4290-96a1-c222560a891c"
    //CHECKOUT_PREFERENCES_ID_MLU = “241113255-3b3d87e3-3817-48ae-8d2a-2bd0df1e2e22";
    // SANDBOX_PUBLIC_KEY_MCO = "TEST-d661b2f9-3241-421d-bd9e-7d1a42b27d1c";
    // PRODUCTION_PUBLIC_KEY_MCO = "APP_USR-399e7628-3d81-423f-a7cf-5ad8341eb06e";
    //CHECKOUT_PREFERENCES_ID_MCO = "241110424-00be5088-ad07-4e2d-bfc9-78f4bffbc3c7";
    // SANDBOX_PUBLIC_KEY_MLV = "TEST-d5639c69-1b7f-4777-9ed0-d0e74bfc2c3b"
    // PRODUCTION_PUBLIC_KEY_MLV = "APP_USR-a865216a-a035-4629-8ec4-b1e1908b8b0d"
    //CHECKOUT_PREFERENCES_ID_MLV = "241113185-a2eef4bd-ccb6-42e0-b543-12f6aab885ef";

    public static final String DUMMY_MERCHANT_PREFERENCES_ID_PE = "242617753-07562444-aa61-48b2-b33c-85d7dd6b812c";


    // * Merchant server vars
    public static final String DUMMY_MERCHANT_BASE_URL = "https://www.mercadopago.com";
    public static final String DUMMY_MERCHANT_GET_CUSTOMER_URI = "/checkout/examples/getCustomer";
    public static final String DUMMY_MERCHANT_CREATE_PAYMENT_URI = "/checkout/examples/doPayment";
    //public static final String DUMMY_MERCHANT_GET_DISCOUNT_URI = "/checkout/examples/getDiscounts";

    // * Merchant access token
    public static final String DUMMY_MERCHANT_ACCESS_TOKEN = "mla-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_AR = "mla-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_BR = "mlb-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_MX = "mlm-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_VZ = "mlv-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_VZ = "mco-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_NO_CCV = "mla-cards-data-tarshop";

    // * Payment item
    public static final String DUMMY_ITEM_ID = "id1";
    public static final Integer DUMMY_ITEM_QUANTITY = 1;
    public static final BigDecimal DUMMY_ITEM_UNIT_PRICE = new BigDecimal("100");

    public static PaymentMethod getDummyPaymentMethod() {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setId("visa");
        paymentMethod.setName("Visa");
        paymentMethod.setPaymentTypeId("credit_card");
        return paymentMethod;
    }

    public static Issuer getDummyIssuer() {
        Issuer issuer = new Issuer();
        issuer.setId((long) 338);
        return issuer;
    }

    public static void startCardActivity(Activity activity, String merchantPublicKey, PaymentMethod paymentMethod) {

        Intent cardIntent = new Intent(activity, CardActivity.class);
        cardIntent.putExtra("merchantPublicKey", merchantPublicKey);
        cardIntent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(paymentMethod));
        activity.startActivityForResult(cardIntent, CARD_REQUEST_CODE);
    }

    public static void startSimpleVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, List<String> excludedPaymentTypes) {

        Intent simpleVaultIntent = new Intent(activity, SimpleVaultActivity.class);
        simpleVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        simpleVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        simpleVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        simpleVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        putListExtra(simpleVaultIntent, "excludedPaymentTypes", excludedPaymentTypes);
        activity.startActivityForResult(simpleVaultIntent, SIMPLE_VAULT_REQUEST_CODE);
    }

    public static void startAdvancedVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, BigDecimal amount, List<String> excludedPaymentTypes) {

        Intent advVaultIntent = new Intent(activity, AdvancedVaultActivity.class);
        advVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        advVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        advVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        advVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        advVaultIntent.putExtra("amount", amount.toString());
        putListExtra(advVaultIntent, "excludedPaymentTypes", excludedPaymentTypes);
        activity.startActivityForResult(advVaultIntent, ADVANCED_VAULT_REQUEST_CODE);
    }

    public static void startFinalVaultActivity(Activity activity, String merchantPublicKey, String merchantBaseUrl, String merchantGetCustomerUri, String merchantAccessToken, BigDecimal amount, List<String> excludedPaymentTypes) {

        Intent finalVaultIntent = new Intent(activity, FinalVaultActivity.class);
        finalVaultIntent.putExtra("merchantPublicKey", merchantPublicKey);
        finalVaultIntent.putExtra("merchantBaseUrl", merchantBaseUrl);
        finalVaultIntent.putExtra("merchantGetCustomerUri", merchantGetCustomerUri);
        finalVaultIntent.putExtra("merchantAccessToken", merchantAccessToken);
        finalVaultIntent.putExtra("amount", amount.toString());
        putListExtra(finalVaultIntent, "excludedPaymentTypes", excludedPaymentTypes);
        activity.startActivityForResult(finalVaultIntent, FINAL_VAULT_REQUEST_CODE);
    }

    public static void createPayment(final Activity activity, String token, Integer installments, Long cardIssuerId, final PaymentMethod paymentMethod, Discount discount) {

        if (paymentMethod != null) {

            LayoutUtil.showProgressLayout(activity);

            // Set item
            Item item = new Item(DUMMY_ITEM_ID, DUMMY_ITEM_QUANTITY,
                    DUMMY_ITEM_UNIT_PRICE);

            // Set payment method id
            String paymentMethodId = paymentMethod.getId();

            // Set campaign id
            Long campaignId = (discount != null) ? discount.getId() : null;

            // Set merchant payment
            MerchantPayment payment = new MerchantPayment(item, installments, cardIssuerId,
                    token, paymentMethodId, campaignId, DUMMY_MERCHANT_ACCESS_TOKEN);

            // Create payment
            MerchantServer.createPayment(activity, DUMMY_MERCHANT_BASE_URL, DUMMY_MERCHANT_CREATE_PAYMENT_URI, payment, new Callback<Payment>() {
                @Override
                public void success(Payment payment) {

                    new MercadoPago.StartActivityBuilder()
                            .setPublicKey(DUMMY_MERCHANT_PUBLIC_KEY)
                            .setActivity(activity)
                            .setPayment(payment)
                            .setPaymentMethod(paymentMethod)
                            .startPaymentResultActivity();
                }

                @Override
                public void failure(ApiException error) {
                    LayoutUtil.showRegularLayout(activity);
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {

            Toast.makeText(activity, "Invalid payment method", Toast.LENGTH_LONG).show();
        }
    }

    private static void putListExtra(Intent intent, String listName, List<String> list) {

        if (list != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {
            }.getType();
            intent.putExtra(listName, gson.toJson(list, listType));
        }
    }
}