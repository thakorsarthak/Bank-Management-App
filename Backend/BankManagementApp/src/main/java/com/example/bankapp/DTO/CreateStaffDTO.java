package com.example.bankapp.DTO;

import com.example.bankapp.enums.Designation;
import com.example.bankapp.enums.Role;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStaffDTO {

	@NotNull(message = "Name is needed")
	private String fullName;

	@NotNull(message = "Email is needed")
	@Email(message = "Invalid email format")
	private String email;

	@NotNull(message = "Password is needed")
	private String password;

	@NotNull(message = "Role is required")
	private Role role; // EMPLOYEE or MANAGER only

	@NotBlank(message = "Branch code is required")
	private String branchCode;

	@NotNull(message = "Designation is required")
	@Column(nullable = false)
	private Designation designation;
}
