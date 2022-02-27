package com.tuum.cbs.services;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.request.AccountInput;


public interface AccountServiceI {
    BankAccount createNewAccount(AccountInput accountInput);

    BankAccount getAccountDetails(String accountId);
}
