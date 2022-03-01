package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.request.TransactionRequest;
import com.tuum.cbs.beans.common.response.Response;

public interface TransactionServiceI {
    Response handleTransaction(TransactionRequest transactionRequest);
}
