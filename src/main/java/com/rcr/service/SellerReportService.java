package com.rcr.service;

import com.rcr.model.Seller;
import com.rcr.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReportById(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
