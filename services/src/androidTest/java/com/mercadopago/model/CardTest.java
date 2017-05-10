package com.mercadopago.model;

import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.CheckoutActivity;
import com.mercadopago.test.BaseTest;
import com.mercadopago.test.StaticMock;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CardTest {

    public void testIsSecurityCodeRequired() {

        Card card = StaticMock.getCard();

        assertTrue(card.isSecurityCodeRequired());
    }

    public void testIsSecurityCodeRequiredNull() {

        Card card = StaticMock.getCard();
        card.setSecurityCode(null);
        assertTrue(!card.isSecurityCodeRequired());
    }

    public void testIsSecurityCodeRequiredLengthZero() {

        Card card = StaticMock.getCard();
        card.getSecurityCode().setLength(0);
        assertTrue(!card.isSecurityCodeRequired());
    }
}
