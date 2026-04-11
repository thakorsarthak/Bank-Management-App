package com.example.bankapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@Configuration
public class TwilioConfig {
    @Value("${twilio.account.sid}")
    private String sid;

    @Value("${twilio.auth.token}")
    private String token;

    @PostConstruct
    public void init() {
        Twilio.init(sid, token);
        System.out.println("Twilio SID loaded: " + sid.substring(0, 6) + "...");
        System.out.println("Twilio token loaded: " + token.substring(0, 6) + "...");
    }


}
