package com.example.bankapp.DTO;

import java.time.Instant;

import com.example.bankapp.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDTO {
    private String accountNumber;
    private String accountHolderName;
    private String email;
    private String contact;
    private Double balance;
    private String panNumber;
    private String aadharNumber;
    private Instant createdAt;
    private String branchCode;
    private String ifscCode;
    private String branchName;
    private AccountStatus status;
    private String productType;
}
