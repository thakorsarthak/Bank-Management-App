package com.example.bankapp.config;

public class RabbitMQConstants {

	    // Exchanges
	    public static final String NOTIFICATION_EXCHANGE = "notification_exchange";

	    // Queues
	    public static final String EMAIL_QUEUE = "email_queue";
	    public static final String SMS_QUEUE = "sms_queue";
	    public static final String OTP_QUEUE = "otp_queue";        //  OTP
	    public static final String OTP_DLQ = "otp_dlq";            // OTP Dead Letter Queue

	    // Routing keys
	    public static final String EMAIL_ROUTING_KEY = "notification.email";
	    public static final String SMS_ROUTING_KEY = "notification.sms";
	    public static final String OTP_ROUTING_KEY = "notification.otp";  //  OTP
	}

