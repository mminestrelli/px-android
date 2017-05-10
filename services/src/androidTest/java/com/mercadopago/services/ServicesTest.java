package com.mercadopago.services;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mercadopago.core.MercadoPagoServices;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ServicesTest {
    private String MLAPublicKey = "TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a";

    @Test
    public void useAppContext() throws Exception {
        MercadoPagoServices services = new MercadoPagoServices.Builder()
                .setContext(InstrumentationRegistry.getContext())
                .setPublicKey(MLAPublicKey)
                .build();
    }
}
