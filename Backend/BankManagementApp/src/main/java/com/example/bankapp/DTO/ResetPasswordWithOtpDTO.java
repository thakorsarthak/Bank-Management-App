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
	    @Size(min = 4, max = 8, message = "PIN must be 4 to 8 digits")
	    private String newPassword;

	    @NotBlank(message = "Confirm Password is required")
	    private String confirmPassword;

}
