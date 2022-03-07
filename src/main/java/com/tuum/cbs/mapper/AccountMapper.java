package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Select("SELECT * FROM ALL_CASH_ACCOUNTS where customer_id = #{customerId}")
    List<CashAccount> getAccountsByCustomerId(@Param("customerId") int customerId);

    // TODO: Change view name
    @Select("SELECT * FROM ALL_CASH_ACCOUNTS where account_id = #{accountId}")
    List<CashAccount> getAccountsByAccountId(@Param("accountId") String accountId);

    @Select("SELECT * FROM CASH_ACCOUNT WHERE account_id = #{accountID} AND currency_code = #{currencyCode}")
    CashAccount getCashAccount(@Param("accountID") String accountID, @Param("currencyCode") String currencyCode);

    @Insert("INSERT INTO BANK_ACCOUNT(account_id, customer_id) VALUES (#{accountId}, #{customerId})")
    int insertBankAccount(BankAccount bankAccount);

    @Insert("INSERT INTO CASH_ACCOUNT(account_id, currency_code, balance) VALUES (#{accountId}, #{currencyCode}, #{balance})")
    int insertCashAccount(CashAccount cashAccount);

    @Update("UPDATE CASH_ACCOUNT SET available_balance = #{availableBalance} WHERE cash_account_id = #{cashAccountId}")
    int updateCashAccount(CashAccount cashAccount);
}
