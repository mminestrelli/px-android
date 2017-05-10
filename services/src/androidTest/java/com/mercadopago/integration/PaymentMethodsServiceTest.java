package com.mercadopago.integration;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.callbacks.Callback;
import com.mercadopago.core.MercadoPagoServices;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.PaymentMethod;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.fail;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Parameterized.class)
public class PaymentMethodsServiceTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //MLA
                {"TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a", "TEST-402488498418864-031613-65d8b170a5c555a0df48d10e8524b187__LA_LC__-247223140"},
                //MLB
                {"TEST-77d0c0c0-0594-4951-ac78-16c03461fcbc", "TEST-6400917752854864-022412-27383fe2f9833e3b3bedd7ea75decdf3__LB_LD__-245099733"},
                //MLM
                {"TEST-0f375857-0881-447c-9b2b-23e97b93f947", "TEST-1734141614479586-042514-1e69ee59c4448cbb243e14745643db92__LB_LC__-253812950"},
                //MCO
                {"TEST-b17d8f8e-5039-4d58-a99f-7a66872741ca", "TEST-4072574620869000-013114-855428e52d442f626473f558495c98be__LD_LA__-242624092"},
                //MLV
                {"TEST-b3d856f6-375e-4f8c-9573-ad28ab8fac95", "TEST-5340596886674075-020209-03897d0bfc450770ebe5f65cac384b19__LA_LB__-242626852"},
                //MPE
                {"TEST-bdd07ce7-a827-45e7-802f-72f0162b9c8c", "TEST-43338635847972-013113-3a484890323ac91eda76434b72989071__LB_LD__-242617753"},
                //MLC
                {"TEST-0baa73db-70a3-4e8a-8f1b-f8f922cf3ae3", "TEST-2976878176839721-013115-90a540e39735b7b1fd8255019ce899b8__LD_LC__-242625384"},
        });
    }

    public String publicKey;
    public String privateKey;

    public PaymentMethodsServiceTest(String publicKey, String privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Test
    public void testAPIResponseWithPublicKey() {

        MercadoPagoServices MLAPublicCredentialServices = new MercadoPagoServices.Builder()
                .setContext(InstrumentationRegistry.getContext())
                .setPublicKey(publicKey)
                .build();

        MLAPublicCredentialServices.getPaymentMethods(new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods) {
                if(paymentMethods == null) {
                    fail("Payment methods null");
                }
                for(PaymentMethod paymentMethod : paymentMethods) {
                    if(!isValidPaymentMethod(paymentMethods.get(0))) {
                        fail(paymentMethod.toString());
                    }
                }
            }

            @Override
            public void failure(ApiException apiException) {
                fail("Should not fail. " + apiException);
            }
        });
    }

    @Test
    public void testAPIResponseWithPrivateKey() throws Exception {

        MercadoPagoServices MLAPublicCredentialServices = new MercadoPagoServices.Builder()
                .setContext(InstrumentationRegistry.getContext())
                .setPrivateKey(privateKey)
                .build();

        MLAPublicCredentialServices.getPaymentMethods(new Callback<List<PaymentMethod>>() {
            @Override
            public void success(List<PaymentMethod> paymentMethods) {
                if(paymentMethods == null || paymentMethods.isEmpty()) {
                    fail("Payment methods null or empty");
                }
                for(PaymentMethod paymentMethod : paymentMethods) {
                    if(!isValidPaymentMethod(paymentMethods.get(0))) {
                        fail(paymentMethod.toString());
                    }
                }
            }

            @Override
            public void failure(ApiException apiException) {
                fail("Should not fail. " + apiException);
            }
        });
    }

    private boolean isValidPaymentMethod(PaymentMethod paymentMethod) {
        return !paymentMethod.getId().isEmpty()
                && !paymentMethod.getName().isEmpty()
                && !paymentMethod.getPaymentTypeId().isEmpty()
                && !paymentMethod.getSettings().isEmpty();
    }
}
