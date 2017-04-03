package com.mercadopago;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mercadopago.callbacks.FingerprintCallback;
import com.mercadopago.util.ProtectionHandler;
import com.mercadopago.util.ProtectionManager;
import com.mercadopago.util.ProtectionUtil;

/**
 * Created by vaserber on 4/3/17.
 */

public class FingerprintActivity extends AppCompatActivity {

    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpsdk_activity_fingerprint);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);


            if (ProtectionUtil.hasLockscreenProtected(this)) {


                if (ProtectionUtil.hasFingerPrintActivated(this)) {

                    try {
                        ProtectionManager.getInstance().generateKey("mercado_pago");
                    } catch (ProtectionManager.FingerprintException e) {
                        e.printStackTrace();
                    }

                    if (ProtectionManager.getInstance().initCipher("mercado_pago")) {
                        //If the cipher is initialized successfully, then create a CryptoObject instance//
                        cryptoObject = ProtectionManager.getInstance().createCryptoObject();
                        ProtectionHandler helper = new ProtectionHandler(this, new FingerprintCallback() {
                            @Override
                            public void onFingerprintAuthenticationSuccessResult() {
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                        helper.startAuth(fingerprintManager, cryptoObject);
                    }
                }


            }
        }
    }
}

