package com.mercadopago.views;

import com.mercadopago.callbacks.FailureRecovery;
import com.mercadopago.exceptions.MercadoPagoError;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.CardToken;
import com.mercadopago.model.PayerCost;
import com.mercadopago.mvp.MvpView;

import java.util.List;

/**
 * Created by vaserber on 10/12/16.
 */

public interface CardVaultView extends MvpView {

    void finishWithResult();

    void showApiExceptionError(ApiException exception);

    void showError(MercadoPagoError mercadoPagoError);

    void askForInstallments();

    void showIssuersSelection();

    void showProgressLayout();

    void askForCardInformation();

    void askForSecurityCodeFromTokenRecovery();

    void askForSecurityCodeFromInstallments();

    void askForSecurityCodeWithoutInstallments();

    void askForInstallmentsFromIssuers();

    void askForInstallmentsFromNewCard();

    void cancelCardVault();

    void backToCardForm();

    void backToIssuersSelection();
}
