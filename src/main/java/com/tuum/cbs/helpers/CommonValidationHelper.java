package com.tuum.cbs.helpers;

import java.util.List;

import static com.tuum.cbs.helpers.ApplicationConstants.SUPPORTED_CURRENCIES;

public class CommonValidationHelper {
    public static boolean isCurrencySupported(List<String> currencyCodeList) {
        for (String code : currencyCodeList) {
            if (!isCurrencySupported(code)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isCurrencySupported(String currencyCode) {
        return List.of(SUPPORTED_CURRENCIES).contains(currencyCode);
    }
}
