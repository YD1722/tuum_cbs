package com.tuum.cbs.helpers;

import com.tuum.cbs.beans.request.AccountInput;

import java.util.List;

public class AccountHelper {
    private static String[] supportedCurrencyList = new String[]{"EUR", "SEK", "GBP", "USD"};

    public static String getAccId() {
        return String.valueOf(Math.random());
    }

    public static String getCashAccId() {
        return String.valueOf(Math.random());
    }

    public static boolean isRequestValid(AccountInput accountInput) {
        return isCurrencySupported(accountInput.getCurrencyCodeList());
    }

    private static boolean isCurrencySupported(List<String> currencyCodeList) {
        for (String code : currencyCodeList) {
            if (!List.of(supportedCurrencyList).contains(code)) {
                return false;
            }
        }

        return true;
    }
}
