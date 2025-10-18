package com.example.bankapp.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordWithOtpDTO {
	
	 private String email;
	    private String contact;

	    @NotBlank(message = "New Password is required")
	    @Size(min = 4, max = 30, message = "Minimum 4 to Max 30 required in Password")
	    private String newPassword;

	    @NotBlank(message = "Confirm Password is required")
	    private String confirmPassword;

}
