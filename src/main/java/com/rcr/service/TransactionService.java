package com.rcr.service;

import com.rcr.model.Order;
import com.rcr.model.Seller;
import com.rcr.model.Transaction;

import java.util.List;

public interface TransactionService {
    public Transaction createTransaction(Order order) throws Exception;
    List<Transaction> getTransactionBySellerId(Seller seller);
    List<Transaction> getAllTransaction();
}
