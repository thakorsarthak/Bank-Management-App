package com.example.bankapp.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDTO {
	private Long id;
    private String accountNumber;
    private String fullName;
    private String email;
    private String contact;
    private String branch;
    private String status;
    private Double balance;
    private String panNumber;
    private String aadhaarNumber;
    private LocalDate createdAt;

}
