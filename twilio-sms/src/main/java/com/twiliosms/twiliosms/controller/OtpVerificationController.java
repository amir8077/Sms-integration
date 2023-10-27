package com.twiliosms.twiliosms.controller;
import com.twilio.exception.ApiException;
import com.twiliosms.twiliosms.payload.OtpVerificationRequest;
import com.twiliosms.twiliosms.payload.PhoneNumberRequest;
import com.twiliosms.twiliosms.service.OtpService;
import com.twiliosms.twiliosms.service.TwilioSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpVerificationController {

    private final TwilioSmsService twilioSmsService;
    private final OtpService otpService;

    @Autowired
    public OtpVerificationController(TwilioSmsService twilioSmsService, OtpService otpService) {
        this.twilioSmsService = twilioSmsService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody PhoneNumberRequest phoneNumberRequest) {
        try {
            String otp = otpService.generateOtp();
            otpService.storeOtpForPhoneNumber(phoneNumberRequest.getPhoneNumber(), otp);
            twilioSmsService.sendOtp(phoneNumberRequest.getPhoneNumber(), otp);
            return ResponseEntity.ok("OTP sent successfully");
        } catch (ApiException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        String storedOtp = otpService.getOtpForPhoneNumber(otpVerificationRequest.getPhoneNumber());

        if (storedOtp != null && storedOtp.equals(otpVerificationRequest.getOtp())) {
            otpService.removeOtpForPhoneNumber(otpVerificationRequest.getPhoneNumber());
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("OTP verification failed");
        }
    }
}
