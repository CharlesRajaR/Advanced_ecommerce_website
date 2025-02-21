package com.rcr.util;

import java.util.Random;

public class OTPutil {
    public static String generateOtp(){
        int length = 6;

        Random random = new Random();

        StringBuilder otp = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}
