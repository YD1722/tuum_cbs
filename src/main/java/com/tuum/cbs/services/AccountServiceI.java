package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.request.AccountRequest;
import com.tuum.cbs.beans.common.response.Response;

public interface AccountServiceI {
    Response createNewAccount(AccountRequest accountRequest);

    Response getAccountDetails(String accountId);
}
