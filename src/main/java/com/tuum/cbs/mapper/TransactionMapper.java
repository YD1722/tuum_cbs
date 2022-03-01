package com.tuum.cbs.mapper;

import com.tuum.cbs.beans.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO TRANSACTION(type, amount, status, timestamp, description) VALUES (#{type}, #{amount}, #{status}, #{timestamp}, #{description})")
    int insertTransaction(Transaction transaction);
}
