package com.mercadopago.preferences;

import com.mercadopago.controllers.CheckoutTimer;

/**
 * Created by mreverter on 1/17/17.
 */
public class FlowPreference {

    private boolean paymentSearchScreenEnabled;
    private boolean reviewAndConfirmScreenEnabled;
    private boolean paymentResultScreenEnabled;
    private boolean paymentApprovedScreenEnabled;
    private boolean paymentRejectedScreenEnabled;
    private boolean paymentPendingScreenEnabled;
    private boolean bankDealsEnabled;
    private boolean installmentsReviewScreenEnabled;
    private boolean discountEnabled;
    private int congratsDisplayTime;
    private Long checkoutTimer;

    private FlowPreference(Builder builder) {
        this.paymentSearchScreenEnabled = builder.paymentSearchScreenEnabled;
        this.reviewAndConfirmScreenEnabled = builder.reviewAndConfirmScreenEnabled;
        this.paymentResultScreenEnabled = builder.paymentResultScreenEnabled;
        this.paymentApprovedScreenEnabled = builder.paymentApprovedScreenEnabled;
        this.paymentRejectedScreenEnabled = builder.paymentRejectedScreenEnabled;
        this.paymentPendingScreenEnabled = builder.paymentPendingScreenEnabled;
        this.bankDealsEnabled = builder.bankDealsEnabled;
        this.installmentsReviewScreenEnabled = builder.installmentsReviewScreenEnabled;
        this.discountEnabled = builder.discountEnabled;
        this.congratsDisplayTime = builder.congratsDisplayTime;
        this.checkoutTimer = builder.checkoutTimer;

    }

    public int getCongratsDisplayTime() {
        return this.congratsDisplayTime;
    }

    public Long getCheckoutTimerInitialTime() {
        return this.checkoutTimer;
    }

    public boolean isPaymentSearchScreenEnabled() {
        return this.paymentSearchScreenEnabled;
    }

    public boolean isReviewAndConfirmScreenEnabled() {
        return this.reviewAndConfirmScreenEnabled;
    }

    public boolean isPaymentResultScreenEnabled() {
        return this.paymentResultScreenEnabled;
    }

    public boolean isPaymentApprovedScreenEnabled() {
        return this.paymentApprovedScreenEnabled;
    }

    public boolean isPaymentRejectedScreenEnabled() {
        return this.paymentRejectedScreenEnabled;
    }

    public boolean isPaymentPendingScreenEnabled() {
        return this.paymentPendingScreenEnabled;
    }

    public boolean isBankDealsEnabled() {
        return this.bankDealsEnabled;
    }

    public boolean isInstallmentsReviewScreenEnabled() {
        return this.installmentsReviewScreenEnabled && !this.reviewAndConfirmScreenEnabled;
    }

    public boolean isDiscountEnabled() {
        return this.discountEnabled;
    }

    public void disableDiscount() {
        this.discountEnabled = false;
    }

    public boolean isCheckoutTimerEnabled() {
        return checkoutTimer != null;
    }

    public static class Builder {

        private boolean bankDealsEnabled = true;
        private boolean paymentSearchScreenEnabled = false;
        private boolean reviewAndConfirmScreenEnabled = true;
        private boolean paymentResultScreenEnabled = true;
        private boolean paymentApprovedScreenEnabled = true;
        private boolean paymentRejectedScreenEnabled = true;
        private boolean paymentPendingScreenEnabled = true;
        private boolean installmentsReviewScreenEnabled = true;
        private boolean discountEnabled = true;
        private int congratsDisplayTime;
        private Long checkoutTimer;

        public Builder enablePaymentSearchScreen() {
            this.paymentSearchScreenEnabled = true;
            return this;
        }

        public Builder disableReviewAndConfirmScreen() {
            this.reviewAndConfirmScreenEnabled = false;
            return this;
        }

        public Builder disablePaymentResultScreen() {
            this.paymentResultScreenEnabled = false;
            return this;
        }

        public Builder disablePaymentApprovedScreen() {
            this.paymentApprovedScreenEnabled = false;
            return this;
        }

        public Builder disablePaymentRejectedScreen() {
            this.paymentRejectedScreenEnabled = false;
            return this;
        }

        public Builder disablePaymentPendingScreen() {
            this.paymentPendingScreenEnabled = false;
            return this;
        }

        public Builder disableBankDeals() {
            this.bankDealsEnabled = false;
            return this;
        }

        public Builder disableInstallmentsReviewScreen() {
            this.installmentsReviewScreenEnabled = false;
            return this;
        }

        public Builder disableDiscount() {
            this.discountEnabled = false;
            return this;
        }

        public Builder setCongratsDisplayTime(int seconds) {
            this.congratsDisplayTime = seconds;
            return this;
        }

        public Builder setCheckoutTimer(Long seconds) {
            this.checkoutTimer = seconds;
            return this;
        }

        public FlowPreference build() {
            return new FlowPreference(this);
        }
    }
}
