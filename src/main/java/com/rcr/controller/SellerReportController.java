package com.rcr.controller;

import com.rcr.model.Seller;
import com.rcr.model.SellerReport;
import com.rcr.service.SellerReportService;
import com.rcr.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller-api")
public class SellerReportController {
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    @GetMapping("/get/seller-report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt){
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport sellerReport = sellerReportService.getSellerReportById(seller);

        return ResponseEntity.ok(sellerReport);
    }

    @PutMapping("/update/seller-report")
    public ResponseEntity<SellerReport> updateReport(@RequestBody SellerReport sellerReport){
        return ResponseEntity.ok(sellerReportService.updateSellerReport(sellerReport));
    }
}
