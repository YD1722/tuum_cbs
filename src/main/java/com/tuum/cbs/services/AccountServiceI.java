package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.requests.AccountCreateRequest;
import com.tuum.cbs.beans.common.response.Response;

public interface AccountServiceI {
    Response createNewAccount(AccountCreateRequest accountCreateRequest);

    Response getAccountDetails(String accountId);
}
