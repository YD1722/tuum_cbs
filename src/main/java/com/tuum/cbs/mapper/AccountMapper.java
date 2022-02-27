package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.BankAccount;
import com.tuum.cbs.beans.CashAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Select("SELECT * FROM BANK_ACCOUNT where customer_id = #{customerId}")
    BankAccount getAccountByCustomerId(@Param("customerId") int customerId);

    @Select("SELECT * FROM BANK_ACCOUNT where account_id = #{accountId}")
    BankAccount getAccountByAccountId(@Param("accountId") String accountId);

    @Select("SELECT * FROM CASH_ACCOUNT where bank_account_id = #{accountID}")
    List<CashAccount> getCashAccountListByAccountId(@Param("accountID") String accountID);

    @Insert("INSERT INTO BANK_ACCOUNT(account_id, customer_id) " +
            " VALUES (#{accountId}, #{customerId})")
    int insertBankAccount(BankAccount bankAccount);

    @Insert("INSERT INTO CASH_ACCOUNT(bank_account_id, currency_code, balance) " +
            " VALUES (#{bankAccountId}, #{currencyCode}, #{balance})")
    int insertCashAccount(CashAccount cashAccount);
}
