package com.tuum.cbs.services;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.request.AccountInput;
import com.tuum.cbs.helpers.AccountHelper;
import com.tuum.cbs.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements AccountServiceI {
    private final PlatformTransactionManager transactionManager;
    private AccountMapper accountMapper;

    public AccountService(PlatformTransactionManager transactionManager, AccountMapper accountMapper) {
        this.transactionManager = transactionManager;
        this.accountMapper = accountMapper;
    }

    @Override
    public BankAccount createNewAccount(AccountInput accountInput) {
        BankAccount bankAccount;
        List<CashAccount> cashAccountList = new ArrayList<>();

        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            bankAccount = accountMapper.getAccountByCustomerId(accountInput.getCustomerId());

            if (bankAccount == null) {
                bankAccount = new BankAccount();

                bankAccount.setAccountId(AccountHelper.getAccId());
                bankAccount.setCustomerId(accountInput.getCustomerId());

                accountMapper.insertBankAccount(bankAccount);
            } else {
                cashAccountList = accountMapper.getCashAccountListByAccountId(bankAccount.getAccountId());

                // Validate existing currencies
            }

            for (String currencyCode : accountInput.getCurrencyCodeList()) {
                CashAccount cashAccount = new CashAccount();

                cashAccount.setBankAccountId(bankAccount.getAccountId());
                cashAccount.setCurrencyCode(currencyCode);
                cashAccount.setBalance(0);

                accountMapper.insertCashAccount(cashAccount);
                cashAccountList.add(cashAccount);
            }

            bankAccount.setCashAccountList(cashAccountList);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            throw e;
        }

        transactionManager.commit(txStatus);

        return bankAccount;
    }
}
