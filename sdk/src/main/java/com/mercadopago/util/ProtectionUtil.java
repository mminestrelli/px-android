package com.mercadopago.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadopago.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.KeyGenerator;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by vaserber on 3/31/17.
 */

public class ProtectionUtil {

    protected ProtectionUtil() {

    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasFingerPrintActivated(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                Toast.makeText(context, "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG).show();
                return false;
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //Check whether the user has granted your app the USE_FINGERPRINT permission//
                // If your app doesn't have this permission, then display the following text//
                Toast.makeText(context, "Please enable the fingerprint permission", Toast.LENGTH_LONG).show();
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                //Check that the user has registered at least one fingerprint//
                // If the user hasn’t configured any fingerprints, then display the following message//
                Toast.makeText(context, "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } else {
            Toast.makeText(context, "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasLockscreenProtected(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            if (keyguardManager.isKeyguardSecure()) {
                return true;
            } else {
                Toast.makeText(context, "This device doesn't have lockscreen enabled", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context, "Your device doesn't support fingerprint authentication", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
