package com.tuum.cbs.helpers;

import java.util.List;

import com.tuum.cbs.beans.common.request.AccountRequest;
import static com.tuum.cbs.helpers.ApplicationConstants.SUPPORTED_CURRENCIES;

public class AccountHelper {
    public static String getAccId() {
        return String.valueOf(Math.random());
    }

    public static String getCashAccId() {
        return String.valueOf(Math.random());
    }

    public static boolean isRequestValid(AccountRequest accountRequest) {
        return isCurrencySupported(accountRequest.getCurrencyCodeList());
    }

    private static boolean isCurrencySupported(List<String> currencyCodeList) {
        for (String code : currencyCodeList) {
            if (!List.of(SUPPORTED_CURRENCIES).contains(code)) {
                return false;
            }
        }

        return true;
    }
}
