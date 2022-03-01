package com.tuum.cbs.helpers;


import com.tuum.cbs.beans.common.request.AccountRequest;

public class AccountHelper {
    public static String getAccId() {
        return String.valueOf(Math.random());
    }

    public static String getCashAccId() {
        return String.valueOf(Math.random());
    }

    public static boolean isRequestValid(AccountRequest accountRequest) {
        return CommonValidationHelper.isCurrencySupported(accountRequest.getCurrencyCodeList());
    }
}
