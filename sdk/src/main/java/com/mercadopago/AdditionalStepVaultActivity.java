package com.mercadopago;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.mercadopago.core.MercadoPagoComponents;
import com.mercadopago.model.EntityType;
import com.mercadopago.model.FinancialInstitution;
import com.mercadopago.model.Identification;
import com.mercadopago.model.IdentificationType;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.Site;
import com.mercadopago.mptracker.MPTracker;
import com.mercadopago.preferences.DecorationPreference;
import com.mercadopago.presenters.AdditionalStepVaultPresenter;
import com.mercadopago.providers.AdditionalStepVaultProviderImpl;
import com.mercadopago.statemachines.AdditionalStepVaultStateMachine;
import com.mercadopago.util.ErrorUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.views.AdditionalStepVaultView;

import java.security.PublicKey;


/**
 * Created by marlanti on 3/23/17.
 */

public class AdditionalStepVaultActivity extends MercadoPagoBaseActivity implements AdditionalStepVaultView {

    private static final String DECORATION_PREFERENCE_BUNDLE = "mDecorationPreference";
    private static final String SELECTED_IDENTIFICATION_BUNDLE = "mSelectedIdentification";
    private static final String SELECTED_IDENTIFICATION_TYPE_BUNDLE = "mSelectedIdentificationType";
    private static final String SELECTED_ENTITY_TYPE_BUNDLE = "mSelectedEntityType";
    private static final String SELECTED_FINANCIAL_INSTITUTIONS_BUNDLE = "mSelectedFinancialInstitution";
    private static final String SITE_BUNDLE = "mSite";
    private static final String PAYMENT_METHOD_BUNDLE = "mPaymentMethod";
    private static final String PUBLIC_KEY_BUNDLE = "mPublicKey";
    private static final String STATE_BUNDLE = "mState";
    private AdditionalStepVaultPresenter mPresenter;
    private DecorationPreference mDecorationPreference;
    private Identification mSelectedIdentification;
    private IdentificationType mSelectedIdentificationType;
    private FinancialInstitution mSelectedFinancialInstitution;
    private EntityType mSelectedEntityType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            createPresenter();
            configurePresenter();
            getActivityParameters();
            mPresenter.validateActivityParameters();
        }
    }

    private void configurePresenter() {
        mPresenter.attachView(this);
        mPresenter.attachResourcesProvider(new AdditionalStepVaultProviderImpl(this));
    }

    private void createPresenter() {
        mPresenter = new AdditionalStepVaultPresenter();
    }

    private void getActivityParameters() {

        String publicKey = getIntent().getStringExtra("merchantPublicKey");
        mDecorationPreference = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("decorationPreference"), DecorationPreference.class);

        PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(
                this.getIntent().getStringExtra("paymentMethod"), PaymentMethod.class);

        Site site = JsonUtil.getInstance().fromJson(this.getIntent().getStringExtra("site"), Site.class);


        mPresenter.setSite(site);
        mPresenter.setPaymentMethod(paymentMethod);
        mPresenter.setPublicKey(publicKey);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(DECORATION_PREFERENCE_BUNDLE, JsonUtil.getInstance().toJson(mDecorationPreference));
        outState.putString(SELECTED_IDENTIFICATION_BUNDLE, JsonUtil.getInstance().toJson(mSelectedIdentification));
        outState.putString(SELECTED_IDENTIFICATION_TYPE_BUNDLE, JsonUtil.getInstance().toJson(mSelectedIdentificationType));
        outState.putString(SELECTED_ENTITY_TYPE_BUNDLE, JsonUtil.getInstance().toJson(mSelectedEntityType));
        outState.putString(SELECTED_FINANCIAL_INSTITUTIONS_BUNDLE, JsonUtil.getInstance().toJson(mSelectedFinancialInstitution));
        outState.putString(SITE_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getSite()));
        outState.putString(PAYMENT_METHOD_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getPaymentMethod()));
        outState.putString(PUBLIC_KEY_BUNDLE, mPresenter.getPublicKey());
        outState.putString(STATE_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getState()));

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {

            createPresenter();
            configurePresenter();

            mDecorationPreference = JsonUtil.getInstance().fromJson(savedInstanceState.getString(DECORATION_PREFERENCE_BUNDLE), DecorationPreference.class);
            mSelectedIdentification = JsonUtil.getInstance().fromJson(savedInstanceState.getString(SELECTED_IDENTIFICATION_BUNDLE), Identification.class);
            mSelectedIdentificationType = JsonUtil.getInstance().fromJson(savedInstanceState.getString(SELECTED_IDENTIFICATION_TYPE_BUNDLE), IdentificationType.class);
            mSelectedEntityType = JsonUtil.getInstance().fromJson(savedInstanceState.getString(SELECTED_ENTITY_TYPE_BUNDLE), EntityType.class);
            mSelectedFinancialInstitution = JsonUtil.getInstance().fromJson(savedInstanceState.getString(SELECTED_FINANCIAL_INSTITUTIONS_BUNDLE), FinancialInstitution.class);
            mPresenter.setSite(JsonUtil.getInstance().fromJson(savedInstanceState.getString(SITE_BUNDLE), Site.class));
            mPresenter.setPaymentMethod(JsonUtil.getInstance().fromJson(savedInstanceState.getString(PAYMENT_METHOD_BUNDLE), PaymentMethod.class));
            mPresenter.setPublicKey(savedInstanceState.getString(PUBLIC_KEY_BUNDLE));
            mPresenter.setState(JsonUtil.getInstance().fromJson(savedInstanceState.getString(STATE_BUNDLE), AdditionalStepVaultStateMachine.class));
        }

    }


    @Override
    public void onInvalidStart(String msg) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onValidStart() {
        MPTracker.getInstance().trackScreen("ADDITIONAL_STEP_VAULT_ACTIVITY", "2", mPresenter.getPublicKey(),
                BuildConfig.VERSION_NAME, this);
        mPresenter.checkFlow();
    }


    @Override
    public void startEntityTypeStep() {

        startEntityTypeStepComponent();
        animateNextSelection();

    }

    @Override
    public void startEntityTypeStepAnimatedBack() {
        startEntityTypeStepComponent();
        animateBackSelection();
    }

    private void startEntityTypeStepComponent() {
        new MercadoPagoComponents.Activities.EntityTypeActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mPresenter.getPublicKey())
                .setPaymentMethod(mPresenter.getPaymentMethod())
                .setIdentification(mSelectedIdentification)
                .setIdentificationType(mSelectedIdentificationType)
                .setDecorationPreference(mDecorationPreference)
                .setSite(mPresenter.getSite())
                .startActivity();
    }

    @Override
    public void startIdentificationStep() {
        startIdentificationStepComponent();
        animateNextSelection();
    }

    @Override
    public void startIdentificationStepAnimatedBack() {
        startIdentificationStepComponent();
        animateBackSelection();
    }

    private void startIdentificationStepComponent() {

        new MercadoPagoComponents.Activities.IdentificationActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mPresenter.getPublicKey())
                .setDecorationPreference(mDecorationPreference)
                .setIdentification(mSelectedIdentification)
                .setIdentificationType(mSelectedIdentificationType)
                .startActivity();
    }


    @Override
    public void startFinancialInstitutionsStep() {

        startFinancialInstitutionsStepComponent();
        animateNextSelection();

    }

    @Override
    public void startFinancialInstitutionsStepAnimatedBack() {

        startFinancialInstitutionsStepComponent();
        animateBackSelection();

    }

    private void startFinancialInstitutionsStepComponent() {
        new MercadoPagoComponents.Activities.FinancialInstitutionsActivityBuilder()
                .setActivity(this)
                .setMerchantPublicKey(mPresenter.getPublicKey())
                .setPaymentMethod(mPresenter.getPaymentMethod())
                .setDecorationPreference(mDecorationPreference)
                .startActivity();
    }


    @Override
    public void animateNextSelection() {
        overridePendingTransition(R.anim.mpsdk_slide_right_to_left_in, R.anim.mpsdk_slide_right_to_left_out);
    }

    @Override
    public void animateBackSelection() {
        overridePendingTransition(R.anim.mpsdk_slide_left_to_right_in, R.anim.mpsdk_slide_left_to_right_out);
    }

    private void resolveFinancialInstitutionsRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            this.mSelectedFinancialInstitution = JsonUtil.getInstance().fromJson(data.getStringExtra("financialInstitution"), FinancialInstitution.class);
            mPresenter.checkFlowWithFinancialInstitutionSelected();

        } else if (resultCode == RESULT_CANCELED) {
            mPresenter.onBackPressed();
        }
    }

    @Override
    public void finishWithResult() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("identification", JsonUtil.getInstance().toJson(mSelectedIdentification));
        returnIntent.putExtra("financialInstitution", JsonUtil.getInstance().toJson(mSelectedFinancialInstitution));

        if (mSelectedEntityType != null) {
            String entityTypeId = mSelectedEntityType.getId();
            returnIntent.putExtra("entityType", entityTypeId);
        }

        setResult(RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.mpsdk_hold, R.anim.mpsdk_hold);
    }

    private void resolveIdentificationRequest(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            this.mSelectedIdentification = JsonUtil.getInstance().fromJson(data.getStringExtra("identification"), Identification.class);
            this.mSelectedIdentificationType = JsonUtil.getInstance().fromJson(data.getStringExtra("identificationType"), IdentificationType.class);

            mPresenter.checkFlowWithIdentificationSelected();

        } else if (resultCode == RESULT_CANCELED) {
            mPresenter.onBackPressed();
        }
    }

    private void resolveEntityTypeRequest(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            this.mSelectedEntityType = JsonUtil.getInstance().fromJson(data.getStringExtra("entityType"), EntityType.class);

            mPresenter.checkFlowWithEntityTypeSelected();

        } else if (resultCode == RESULT_CANCELED) {
            mPresenter.onBackPressed();
        }
    }

    @Override
    public void finishWithCancel() {
        setResult(RESULT_CANCELED);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MercadoPagoComponents.Activities.FINANCIAL_INSTITUTIONS_REQUEST_CODE) {
            resolveFinancialInstitutionsRequest(resultCode, data);
        } else if (requestCode == MercadoPagoComponents.Activities.IDENTIFICATION_REQUEST_CODE) {
            resolveIdentificationRequest(resultCode, data);
        } else if (requestCode == MercadoPagoComponents.Activities.ENTITY_TYPE_REQUEST_CODE) {
            resolveEntityTypeRequest(resultCode, data);
        }

    }

    @Override
    public void onError(String message) {
        ErrorUtil.startErrorActivity(this, message, false);
    }
}
