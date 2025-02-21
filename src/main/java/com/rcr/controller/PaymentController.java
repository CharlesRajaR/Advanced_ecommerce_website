package com.rcr.controller;

import com.rcr.customRes.ApiResponse;
import com.rcr.domain.PaymentLinkResponse;
import com.rcr.domain.PaymentStatus;
import com.rcr.model.*;
import com.rcr.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UserService userService;
    private final SellerReportService sellerReportService;
    private final SellerService sellerService;
    private final TransactionService transactionService;

    @PutMapping("/payment-success")
    public ResponseEntity<ApiResponse> paymentSuccessHandler(@RequestParam String paymentId,
                                                  @RequestParam String paymentLinkId,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        PaymentLinkResponse paymentLinkResponse;

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentLinkId(paymentLinkId);

        boolean paymentSuccess = paymentService.proceedPayment(
                paymentOrder, paymentId, paymentLinkId
        );

        if(paymentSuccess){
            for(Order order : paymentOrder.getOrders()){
                transactionService.createTransaction(order);
                Seller seller = sellerService.getSellerById(order.getSellerId());
                SellerReport report = sellerReportService.getSellerReportById(seller);
                report.setTotalOrders(report.getTotalOrders()+1);
                report.setTotalEarnings(report.getTotalEarnings() + order.getTotalSellingPrice());
                report.setTotalSales(report.getTotalSales() + order.getTotalItem());
                sellerReportService.updateSellerReport(report);

                orderService.updatePaymentStatus(order.getId(), PaymentStatus.SUCCESSFUL);
            }
        }

        ApiResponse res = new ApiResponse();
        res.setMessage("payment successfull");

        return ResponseEntity.ok(res);
    }
}
