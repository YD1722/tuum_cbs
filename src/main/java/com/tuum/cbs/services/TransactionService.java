package com.tuum.cbs.services;

import com.tuum.cbs.beans.CashAccount;
import com.tuum.cbs.beans.Transaction;
import com.tuum.cbs.beans.common.ResponseStatus;
import com.tuum.cbs.beans.common.TransactionDirection;
import com.tuum.cbs.beans.common.TransactionStatus;
import com.tuum.cbs.beans.common.requests.TransactionCreateRequest;
import com.tuum.cbs.beans.common.response.Response;
import com.tuum.cbs.beans.common.response.TransactionCreateResponse;
import com.tuum.cbs.beans.common.response.TransactionGetResponse;
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
    private MessageServiceI messageServiceI;

    public TransactionService(CashAccountServiceI cashAccountServiceI, TransactionMapper transactionMapper, RabbitMqMessageService rabbitMqMessageService) {
        this.cashAccountServiceI = cashAccountServiceI;
        this.transactionMapper = transactionMapper;
        this.messageServiceI = rabbitMqMessageService;
    }

    @Override
    @Transactional
    public Response handleTransaction(TransactionCreateRequest transactionCreateRequest) {
        Response response;

        try {
            CashAccount cashAccount = cashAccountServiceI.getCashAccount(transactionCreateRequest.getAccountId(), transactionCreateRequest.getCurrencyCode());

            if (cashAccount == null) {
                response = ServiceResponseHandler.generateErrorResponse("Account not found");
                return response;
            }

            // TODO: Implement factory method
            if (transactionCreateRequest.getDirection() == TransactionDirection.IN.getValue()) {
                response = deposit(cashAccount, transactionCreateRequest);
                messageServiceI.send(response);
            } else if (transactionCreateRequest.getDirection() == TransactionDirection.OUT.getValue()) {
                response = withdraw(cashAccount, transactionCreateRequest);
                messageServiceI.send(response);
            } else {
                logger.error("Invalid transaction direction");
                response = ServiceResponseHandler.generateErrorResponse("Invalid transaction");
            }
        } catch (Exception e) {
            logger.error("Error", e);
            response = ServiceResponseHandler.generateErrorResponse();
        }

        return response;
    }

    @Override
    public Response getTransactions(String accountId) {
        try {
            List<TransactionGetResponse> transactionList = transactionMapper.getTransactionDetails(accountId);
            return ServiceResponseHandler.generateResponse(ResponseStatus.SUCCESS, transactionList);
        } catch (Exception e) {
            logger.error("Error in getting transaction details", e);
            return ServiceResponseHandler.generateErrorResponse();
        }
    }

    private Response deposit(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().add(transactionCreateRequest.getAmount());

        try {
            return doTransaction(cashAccount, transactionCreateRequest, balanceAfterTx);
        } catch (Exception e) {
            return ServiceResponseHandler.generateErrorResponse("Error in deposit");
        }
    }

    private Response withdraw(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().subtract(transactionCreateRequest.getAmount());

        if (balanceAfterTx.compareTo(MIN_ACC_BALANCE) < 0) {
            return ServiceResponseHandler.generateErrorResponse("Insufficient account balance");
        }

        try {
            return doTransaction(cashAccount, transactionCreateRequest, balanceAfterTx);
        } catch (Exception e) {
            logger.error("Error", e);

            return ServiceResponseHandler.generateErrorResponse("Transaction Failed");
        }
    }

    private Response doTransaction(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest, BigDecimal balanceAfterTx) {
        cashAccount.setAvailableBalance(balanceAfterTx);

        try {
            // TODO: log failed transactions as well
            cashAccountServiceI.updateCashAccount(cashAccount);
            Transaction transaction = updateTransaction(cashAccount, transactionCreateRequest, TransactionStatus.SUCCESS);

            return ServiceResponseHandler.generateResponse(ResponseStatus.SUCCESS, getTransactionResponseData(transaction, transactionCreateRequest, cashAccount));
        } catch (Exception e) {
            logger.error("Error in transaction", e);
            throw e;
        }
    }

    private Transaction updateTransaction(CashAccount updatedCashAcc, TransactionCreateRequest transactionCreateRequest, TransactionStatus transactionStatus) {
        Transaction transaction = new Transaction();

        transaction.setCashAccountId(updatedCashAcc.getCashAccountId());
        transaction.setAmount(transactionCreateRequest.getAmount());
        transaction.setTransactionTime(new Timestamp(System.currentTimeMillis()));
        transaction.setDirection(transactionCreateRequest.getDirection());
        transaction.setStatus(transactionStatus.getValue());
        transaction.setDescription(transactionCreateRequest.getDescription());

        try {
            transactionMapper.insertTransaction(transaction);
        } catch (Exception e) {
            logger.error("Error", e);
            throw e;
        }

        return transaction;
    }

    private TransactionCreateResponse getTransactionResponseData(Transaction transaction, TransactionCreateRequest transactionCreateRequest, CashAccount cashAccount) {
        TransactionCreateResponse transactionCreateResponse = new TransactionCreateResponse();

        transactionCreateResponse.setTransactionId(transaction.getTransactionId());
        transactionCreateResponse.setAccountId(transactionCreateRequest.getAccountId());
        transactionCreateResponse.setTransactionDirection(transactionCreateRequest.getDirection());
        transactionCreateResponse.setCurrencyCode(transactionCreateRequest.getCurrencyCode());
        transactionCreateResponse.setAmount(transactionCreateRequest.getAmount());
        transactionCreateResponse.setDescription(transactionCreateRequest.getDescription());
        transactionCreateResponse.setNewBalance(cashAccount.getAvailableBalance());

        return transactionCreateResponse;
    }
}
