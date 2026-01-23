package com.example.bankapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    private String accountNumber;
    private String accountHolderName;
    private Double balance;
    private String email;
    private Long contact;
    private String branchCode;
    private String ifscCode;
    private String branchName;
    private String productCode;
    private String productType;
    private String address;
}


