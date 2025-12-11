package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

	private String email;
    private String phone;
    private String messageType;  //  otp reset , or any other
    private String otp;

}
