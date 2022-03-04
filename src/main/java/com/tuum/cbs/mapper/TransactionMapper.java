package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO TRANSACTION_LOGS (type, amount, status, timestamp, description) VALUES (#{type}, #{amount}, #{status}, #{timestamp}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "transactionId", keyColumn = "transactionId")
    int insertTransaction(Transaction transaction);
}
