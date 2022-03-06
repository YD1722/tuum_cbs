package com.tuum.cbs.helpers;

import java.util.Random;

import static com.tuum.cbs.helpers.ApplicationConstants.*;

public class AccountHelper {
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
