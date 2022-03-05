package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.requests.TransactionCreateRequest;
import com.tuum.cbs.beans.common.response.Response;

public interface TransactionServiceI {
    Response handleTransaction(TransactionCreateRequest transactionCreateRequest);

    Response getTransactions(String accountId);
}
