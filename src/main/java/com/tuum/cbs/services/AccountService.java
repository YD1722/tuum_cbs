package com.tuum.cbs.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.requests.AccountCreateRequest;
import com.tuum.cbs.beans.common.response.AccountResponse;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.helpers.AccountHelper;
import com.tuum.cbs.helpers.ArrayUtils;
import com.tuum.cbs.mapper.AccountMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService implements AccountServiceI {
    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private AccountMapper accountMapper;
    private CashAccountServiceI cashAccountServiceI;

    public AccountService(AccountMapper accountMapper, ObjectMapper objectMapper, CashAccountServiceI cashAccountServiceI) {
        this.accountMapper = accountMapper;
        this.cashAccountServiceI = cashAccountServiceI;
    }

    @Override
    @Transactional
    public Response createNewAccount(AccountCreateRequest accountCreateRequest) {
        // TODO: Move to separate service response handler
        Response response = new Response();

        try {
            String accountId;
            List<String> currencyCodeList = accountCreateRequest.getCurrencyCodeList();
            List<CashAccount> customerCashAccountList = accountMapper.getAccountsByCustomerId(accountCreateRequest.getCustomerId());

            if (customerCashAccountList == null || customerCashAccountList.size() == 0) {
                BankAccount bankAccount = createNewBankAccount(accountCreateRequest);
                accountId = bankAccount.getAccountId();

                accountMapper.insertBankAccount(bankAccount);
            } else {
                currencyCodeList = getMissingCurrencyList(currencyCodeList, getExistingCurrencyList(customerCashAccountList));
                accountId = customerCashAccountList.get(0).getAccountId();
            }

            List<CashAccount> cashAccountList = this.cashAccountServiceI.createNewCashAccounts(accountId, currencyCodeList);

            response.setData(getAccountResponse(accountId, accountCreateRequest.getCustomerId(), cashAccountList));
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (PersistenceException e) {
            logger.error("Error", e);
            response.setStatus(ResponseStatus.ERROR);
        } catch (Exception e) {
            logger.error("Error", e);
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    @Override
    public Response getAccountDetails(String accountId) {
        Response response = new Response();

        try {
            List<CashAccount> customerCashAccountList = accountMapper.getAccountsByAccountId(accountId);

            if (customerCashAccountList == null || customerCashAccountList.size() == 0) {
                response.setMessage("Account not found");
                response.setStatus(ResponseStatus.ERROR);

                return response;
            }

            response.setData(getAccountResponse(accountId, customerCashAccountList.get(0).getCustomerId(), customerCashAccountList));
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            logger.error("Error", e);
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    private AccountResponse getAccountResponse(String accountId, int customerId, List<CashAccount> cashAccountList) {
        // TODO: Use Separate factory to resolve response objects
        AccountResponse accountResponse = new AccountResponse();

        accountResponse.setAccountId(accountId);
        accountResponse.setCustomerId(customerId);

        for (CashAccount cashAccount : cashAccountList) {
            accountResponse.addBalance(cashAccount.getCurrencyCode(), cashAccount.getAvailableBalance());
        }

        return accountResponse;
    }


    private List<String> getExistingCurrencyList(List<CashAccount> customerCashAccountList) {
        List<String> list = new ArrayList<>();

        for (CashAccount customerCashAccount : customerCashAccountList) {
            list.add(customerCashAccount.getCurrencyCode());
        }

        return list;
    }

    private List<String> getMissingCurrencyList(List<String> reqCurrencyList, List<String> existingCurrencyList) {
        return ArrayUtils.getListsDiff(reqCurrencyList, existingCurrencyList);
    }

    private BankAccount createNewBankAccount(AccountCreateRequest accountCreateRequest) {
        BankAccount bankAccount = new BankAccount();

        bankAccount.setAccountId(AccountHelper.getAccId());
        bankAccount.setCustomerId(accountCreateRequest.getCustomerId());

        return bankAccount;
    }
}
