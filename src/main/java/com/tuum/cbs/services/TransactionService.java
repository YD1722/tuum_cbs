package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.Transaction;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.TransactionStatus;
import com.tuum.cbs.beans.common.request.TransactionRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.beans.common.response.TransactionResponse;
import com.tuum.cbs.mapper.TransactionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tuum.cbs.helpers.ApplicationConstants.MIN_ACC_BALANCE;

@Service
public class TransactionService implements TransactionServiceI {
    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private TransactionMapper transactionMapper;
    private CashAccountServiceI cashAccountServiceI;

    public TransactionService(CashAccountServiceI cashAccountServiceI, TransactionMapper transactionMapper) {
        this.cashAccountServiceI = cashAccountServiceI;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public Response handleTransaction(TransactionRequest transactionRequest) {
        Response response = new Response();

        try {
            CashAccount cashAccount = cashAccountServiceI.getCashAccount(transactionRequest.getAccountId(), transactionRequest.getCurrencyCode());

            if (cashAccount == null) {
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage("Account not found");

                return response;
            }

            // TODO: Implement factory method
            if (transactionRequest.getDirection() == TransactionDirection.IN.getValue()) {
                deposit(cashAccount, transactionRequest, response);
            } else if (transactionRequest.getDirection() == TransactionDirection.OUT.getValue()) {
                withdraw(cashAccount, transactionRequest, response);
            } else {
                logger.error("Invalid transaction direction");

                response.setMessage("Invalid transaction direction");
                response.setStatus(ResponseStatus.ERROR);
            }
        } catch (Exception e) {
            logger.error("Error", e);

            response.setStatus(ResponseStatus.ERROR);
        }

        return response;
    }

    @Override
    public Response getTransactions(String accountId) {
        try {
            List<Transaction> transactionList = transactionMapper.getTransactionDetails(accountId);
            return ServiceResponseHandler.generateResponse(ResponseStatus.SUCCESS, transactionList);
        } catch (Exception e) {
            logger.error("Error in getting transaction details", e);
            return ServiceResponseHandler.generateErrorResponse();
        }
    }

    private void deposit(CashAccount cashAccount, TransactionRequest transactionRequest, Response response) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().add(transactionRequest.getAmount());

        try {
            doTransaction(cashAccount, transactionRequest, response, balanceAfterTx);
        } catch (Exception e) {
            response.setMessage("Error in deposit");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void withdraw(CashAccount cashAccount, TransactionRequest transactionRequest, Response response) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().subtract(transactionRequest.getAmount());

        if (balanceAfterTx.compareTo(MIN_ACC_BALANCE) < 0) {
            response.setMessage("Insufficient account balance");
            response.setStatus(ResponseStatus.ERROR);

            return;
        }

        try {
            doTransaction(cashAccount, transactionRequest, response, balanceAfterTx);
        } catch (Exception e) {
            logger.error("Error", e);

            response.setMessage("Error in withdraw");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void doTransaction(CashAccount cashAccount, TransactionRequest transactionRequest, Response response, BigDecimal balanceAfterTx) {
        cashAccount.setAvailableBalance(balanceAfterTx);

        try {
            cashAccountServiceI.updateCashAccount(cashAccount);
            Transaction transaction = updateTransaction(cashAccount, transactionRequest, TransactionStatus.SUCCESS); // Fail log transaction

            response.setStatus(ResponseStatus.SUCCESS);
            response.setData(getTransactionResponseData(transaction, transactionRequest, cashAccount));
        } catch (Exception e) {
            logger.error("Error", e);
            throw e;
        }
    }

    private Transaction updateTransaction(CashAccount updatedCashAcc, TransactionRequest transactionRequest, TransactionStatus transactionStatus) {
        Transaction transaction = new Transaction();

        transaction.setAmount(updatedCashAcc.getAvailableBalance());
        transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
        transaction.setDirection(transactionRequest.getDirection());
        transaction.setStatus(transactionStatus.getValue());
        transaction.setDescription(transactionRequest.getDescription());

        try {
            transactionMapper.insertTransaction(transaction);
        } catch (Exception e) {
            logger.error("Error", e);
            throw e;
        }

        return transaction;
    }

    private TransactionResponse getTransactionResponseData(Transaction transaction, TransactionRequest transactionRequest, CashAccount cashAccount) {
        TransactionResponse transactionResponse = new TransactionResponse();

        transactionResponse.setTransactionId(transaction.getTransactionId());
        transactionResponse.setAccountId(transactionRequest.getAccountId());
        transactionResponse.setTransactionDirection(transactionRequest.getDirection());
        transactionResponse.setCurrencyCode(transactionRequest.getCurrencyCode());
        transactionResponse.setAmount(transactionRequest.getAmount());
        transactionResponse.setDescription(transactionRequest.getDescription());
        transactionResponse.setNewBalance(cashAccount.getAvailableBalance());

        return transactionResponse;
    }
}
