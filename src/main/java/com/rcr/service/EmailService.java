package com.rcr.service;

import jakarta.mail.MessagingException;

public interface EmailService{
    public void sendVerificationOTPEmail(String user_email, String otp, String subject, String text)throws MessagingException;
}
