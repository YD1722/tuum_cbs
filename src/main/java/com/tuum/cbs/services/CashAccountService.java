package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.mapper.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CashAccountService implements CashAccountServiceI {
    Logger logger = LoggerFactory.getLogger(CashAccountService.class);

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

    @Override
    @Transactional
    public void updateCashAccount(CashAccount cashAccount) {
        try {
            accountMapper.updateCashAccount(cashAccount);
        } catch (Exception e) {
            logger.error("Error updating cash account", e);
            throw e;
        }
    }

    @Override
    public CashAccount getCashAccount(String accountId, String currencyCode) {
        try {
            CashAccount cashAccount = accountMapper.getCashAccount(accountId, currencyCode);
            return cashAccount;
        } catch (Exception e) {
            logger.error("Error in getting cash account", e);
            throw e;
        }
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
