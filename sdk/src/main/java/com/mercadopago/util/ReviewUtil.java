package com.mercadopago.util;

import android.content.Context;

import com.mercadopago.R;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentMethodSearchItem;

/**
 * Created by vaserber on 11/9/16.
 */

public class ReviewUtil {
    private ReviewUtil() {

    }

    public static int getPaymentInfoStringForItem(PaymentMethodSearchItem item) {
        int string;

        String itemId = formatItemId(item.getId());

        switch (itemId) {
            case "pagofacil":
                string = R.string.mpsdk_review_off_text;
                break;
            case "rapipago":
                string = R.string.mpsdk_review_off_text;
                break;
            case "bapropagos":
                string = R.string.mpsdk_review_off_text;
                break;
            case "redlink_atm":
                string = R.string.mpsdk_review_off_text;
                break;
            case "bancomer_7eleven":
                string = R.string.mpsdk_review_off_text;
                break;
            case "bancomer_atm":
                string = R.string.mpsdk_review_off_text;
                break;
            case "banamex_telecomm":
                string = R.string.mpsdk_review_off_text;
                break;
            case "serfin_atm":
                string = R.string.mpsdk_review_off_text;
                break;
            case "banamex_atm":
                string = R.string.mpsdk_review_off_text;
                break;
            case "oxxo":
                string = R.string.mpsdk_review_off_text;
                break;
            case "cargavirtual":
                string = R.string.mpsdk_review_off_text_2;
                break;
            case "redlink_bank_transfer":
                string = R.string.mpsdk_review_off_text_2;
                break;
            case "bancomer_bank_transfer":
                string = R.string.mpsdk_review_off_text_3;
                break;
            case "banamex_bank_transfer":
                string = R.string.mpsdk_review_off_text_3;
                break;
            case "serfin_bank_transfer":
                string = R.string.mpsdk_review_off_text_3;
                break;
            case "bolbradesco":
                string = R.string.mpsdk_review_off_text_4;
                break;
            case "pagoefectivo_atm":
                string = R.string.mpsdk_review_off_text;
            break;

            case "pagoefectivo_atm_bank_transfer":
                string = R.string.mpsdk_review_off_text_3;
            break;
            case "mercantil_atm":
                string = R.string.mpsdk_review_off_text;
            break;
            case "mercantil_bank_transfer":
                string = R.string.mpsdk_review_off_text_3;
            break;
            case "provincial":
                string = R.string.mpsdk_review_off_text;
            break;
            default:
                string = R.string.mpsdk_review_off_text_default;
        }
        return string;
    }

    public static String getPaymentNameForItem(PaymentMethodSearchItem item, PaymentMethod paymentMethod,
                                               Context context) {

        String string;
        String itemId = formatItemId(item.getId());

        switch (itemId) {
            case "bapropagos":
                string = "Provincia NET";
                break;
            case "pagofacil":
                string = paymentMethod.getName();
                break;
            case "rapipago":
                string = paymentMethod.getName();
                break;
            case "redlink_atm":
                string = item.getDescription() + " " + paymentMethod.getName();
                break;
            case "redlink_bank_transfer":
                string = item.getDescription() + " " + paymentMethod.getName();
                break;
            case "bancomer_7eleven":
                string = "7 Eleven";
                break;
            case "bancomer_atm":
                string = context.getResources().getString(R.string.mpsdk_your_atm) + " " + paymentMethod.getName();
                break;
            case "banamex_telecomm":
                string = "Telecomm";
                break;
            case "serfin_atm":
                string = paymentMethod.getName();
                break;
            case "banamex_atm":
                string = context.getResources().getString(R.string.mpsdk_your_atm) + " " + paymentMethod.getName();
                break;
            case "oxxo":
                string = paymentMethod.getName();
                break;
            case "bancomer_bank_transfer":
                string = context.getResources().getString(R.string.mpsdk_homebanking) + " " + paymentMethod.getName();
                break;
            case "banamex_bank_transfer":
                string = context.getResources().getString(R.string.mpsdk_homebanking) + " " + paymentMethod.getName();
                break;
            case "serfin_bank_transfer":
                string = context.getResources().getString(R.string.mpsdk_homebanking) + " " + paymentMethod.getName();
                break;
            case "bolbradesco":
                string = "boleto";
                break;
            case "pagoefectivo_atm":
                string = context.getResources().getString(R.string.mpsdk_your_atm) + " " + item.getDescription();
            break;
            case "pagoefectivo_atm_bank_transfer":
                string = context.getResources().getString(R.string.mpsdk_homebanking) + " " + item.getDescription();
            break;
            case "mercantil_atm":
                string = context.getResources().getString(R.string.mpsdk_your_atm) + " " + item.getDescription();
            break;
            case "provincial":
                string = paymentMethod.getName();
            break;
            case "mercantil_bank_transfer":
                string = context.getResources().getString(R.string.mpsdk_homebanking) + " " + item.getDescription();
            break;


            default:
                string = paymentMethod.getName();
        }
        return string;
    }

    private static String formatItemId(String itemId){

        String res = itemId;

        if(isAtm(itemId)){
            if(isBankTransfer(itemId)){
                res = "pagoefectivo_atm_bank_transfer";
            }else{
                res= "pagoefectivo_atm";
            }

        }
        return res;
    }

    private static boolean isAtm(String itemId) {

        return itemId.contains("pagoefectivo_atm");
    }

    private static boolean isBankTransfer(String itemId){
        return itemId.contains("bank_transfer");
    }
}
