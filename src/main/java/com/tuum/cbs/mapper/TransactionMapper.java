package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO TRANSACTION_LOGS (cash_account_id, direction, amount, status, transaction_time, description) VALUES (#{cashAccountId}, #{direction}, #{amount}, #{status}, #{transactionTime}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transaction_id")
    int insertTransaction(Transaction transaction);

    @Select("SELECT * from VW_TRANSACTION_LOGS WHERE account_id = #{accountId}")
    List<Transaction> getTransactionDetails(@Param("accountId") String accountId);
}
