package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CashAccountService implements CashAccountServiceI {
    private AccountMapper accountMapper;

    public CashAccountService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public List<CashAccount> createNewCashAccounts(String accountId, List<String> currencyCodeList) {
        List<CashAccount> cashAccountList;

        try {
            cashAccountList = getCashAccountList(accountId, currencyCodeList);

            // TODO: Batch insert
            for (CashAccount cashAccount : cashAccountList) {
                accountMapper.insertCashAccount(cashAccount);
            }
        } catch (Exception e) {
            throw e;
        }

        return cashAccountList;
    }


    private List<CashAccount> getCashAccountList(String accountId, List<String> currencyCodeList) {
        List<CashAccount> cashAccountList = new ArrayList<>();

        for (String currencyCode : currencyCodeList) {
            CashAccount cashAccount = new CashAccount();

            cashAccount.setAccountId(accountId);
            cashAccount.setCurrencyCode(currencyCode);

            cashAccountList.add(cashAccount);
        }

        return cashAccountList;
    }
}
