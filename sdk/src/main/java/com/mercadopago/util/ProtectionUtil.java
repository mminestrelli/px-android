package com.mercadopago.util;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.mercadopago.R;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by vaserber on 3/31/17.
 */

public class ProtectionUtil {

    protected ProtectionUtil() {

    }

    public static boolean hasFingerPrintActivated(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
//                textView.setText("Your device doesn't support fingerprint authentication");
                return false;
            } else if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //Check whether the user has granted your app the USE_FINGERPRINT permission//
                // If your app doesn't have this permission, then display the following text//
//                textView.setText("Please enable the fingerprint permission");
                return false;
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                //Check that the user has registered at least one fingerprint//
                // If the user hasn’t configured any fingerprints, then display the following message//
//                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean hasLockscreenProtected(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            return keyguardManager.isKeyguardSecure();
        } else {
            return false;
        }
    }
}
