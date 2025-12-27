package com.example.bankapp.DTO;

import java.time.LocalDate;

import com.example.bankapp.enums.AccountStatus;
import com.example.bankapp.enums.Designation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeResponseDTO {

	private Long accountId;
    private String fullName;
    private String email;
    private String branchCode;
    private Designation designation;
    private AccountStatus status;
    private LocalDate joiningDate;

}
