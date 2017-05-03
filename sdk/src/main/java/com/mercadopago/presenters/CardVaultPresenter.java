package com.mercadopago.presenters;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.controllers.PaymentMethodGuessingController;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.Card;
import com.mercadopago.model.CardInfo;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Installment;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentRecovery;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.mvp.MvpPresenter;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.preferences.PaymentPreference;
import com.mercadopago.providers.CardVaultProvider;
import com.mercadopago.util.TextUtils;
import com.mercadopago.views.CardVaultView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by vaserber on 10/12/16.
 */

public class CardVaultPresenter extends MvpPresenter<CardVaultView, CardVaultProvider> {

    protected FailureRecovery mFailureRecovery;
    protected String mBin;

    //Activity parameters
    protected PaymentRecovery mPaymentRecovery;
    protected PaymentPreference mPaymentPreference;
    protected List<PaymentMethod> mPaymentMethodList;
    protected Site mSite;
    protected Boolean mInstallmentsEnabled;
    protected Boolean mInstallmentsReviewEnabled;
    protected Boolean mAutomaticSelection;
    protected String mPublicKey;
    protected BigDecimal mAmount;
    protected String mMerchantBaseUrl;
    protected String mMerchantDiscountUrl;
    protected String mMerchantGetDiscountUri;
    protected Map<String, String> mDiscountAdditionalInfo;
    protected Boolean mInstallmentsListShown;
    protected Boolean mIssuersListShown;

    //Activity result
    protected PaymentMethod mPaymentMethod;
    protected PayerCost mPayerCost;
    protected Issuer mIssuer;

    //Card Info
    protected CardInfo mCardInfo;
    protected Token mToken;
    protected Card mCard;

    //Discount
    protected Boolean mDiscountEnabled;
    protected Boolean mDirectDiscountEnabled;
    protected Discount mDiscount;
    protected String mPayerEmail;
    protected List<PayerCost> mPayerCostsList;
    protected List<Issuer> mIssuersList;

    public CardVaultPresenter() {
        super();
        this.mInstallmentsEnabled = true;
        this.mDiscountEnabled = true;
        this.mPaymentPreference = new PaymentPreference();
    }

    public void initialize() {
        try {
            validateParameters();
            onValidStart();
        } catch (IllegalStateException exception) {
            getView().showError(new MercadoPagoError(exception.getMessage(), false));
        }
    }

    private boolean viewAttached() {
        return getView() != null;
    }

    public void setPaymentRecovery(PaymentRecovery paymentRecovery) {
        this.mPaymentRecovery = paymentRecovery;
    }

    public void setPaymentPreference(PaymentPreference paymentPreference) {
        this.mPaymentPreference = paymentPreference;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.mPaymentMethodList = paymentMethodList;
    }

    public void setSite(Site site) {
        this.mSite = site;
    }

    public void setInstallmentsEnabled(Boolean installmentsEnabled) {
        this.mInstallmentsEnabled = installmentsEnabled;
    }

    public void setCard(Card card) {
        this.mCard = card;
    }

    public void setPublicKey(String publicKey) {
        this.mPublicKey = publicKey;
    }

    public void setAmount(BigDecimal amount) {
        this.mAmount = amount;
    }

    public void setFailureRecovery(FailureRecovery failureRecovery) {
        this.mFailureRecovery = failureRecovery;
    }

    public Issuer getIssuer() {
        return mIssuer;
    }

    public void setIssuer(Issuer mIssuer) {
        this.mIssuer = mIssuer;
    }

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token mToken) {
        this.mToken = mToken;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(PaymentMethod mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public PayerCost getPayerCost() {
        return mPayerCost;
    }

    public void setPayerCost(PayerCost mPayerCost) {
        this.mPayerCost = mPayerCost;
    }

    public BigDecimal getAmount() {
        return mAmount;
    }

    public PaymentRecovery getPaymentRecovery() {
        return mPaymentRecovery;
    }

    public PaymentPreference getPaymentPreference() {
        return mPaymentPreference;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return mPaymentMethodList;
    }

    public Site getSite() {
        return mSite;
    }

    public Card getCard() {
        return mCard;
    }

    public String getPublicKey() {
        return mPublicKey;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.mCardInfo = cardInfo;
        if (mCardInfo == null) {
            mBin = "";
        } else {
            mBin = mCardInfo.getFirstSixDigits();
        }
    }

    public void setPayerEmail(String payerEmail) {
        this.mPayerEmail = payerEmail;
    }

    public String getPayerEmail() {
        return this.mPayerEmail;
    }

    public void setDiscount(Discount discount) {
        this.mDiscount = discount;
    }

    public Discount getDiscount() {
        return mDiscount;
    }

    public void setDiscountEnabled(Boolean discountEnabled) {
        this.mDiscountEnabled = discountEnabled;
    }

    public Boolean getDiscountEnabled() {
        return this.mDiscountEnabled;
    }

    public void setDiscountAdditionalInfo(Map<String, String> discountAdditionalInfo) {
        this.mDiscountAdditionalInfo = discountAdditionalInfo;
    }

    public Map<String, String> getDiscountAdditionalInfo() {
        return this.mDiscountAdditionalInfo;
    }

    public void setInstallmentsReviewEnabled(Boolean installmentReviewEnabled) {
        this.mInstallmentsReviewEnabled = installmentReviewEnabled;
    }

    public Boolean getInstallmentsReviewEnabled() {
        return this.mInstallmentsReviewEnabled;
    }

    public CardInfo getCardInfo() {
        return mCardInfo;
    }

    public Integer getCardNumberLength() {
        return PaymentMethodGuessingController.getCardNumberLength(mPaymentMethod, mBin);
    }

    public void setMerchantBaseUrl(String merchantBaseUrl) {
        this.mMerchantBaseUrl = merchantBaseUrl;
    }

    public String getMerchantBaseUrl() {
        return this.mMerchantBaseUrl;
    }

    public void setMerchantDiscountBaseUrl(String merchantDiscountUrl) {
        this.mMerchantDiscountUrl = merchantDiscountUrl;
    }

    public String getMerchantDiscountBaseUrl() {
        return this.mMerchantDiscountUrl;
    }

    public void setMerchantGetDiscountUri(String merchantGetDiscountUri) {
        this.mMerchantGetDiscountUri = merchantGetDiscountUri;
    }

    public String getMerchantGetDiscountUri() {
        return mMerchantGetDiscountUri;
    }

    public void setDirectDiscountEnabled(Boolean directDiscountEnabled) {
        this.mDirectDiscountEnabled = directDiscountEnabled;
    }

    public Boolean getDirectDiscountEnabled() {
        return this.mDirectDiscountEnabled;
    }

    public void setAutomaticSelection(Boolean automaticSelection) {
        this.mAutomaticSelection = automaticSelection;
    }

    public Boolean getAutomaticSelection() {
        return mAutomaticSelection;
    }

    public Boolean isInstallmentsListShown() {
        return mInstallmentsListShown;
    }

    public Boolean isIssuersListShown() {
        return mIssuersListShown;
    }

    public void setInstallmentsListShown(Boolean installmentsListShown) {
        mInstallmentsListShown = installmentsListShown;
    }

    public void setIssuersListShown(Boolean issuersListShown) {
        mIssuersListShown = issuersListShown;
    }

    private void checkStartInstallmentsActivity() {
        if (isInstallmentsEnabled() && mPayerCost == null) {
            mInstallmentsListShown = true;
            askForInstallments();
        } else {
            getView().finishWithResult();
        }
    }

    private void askForInstallments() {
        if (mIssuersListShown) {
            getView().askForInstallmentsFromIssuers();
        } else if (!savedCardAvailable()) {
            getView().askForInstallmentsFromNewCard();
        } else {
            getView().askForInstallments();
        }
    }

    private void checkStartIssuersActivity() {
        if (mIssuer == null) {
            mIssuersListShown = true;
            getView().startIssuersActivity();
        } else {
            checkStartInstallmentsActivity();
        }
    }

    public boolean isInstallmentsEnabled() {
        return mInstallmentsEnabled;
    }

    private void validateParameters() throws IllegalStateException {
        if (mPublicKey == null) {
            throw new IllegalStateException(getResourcesProvider().getMissingPublicKeyErrorMessage());
        } else if (mInstallmentsEnabled) {
            if (mSite == null) {
                throw new IllegalStateException(getResourcesProvider().getMissingSiteErrorMessage());
            } else if (mAmount == null) {
                throw new IllegalStateException(getResourcesProvider().getMissingAmountErrorMessage());
            }
        }
    }

    public void recoverFromFailure() {
        if (mFailureRecovery != null) {
            mFailureRecovery.recover();
        }
    }

    public List<PayerCost> getPayerCostList() {
        return mPayerCostsList;
    }

    private void getInstallmentsForCardAsync(final Card card) {
        String bin = TextUtils.isEmpty(mCardInfo.getFirstSixDigits()) ? "" : mCardInfo.getFirstSixDigits();
        Long issuerId = mCard.getIssuer() == null ? null : mCard.getIssuer().getId();
        String paymentMethodId = card.getPaymentMethod() == null ? "" : card.getPaymentMethod().getId();

        getResourcesProvider().getInstallmentsAsync(bin, issuerId, paymentMethodId, getTotalAmount(), new OnResourcesRetrievedCallback<List<Installment>>() {
            @Override
            public void onSuccess(List<Installment> installments) {
                resolveInstallmentsList(installments);
            }

            @Override
            public void onFailure(MercadoPagoError error) {
                if (viewAttached()) {
                    getView().showError(error);

                    setFailureRecovery(new FailureRecovery() {
                        @Override
                        public void recover() {
                            getInstallmentsForCardAsync(card);
                        }
                    });
                }
            }
        });
    }

    private void resolveInstallmentsList(List<Installment> installments) {
        String errorMessage = null;
        if (installments.size() == 0) {
            errorMessage = getResourcesProvider().getMissingInstallmentsForIssuerErrorMessage();
        } else if (installments.size() == 1) {
            resolvePayerCosts(installments.get(0).getPayerCosts());
        } else {
            errorMessage = getResourcesProvider().getMultipleInstallmentsForIssuerErrorMessage();
        }
        if (errorMessage != null && isViewAttached()) {
            getView().showError(new MercadoPagoError(errorMessage, false));
        }
    }

    private BigDecimal getTotalAmount() {
        BigDecimal amount;

        if (!mDiscountEnabled || mDiscount == null) {
            amount = mAmount;
        } else {
            amount = mDiscount.getAmountWithDiscount(mAmount);
        }
        return amount;
    }

    private void resolvePayerCosts(List<PayerCost> payerCosts) {
        PayerCost defaultPayerCost = mPaymentPreference.getDefaultInstallments(payerCosts);
        mPayerCostsList = payerCosts;

        if (defaultPayerCost != null) {
            mPayerCost = defaultPayerCost;
            getView().askForSecurityCodeWithoutInstallments();
        } else if (mPayerCostsList.isEmpty()) {
            getView().showError(new MercadoPagoError(getResourcesProvider().getMissingPayerCostsErrorMessage(), false));
        } else if (mPayerCostsList.size() == 1) {
            mPayerCost = payerCosts.get(0);
            getView().askForSecurityCodeWithoutInstallments();
        } else {
            mInstallmentsListShown = true;
            getView().askForInstallments();
        }
    }

    public void resolveIssuersRequest(Issuer issuer) {
        mIssuersListShown = true;
        setIssuer(issuer);
        checkStartInstallmentsActivity();
    }

    public void resolveInstallmentsRequest(PayerCost payerCost, Discount discount) {
        mInstallmentsListShown = true;
        setPayerCost(payerCost);
        setDiscount(discount);

        if (savedCardAvailable()) {
            if (mInstallmentsListShown) {
                getView().askForSecurityCodeFromInstallments();
            } else {
                getView().askForSecurityCodeWithoutInstallments();
            }
        } else {
            getView().finishWithResult();
        }
    }

    public void resolveSecurityCodeRequest(Token token) {
        setToken(token);
        if (tokenRecoveryAvailable()) {
            setPayerCost(getPaymentRecovery().getPayerCost());
            setIssuer(getPaymentRecovery().getIssuer());
        }
        getView().finishWithResult();
    }

    public void resolveNewCardRequest(PaymentMethod paymentMethod, Token token, Boolean directDiscountEnabled, PayerCost payerCost,
                                      Issuer issuer, List<PayerCost> payerCosts, List<Issuer> issuers, Discount discount) {

            setPaymentMethod(paymentMethod);
            setToken(token);
            setCardInfo(new CardInfo(token));
            setDirectDiscountEnabled(directDiscountEnabled);
            setPayerCost(payerCost);
            setIssuer(issuer);
            setPayerCostsList(payerCosts);
            setIssuersList(issuers);

            if (discount != null) {
                setDiscount(discount);
            }

            checkStartIssuersActivity();
    }

    public void onResultCancel() {
        getView().cancelCardVault();
    }

    private void onValidStart() {
        mInstallmentsListShown = false;
        mIssuersListShown = false;
        if (viewAttached()) {
            getView().showProgressLayout();
        }
        if (tokenRecoveryAvailable()) {
            startTokenRecoveryFlow();
        } else if (savedCardAvailable()) {
            startSavedCardFlow();
        } else {
            startNewCardFlow();
        }
    }

    private void startTokenRecoveryFlow() {
        setCardInfo(new CardInfo(getPaymentRecovery().getToken()));
        setPaymentMethod(getPaymentRecovery().getPaymentMethod());
        setToken(getPaymentRecovery().getToken());
        getView().askForSecurityCodeFromTokenRecovery();
    }

    private void startSavedCardFlow() {
        setCardInfo(new CardInfo(getCard()));
        setPaymentMethod(getCard().getPaymentMethod());
        setIssuer(getCard().getIssuer());
        if (isInstallmentsEnabled()) {
            getInstallmentsForCardAsync(getCard());
        } else {
            getView().askForSecurityCodeWithoutInstallments();
        }
    }

    private void startNewCardFlow() {
        getView().askForCardInformation();
    }

    private boolean tokenRecoveryAvailable() {
        return getPaymentRecovery() != null && getPaymentRecovery().isTokenRecoverable();
    }

    private boolean savedCardAvailable() {
        return getCard() != null;
    }

    public void setPayerCostsList(List<PayerCost> payerCostsList) {
        this.mPayerCostsList = payerCostsList;
    }

    private void setIssuersList(List<Issuer> issuers) {
        mIssuersList = issuers;
    }

    public List<Issuer> getIssuersList() {
        return mIssuersList;
    }
}
