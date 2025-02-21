package com.rcr.model;

import com.rcr.domain.PaymentStatus;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PaymentDetails {
    private String payment_id;
    private String razorpay_payment_link_id;
    private String razorpay_payment_link_reference_id;
    private String razorpay_payment_link_status;
    private String razorpay_payment_id;
    private PaymentStatus status;
}
