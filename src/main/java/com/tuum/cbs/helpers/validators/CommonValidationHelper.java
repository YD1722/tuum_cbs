package com.tuum.cbs.helpers.validators;

import java.util.List;

import static com.tuum.cbs.helpers.ApplicationConstants.SUPPORTED_CURRENCIES;

public class CommonValidationHelper {
    public static boolean isCurrencySupported(String currencyCode) {
        return List.of(SUPPORTED_CURRENCIES).contains(currencyCode);
    }
}
