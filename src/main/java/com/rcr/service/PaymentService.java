package com.rcr.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.rcr.model.Order;
import com.rcr.model.PaymentOrder;
import com.rcr.model.User;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    public PaymentOrder createOrder(User user, Set<Order> order);
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception;

    public PaymentOrder getPaymentOrderByPaymentLinkId(String paymentLinkId);
    public Boolean proceedPayment(PaymentOrder paymentOrder,
                                  String paymentId,
                                  String paymentLinkId) throws RazorpayException;

    public PaymentLink createRazorpayPaymentLink(User user,
                                                 Long orderId,
                                                 Long amount);
    public String createStripePaymentLink(User user,
                                                 Long orderId,
                                                 Long amount) throws StripeException;
}
