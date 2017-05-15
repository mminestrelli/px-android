package com.mercadopago.core;

import android.content.Context;

import com.mercadopago.BuildConfig;
import com.mercadopago.adapters.ErrorHandlingCallAdapter;
import com.mercadopago.callbacks.Callback;
import com.mercadopago.controllers.CustomServicesHandler;
import com.mercadopago.model.BankDeal;
import com.mercadopago.model.Campaign;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.Device;
import com.mercadopago.model.Discount;
import com.mercadopago.model.IdentificationType;
import com.mercadopago.model.Installment;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PayerIntent;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentBody;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.model.Instructions;
import com.mercadopago.model.SavedCardToken;
import com.mercadopago.model.SecurityCodeIntent;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.mptracker.MPTracker;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.preferences.ServicePreference;
import com.mercadopago.services.BankDealService;
import com.mercadopago.services.CheckoutService;
import com.mercadopago.services.DiscountService;
import com.mercadopago.services.GatewayService;
import com.mercadopago.services.IdentificationService;
import com.mercadopago.services.PaymentService;
import com.mercadopago.util.HttpClientUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.TextUtil;
import com.mercadopago.util.TextUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mreverter on 1/17/17.
 */

public class MercadoPagoServices {
    public static final int BIN_LENGTH = 6;

    private static final String MP_API_BASE_URL = "https://api.mercadopago.com";

    private static final String PAYMENT_RESULT_API_VERSION = "1.3.x";
    private static final String PAYMENT_METHODS_OPTIONS_API_VERSION = "1.3.x";

    public static final int DEFAULT_CONNECT_TIMEOUT = 10;
    public static final int DEFAULT_READ_TIMEOUT = 20;
    public static final int DEFAULT_WRITE_TIMEOUT = 20;

    public static final int DEFAULT_PAYMENT_CONNECT_TIMEOUT = 10;
    public static final int DEFAULT_PAYMENT_READ_TIMEOUT = 20;
    public static final int DEFAULT_PAYMENT_WRITE_TIMEOUT = 20;

    private ServicePreference mServicePreference;
    private String mPublicKey;
    private String mPrivateKey;
    private Context mContext;

    private MercadoPagoServices(MercadoPagoServices.Builder builder) {
        this.mContext = builder.mContext;
        this.mPublicKey = builder.mPublicKey;
        this.mPrivateKey = builder.mPrivateKey;
        this.mServicePreference = CustomServicesHandler.getInstance().getServicePreference();

        System.setProperty("http.keepAlive", "false");
    }

    public void getPreference(String checkoutPreferenceId, Callback<CheckoutPreference> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_PREFERENCE", "3", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        CheckoutService service = getDefaultRetrofit().create(CheckoutService.class);
        service.getPreference(checkoutPreferenceId, this.mPublicKey).enqueue(callback);
    }

    public void getInstructions(Long paymentId, String paymentTypeId, final Callback<Instructions> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_INSTRUCTIONS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        CheckoutService service = getDefaultRetrofit().create(CheckoutService.class);
        service.getPaymentResult(mContext.getResources().getConfiguration().locale.getLanguage(), paymentId, this.mPublicKey, paymentTypeId, PAYMENT_RESULT_API_VERSION).enqueue(callback);
    }

    public void getPaymentMethodSearch(BigDecimal amount, List<String> excludedPaymentTypes, List<String> excludedPaymentMethods, Payer payer, Site site, final Callback<PaymentMethodSearch> callback) {

        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_PAYMENT_METHOD_SEARCH", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        PayerIntent payerIntent = new PayerIntent(payer);
        CheckoutService service = getDefaultRetrofit().create(CheckoutService.class);
        String separator = ",";
        String excludedPaymentTypesAppended = getListAsString(excludedPaymentTypes, separator);
        String excludedPaymentMethodsAppended = getListAsString(excludedPaymentMethods, separator);
        String siteId = site == null ? "" : site.getId();
        service.getPaymentMethodSearch(mContext.getResources().getConfiguration().locale.getLanguage(), this.mPublicKey, amount, excludedPaymentTypesAppended, excludedPaymentMethodsAppended, payerIntent, siteId, PAYMENT_METHODS_OPTIONS_API_VERSION).enqueue(callback);
    }

    public void createPayment(final PaymentBody paymentBody, final Callback<Payment> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "CREATE_PAYMENT", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        CheckoutService service = getDefaultRetrofit(DEFAULT_PAYMENT_CONNECT_TIMEOUT, DEFAULT_PAYMENT_READ_TIMEOUT, DEFAULT_PAYMENT_WRITE_TIMEOUT).create(CheckoutService.class);
        service.createPayment(paymentBody.getTransactionId(), paymentBody).enqueue(callback);
    }

    public void createToken(final SavedCardToken savedCardToken, final Callback<Token> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                savedCardToken.setDevice(mContext);
                GatewayService service = getGatewayRetrofit().create(GatewayService.class);
                service.getToken(mPublicKey, mPrivateKey, savedCardToken).enqueue(callback);
            }
        }).start();
    }

    public void createToken(final CardToken cardToken, final Device device, final Callback<Token> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                cardToken.setDevice(device);
                GatewayService service = getGatewayRetrofit().create(GatewayService.class);
                service.getToken(mPublicKey, mPrivateKey, cardToken).enqueue(callback);
            }
        }).start();
    }

    public void cloneToken(final String tokenId, final Callback<Token> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "CLONE_TOKEN", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        GatewayService service = getGatewayRetrofit().create(GatewayService.class);
        service.getToken(tokenId, this.mPublicKey, mPrivateKey).enqueue(callback);
    }

    public void putSecurityCode(final String tokenId, final SecurityCodeIntent securityCodeIntent, final Callback<Token> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "CLONE_TOKEN", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        GatewayService service = getGatewayRetrofit().create(GatewayService.class);
        service.getToken(tokenId, this.mPublicKey, mPrivateKey, securityCodeIntent).enqueue(callback);
    }

    public void getBankDeals(final Callback<List<BankDeal>> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_BANK_DEALS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        BankDealService service = getDefaultRetrofit().create(BankDealService.class);
        service.getBankDeals(this.mPublicKey, mPrivateKey, mContext.getResources().getConfiguration().locale.toString()).enqueue(callback);
    }


    public void getIdentificationTypes(Callback<List<IdentificationType>> callback) {
        IdentificationService service = getDefaultRetrofit().create(IdentificationService.class);
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_IDENTIFICATION_TYPES", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        service.getIdentificationTypes(this.mPublicKey, this.mPrivateKey).enqueue(callback);
    }

    public void getInstallments(String bin, BigDecimal amount, Long issuerId, String paymentMethodId, Callback<List<Installment>> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_INSTALLMENTS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        PaymentService service = getDefaultRetrofit().create(PaymentService.class);
        service.getInstallments(this.mPublicKey, mPrivateKey, bin, amount, issuerId, paymentMethodId,
                mContext.getResources().getConfiguration().locale.toString()).enqueue(callback);
    }

    public void getIssuers(String paymentMethodId, String bin, final Callback<List<Issuer>> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_ISSUERS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        PaymentService service = getDefaultRetrofit().create(PaymentService.class);
        service.getIssuers(this.mPublicKey, mPrivateKey, paymentMethodId, bin).enqueue(callback);
    }

    public void getPaymentMethods(final Callback<List<PaymentMethod>> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_PAYMENT_METHODS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        PaymentService service = getDefaultRetrofit().create(PaymentService.class);
        service.getPaymentMethods(this.mPublicKey, mPrivateKey).enqueue(callback);
    }

    public void getDirectDiscount(String amount, String payerEmail, final Callback<Discount> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_DIRECT_DISCOUNT", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        DiscountService service = getDefaultRetrofit().create(DiscountService.class);
        service.getDirectDiscount(this.mPublicKey, amount, payerEmail).enqueue(callback);
    }

    public void getCodeDiscount(String amount, String payerEmail, String couponCode, final Callback<Discount> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_CODE_DISCOUNT", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        DiscountService service = getDefaultRetrofit().create(DiscountService.class);
        service.getCodeDiscount(this.mPublicKey, amount, payerEmail, couponCode).enqueue(callback);
    }

    public void getCampaigns(final Callback<List<Campaign>> callback) {
        MPTracker.getInstance().trackEvent("NO_SCREEN", "GET_CAMPAIGNS", "1", mPublicKey, BuildConfig.VERSION_NAME, mContext);
        DiscountService service = getDefaultRetrofit().create(DiscountService.class);
        service.getCampaigns(this.mPublicKey).enqueue(callback);
    }

    public static List<PaymentMethod> getValidPaymentMethodsForBin(String bin, List<PaymentMethod> paymentMethods) {
        if (bin.length() == BIN_LENGTH) {
            List<PaymentMethod> validPaymentMethods = new ArrayList<>();
            for (PaymentMethod pm : paymentMethods) {
                if (pm.isValidForBin(bin)) {
                    validPaymentMethods.add(pm);
                }
            }
            return validPaymentMethods;
        } else
            throw new RuntimeException("Invalid bin: " + BIN_LENGTH + " digits needed, " + bin.length() + " found");
    }

    private Retrofit getDefaultRetrofit() {
        return getDefaultRetrofit(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
    }

    private Retrofit getDefaultRetrofit(int connectTimeout, int readTimeout, int writeTimeout) {
        String baseUrl;
        if (mServicePreference != null && !TextUtil.isEmpty(mServicePreference.getDefaultBaseURL())) {
            baseUrl = mServicePreference.getDefaultBaseURL();
        } else {
            baseUrl = MP_API_BASE_URL;
        }
        return getRetrofit(baseUrl, connectTimeout, readTimeout, writeTimeout);
    }

    private Retrofit getGatewayRetrofit() {
        return getGatewayRetrofit(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_WRITE_TIMEOUT);
    }

    private Retrofit getGatewayRetrofit(int connectTimeout, int readTimeout, int writeTimeout) {
        String baseUrl;
        if (mServicePreference != null && !TextUtil.isEmpty(mServicePreference.getGatewayBaseURL())) {
            baseUrl = mServicePreference.getGatewayBaseURL();
        } else if (mServicePreference != null && !TextUtil.isEmpty(mServicePreference.getDefaultBaseURL())) {
            baseUrl = mServicePreference.getDefaultBaseURL();
        } else {
            baseUrl = MP_API_BASE_URL;
        }
        return getRetrofit(baseUrl, connectTimeout, readTimeout, writeTimeout);
    }

    private Retrofit getRetrofit(String baseUrl, int connectTimeout, int readTimeout, int writeTimeout) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(JsonUtil.getInstance().getGson()))
                .client(HttpClientUtil.getClient(this.mContext, connectTimeout, readTimeout, writeTimeout))
                .addCallAdapterFactory(new ErrorHandlingCallAdapter.ErrorHandlingCallAdapterFactory())
                .build();
    }

    public static class Builder {

        private Context mContext;
        private String mPublicKey;
        public String mPrivateKey;
        public ServicePreference mServicePreference;

        public Builder() {

            mContext = null;
            mPublicKey = null;
        }

        public MercadoPagoServices.Builder setContext(Context context) {

            if (context == null) throw new IllegalArgumentException("context is null");
            this.mContext = context;
            return this;
        }

        public MercadoPagoServices.Builder setPrivateKey(String key) {

            this.mPrivateKey = key;
            return this;
        }

        public MercadoPagoServices.Builder setPublicKey(String key) {

            this.mPublicKey = key;
            return this;
        }

        public MercadoPagoServices.Builder setServicePreference(ServicePreference servicePreference) {

            this.mServicePreference = servicePreference;
            return this;
        }

        public MercadoPagoServices build() {

            if (this.mContext == null) throw new IllegalStateException("context is null");
            if (TextUtils.isEmpty(this.mPublicKey) && TextUtil.isEmpty(this.mPrivateKey))
                throw new IllegalStateException("key is null");

            return new MercadoPagoServices(this);
        }
    }

    private String getListAsString(List<String> list, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null) {
            for (String typeId : list) {
                stringBuilder.append(typeId);
                if (!typeId.equals(list.get(list.size() - 1))) {
                    stringBuilder.append(separator);
                }
            }
        }
        return stringBuilder.toString();
    }
}
