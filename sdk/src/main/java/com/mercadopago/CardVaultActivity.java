package com.mercadopago;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.core.MercadoPagoComponents;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Card;
import com.mercadopago.model.CardInfo;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentRecovery;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.mptracker.MPTracker;
import com.mercadopago.preferences.DecorationPreference;
import com.mercadopago.preferences.PaymentPreference;
import com.mercadopago.presenters.CardVaultPresenter;
import com.mercadopago.providers.CardVaultProviderImpl;
import com.mercadopago.util.ApiUtil;
import com.mercadopago.util.ErrorUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.mercadopago.views.CardVaultView;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by vaserber on 10/12/16.
 */

public class CardVaultActivity extends AppCompatActivity implements CardVaultView {

    protected CardVaultPresenter mCardVaultPresenter;
    protected DecorationPreference mDecorationPreference;
    protected boolean mActivityActive;

    //Parameters
    protected String mPublicKey;
    protected String mPrivateKey;

    //View controls
    private Boolean mShowBankDeals;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeCreation(savedInstanceState);
        createPresenter();

        if (savedInstanceState == null) {
            getActivityParameters();
        }

        mCardVaultPresenter.attachView(this);
        mCardVaultPresenter.attachResourcesProvider(new CardVaultProviderImpl(this, mPublicKey, mPrivateKey));

        mActivityActive = true;
        setContentView();

        initialize();
    }

    protected void createPresenter() {
        if (mCardVaultPresenter == null) {
            mCardVaultPresenter = new CardVaultPresenter();
        }
    }

    private void onBeforeCreation(Bundle savedInstanceState) {
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState);
        }
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        mCardVaultPresenter = new CardVaultPresenter();

        BigDecimal amountValue = null;
        String amount = savedInstanceState.getString("amount");
        if (amount != null) {
            amountValue = new BigDecimal(amount);
        }
        mCardVaultPresenter.setAmount(amountValue);

        List<PaymentMethod> paymentMethods;
        try {
            Type listType = new TypeToken<List<PaymentMethod>>() {
            }.getType();
            paymentMethods = JsonUtil.getInstance().getGson().fromJson(savedInstanceState.getString("paymentMethodList"), listType);
        } catch (Exception ex) {
            paymentMethods = null;
        }

        List<PayerCost> payerCosts;
        try {
            Type listType = new TypeToken<List<Card>>() {
            }.getType();
            payerCosts = JsonUtil.getInstance().getGson().fromJson(savedInstanceState.getString("payerCostsList"), listType);
        } catch (Exception ex) {
            payerCosts = null;
        }

        mCardVaultPresenter.setPaymentMethodList(paymentMethods);
        mCardVaultPresenter.setPayerCostsList(payerCosts);
        PaymentPreference paymentPreference = JsonUtil.getInstance().fromJson(savedInstanceState.getString("paymentPreference"), PaymentPreference.class);
        if (paymentPreference == null) {
            paymentPreference = new PaymentPreference();
        }

        mCardVaultPresenter.setPaymentPreference(paymentPreference);
        mCardVaultPresenter.setPaymentRecovery(JsonUtil.getInstance().fromJson(savedInstanceState.getString("paymentRecovery"), PaymentRecovery.class));
        mCardVaultPresenter.setCard(JsonUtil.getInstance().fromJson(savedInstanceState.getString("card"), Card.class));
        mCardVaultPresenter.setPublicKey(savedInstanceState.getString("merchantPublicKey"));
        mPrivateKey = savedInstanceState.getString("privateKey");
        mPublicKey = savedInstanceState.getString("merchantPublicKey");
        mCardVaultPresenter.setSite(JsonUtil.getInstance().fromJson(savedInstanceState.getString("site"), Site.class));
        mCardVaultPresenter.setPaymentMethod(JsonUtil.getInstance().fromJson(savedInstanceState.getString("paymentMethod"), PaymentMethod.class));
        mCardVaultPresenter.setIssuer(JsonUtil.getInstance().fromJson(savedInstanceState.getString("issuer"), Issuer.class));
        mCardVaultPresenter.setPayerCost(JsonUtil.getInstance().fromJson(savedInstanceState.getString("payerCost"), PayerCost.class));
        mCardVaultPresenter.setToken(JsonUtil.getInstance().fromJson(savedInstanceState.getString("token"), Token.class));
        mCardVaultPresenter.setCardInfo(JsonUtil.getInstance().fromJson(savedInstanceState.getString("cardInfo"), CardInfo.class));
        mCardVaultPresenter.setInstallmentsEnabled(savedInstanceState.getBoolean("installmentsEnabled", false));
        mCardVaultPresenter.setInstallmentsReviewEnabled(savedInstanceState.getBoolean("installmentsReviewEnabled", false));
        mCardVaultPresenter.setInstallmentsListShown(savedInstanceState.getBoolean("installmentsListShown", false));
        mCardVaultPresenter.setIssuersListShown(savedInstanceState.getBoolean("issuersListShown", false));

        mShowBankDeals = savedInstanceState.getBoolean("showBankDeals", true);
        mDecorationPreference = JsonUtil.getInstance().fromJson(savedInstanceState.getString("decorationPreference"), DecorationPreference.class);

    }

    private void getActivityParameters() {
        Boolean installmentsEnabled = getIntent().getBooleanExtra("installmentsEnabled", true);
        Boolean directDiscountEnabled = getIntent().getBooleanExtra("directDiscountEnabled", true);
        Boolean installmentsReviewEnabled = getIntent().getBooleanExtra("installmentsReviewEnabled", true);
        mPublicKey = getIntent().getStringExtra("merchantPublicKey");
        mPrivateKey = getIntent().getStringExtra("payerAccessToken");
        PaymentPreference paymentPreference = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("paymentPreference"), PaymentPreference.class);
        mDecorationPreference = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("decorationPreference"), DecorationPreference.class);

        Site site = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("site"), Site.class);
        Card card = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("card"), Card.class);
        PaymentRecovery paymentRecovery = JsonUtil.getInstance().fromJson(this.getIntent().getStringExtra("paymentRecovery"), PaymentRecovery.class);
        BigDecimal amountValue = null;
        String amount = getIntent().getStringExtra("amount");
        String payerEmail = getIntent().getStringExtra("payerEmail");
        Discount discount = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("discount"), Discount.class);
        Boolean discountEnabled = getIntent().getBooleanExtra("discountEnabled", true);
        Boolean automaticSelection = getIntent().getBooleanExtra("automaticSelection", false);

        String discountAdditionalInfo = getIntent().getStringExtra("discountAdditionalInfo");
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> discountAdditionalInfoMap = JsonUtil.getInstance().getGson().fromJson(discountAdditionalInfo, type);

        if (amount != null) {
            amountValue = new BigDecimal(amount);
        }
        List<PaymentMethod> paymentMethods;
        try {
            Type listType = new TypeToken<List<PaymentMethod>>() {
            }.getType();
            paymentMethods = JsonUtil.getInstance().getGson().fromJson(this.getIntent().getStringExtra("paymentMethodList"), listType);
        } catch (Exception ex) {
            paymentMethods = null;
        }

        if (paymentPreference == null) {
            paymentPreference = new PaymentPreference();
        }

        mShowBankDeals = getIntent().getBooleanExtra("showBankDeals", true);

        mCardVaultPresenter.setPublicKey(mPublicKey);
        mCardVaultPresenter.setCard(card);
        mCardVaultPresenter.setInstallmentsEnabled(installmentsEnabled);
        mCardVaultPresenter.setSite(site);
        mCardVaultPresenter.setPaymentRecovery(paymentRecovery);
        mCardVaultPresenter.setAmount(amountValue);
        mCardVaultPresenter.setPaymentMethodList(paymentMethods);
        mCardVaultPresenter.setPaymentPreference(paymentPreference);
        mCardVaultPresenter.setPayerEmail(payerEmail);
        mCardVaultPresenter.setDiscount(discount);
        mCardVaultPresenter.setDiscountEnabled(discountEnabled);
        mCardVaultPresenter.setDirectDiscountEnabled(directDiscountEnabled);
        mCardVaultPresenter.setInstallmentsReviewEnabled(installmentsReviewEnabled);
        mCardVaultPresenter.setAutomaticSelection(automaticSelection);
    }

    private void setContentView() {
        setContentView(R.layout.mpsdk_activity_card_vault);
    }

    protected void initialize() {
        MPTracker.getInstance().trackScreen("CARD_VAULT", "2", mCardVaultPresenter.getPublicKey(), BuildConfig.VERSION_NAME, this);
        mCardVaultPresenter.initialize();
    }

    @Override
    public void showProgressLayout() {
        LayoutUtil.showProgressLayout(this);
    }

    @Override
    public void askForSecurityCodeFromTokenRecovery() {
        startSecurityCodeActivity();
        animateTransitionFadeInFadeOut();
    }

    @Override
    public void askForSecurityCodeFromInstallments() {
        startSecurityCodeActivity();
        animateTransitionSlideInSlideOut();
    }

    @Override
    public void askForSecurityCodeWithoutInstallments() {
        startSecurityCodeActivity();
        animateTransitionFadeInFadeOut();
    }

    private void startSecurityCodeActivity() {
        new MercadoPagoComponents.Activities.SecurityCodeActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mCardVaultPresenter.getPublicKey())
                .setPaymentMethod(mCardVaultPresenter.getPaymentMethod())
                .setCardInfo(mCardVaultPresenter.getCardInfo())
                .setToken(mCardVaultPresenter.getToken())
                .setCard(mCardVaultPresenter.getCard())
                .setDecorationPreference(mDecorationPreference)
                .setPayerAccessToken(mPrivateKey)
                .startActivity();
    }

    @Override
    public void askForCardInformation() {
        startGuessingCardActivity();
    }

    private void startGuessingCardActivity() {
        final Activity context = this;
        runOnUiThread(new Runnable() {
            public void run() {
                new MercadoPagoComponents.Activities.GuessingCardActivityBuilder()
                        .setActivity(context)
                        .setMerchantPublicKey(mCardVaultPresenter.getPublicKey())
                        .setAmount(mCardVaultPresenter.getAmount())
                        .setPayerEmail(mCardVaultPresenter.getPayerEmail())
                        .setPayerAccessToken(mPrivateKey)
                        .setDiscount(mCardVaultPresenter.getDiscount())
                        .setAmount(mCardVaultPresenter.getAmount())
                        .setDiscountEnabled(mCardVaultPresenter.getDiscountEnabled())
                        .setDirectDiscountEnabled(mCardVaultPresenter.getDirectDiscountEnabled())
                        .setShowBankDeals(mShowBankDeals)
                        .setPaymentPreference(mCardVaultPresenter.getPaymentPreference())
                        .setAcceptedPaymentMethods(mCardVaultPresenter.getPaymentMethodList())
                        .setShowDiscount(mCardVaultPresenter.getAutomaticSelection())
                        .setDecorationPreference(mDecorationPreference)
                        .setPaymentRecovery(mCardVaultPresenter.getPaymentRecovery())
                        .startActivity();
                overridePendingTransition(R.anim.mpsdk_slide_right_to_left_in, R.anim.mpsdk_slide_right_to_left_out);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MercadoPagoComponents.Activities.GUESSING_CARD_REQUEST_CODE) {
            resolveGuessingCardRequest(resultCode, data);
        } else if (requestCode == MercadoPagoComponents.Activities.ISSUERS_REQUEST_CODE) {
            resolveIssuersRequest(resultCode, data);
        } else if (requestCode == MercadoPagoComponents.Activities.INSTALLMENTS_REQUEST_CODE) {
            resolveInstallmentsRequest(resultCode, data);
        } else if (requestCode == MercadoPagoComponents.Activities.SECURITY_CODE_REQUEST_CODE) {
            resolveSecurityCodeRequest(resultCode, data);
        } else if (requestCode == ErrorUtil.ERROR_REQUEST_CODE) {
            resolveErrorRequest(resultCode, data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCardVaultPresenter != null) {
            outState.putBoolean("installmentsEnabled", mCardVaultPresenter.isInstallmentsEnabled());
            outState.putBoolean("installmentsReviewEnabled", mCardVaultPresenter.getInstallmentsReviewEnabled());
            outState.putString("merchantPublicKey", mCardVaultPresenter.getPublicKey());
            outState.putString("privateKey", mPrivateKey);
            outState.putString("site", JsonUtil.getInstance().toJson(mCardVaultPresenter.getSite()));
            outState.putString("card", JsonUtil.getInstance().toJson(mCardVaultPresenter.getCard()));
            outState.putString("paymentRecovery", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPaymentRecovery()));
            outState.putBoolean("showBankDeals", mShowBankDeals);
            outState.putBoolean("installmentsListShown", mCardVaultPresenter.isInstallmentsListShown());
            outState.putBoolean("issuersListShown", mCardVaultPresenter.isIssuersListShown());

            if (mCardVaultPresenter.getPayerCostList() != null) {
                outState.putString("payerCostsList", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPayerCostList()));
            }

            if (mCardVaultPresenter.getAmount() != null) {
                outState.putString("amount", mCardVaultPresenter.getAmount().toString());
            }

            if (mCardVaultPresenter.getPaymentMethodList() != null) {
                outState.putString("paymentMethodList", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPaymentMethodList()));
            }

            if (mCardVaultPresenter.getPaymentPreference() != null) {
                outState.putString("paymentPreference", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPaymentPreference()));
            }

            if (mCardVaultPresenter.getPaymentMethod() != null) {
                outState.putString("paymentMethod", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPaymentMethod()));
            }

            if (mCardVaultPresenter.getIssuer() != null) {
                outState.putString("issuer", JsonUtil.getInstance().toJson(mCardVaultPresenter.getIssuer()));
            }

            if (mCardVaultPresenter.getPayerCost() != null) {
                outState.putString("payerCost", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPayerCost()));
            }

            if (mCardVaultPresenter.getToken() != null) {
                outState.putString("token", JsonUtil.getInstance().toJson(mCardVaultPresenter.getToken()));
            }

            if (mCardVaultPresenter.getCardInfo() != null) {
                outState.putString("cardInfo", JsonUtil.getInstance().toJson(mCardVaultPresenter.getCardInfo()));
            }

            if (mDecorationPreference != null) {
                outState.putString("decorationPreference", JsonUtil.getInstance().toJson(mDecorationPreference));
            }
        }
    }

    private void resolveErrorRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            mCardVaultPresenter.recoverFromFailure();
        } else {
            setResult(resultCode, data);
            finish();
        }
    }

    protected void resolveIssuersRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Issuer issuer = JsonUtil.getInstance().fromJson(bundle.getString("issuer"), Issuer.class);

            mCardVaultPresenter.resolveIssuersRequest(issuer);

        } else if (resultCode == RESULT_CANCELED) {
            MPTracker.getInstance().trackEvent("INSTALLMENTS", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                    mCardVaultPresenter.getSite().getId(), BuildConfig.VERSION_NAME, this);
            mCardVaultPresenter.onResultCancel();
        }
    }

    protected void resolveInstallmentsRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            PayerCost payerCost = JsonUtil.getInstance().fromJson(bundle.getString("payerCost"), PayerCost.class);
            Discount discount = JsonUtil.getInstance().fromJson(bundle.getString("discount"), Discount.class);

            mCardVaultPresenter.resolveInstallmentsRequest(payerCost, discount);

        } else if (resultCode == RESULT_CANCELED) {
            MPTracker.getInstance().trackEvent("INSTALLMENTS", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                    mCardVaultPresenter.getSite().getId(), BuildConfig.VERSION_NAME, this);
            mCardVaultPresenter.onResultCancel();
        }
    }

    protected void resolveGuessingCardRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);
            Token token = JsonUtil.getInstance().fromJson(data.getStringExtra("token"), Token.class);
            Discount discount = JsonUtil.getInstance().fromJson(data.getStringExtra("discount"), Discount.class);
            Boolean directDiscountEnabled = data.getBooleanExtra("directDiscountEnabled", true);

            Issuer issuer = JsonUtil.getInstance().fromJson(data.getStringExtra("issuer"), Issuer.class);
            PayerCost payerCost = JsonUtil.getInstance().fromJson(data.getStringExtra("payerCost"), PayerCost.class);

            List<PayerCost> payerCosts;
            try {
                Type listType = new TypeToken<List<PayerCost>>() {
                }.getType();
                payerCosts = JsonUtil.getInstance().getGson().fromJson(data.getStringExtra("payerCosts"), listType);
            } catch (Exception ex) {
                payerCosts = null;
            }

            List<Issuer> issuers;
            try {
                Type listType = new TypeToken<List<Issuer>>() {
                }.getType();
                issuers = JsonUtil.getInstance().getGson().fromJson(data.getStringExtra("issuers"), listType);
            } catch (Exception ex) {
                issuers = null;
            }

            mCardVaultPresenter.resolveNewCardRequest(paymentMethod, token, directDiscountEnabled, payerCost, issuer, payerCosts, issuers, discount);

        } else if (resultCode == RESULT_CANCELED) {
            if (mCardVaultPresenter.getSite() == null) {
                MPTracker.getInstance().trackEvent("GUESSING_CARD", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                        BuildConfig.VERSION_NAME, this);
            } else {
                MPTracker.getInstance().trackEvent("GUESSING_CARD", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                        mCardVaultPresenter.getSite().getId(), BuildConfig.VERSION_NAME, this);
            }
            mCardVaultPresenter.onResultCancel();
        }
    }

    protected void resolveSecurityCodeRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Token token = JsonUtil.getInstance().fromJson(data.getStringExtra("token"), Token.class);

            mCardVaultPresenter.resolveSecurityCodeRequest(token);

        } else if (resultCode == RESULT_CANCELED) {
            if (mCardVaultPresenter.getSite() == null) {
                MPTracker.getInstance().trackEvent("SECURITY_CODE_CARD", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                        BuildConfig.VERSION_NAME, this);
            } else {
                MPTracker.getInstance().trackEvent("SECURITY_CODE_CARD", "CANCELED", "2", mCardVaultPresenter.getPublicKey(),
                        mCardVaultPresenter.getSite().getId(), BuildConfig.VERSION_NAME, this);
            }
            mCardVaultPresenter.onResultCancel();
        }
    }

    @Override
    public void cancelCardVault() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void startIssuersActivity() {
        new MercadoPagoComponents.Activities.IssuersActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mCardVaultPresenter.getPublicKey())
                .setPayerAccessToken(mPrivateKey)
                .setPaymentMethod(mCardVaultPresenter.getPaymentMethod())
                .setCardInfo(mCardVaultPresenter.getCardInfo())
                .setIssuers(mCardVaultPresenter.getIssuersList())
                .setDecorationPreference(mDecorationPreference)
                .startActivity();
        animateTransitionSlideInSlideOut();
    }

    @Override
    public void askForInstallmentsFromIssuers() {
        startInstallmentsActivity();
        animateTransitionHold();
    }

    @Override
    public void askForInstallmentsFromNewCard() {
        startInstallmentsActivity();
        animateTransitionSlideInSlideOut();
    }

    @Override
    public void askForInstallments() {
        startInstallmentsActivity();
        animateTransitionFadeInFadeOut();
    }

    private void startInstallmentsActivity() {
        new MercadoPagoComponents.Activities.InstallmentsActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mCardVaultPresenter.getPublicKey())
                .setPayerAccessToken(mPrivateKey)
                .setPaymentMethod(mCardVaultPresenter.getPaymentMethod())
                .setAmount(mCardVaultPresenter.getAmount())
                .setPayerEmail(mCardVaultPresenter.getPayerEmail())
                .setDiscount(mCardVaultPresenter.getDiscount())
                .setDiscountEnabled(mCardVaultPresenter.getDiscountEnabled())
                .setIssuer(mCardVaultPresenter.getIssuer())
                .setPaymentPreference(mCardVaultPresenter.getPaymentPreference())
                .setSite(mCardVaultPresenter.getSite())
                .setDecorationPreference(mDecorationPreference)
                .setInstallmentsEnabled(mCardVaultPresenter.isInstallmentsEnabled())
                .setInstallmentsReviewEnabled(mCardVaultPresenter.getInstallmentsReviewEnabled())
                .setCardInfo(mCardVaultPresenter.getCardInfo())
                .setPayerCosts(mCardVaultPresenter.getPayerCostList())
                .startActivity();
    }

    public void animateTransitionHold() {
        overridePendingTransition(R.anim.mpsdk_hold, R.anim.mpsdk_hold);
    }

    public void animateTransitionSlideInSlideOut() {
        overridePendingTransition(R.anim.mpsdk_slide_right_to_left_in, R.anim.mpsdk_slide_right_to_left_out);
    }

    private void animateTransitionFadeInFadeOut() {
        overridePendingTransition(R.anim.mpsdk_fade_in_seamless, R.anim.mpsdk_fade_out_seamless);
    }

    @Override
    public void finishWithResult() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("payerCost", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPayerCost()));
        returnIntent.putExtra("paymentMethod", JsonUtil.getInstance().toJson(mCardVaultPresenter.getPaymentMethod()));
        returnIntent.putExtra("token", JsonUtil.getInstance().toJson(mCardVaultPresenter.getToken()));
        returnIntent.putExtra("issuer", JsonUtil.getInstance().toJson(mCardVaultPresenter.getIssuer()));
        returnIntent.putExtra("discount", JsonUtil.getInstance().toJson(mCardVaultPresenter.getDiscount()));
        setResult(RESULT_OK, returnIntent);
        finish();
        animateTransitionSlideInSlideOut();
    }

    @Override
    public void showApiExceptionError(ApiException exception) {
        ApiUtil.showApiExceptionError(this, exception);
    }

    @Override
    public void showError(MercadoPagoError error) {
        if (error.isApiException()) {
            showApiExceptionError(error.getApiException());
        } else {
            ErrorUtil.startErrorActivity(this, error);
        }
    }

}
