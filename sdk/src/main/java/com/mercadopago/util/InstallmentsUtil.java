package com.mercadopago.util;

import com.mercadopago.constants.Sites;
import com.mercadopago.model.Site;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by marlanti on 4/6/17.
 */

public class InstallmentsUtil {


    private static Set<String> sitesWithoutInstallmentRate;

    public static boolean showNoInstallmentRate(Site site) {
        loadSitesWhichCantShowInstallmentRate();

        return sitesWithoutInstallmentRate.contains(site.getId());
    }

    private static void loadSitesWhichCantShowInstallmentRate(){
        if(sitesWithoutInstallmentRate ==null || sitesWithoutInstallmentRate.isEmpty()){
            sitesWithoutInstallmentRate = new HashSet<>();
            sitesWithoutInstallmentRate.add(Sites.COLOMBIA.getId());
        }
    }
}
