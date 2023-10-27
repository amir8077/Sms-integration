package com.twiliosms.twiliosms.service;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {

    @Value("${twilio.from.phone.number}")
    private String twilioPhoneNumber;

    public void sendOtp(String toPhoneNumber, String otp) {
        try {
            String messageBody = "Your OTP code is: " + otp;
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(toPhoneNumber),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();

            System.out.println("OTP sent with SID: " + message.getSid());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}
