package com.twiliosms.twiliosms.service;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final int OTP_LENGTH = 6;

    private final Map<String, String> otpStore = new HashMap<>(); // Store OTPs temporarily

    public String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    public void storeOtpForPhoneNumber(String phoneNumber, String otp) {
        otpStore.put(phoneNumber, otp);
    }

    public String getOtpForPhoneNumber(String phoneNumber) {
        return otpStore.get(phoneNumber);
    }

    public void removeOtpForPhoneNumber(String phoneNumber) {
        otpStore.remove(phoneNumber);
    }
}
