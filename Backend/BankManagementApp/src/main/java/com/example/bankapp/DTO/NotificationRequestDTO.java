package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

	 private String email;
	    private String phone;
	    private String subject;
	    private String message;
}
