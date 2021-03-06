package com.mercadopago.paymentvault;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.callbacks.OnSelectedCallback;
import com.mercadopago.constants.PaymentTypes;
import com.mercadopago.constants.Sites;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.Campaign;
import com.mercadopago.model.Card;
import com.mercadopago.model.CustomSearchItem;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Payer;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearch;
import com.mercadopago.model.PaymentMethodSearchItem;
import com.mercadopago.model.PaymentPreference;
import com.mercadopago.model.Site;
import com.mercadopago.mvp.OnResourcesRetrievedCallback;
import com.mercadopago.presenters.PaymentVaultPresenter;
import com.mercadopago.providers.PaymentVaultProvider;
import com.mercadopago.utils.Campaigns;
import com.mercadopago.utils.Discounts;
import com.mercadopago.utils.PaymentMethodSearchs;
import com.mercadopago.views.PaymentVaultView;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by mreverter on 1/30/17.
 */

public class PaymentVaultPresenterTest {

    @Test
    public void ifSiteNotSetShowInvalidSiteError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_SITE, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidCurrencySetShowInvalidSiteError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(new Site("invalid_id", "invalid_currency"));
        presenter.setAmount(BigDecimal.TEN);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_SITE, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifAmountNotSetShowInvalidAmountError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_AMOUNT, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidAmountSetShowInvalidAmountError() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN.negate());

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_AMOUNT, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifPaymentMethodSearchHasItemsShowThem() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertEquals(paymentMethodSearch.getGroups(), mockedView.searchItemsShown);
    }

    @Test
    public void ifPaymentMethodSearchHasPayerCustomOptionsShowThem() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertEquals(paymentMethodSearch.getCustomSearchItems(), mockedView.customOptionsShown);
    }

    //Automatic selections

    @Test
    public void ifOnlyUniqueSearchItemAvailableRestartWithItSelected() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyTicketMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertEquals(paymentMethodSearch.getGroups().get(0), mockedView.itemShown);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableStartCardFlow() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertTrue(mockedView.cardFlowStarted);
        assertEquals(paymentMethodSearch.getGroups().get(0).getId(), mockedView.selectedPaymentType);
        assertEquals(amount, mockedView.amountSentToCardFlow);

    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableAndCardAvailableDoNotSelectAutomatically() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndOneCardMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyCardPaymentTypeAvailableAndAccountMoneyAvailableDoNotSelectAutomatically() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyCreditCardAndAccountMoneyMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAccountMoneyEnabled(true);
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyOffPaymentTypeAvailableAndAccountMoneyAvailableDoNotSelectAutomatically() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyOneOffTypeAndAccountMoneyMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAccountMoneyEnabled(true);
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertTrue(mockedView.customOptionsShown != null);
        assertFalse(mockedView.cardFlowStarted);
        assertFalse(mockedView.isItemShown);
    }

    @Test
    public void ifOnlyAccountMoneySelectItAutomatically() {

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodSearchWithOnlyAccountMoneyMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        BigDecimal amount = BigDecimal.TEN;
        presenter.setAccountMoneyEnabled(true);
        presenter.setAmount(amount);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        assertEquals(paymentMethodSearch.getCustomSearchItems().get(0).getPaymentMethodId(), mockedView.selectedPaymentMethod.getId());
    }

    //User selections

    @Test
    public void ifItemSelectedShowItsChildren() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateItemSelection(1);

        assertEquals(paymentMethodSearch.getGroups().get(1).getChildren(), mockedView.searchItemsShown);
        assertEquals(paymentMethodSearch.getGroups().get(1), mockedView.itemShown);
    }

    @Test
    public void ifCardPaymentTypeSelectedStartCardFlow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertEquals(paymentMethodSearch.getGroups().get(0).getId(), mockedView.selectedPaymentType);
        assertTrue(mockedView.cardFlowStarted);
    }

    @Test
    public void ifSavedCardSelectedStartSavedCardFlow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateCustomItemSelection(1);

        assertTrue(mockedView.savedCardFlowStarted);
        assertTrue(mockedView.savedCardSelected.equals(paymentMethodSearch.getCards().get(0)));
    }

    @Test
    public void ifAccountMoneySelectedSelectIt() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateCustomItemSelection(0);

        assertEquals(paymentMethodSearch.getCustomSearchItems().get(0).getPaymentMethodId(), mockedView.selectedPaymentMethod.getId());
    }

    //Payment Preference tests
    @Test
    public void ifAllPaymentTypesExcludedShowError() {

        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setExcludedPaymentTypeIds(PaymentTypes.getAllPaymentTypes());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize();

        assertEquals(MockedProvider.ALL_TYPES_EXCLUDED, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidDefaultInstallmentsShowError() {

        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setDefaultInstallments(BigDecimal.ONE.negate().intValue());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_DEFAULT_INSTALLMENTS, mockedView.errorShown.getMessage());
    }

    @Test
    public void ifInvalidMaxInstallmentsShowError() {

        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setMaxAcceptedInstallments(BigDecimal.ONE.negate().intValue());

        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getPaymentMethodWithoutCustomOptionsMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setSite(Sites.ARGENTINA);
        presenter.setAmount(BigDecimal.TEN);
        presenter.setPaymentPreference(paymentPreference);

        presenter.initialize();

        assertEquals(MockedProvider.INVALID_MAX_INSTALLMENTS, mockedView.errorShown.getMessage());
    }

    //Discounts
    @Test
    public void ifDiscountsAreNotEnabledNotShowDiscountRow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setDiscountEnabled(false);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertTrue(mockedView.showedDiscountRow);
        assertTrue(presenter.getDiscount() == null);
    }

    @Test
    public void ifDiscountsAreEnabledGetDirectDiscount() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        Discount discount = Discounts.getDiscountWithAmountOffMLA();
        provider.setDiscountResponse(discount);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertEquals(discount.getId(), presenter.getDiscount().getId());
    }

    @Test
    public void ifHasNotDirectDiscountsShowDiscountRow() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        ApiException apiException = Discounts.getDoNotFindCampaignApiException();
        MPException mpException = new MPException(apiException);
        provider.setDiscountResponse(mpException);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertTrue(provider.failedResponse.getApiException().getError().equals(provider.CAMPAIGN_DOES_NOT_MATCH_ERROR));
        assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void ifIsDirectDiscountNotEnabledNotGetDirectDiscount() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);
        presenter.setDirectDiscountEnabled(false);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertTrue(mockedView.showedDiscountRow);
    }

    @Test
    public void ifHasNotDirectDiscountSetFalseDirectDiscountEnabled() {
        MockedView mockedView = new MockedView();
        MockedProvider provider = new MockedProvider();

        PaymentMethodSearch paymentMethodSearch = PaymentMethodSearchs.getCompletePaymentMethodSearchMLA();
        provider.setPaymentMethodSearchResponse(paymentMethodSearch);

        List<Campaign> campaigns = Campaigns.getCampaigns();
        provider.setCampaignsResponse(campaigns);

        ApiException apiException = Discounts.getDoNotFindCampaignApiException();
        MPException mpException = new MPException(apiException);
        provider.setDiscountResponse(mpException);

        PaymentVaultPresenter presenter = new PaymentVaultPresenter();
        presenter.attachView(mockedView);
        presenter.attachResourcesProvider(provider);

        presenter.setAmount(BigDecimal.TEN);
        presenter.setSite(Sites.ARGENTINA);

        presenter.initialize();

        mockedView.simulateItemSelection(0);

        assertFalse(presenter.getDirectDiscountEnabled());
        assertTrue(mockedView.showedDiscountRow);
    }

    private class MockedProvider implements PaymentVaultProvider {

        private static final String INVALID_SITE = "invalid site";
        private static final String INVALID_AMOUNT = "invalid amount";
        private static final String ALL_TYPES_EXCLUDED = "all types excluded";
        private static final String INVALID_DEFAULT_INSTALLMENTS = "invalid default installments";
        private static final String INVALID_MAX_INSTALLMENTS = "invalid max installments";
        private static final String STANDARD_ERROR_MESSAGE = "standard error";
        private static final String EMPTY_PAYMENT_METHODS = "empty payment methods";
        private static final String CAMPAIGN_DOES_NOT_MATCH_ERROR = "campaign-doesnt-match";

        private boolean shouldFail;
        private boolean shouldDiscountFail;
        private boolean shouldCampaignFail;
        private PaymentMethodSearch successfulResponse;
        private Discount successfulDiscountResponse;
        private List<Campaign> successfulCampaignsResponse;
        private MPException failedResponse;

        private void setPaymentMethodSearchResponse(PaymentMethodSearch paymentMethodSearch) {
            shouldFail = false;
            successfulResponse = paymentMethodSearch;
        }

        public void setPaymentMethodSearchResponse(MPException exception) {
            shouldFail = true;
            failedResponse = exception;
        }

        private void setDiscountResponse(Discount discount) {
            shouldDiscountFail = false;
            successfulDiscountResponse = discount;
        }

        private void setDiscountResponse(MPException exception) {
            shouldDiscountFail = true;
            failedResponse = exception;
        }

        private void setCampaignsResponse(List<Campaign> campaigns) {
            shouldDiscountFail = false;
            successfulCampaignsResponse = campaigns;
        }

        private void setCampaingsResponse(MPException exception) {
            shouldCampaignFail = true;
            failedResponse = exception;
        }

        @Override
        public String getTitle() {
            return "¿Cómo quieres pagar?";
        }

        @Override
        public void getPaymentMethodSearch(BigDecimal amount, PaymentPreference paymentPreference, Payer payer, Boolean accountMoneyEnabled, OnResourcesRetrievedCallback<PaymentMethodSearch> onResourcesRetrievedCallback) {
            if (shouldFail) {
                onResourcesRetrievedCallback.onFailure(failedResponse);
            } else {
                onResourcesRetrievedCallback.onSuccess(successfulResponse);
            }
        }

        @Override
        public void getDirectDiscount(String amount, String payerEmail, OnResourcesRetrievedCallback<Discount> onResourcesRetrievedCallback) {
            if (shouldDiscountFail) {
                onResourcesRetrievedCallback.onFailure(failedResponse);
            } else {
                onResourcesRetrievedCallback.onSuccess(successfulDiscountResponse);
            }
        }

        @Override
        public void getCampaigns(OnResourcesRetrievedCallback<List<Campaign>> onResourcesRetrievedCallback) {
            if (shouldCampaignFail) {
                onResourcesRetrievedCallback.onFailure(failedResponse);
            } else {
                onResourcesRetrievedCallback.onSuccess(successfulCampaignsResponse);
            }
        }

        @Override
        public String getInvalidSiteConfigurationErrorMessage() {
            return INVALID_SITE;
        }

        @Override
        public String getInvalidAmountErrorMessage() {
            return INVALID_AMOUNT;
        }

        @Override
        public String getAllPaymentTypesExcludedErrorMessage() {
            return ALL_TYPES_EXCLUDED;
        }

        @Override
        public String getInvalidDefaultInstallmentsErrorMessage() {
            return INVALID_DEFAULT_INSTALLMENTS;
        }

        @Override
        public String getInvalidMaxInstallmentsErrorMessage() {
            return INVALID_MAX_INSTALLMENTS;
        }

        @Override
        public String getStandardErrorMessage() {
            return STANDARD_ERROR_MESSAGE;
        }

        @Override
        public String getEmptyPaymentMethodsErrorMessage() {
            return EMPTY_PAYMENT_METHODS;
        }
    }

    private class MockedView implements PaymentVaultView {

        private List<PaymentMethodSearchItem> searchItemsShown;
        private MPException errorShown;
        private List<CustomSearchItem> customOptionsShown;
        private PaymentMethodSearchItem itemShown;
        private boolean cardFlowStarted;
        private BigDecimal amountSentToCardFlow;
        private boolean isItemShown;
        private PaymentMethod selectedPaymentMethod;
        private OnSelectedCallback<PaymentMethodSearchItem> itemSelectionCallback;
        private OnSelectedCallback<CustomSearchItem> customItemSelectionCallback;
        private String title;
        private boolean savedCardFlowStarted;
        private Card savedCardSelected;
        private String selectedPaymentType;
        private Boolean showedDiscountRow;

        @Override
        public void startSavedCardFlow(Card card, BigDecimal transactionAmount) {
            this.savedCardFlowStarted = true;
            this.savedCardSelected = card;
        }

        @Override
        public void restartWithSelectedItem(PaymentMethodSearchItem item) {
            this.itemShown = item;
            this.isItemShown = true;
            this.searchItemsShown = item.getChildren();
        }

        @Override
        public void showProgress() {
            //Not yet tested
        }

        @Override
        public void hideProgress() {
            //Not yet tested
        }

        @Override
        public void showCustomOptions(List<CustomSearchItem> customSearchItems, OnSelectedCallback<CustomSearchItem> customSearchItemOnSelectedCallback) {
            this.customOptionsShown = customSearchItems;
            this.customItemSelectionCallback = customSearchItemOnSelectedCallback;
        }

        @Override
        public void showSearchItems(List<PaymentMethodSearchItem> searchItems, OnSelectedCallback<PaymentMethodSearchItem> paymentMethodSearchItemSelectionCallback) {
            this.searchItemsShown = searchItems;
            this.itemSelectionCallback = paymentMethodSearchItemSelectionCallback;
        }

        @Override
        public void showError(MPException mpException) {
            errorShown = mpException;
        }

        @Override
        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public void setFailureRecovery(FailureRecovery failureRecovery) {
            //Not yet tested
        }

        @Override
        public void startCardFlow(String paymentType, BigDecimal transactionAmount) {
            cardFlowStarted = true;
            amountSentToCardFlow = transactionAmount;
            selectedPaymentType = paymentType;
        }

        @Override
        public void startPaymentMethodsActivity() {
            //Not yet tested
        }

        @Override
        public void selectPaymentMethod(PaymentMethod selectedPaymentMethod) {
            this.selectedPaymentMethod = selectedPaymentMethod;
        }

        @Override
        public void showDiscountRow(BigDecimal transactionAmount) {
            this.showedDiscountRow = true;
        }

        @Override
        public void startDiscountActivity(BigDecimal transactionAmount) {
            //Not yet tested
        }

        @Override
        public void cleanPaymentMethodOptions() {
            //Not yet tested
        }

        private void simulateItemSelection(int index) {
            itemSelectionCallback.onSelected(searchItemsShown.get(index));
        }

        private void simulateCustomItemSelection(int index) {
            customItemSelectionCallback.onSelected(customOptionsShown.get(index));
        }
    }

}
