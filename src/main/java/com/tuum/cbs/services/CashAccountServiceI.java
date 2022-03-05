package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;

import java.util.List;

public interface CashAccountServiceI {
    List<CashAccount> createNewCashAccounts(String accountId, List<String> currencyCodeList);
}
