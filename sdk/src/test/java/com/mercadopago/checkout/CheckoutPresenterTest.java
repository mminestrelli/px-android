package com.mercadopago.checkout;

import com.mercadopago.constants.Sites;
import com.mercadopago.core.MercadoPagoCheckout;
import com.mercadopago.exceptions.CheckoutPreferenceException;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.mocks.Discounts;
import com.mercadopago.mocks.Installments;
import com.mercadopago.mocks.Issuers;
import com.mercadopago.mocks.PaymentMethods;
import com.mercadopago.mocks.Payments;
import com.mercadopago.mocks.Tokens;
import com.mercadopago.model.Campaign;
import com.mercadopago.model.Customer;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Issuer;
import com.mercadopago.model.Item;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PayerCost;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentData;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.model.PaymentRecovery;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.model.Site;
import com.mercadopago.model.Token;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.preferences.CheckoutPreference;
import com.mercadopago.preferences.FlowPreference;
import com.mercadopago.presenters.CheckoutPresenter;
import com.mercadopago.providers.CheckoutProvider;
import com.mercadopago.utils.PaymentMethodSearchs;
import com.mercadopago.views.CheckoutView;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CheckoutPresenterTest {

    //Validations
    @Test
    public void onCheckoutInitializedWithoutCheckoutPreferenceThenShowError() {

        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        presenter.initialize();
        assertTrue(view.isErrorShown);
    }

    //Discounts
    @Test
    public void ifDiscountNotSetAndDiscountsEnabledThenGetDiscountCampaigns() {

        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Item", BigDecimal.TEN))
                .build();

        presenter.setCheckoutPreference(preference);

        provider.setCampaignsResponse(Discounts.getCampaigns());
        presenter.initialize();
        assertTrue(provider.campaignsRequested);
    }

    @Test
    public void ifDirectDiscountCampaignAvailableThenRequestDirectDiscount() {

        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Item", BigDecimal.TEN))
                .build();

        presenter.setCheckoutPreference(preference);

        provider.setCampaignsResponse(Discounts.getCampaigns());
        presenter.initialize();
        assertTrue(provider.directDiscountRequested);
    }

    @Test
    public void ifNullCampaignsRetrievedThenDisableDiscounts() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Item", BigDecimal.TEN))
                .build();

        presenter.setCheckoutPreference(preference);

        presenter.initialize();
        assertFalse(presenter.isDiscountEnabled());
    }

    @Test
    public void ifEmptyCampaignsRetrievedThenDisableDiscounts() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Item", BigDecimal.TEN))
                .build();

        presenter.setCheckoutPreference(preference);

        provider.setCampaignsResponse(new ArrayList<Campaign>());
        presenter.initialize();
        assertFalse(presenter.isDiscountEnabled());
    }

    //Preferences configuration

    @Test
    public void ifPreferenceSetHasIdThenRetrievePreferenceFromMercadoPago() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setId("dummy id")
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        presenter.setCheckoutPreference(preference);

        presenter.initialize();
        assertTrue(provider.checkoutPreferenceRequested);
    }

    @Test
    public void ifPreferenceSetDoesNotHaveIdThenDoNotRetrievePreferenceFromMercadoPago() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);

        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Dummy", BigDecimal.TEN))
                .build();

        presenter.setCheckoutPreference(preference);

        presenter.initialize();
        assertFalse(provider.checkoutPreferenceRequested);
    }

    @Test
    public void ifPreferenceIsInvalidThenShowError() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .setId("Dummy Id")
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        presenter.setCheckoutPreference(preference);
        presenter.initialize();
        assertTrue(view.isErrorShown);
    }

    @Test
    public void ifCheckoutInitiatedThenRequestPaymentMethodSearch() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .addItem(new Item("id", BigDecimal.TEN))
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        presenter.setCheckoutPreference(preference);
        presenter.initialize();
        assertTrue(provider.paymentMethodSearchRequested);
    }

    //Flow started

    @Test
    public void ifCheckoutInitiatedThenStartPaymentMethodSelection() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .addItem(new Item("id", BigDecimal.TEN))
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        provider.setPaymentMethodSearchResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        presenter.setCheckoutPreference(preference);
        presenter.initialize();
        assertTrue(view.paymentMethodSelectionShown);
    }

    //Response from payment methodSelection
    @Test
    public void ifOkPaymentMethodSelectionResponseReceivedThenStartRyC() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .addItem(new Item("id", BigDecimal.TEN))
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        provider.setPaymentMethodSearchResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        presenter.setCheckoutPreference(preference);
        presenter.initialize();

        //Payment method off, no issuer, installments or token
        presenter.onPaymentMethodSelectionResponse(PaymentMethods.getPaymentMethodOff(), null, null, null, null);
        assertTrue(view.reviewAndConfirmShown);
    }


    //Review and confirm disabled

    @Test
    public void ifPaymentDataRequestedAndReviewConfirmDisabledThenFinishWithPaymentData() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .addItem(new Item("id", BigDecimal.TEN))
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        provider.setPaymentMethodSearchResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());

        FlowPreference flowPreference = new FlowPreference.Builder()
                .disableReviewAndConfirmScreen()
                .build();
        presenter.setRequestedResult(MercadoPagoCheckout.PAYMENT_DATA_RESULT_CODE);
        presenter.setCheckoutPreference(preference);
        presenter.setFlowPreference(flowPreference);
        presenter.initialize();

        //Payment method off, no issuer, installments or token
        PaymentMethod paymentMethod = PaymentMethods.getPaymentMethodOff();
        presenter.onPaymentMethodSelectionResponse(paymentMethod, null, null, null, null);
        assertEquals(paymentMethod.getId(), view.paymentDataFinalResponse.getPaymentMethod().getId());
    }

    @Test
    public void ifPaymentRequestedAndReviewConfirmDisabledThenStartPaymentResultScreen() {
        MockedProvider provider = new MockedProvider();
        MockedView view = new MockedView();

        CheckoutPresenter presenter = new CheckoutPresenter();
        presenter.attachResourcesProvider(provider);
        presenter.attachView(view);


        //Real preference, without items
        CheckoutPreference preference = new CheckoutPreference.Builder()
                .addItem(new Item("id", BigDecimal.TEN))
                .setSite(Sites.ARGENTINA)
                .build();

        provider.setCheckoutPreferenceResponse(preference);
        provider.setPaymentMethodSearchResponse(PaymentMethodSearchs.getCompletePaymentMethodSearchMLA());
        provider.setPaymentResponse(Payments.getApprovedPayment());

        FlowPreference flowPreference = new FlowPreference.Builder()
                .disableReviewAndConfirmScreen()
                .build();

        presenter.setRequestedResult(MercadoPagoCheckout.PAYMENT_RESULT_CODE);
        presenter.setCheckoutPreference(preference);
        presenter.setFlowPreference(flowPreference);
        presenter.initialize();

        PaymentMethod paymentMethod = PaymentMethods.getPaymentMethodOn();
        Issuer issuer = Issuers.getIssuers().get(0);
        PayerCost payerCost = Installments.getInstallments().getPayerCosts().get(0);
        Token token = Tokens.getVisaToken();

        presenter.onPaymentMethodSelectionResponse(paymentMethod, issuer, payerCost, token, null);
        assertTrue(view.paymentResultShown);
    }


    public class MockedView implements CheckoutView {

        private MercadoPagoError errorShown;
        private boolean isErrorShown = false;
        public boolean paymentMethodSelectionShown = false;
        public boolean reviewAndConfirmShown = false;
        public PaymentData paymentDataFinalResponse;
        public boolean paymentResultShown = false;

        @Override
        public void showError(MercadoPagoError error) {
            this.isErrorShown = true;
            this.errorShown = error;
        }

        @Override
        public void showProgress() {

        }

        @Override
        public void showReviewAndConfirm() {
            reviewAndConfirmShown = true;
        }

        @Override
        public void showPaymentMethodSelection() {
            paymentMethodSelectionShown = true;
        }

        @Override
        public void showPaymentResult(PaymentResult paymentResult) {
            paymentResultShown = true;
        }

        @Override
        public void backToReviewAndConfirm() {

        }

        @Override
        public void backToPaymentMethodSelection() {

        }

        @Override
        public void finishWithPaymentResult() {

        }

        @Override
        public void finishWithPaymentResult(Integer customResultCode) {

        }

        @Override
        public void finishWithPaymentResult(Payment payment) {

        }

        @Override
        public void finishWithPaymentResult(Integer customResultCode, Payment payment) {

        }

        @Override
        public void finishWithPaymentDataResult(PaymentData paymentData, Boolean paymentMethodEdited) {
            paymentDataFinalResponse = paymentData;
        }

        @Override
        public void cancelCheckout() {

        }

        @Override
        public void cancelCheckout(MercadoPagoError mercadoPagoError) {

        }

        @Override
        public void cancelCheckout(Integer customResultCode, PaymentData paymentData, Boolean paymentMethodEdited) {

        }

        @Override
        public void startPaymentRecoveryFlow(PaymentRecovery paymentRecovery) {

        }
    }

    public class MockedProvider implements CheckoutProvider {

        private boolean campaignsRequested = false;
        private boolean directDiscountRequested = false;
        private List<Campaign> campaigns;
        private boolean checkoutPreferenceRequested = false;
        private CheckoutPreference preference;
        private boolean paymentMethodSearchRequested = false;
        private PaymentMethodSearch paymentMethodSearchResponse;
        private Payment paymentResponse;

        @Override
        public void getCheckoutPreference(String checkoutPreferenceId, OnResourcesRetrievedCallback<CheckoutPreference> onResourcesRetrievedCallback) {
            checkoutPreferenceRequested = true;
            onResourcesRetrievedCallback.onSuccess(preference);
        }

        @Override
        public void getDiscountCampaigns(OnResourcesRetrievedCallback<List<Campaign>> callback) {
            this.campaignsRequested = true;
            callback.onSuccess(campaigns);
        }

        @Override
        public void getDirectDiscount(BigDecimal amount, String payerEmail, OnResourcesRetrievedCallback<Discount> onResourcesRetrievedCallback) {
            this.directDiscountRequested = true;
        }

        @Override
        public void getPaymentMethodSearch(BigDecimal amount, List<String> excludedPaymentTypes, List<String> excludedPaymentMethods, Payer payer, Site site, OnResourcesRetrievedCallback<PaymentMethodSearch> onPaymentMethodSearchRetrievedCallback, OnResourcesRetrievedCallback<Customer> onCustomerRetrievedCallback) {
            this.paymentMethodSearchRequested = true;
            onPaymentMethodSearchRetrievedCallback.onSuccess(paymentMethodSearchResponse);
        }

        @Override
        public String getCheckoutExceptionMessage(CheckoutPreferenceException exception) {
            return null;
        }

        @Override
        public String getCheckoutExceptionMessage(IllegalStateException exception) {
            return null;
        }

        @Override
        public void createPayment(String transactionId, CheckoutPreference checkoutPreference, PaymentData paymentData, Boolean binaryMode, String customerId, OnResourcesRetrievedCallback<Payment> onResourcesRetrievedCallback) {
            onResourcesRetrievedCallback.onSuccess(paymentResponse);
        }

        public void setCampaignsResponse(List<Campaign> campaigns) {
            this.campaigns = campaigns;
        }

        public void setCheckoutPreferenceResponse(CheckoutPreference preference) {
            this.preference = preference;
        }

        public void setPaymentMethodSearchResponse(PaymentMethodSearch paymentMethodSearchResponse) {
            this.paymentMethodSearchResponse = paymentMethodSearchResponse;
        }

        public void setPaymentResponse(Payment paymentResponse) {
            this.paymentResponse = paymentResponse;
        }
    }
}
