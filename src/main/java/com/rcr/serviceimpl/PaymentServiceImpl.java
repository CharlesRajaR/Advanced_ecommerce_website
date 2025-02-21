package com.rcr.serviceimpl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.rcr.domain.PaymentOrderStatus;
import com.rcr.domain.PaymentStatus;
import com.rcr.model.Order;
import com.rcr.model.PaymentOrder;
import com.rcr.model.User;
import com.rcr.repository.OrderRepository;
import com.rcr.repository.PaymentOrderRepository;
import com.rcr.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentOrderRepository paymentOrderRepository;
    private final OrderRepository orderRepository;
    @Value("${razorpay.api.key}")
    private String apiKey;
    @Value("${razorpay.secret.key}")
    private String secretKey;
    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Override
    public PaymentOrder createOrder(User user, Set<Order> order) {
        Long amount = order.stream().mapToLong(Order::getTotalSellingPrice).sum();
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setOrders(order);
        paymentOrder.setAmount(amount);
        paymentOrder.setUser(user);

        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
       PaymentOrder paymentOrder = paymentOrderRepository.findById(orderId)
               .orElseThrow(() -> new Exception("payment order not found..."));
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentLinkId(String paymentLinkId) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
        return paymentOrder;
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentStatus.PENDING)){
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secretKey);
            Payment payment = razorpayClient.payments.fetch(paymentId);

            String status = payment.get("status");
            if(status.equals("captured")){
                Set<Order> orders = paymentOrder.getOrders();
                for(Order order: orders){
                    order.setPaymentStatus(PaymentStatus.SUCCESSFUL);
                    orderRepository.save(order);
                }
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESSFUL);
                paymentOrderRepository.save(paymentOrder);
                return true;
            }
            paymentOrder.setStatus(PaymentOrderStatus.FAILURE);
            paymentOrderRepository.save(paymentOrder);
        }
//        else{
//
//        }
        return false;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(User user, Long orderId, Long amount) {
        amount = amount * 100;

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, secretKey);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getUsername());
            customer.put("email", user.getEmail());

            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);

            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http:localhost:3000");
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            return paymentLink;

        }
        catch (RazorpayException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createStripePaymentLink(User user, Long orderId, Long amount) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("")
                .setCancelUrl("")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("USD")
                                .setUnitAmount(amount * 100)
                                .setProductData(
                                        SessionCreateParams
                                                .LineItem.PriceData.ProductData
                                                .builder().setName("raja bazaar payment")
                                                .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
