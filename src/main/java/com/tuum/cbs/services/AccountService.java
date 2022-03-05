package com.tuum.cbs.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.request.AccountRequest;
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
    private ObjectMapper objectMapper;
    private CashAccountServiceI cashAccountServiceI;

    public AccountService(AccountMapper accountMapper, ObjectMapper objectMapper, CashAccountServiceI cashAccountServiceI) {
        this.accountMapper = accountMapper;
        this.objectMapper = objectMapper;
        this.cashAccountServiceI = cashAccountServiceI;
    }

    @Override
    @Transactional
    public Response createNewAccount(AccountRequest accountRequest) {
        Response response = new Response();

        try {
            String accountId;
            List<String> currencyCodeList = accountRequest.getCurrencyCodeList();
            List<CashAccount> customerCashAccountList = accountMapper.getAccountsByCustomerId(accountRequest.getCustomerId());

            if (customerCashAccountList == null || customerCashAccountList.size() == 0) {
                BankAccount bankAccount = createNewBankAccount(accountRequest);
                accountId = bankAccount.getAccountId();

                accountMapper.insertBankAccount(bankAccount);
            } else {
                currencyCodeList = getMissingCurrencyList(currencyCodeList, getExistingCurrencyList(customerCashAccountList));
                accountId = customerCashAccountList.get(0).getAccountId();
            }

            List<CashAccount> cashAccountList = this.cashAccountServiceI.createNewCashAccounts(accountId, currencyCodeList);

            response.setData(getAccountResponse(accountId, accountRequest.getCustomerId(), cashAccountList));
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
                response.setError("Account not found");
                response.setStatus(ResponseStatus.ERROR);

                return null;
            }

            response.setData(getAccountResponse(accountId, customerCashAccountList.get(0).getCustomerId(), customerCashAccountList));
            response.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception e) {
            logger.error("Error", e);
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    private ObjectNode getAccountResponse(String accountId, int customerId, List<CashAccount> cashAccountList) {
        ObjectNode response = objectMapper.createObjectNode();

        response.put("accountId", accountId);
        response.put("customerId", customerId);

        List<ObjectNode> balanceList = getBalanceList(cashAccountList);

        response.put("balances", String.valueOf(balanceList));

        return response;
    }

    private List<ObjectNode> getBalanceList(List<CashAccount> cashAccountList) {
        List<ObjectNode> balanceList = new ArrayList<>();

        for (CashAccount cashAccount : cashAccountList) {
            ObjectNode balance = objectMapper.createObjectNode();

            balance.put("amount", cashAccount.getAvailableBalance());
            balance.put("currency", cashAccount.getCurrencyCode());

            balanceList.add(balance);
        }

        return balanceList;
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

    private BankAccount createNewBankAccount(AccountRequest accountRequest) {
        BankAccount bankAccount = new BankAccount();

        bankAccount.setAccountId(AccountHelper.getAccId());
        bankAccount.setCustomerId(accountRequest.getCustomerId());

        return bankAccount;
    }
}
