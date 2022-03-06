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
import com.tuum.cbs.beans.message.TransactionMessage;
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
        Response response = new Response();

        try {
            CashAccount cashAccount = cashAccountServiceI.getCashAccount(transactionCreateRequest.getAccountId(), transactionCreateRequest.getCurrencyCode());

            if (cashAccount == null) {
                response.setStatus(ResponseStatus.ERROR);
                response.setMessage("Account not found");

                return response;
            }

            // TODO: Implement factory method
            if (transactionCreateRequest.getDirection() == TransactionDirection.IN.getValue()) {
                deposit(cashAccount, transactionCreateRequest, response);
            } else if (transactionCreateRequest.getDirection() == TransactionDirection.OUT.getValue()) {
                withdraw(cashAccount, transactionCreateRequest, response);
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
            List<TransactionGetResponse> transactionList = transactionMapper.getTransactionDetails(accountId);
            return ServiceResponseHandler.generateResponse(ResponseStatus.SUCCESS, transactionList);
        } catch (Exception e) {
            logger.error("Error in getting transaction details", e);
            return ServiceResponseHandler.generateErrorResponse();
        }
    }

    private void deposit(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest, Response response) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().add(transactionCreateRequest.getAmount());

        try {
            doTransaction(cashAccount, transactionCreateRequest, response, balanceAfterTx);
        } catch (Exception e) {
            response.setMessage("Error in deposit");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void withdraw(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest, Response response) {
        BigDecimal balanceAfterTx = cashAccount.getAvailableBalance().subtract(transactionCreateRequest.getAmount());

        if (balanceAfterTx.compareTo(MIN_ACC_BALANCE) < 0) {
            response.setMessage("Insufficient account balance");
            response.setStatus(ResponseStatus.ERROR);

            return;
        }

        try {
            doTransaction(cashAccount, transactionCreateRequest, response, balanceAfterTx);
        } catch (Exception e) {
            logger.error("Error", e);

            response.setMessage("Error in withdraw");
            response.setStatus(ResponseStatus.ERROR);
        }
    }

    private void doTransaction(CashAccount cashAccount, TransactionCreateRequest transactionCreateRequest, Response response, BigDecimal balanceAfterTx) {
        cashAccount.setAvailableBalance(balanceAfterTx);

        try {
            cashAccountServiceI.updateCashAccount(cashAccount);
            // TODO: log failed transactions as well
            Transaction transaction = updateTransaction(cashAccount, transactionCreateRequest, TransactionStatus.SUCCESS);

            response.setStatus(ResponseStatus.SUCCESS);
            response.setData(getTransactionResponseData(transaction, transactionCreateRequest, cashAccount));

            messageServiceI.send(response);
        } catch (Exception e) {
            logger.error("Error", e);
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
