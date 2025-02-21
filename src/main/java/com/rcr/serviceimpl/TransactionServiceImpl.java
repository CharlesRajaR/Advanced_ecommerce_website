package com.rcr.serviceimpl;

import com.rcr.model.Order;
import com.rcr.model.Seller;
import com.rcr.model.Transaction;
import com.rcr.repository.TransactionRepository;
import com.rcr.service.SellerService;
import com.rcr.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerService sellerService;

    @Override
    public Transaction createTransaction(Order order) throws Exception {
        Transaction transaction = new Transaction();
        Seller seller = sellerService.getSellerById(order.getSellerId());
        transaction.setSeller(seller);
        transaction.setUser(order.getUser());
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionBySellerId(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }
}
