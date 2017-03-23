package com.mercadopago.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.mercadopago.model.Payment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaserber on 3/16/17.
 */

public class StorageUtil {

    protected StorageUtil() {

    }

    public static void saveInFile(Context context, Map<String, String> map, String fileName) {
        try {

            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
            objectOutputStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public static Map<String, String> addToStorageMap(Context context, String key, String value, String fileName) {
        Map<String, String> map = StorageUtil.getStorageMap(context, fileName);
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            map.put(key, value);
        }
        return map;
    }

    public static Map<String, String> deleteOfStorageMap(Context context, String key, String fileName) {
        Map<String, String> map = StorageUtil.getStorageMap(context, fileName);
        if (!TextUtils.isEmpty(key) && map.containsKey(key)) {
            map.remove(key);
        }
        return map;
    }

    public static Map<String, String> getStorageMap(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            Map map = (HashMap) objectInputStream.readObject();
            objectInputStream.close();
            return map;

        } catch (Exception e) {
            Map<String, String> map = new HashMap();
            return map;
        }
    }

    public static String createFileName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        String name = stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
        name += " - Mercado Pago";
        Log.d("log", name);
        return name;
    }

    public static String getValueFromFile(Context context, String fileName, String key) {
        Map<String, String> map = getStorageMap(context, fileName);
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    public static boolean hasToEraseEncryptedCvv(String paymentStatus, String paymentStatusDetail) {
        return paymentStatus.equals(Payment.StatusCodes.STATUS_REJECTED) &&
                !paymentStatusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_DUPLICATED_PAYMENT) &&
                !paymentStatusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_INSUFFICIENT_AMOUNT) &&
                !paymentStatusDetail.equals(Payment.StatusCodes.STATUS_DETAIL_CC_REJECTED_CARD_DISABLED);
    }

}
