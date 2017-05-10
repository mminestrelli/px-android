package com.mercadopago.model;

/**
 * Created by mromar on 8/19/16.
 */
public class PaymentRecovery {
    private Token mToken;
    private String mStatus;
    private String mStatusDetail;
    private PaymentMethod mPaymentMethod;
    private PayerCost mPayerCost;
    private Issuer mIssuer;

    public PaymentRecovery(Token token, PaymentMethod paymentMethod, PayerCost payerCost, Issuer issuer, String paymentStatus, String paymentStatusDetail) {

        validate(token, paymentMethod, payerCost, issuer, paymentStatus, paymentStatusDetail);
        mToken = token;
        mPaymentMethod = paymentMethod;
        mPayerCost = payerCost;
        mIssuer = issuer;
        mStatusDetail = paymentStatusDetail;
    }

    private void validate(Token token, PaymentMethod paymentMethod, PayerCost payerCost, Issuer issuer, String paymentStatus, String paymentStatusDetail) {
        if (token == null) {
            throw new IllegalStateException("token is null");
        }

        if (paymentMethod == null) {
            throw new IllegalStateException("payment method is null");
        }

        if (payerCost == null) {
            throw new IllegalStateException("payer cost is null");
        }

        if (issuer == null) {
            throw new IllegalStateException("issuer is null");
        }

        if (!isRecoverablePaymentStatus(paymentStatus, paymentStatusDetail)) {
            throw new IllegalStateException("this payment is not recoverable");
        }
    }

    public Token getToken() {
        return mToken;
    }

    public PaymentMethod getPaymentMethod() {
        return mPaymentMethod;
    }

    public PayerCost getPayerCost() {
        return mPayerCost;
    }

    public Issuer getIssuer() {
        return mIssuer;
    }

    public boolean isTokenRecoverable() {
        return isStatusDetailCallForAuthorize(mStatusDetail);
    }

    private boolean isRecoverablePaymentStatus(String paymentStatus, String paymentStatusDetail) {
        return Payment.StatusCodes.STATUS_REJECTED.equals(paymentStatus) && isPaymentStatusRecoverable(paymentStatusDetail);
    }

    private boolean isPaymentStatusRecoverable(String statusDetail) {
        return Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_OTHER.equals(statusDetail) ||
                Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_CARD_NUMBER.equals(statusDetail) ||
                Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_SECURITY_CODE.equals(statusDetail) ||
                Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_BAD_FILLED_DATE.equals(statusDetail) ||
                Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(statusDetail);
    }

    private Boolean isStatusDetailCallForAuthorize(String statusDetail) {
        return statusDetail != null &&
                Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CALL_FOR_AUTHORIZE.equals(statusDetail);
    }

}
