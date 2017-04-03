package com.mercadopago.util;

import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by vaserber on 3/31/17.
 */

public class ProtectionManager {

    private static ProtectionManager instance;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;

    private ProtectionManager() {

    }

    public static ProtectionManager getInstance() {
        if (instance == null) {
            instance = new ProtectionManager();
        }
        return instance;
    }

    //Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//
    @TargetApi(Build.VERSION_CODES.M)
    public void generateKey(String keyName) throws FingerprintException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
                keyStore = KeyStore.getInstance("AndroidKeyStore");

                //Generate the key//
                keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

                //Initialize an empty KeyStore//
                keyStore.load(null);

                //Initialize the KeyGenerator//
                keyGenerator.init(new

                        //Specify the operation(s) this key can be used for//
                        KeyGenParameterSpec.Builder(keyName,
                        KeyProperties.PURPOSE_ENCRYPT |
                                KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                        //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(
                                KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .build());

                //Generate the key//
                keyGenerator.generateKey();

            } catch (KeyStoreException
                    | NoSuchAlgorithmException
                    | NoSuchProviderException
                    | InvalidAlgorithmParameterException
                    | CertificateException
                    | IOException exc) {
                exc.printStackTrace();
                throw new FingerprintException(exc);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean initCipher(String keyName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
                cipher = Cipher.getInstance(
                        KeyProperties.KEY_ALGORITHM_AES + "/"
                                + KeyProperties.BLOCK_MODE_CBC + "/"
                                + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            } catch (NoSuchAlgorithmException |
                    NoSuchPaddingException e) {
                throw new RuntimeException("Failed to get Cipher", e);
            }

            try {
                keyStore.load(null);
                SecretKey key = (SecretKey) keyStore.getKey(keyName,
                        null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                //Return true if the cipher has been initialized successfully//
                return true;
            } catch (KeyPermanentlyInvalidatedException e) {

                //Return false if cipher initialization failed//
                return false;
            } catch (KeyStoreException | CertificateException
                    | UnrecoverableKeyException | IOException
                    | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException("Failed to init Cipher", e);
            }
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public FingerprintManager.CryptoObject createCryptoObject() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return new FingerprintManager.CryptoObject(cipher);
        }
        return null;
    }

    public class FingerprintException extends Exception {

        public FingerprintException(Exception e) {
            super(e);
        }
    }
}
