package com.tuum.cbs.services;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.request.AccountRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.helpers.AccountHelper;
import com.tuum.cbs.mapper.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// TODO: where to put the mark? interface or class
@Service
public class AccountService implements AccountServiceI {
    private final PlatformTransactionManager transactionManager;
    private AccountMapper accountMapper;

    public AccountService(PlatformTransactionManager transactionManager, AccountMapper accountMapper) {
        this.transactionManager = transactionManager;
        this.accountMapper = accountMapper;
    }

    @Override
    @Transactional
    public Response createNewAccount(AccountRequest accountRequest) {
        Response response = new Response();

        try {
            BankAccount bankAccount = accountMapper.getAccountByCustomerId(accountRequest.getCustomerId());
            List<CashAccount> cashAccountList = new ArrayList<>();

            if (bankAccount == null) {
                bankAccount = new BankAccount();

                bankAccount.setAccountId(AccountHelper.getAccId());
                bankAccount.setCustomerId(accountRequest.getCustomerId());

                accountMapper.insertBankAccount(bankAccount);
            } else {
                cashAccountList = accountMapper.getCashAccountListByAccountId(bankAccount.getAccountId());

                // Validate existing currencies
            }

            for (String currencyCode : accountRequest.getCurrencyCodeList()) {
                CashAccount cashAccount = new CashAccount();

                cashAccount.setBankAccountId(bankAccount.getAccountId());
                cashAccount.setCurrencyCode(currencyCode);
                cashAccount.setBalance(0);
                cashAccount.setAvailableBalance(0);

                accountMapper.insertCashAccount(cashAccount);
                cashAccountList.add(cashAccount);
            }

            bankAccount.setCashAccountList(cashAccountList);

            response.setData(bankAccount);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    @Override
    public Response getAccountDetails(String accountId) {
        Response response = new Response();

        // TODO: Reconsider about table architecture or get data from a view
        try {
            BankAccount bankAccount = accountMapper.getAccountByAccountId(accountId);

            if (bankAccount == null) {
                return null;
            }

            List<CashAccount> cashAccountList = accountMapper.getCashAccountListByAccountId(accountId);

            bankAccount.setCashAccountList(cashAccountList);

            response.setData(bankAccount);
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }
}
