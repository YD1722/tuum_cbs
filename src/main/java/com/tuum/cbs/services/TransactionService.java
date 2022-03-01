package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.Transaction;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.TransactionStatus;
import com.tuum.cbs.beans.common.request.TransactionRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.beans.common.response.TransactionResponse;
import com.tuum.cbs.mapper.AccountMapper;
import com.tuum.cbs.mapper.TransactionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

import static com.tuum.cbs.helpers.ApplicationConstants.MIN_ACC_BALANCE;

@Service
public class TransactionService implements TransactionServiceI {
    private AccountMapper accountMapper;
    private TransactionMapper transactionMapper;

    public TransactionService(AccountMapper accountMapper, TransactionMapper transactionMapper) {
        this.accountMapper = accountMapper;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public Response handleTransaction(TransactionRequest transactionRequest) {
        Response response = new Response();

        try {
            CashAccount cashAccount = accountMapper.getCashAccount(transactionRequest.getAccountId(), transactionRequest.getCurrencyCode());

            if (cashAccount == null) {
                response.setStatus(ResponseStatus.ERROR);
                response.setError("Account not found");

                return response;
            }

            if (transactionRequest.getDirection() == TransactionDirection.IN.getValue()) {
                deposit(cashAccount, transactionRequest, response);
            } else if (transactionRequest.getDirection() == TransactionDirection.OUT.getValue()) {
                withdraw(cashAccount, transactionRequest, response);
            }
        } catch (Exception e) {
            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    private void deposit(CashAccount cashAccount, TransactionRequest transactionRequest, Response response) {
        double balanceAfterTx = cashAccount.getAvailableBalance() + transactionRequest.getAmount();

        try {
            doTransaction(cashAccount, transactionRequest, response, balanceAfterTx);
        } catch (Exception e) {
            response.setError("Error in deposit");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void withdraw(CashAccount cashAccount, TransactionRequest transactionRequest, Response response) {
        double balanceAfterTx = cashAccount.getAvailableBalance() - transactionRequest.getAmount();

        if (balanceAfterTx < MIN_ACC_BALANCE) {
            response.setError("Insufficient account balance");
            response.setStatus(ResponseStatus.ERROR);

            return;
        }

        try {
            doTransaction(cashAccount, transactionRequest, response, balanceAfterTx);
        } catch (Exception e) {
            // TODO: Separate transaction log table
            response.setError("Error in withdraw");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void doTransaction(CashAccount cashAccount, TransactionRequest transactionRequest, Response response, double balanceAfterTx) {
        cashAccount.setAvailableBalance(balanceAfterTx);

        try {
            accountMapper.updateCashAccount(cashAccount);
            updateTransaction(cashAccount, transactionRequest, TransactionStatus.SUCCESS);

            response.setStatus(ResponseStatus.SUCCESS);
            response.setData(getTransactionResponseData(cashAccount, transactionRequest));
        } catch (Exception e) {
            throw e;
        }
    }

    private void updateTransaction(CashAccount updatedCashAcc, TransactionRequest transactionRequest, TransactionStatus transactionStatus) {
        Transaction transaction = new Transaction();

        transaction.setAmount(updatedCashAcc.getAvailableBalance());
        transaction.setTimestamp(new Timestamp(System.currentTimeMillis())); // TODO: Using java time
        transaction.setType(transactionRequest.getDirection());
        transaction.setStatus(transactionStatus.getValue());
        transaction.setDescription(transactionRequest.getDescription());

        try {
            transactionMapper.insertTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private TransactionResponse getTransactionResponseData(CashAccount cashAccount, TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setAccountId(transactionRequest.getAccountId());
        transactionResponse.setTransactionDirection(transactionRequest.getDirection());
        transactionResponse.setCurrencyCode(transactionRequest.getCurrencyCode());
        transactionResponse.setAmount(transactionRequest.getAmount());
        transactionResponse.setDescription(transactionRequest.getDescription());
        transactionResponse.setNewBalance(cashAccount.getAvailableBalance());

        return transactionResponse;
    }
}
