package com.tuum.cbs.helpers;

import java.util.Random;

public class AccountHelper {
    // TODO: Get this from a config file
    private final static String COUNTRY_CODE = "EE";
    private final static String CHECK_DIGITS = "33";
    private final static String BANK_ID = "22";
    private final static String BRANCH_ID = "00";

    public static String getAccId() {
        return COUNTRY_CODE + CHECK_DIGITS + BANK_ID + BRANCH_ID + getRandomBankAccNumber();
    }

    // TODO: Replace with actual logic to avoid same acc number generation
    private static String getRandomBankAccNumber() {
        String accNum = "";

        for (int i = 0; i < 3; i++) {
            accNum += String.format("%04d", new Random().nextInt(10000));
        }

        return accNum;
    }
}
