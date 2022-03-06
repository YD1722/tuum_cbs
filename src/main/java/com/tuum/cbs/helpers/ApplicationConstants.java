package com.tuum.cbs.helpers;

import java.math.BigDecimal;

public class ApplicationConstants {
    public static String[] SUPPORTED_CURRENCIES = new String[]{"EUR", "SEK", "GBP", "USD"};
    public static BigDecimal MIN_ACC_BALANCE = BigDecimal.ZERO;

    // TODO: Update these from a config file
    public static String COUNTRY_CODE = "EE";
    public static String CHECK_DIGITS = "33";
    public static String BANK_ID = "22";
    public static String BRANCH_ID = "00";

}
