package com.mercadopago.util;

import com.mercadopago.model.Site;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marlanti on 3/31/17.
 */

public class UnlockCardUtil {

    private static Map<String, String> mCardUnlockingLinks;


    public static String getCardUnlockingLink(String mSiteId, Long mIssuerId) {
        if (!consistentUnlockingLinkParameters(mSiteId, mIssuerId)) {
            return null;
        }
        loadCardUnlockingLinks();
        String key = getCardUnlockingLinkKey(mSiteId, mIssuerId);
        return mCardUnlockingLinks.get(key);
    }

    public static boolean consistentUnlockingLinkParameters(String mSiteId, Long mIssuerId) {
        return isSite(mSiteId) && isIssuer(mIssuerId);
    }


    private static String getCardUnlockingLinkKey(String mSiteId, Long mIssuerId) {

        if (!consistentUnlockingLinkParameters(mSiteId, mIssuerId)) {
            return null;
        }

        return mSiteId + "_" + mIssuerId.toString();

    }

    private static void loadCardUnlockingLinks() {
        if (mCardUnlockingLinks == null || mCardUnlockingLinks.isEmpty()) {
            mCardUnlockingLinks = new HashMap<>();
            mCardUnlockingLinks.put("MLV_1050", "https://www.provincial.com");
        }

    }

    public static boolean isSite(String siteId) {
        return siteId != null && !siteId.trim().isEmpty();
    }

    private static boolean isIssuer(Long mIssuerId) {
        return mIssuerId != null;
    }

}
