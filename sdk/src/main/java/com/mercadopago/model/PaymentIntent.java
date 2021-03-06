package com.mercadopago.model;

import com.google.gson.annotations.SerializedName;

public class PaymentIntent {

    private Long transactionId;
    private Integer installments;
    private Long issuerId;
    private String paymentMethodId;
    private String prefId;
    @SerializedName("token")
    private String tokenId;
    private Boolean binaryMode;
    private String publicKey;
    private String email;
    private String couponCode;
    private Payer payer;
    private Float couponAmount;
    private Integer campaignId;

    public void setCouponAmount(Float couponAmount) {
        this.couponAmount = couponAmount;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPrefId() {
        return prefId;
    }

    public void setPrefId(String prefId) {
        this.prefId = prefId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public void setBinaryMode(Boolean binaryMode) {
        this.binaryMode = binaryMode;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public Boolean getBinaryMode() {
        return this.binaryMode;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public Payer getPayer() {
        return payer;
    }

    public Float getCouponAmount() {
        return this.couponAmount;
    }

    public Integer getCampaignId() {
        return this.campaignId;
    }
}
