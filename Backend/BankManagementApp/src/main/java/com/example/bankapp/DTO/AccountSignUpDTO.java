package com.example.bankapp.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountSignUpDTO {
	private String accountHolderName;
	private Double balance;
	private String email;
	@Column(nullable = false)
	private String password;
	private String confirmPassword;
	@Column(nullable = false)
	private String pin;
	private String confirmPin;
	@Column(nullable = false, unique = true)
	private Long contact;
	@Column(nullable = false, length = 20 )
	@NotBlank(message = "PAN is required")
    @Pattern(
        regexp = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$",
        message = "Invalid PAN format"
    )
	private String panNo;
	 @NotBlank(message = "Aadhaar number is required")
	    @Pattern(
	        regexp = "^[0-9]{12}$",
	        message = "Aadhaar must be 12 digits"
	    )
	private String aadhaarNo;
	private String accountType;
	 @Column(nullable = false, length = 4)
	private String branchCode;
	  @Column(nullable = false, length = 2)
	private String productCode;

  private AddressDTO address;


}
