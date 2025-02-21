package com.rcr.serviceimpl;

import com.rcr.model.Seller;
import com.rcr.model.SellerReport;
import com.rcr.repository.SellerReportRepository;
import com.rcr.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;
    @Override
    public SellerReport getSellerReportById(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if(report == null){
            report = new SellerReport();
            report.setSeller(seller);
            report = sellerReportRepository.save(report);
        }
        return report;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {

        return sellerReportRepository.save(sellerReport);
    }
}
