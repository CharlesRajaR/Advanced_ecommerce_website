package com.rcr.controller;

import com.rcr.model.Seller;
import com.rcr.model.Transaction;
import com.rcr.service.SellerService;
import com.rcr.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class TransactionController {
    private final TransactionService transactionService;
    private final SellerService sellerService;
    @GetMapping("/seller-api/get/all/transactions")
    public ResponseEntity<List<Transaction>> getTransactionBySellerId(@RequestHeader("Authorization") String jwt){
        Seller seller = sellerService.getSellerProfile(jwt);

        return ResponseEntity.ok(transactionService.getTransactionBySellerId(seller));
    }

    @GetMapping("/admin-api/get/all/transactions")
    public ResponseEntity<List<Transaction>> getAllTransaction(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(transactionService.getAllTransaction());
    }
}
