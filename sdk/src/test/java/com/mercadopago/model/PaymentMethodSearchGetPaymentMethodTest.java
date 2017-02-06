package com.mercadopago.model;

import com.mercadopago.utils.PaymentMethodsSearch;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by mreverter on 27/4/16.
 */
@RunWith(Parameterized.class)
public class PaymentMethodSearchGetPaymentMethodTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
                {"oxxo", "oxxo", "ticket"},
                {"bancomer_bank_transfer", "bancomer", "bank_transfer"},
                {"bancomer.ticket", "bancomer", "ticket"},
                {"banamex.bank_transfer", "banamex", "bank_transfer"},
                {"banamex_ticket", "banamex", "ticket"},
                {"serfin_bank_transfer", "serfin", "bank_transfer"},
                {"serfin.ticket", "serfin", "ticket"},
                {"pagoefectivo_atm_pagoefectivo_atm", "pagoefectivo_atm", "pagoefectivo_atm"},
                {"invalid_item", "invalid_item", ""}
        });
    }

    public PaymentMethodSearch paymentMethodSearch;

    public String mItemId;

    public String mPaymentMethodId;

    public String mPaymentTypeId;


    public PaymentMethodSearchGetPaymentMethodTest(String itemId, String paymentMethodId, String paymentTypeId){
        this.paymentMethodSearch = getPaymentMethodSearch();
        this.mItemId = itemId;
        this.mPaymentMethodId = paymentMethodId;
        this.mPaymentTypeId = paymentTypeId;
    }

    @Test
    public void testGetPaymentMethodByItem() {
        PaymentMethodSearchItem item = new PaymentMethodSearchItem();
        item.setId(mItemId);

        PaymentMethod paymentMethod = paymentMethodSearch.getPaymentMethodBySearchItem(item);

        if(paymentMethod != null) {
            Assert.assertEquals(mPaymentMethodId, paymentMethod.getId());
            Assert.assertEquals(mPaymentTypeId, paymentMethod.getPaymentTypeId());
        } else {
            Assert.assertEquals(mPaymentMethodId, "invalid_item");
        }
    }

    private PaymentMethodSearch getPaymentMethodSearch() {
        PaymentMethodSearch paymentMethodSearch = PaymentMethodsSearch.getPaymentMethodSearchWithoutCustomOptionsAsJson();
        return paymentMethodSearch;
    }
}